package net.pableras.sichefbeta.views.recetasnube

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_recetas_nube.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.adapters.CustomAdapterNube
import net.pableras.sichefbeta.model.Receta
import net.pableras.sichefbeta.model.RecetaAux
import net.pableras.sichefbeta.model.User
import net.pableras.sichefbeta.views.detail.DetailActivity
import net.pableras.sichefbeta.views.home.HomeActivity

class RecetasNubeActivity : AppCompatActivity() {

    private lateinit var adapter: CustomAdapterNube
    lateinit var recetasFS: FirebaseFirestore

    lateinit var auth: FirebaseAuth
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_recetas_nube)

        recetasFS = FirebaseFirestore.getInstance()

        user = User()
        user = intent.getSerializableExtra("user") as User

        showRecetas()
        leerRecetas(user)
    }

    private fun leerRecetas(user: User){
        val docRef = recetasFS.collection("recetas")
        docRef.addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.w(HomeActivity.TAG, "Listen failed.", e)
                return@EventListener
            }

            val recetasAux = ArrayList<Receta>()
            for (doc in value!!) {
                val receta = doc.toObject(Receta::class.java)
                if (receta.uid != user.id) {
                    val recetaAux = doc.toObject(Receta::class.java)
                    recetasAux.add(recetaAux)
                    //Log.d(HomeActivity.TAG, recetasAux.toString())
                }
            }
            pintarArray(recetasAux)
        })
    }

    private fun showRecetas(){
        adapter = CustomAdapterNube(this, R.layout.rownube)
        rvRecetNube.layoutManager = LinearLayoutManager(this)
        rvRecetNube.adapter = adapter
    }

    fun pintarArray(recetasAux: ArrayList<Receta>) {
        //llama a la funcion que actualiza el RV
        adapter.setRecetasNube(recetasAux)
    }

    fun onClickNube(v: View){
        val receta = v.tag as Receta
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("receta", receta)
        startActivity(intent)
    }
}
