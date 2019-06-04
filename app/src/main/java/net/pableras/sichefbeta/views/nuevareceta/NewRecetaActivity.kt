package net.pableras.sichefbeta.views.nuevareceta

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_receta.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.model.Receta
import net.pableras.sichefbeta.model.User
import org.jetbrains.anko.toast

class NewRecetaActivity : AppCompatActivity() {
    companion object {
        const val TAG = "pablerasChefRegist"
    }

    lateinit var user: User
    lateinit var receta: Receta
    lateinit var recetasFS: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_receta)

        user = intent.getSerializableExtra("user") as User

        recetasFS = FirebaseFirestore.getInstance()

        btnSaveNewReceta.setOnClickListener { newReceta() }
        btncancel.setOnClickListener { finish() }
    }

    private fun newReceta() {
        receta = Receta()
        receta.uid = user.id
        receta.cid = ArrayList()

        if (etNewTitle.text.isEmpty()) {
            etNewTitle.error
            toast("todos los campos son obligatorios")
        }else{
            receta.title = etNewTitle.text.toString()

            if (etNewComens.text.isEmpty()) {
                etNewComens.error
                toast("todos los campos son obligatorios")
            }else{
                receta.comensales = etNewComens.text.toString()

                if (etNewIngred.text.isEmpty()) {
                    etNewIngred.error
                    toast("todos los campos son obligatorios")
                }else{
                    receta.ingredientes = etNewIngred.text.toString()

                    if (etNewPrep.text.isEmpty()) {
                        etNewPrep.error
                        toast("todos los campos son obligatorios")
                    }else {
                        receta.preparacion = etNewPrep.text.toString()

                        if (etNewObser.text.isNotEmpty()){
                            receta.observaciones = etNewObser.text.toString()
                        }
                        saveReceta(receta)
                    }
                }
            }
        }
    }

    fun saveReceta(receta: Receta){
        receta.id = recetasFS.collection("recetas").document().id

        recetasFS.collection("recetas").document(receta.id)
            .set(receta)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
             finish() }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e)
            error("No se ha podido guardar la receta") }
    }

}
