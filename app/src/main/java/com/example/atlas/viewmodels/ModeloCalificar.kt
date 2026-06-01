package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.modelos.EstadoCalificar
import com.example.atlas.objetosDB.Entrenador
import com.example.atlas.objetosDB.Valoracion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UIStateCalificar(
    val calificacion: Int = 0,
    val comentario: String = "",
    val entrenadorSeleccionado: Entrenador? = null,
    val todosEntrenadores: List<Entrenador> = emptyList(),
    val misEntrenadoresIds: List<String> = emptyList()
)

class ModeloCalificar : ViewModel() {
    private val _uiState = MutableStateFlow(UIStateCalificar())
    val uiState: StateFlow<UIStateCalificar> = _uiState.asStateFlow()

    init {
        fetchEntrenadores()
        fetchMisEntrenadores()
    }

    private fun fetchEntrenadores() {
        database.getReference("entrenadores").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Entrenador>()
                for (child in snapshot.children) {
                    child.getValue<Entrenador>()?.let { lista.add(it) }
                }
                _uiState.update { it.copy(todosEntrenadores = lista) }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchMisEntrenadores() {
        val uid = auth.currentUser?.uid ?: return
        database.getReference("deportistas/$uid/entrenadores").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<String>()
                for (child in snapshot.children) {
                    child.getValue<String>()?.let { lista.add(it) }
                }
                _uiState.update { it.copy(misEntrenadoresIds = lista) }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun actualizarCalificacion(valor: Int) {
        _uiState.update { it.copy(calificacion = valor) }
    }

    fun actualizarComentario(texto: String) {
        _uiState.update { it.copy(comentario = texto) }
    }

    fun seleccionarEntrenador(entrenador: Entrenador) {
        _uiState.update { it.copy(entrenadorSeleccionado = entrenador) }
    }

    fun agregarEntrenador(entrenadorId: String) {
        val uid = auth.currentUser?.uid ?: return
        val ref = database.getReference("deportistas/$uid/entrenadores")
        ref.get().addOnSuccessListener { snapshot ->
            val lista = mutableListOf<String>()
            for (child in snapshot.children) {
                child.getValue<String>()?.let { lista.add(it) }
            }
            if (!lista.contains(entrenadorId)) {
                lista.add(entrenadorId)
                ref.setValue(lista)
            }
        }
    }

    fun enviarCalificacion(onComplete: () -> Unit) {
        val state = _uiState.value
        val entrenadorId = state.entrenadorSeleccionado?.id ?: return
        val uid = auth.currentUser?.uid ?: return
        
        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val valoracion = Valoracion(
            idDeportista = uid,
            idEntrenador = entrenadorId,
            calificacion = state.calificacion,
            comentario = state.comentario,
            fecha = fecha
        )

        val ref = database.getReference("valoraciones").push()
        ref.setValue(valoracion).addOnSuccessListener {
            actualizarPromedioEntrenador(entrenadorId)
            onComplete()
        }
    }

    private fun actualizarPromedioEntrenador(entrenadorId: String) {
        database.getReference("valoraciones").orderByChild("idEntrenador").equalTo(entrenadorId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var suma = 0
                    var count = 0
                    for (child in snapshot.children) {
                        val v = child.getValue<Valoracion>()
                        v?.let {
                            suma += it.calificacion
                            count++
                        }
                    }
                    if (count > 0) {
                        val promedio = suma.toDouble() / count
                        database.getReference("entrenadores/$entrenadorId/calificacionPromedio").setValue(promedio)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
