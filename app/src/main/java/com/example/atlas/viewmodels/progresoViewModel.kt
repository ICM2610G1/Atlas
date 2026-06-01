package com.example.atlas.viewmodels


import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.mStorageRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProgresoUiState(
    val objetivo: String = "",
    val metaCalorica: String = "",
    val fecha: String = "",
    val fotos: List<FotoProgreso> = emptyList(),
    val enableObjetivo: Boolean = false,
    val enableMetacalorica: Boolean = false,
    val enableFecha: Boolean = false,
)

class ProgresoViewModel : ViewModel() {

    private val _progreso = MutableStateFlow(ProgresoUiState())
    val progreso = _progreso.asStateFlow()

    fun updateObjetivo(valor: String) =
        _progreso.update { it.copy(objetivo = valor) }

    fun updateMetaCaloria(valor: String) =
        _progreso.update { it.copy(metaCalorica = valor) }

    fun updateFecha(valor: String) =
        _progreso.update { it.copy(fecha = valor) }

    fun updateEnableObjetivo(valor: Boolean) =
        _progreso.update { it.copy(enableObjetivo = valor) }

    fun updateEnableMetacalorica(valor: Boolean) =
        _progreso.update { it.copy(enableMetacalorica = valor) }

    fun updateEnableFecha(valor: Boolean) =
        _progreso.update { it.copy(enableFecha = valor) }

    fun guardarDatos() {
        val uid = auth.currentUser?.uid ?: return
        val state = _progreso.value
        val datos = mapOf(
            "objetivo" to state.objetivo,
            "metaCalorica" to state.metaCalorica,
            "fecha" to state.fecha
        )
        database.getReference("progreso/$uid").updateChildren(datos)
    }

    fun subirFoto(uri: Uri) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val nombreArchivo = "${System.currentTimeMillis()}.jpg"
            val imageRef = mStorageRef.child("images/evolucion/$uid/$nombreArchivo")

            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uriNube ->
                        val url = uriNube.toString()
                        val foto = FotoProgreso(
                            url = url,
                            fecha = System.currentTimeMillis()
                        )

                        database.getReference("progreso/$uid/fotos").push().setValue(foto)
                        _progreso.update { s ->
                            s.copy(fotos = s.fotos + foto)
                        }
                    }
                }
        }
    }
        fun cargarFotos() {

            val uid = auth.currentUser?.uid ?: return

            val dbReference = database.getReference("progreso/$uid/fotos")

            dbReference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val listaFotos = mutableListOf<FotoProgreso>()
                    for (child in snapshot.children) {
                        val foto = child.getValue(FotoProgreso::class.java)
                        foto?.let {
                            listaFotos.add(it)
                        }
                    }

                    _progreso.update {
                        it.copy(fotos = listaFotos)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

    }
data class FotoProgreso(
    val url: String = "",
    val fecha: Long = 0
)