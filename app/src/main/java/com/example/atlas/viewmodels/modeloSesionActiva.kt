import androidx.lifecycle.ViewModel
import com.example.atlas.database
import com.example.atlas.objetosDB.Sesion
import com.example.atlas.objetosDB.sesionAerobica
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SesionActivaViewModel : ViewModel() {
    private val _sesionTrote = MutableStateFlow<sesionAerobica?>(null)
    val sesionTrote: StateFlow<sesionAerobica?> = _sesionTrote.asStateFlow()
    private var sesionesListener: ValueEventListener? = null
    private var troteListener: ValueEventListener? = null

    fun cargarSesionActiva(userId: String) {
        sesionesListener = database.getReference("Sesiones")
            .orderByChild("userId")
            .equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (hijo in snapshot.children) {
                        val sesion = hijo.getValue<Sesion>() ?: continue
                        if (!sesion.completado && sesion.idAerobico.isNotEmpty()) {
                            cargarTrote(sesion.idAerobico)
                            return
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun cargarTrote(idAerobico: String) {
        troteListener = database.getReference("SesionesTrote").child(idAerobico)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _sesionTrote.value = snapshot.getValue<sesionAerobica>()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}