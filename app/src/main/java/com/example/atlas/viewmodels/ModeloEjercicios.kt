import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.database
import com.example.atlas.objetosDB.Ejercicio
import com.example.atlas.objetosDB.sesionFuerza
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EjercicioGuardado(
    val firebaseKey: String = "",
    val ejercicio: Ejercicio = Ejercicio()
)

class EjercicioViewModel : ViewModel() {

    private val _ejerciciosSesion = MutableStateFlow<List<EjercicioGuardado>>(emptyList())
    val ejerciciosSesion = _ejerciciosSesion.asStateFlow()

    private var idFuerzaActual: String = ""
    private var velFuerza: ValueEventListener? = null
    fun iniciarSesionFuerza(idSesion: String) {
        if (idFuerzaActual.isNotEmpty()) {
            escucharEjercicios(idFuerzaActual)
            return
        }

        val refFuerza = database.getReference("SesionesFUERZA")
        val nuevoIdFuerza = refFuerza.push().key ?: return

        val nuevaSesionFuerza = sesionFuerza(sesionId = idSesion)

        refFuerza.child(nuevoIdFuerza).setValue(nuevaSesionFuerza)
            .addOnSuccessListener {
                database.getReference("Sesiones/$idSesion/idFuerza").setValue(nuevoIdFuerza)
                idFuerzaActual = nuevoIdFuerza
                escucharEjercicios(nuevoIdFuerza)
                Log.d("EjercicioViewModel", "SesionesFUERZA creado: $nuevoIdFuerza")
            }
    }

    private fun escucharEjercicios(idFuerza: String) {
        if (velFuerza != null) return

        val dbRef = database.getReference("SesionesFUERZA/$idFuerza/ejercicios")
        velFuerza = dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<EjercicioGuardado>()
                for (child in snapshot.children) {
                    val ej = Ejercicio(
                        idActividad = child.child("idActividad").getValue(String::class.java) ?: "",
                        nombre = child.child("nombre").getValue(String::class.java) ?: "",
                        grupoMuscular = child.child("grupoMuscular").getValue(String::class.java)
                            ?: "",
                        series = child.child("series").getValue(Int::class.java) ?: 0,
                        repeticiones = child.child("repeticiones").getValue(Int::class.java) ?: 0,
                        peso = child.child("peso").getValue(Double::class.java) ?: 0.0,
                        completado = child.child("completado").getValue(Boolean::class.java)
                            ?: false
                    )
                    lista.add(EjercicioGuardado(firebaseKey = child.key ?: "", ejercicio = ej))
                }
                _ejerciciosSesion.value = lista
                Log.d("EjercicioViewModel", "Ejercicios cargados: ${lista.size}")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("EjercicioViewModel", "Error leyendo ejercicios", error.toException())
            }
        })
    }
    fun toggleCompletado(firebaseKey: String, estadoActual: Boolean) {
        if (idFuerzaActual.isEmpty()) {
            Log.w("EjercicioViewModel", "No hay sesión activa para actualizar")
            return
        }

        val ref = database.getReference("SesionesFUERZA/$idFuerzaActual/ejercicios/$firebaseKey/completado")
        ref.setValue(!estadoActual)
    }
    fun guardarEjercicio(
        idActividad: String,
        nombre: String,
        grupoMuscular: String,
        series: Int,
        repeticiones: Int,
        peso: Double
    ) {
        if (idFuerzaActual.isEmpty()) {
            Log.w("EjercicioViewModel", "No hay sesión de fuerza activa")
            return
        }

        val dbRef = database.getReference("SesionesFUERZA/$idFuerzaActual/ejercicios")
        val nuevoKey = dbRef.push().key ?: return

        val ejercicio = Ejercicio(
            idActividad   = idActividad,
            nombre        = nombre,
            grupoMuscular = grupoMuscular,
            series        = series,
            repeticiones  = repeticiones,
            peso          = peso,
            completado    = false
        )

        dbRef.child(nuevoKey).setValue(ejercicio)
            .addOnSuccessListener {
                Log.d("EjercicioViewModel", "Ejercicio '$nombre' guardado con key $nuevoKey")
            }
            .addOnFailureListener {
                Log.e("EjercicioViewModel", "Error guardando ejercicio", it)
            }
    }

    override fun onCleared() {
        super.onCleared()
        if (idFuerzaActual.isNotEmpty() && velFuerza != null) {
            database.getReference("SesionesFUERZA/$idFuerzaActual/ejercicios")
                .removeEventListener(velFuerza!!)
        }
    }
}
