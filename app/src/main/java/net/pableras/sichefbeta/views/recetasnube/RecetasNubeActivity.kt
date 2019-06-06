package net.pableras.sichefbeta.views.recetasnube

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
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
//SearchView.OnQueryTextListener
class RecetasNubeActivity : AppCompatActivity() {

    private lateinit var recetas: ArrayList<Receta>
    private lateinit var recetasAL: ArrayList<Receta>
    //private lateinit var searchView: SearchView
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.home, menu)
//        /***************************** FILTRO ********************************/
//        val searchItem = menu.findItem(R.id.app_bar_search)
//        searchView = searchItem.actionView as SearchView
//        searchView.queryHint = "Search..."
//        searchView.setOnQueryTextListener(this)
//        /***************************** FILTRO ********************************/
//        return true
//    }
//    /***************************** FILTRO ********************************/
//    override fun onQueryTextChange(query: String): Boolean {
//        recetas.clear()
//        recetas.addAll(recetasAL.filter { p -> p.title.contains(query) })
//        adapter.notifyDataSetChanged()
//        return false
//    }
//
//    override fun onQueryTextSubmit(text: String): Boolean {
//        return false
//    }
//    /***************************** FILTRO ********************************/

    private fun leerRecetas(user: User){
        val docRef = recetasFS.collection("recetas")
        docRef.addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.w(HomeActivity.TAG, "Listen failed.", e)
                return@EventListener
            }

            recetas = ArrayList()
            recetasAL = ArrayList()
            for (doc in value!!) {
                val receta = doc.toObject(Receta::class.java)
                if (receta.uid != user.id) {
                    recetas.add(receta)
                    recetasAL.add(receta)
                    //Log.d(HomeActivity.TAG, recetasAux.toString())
                }
            }
            pintarArray(recetas)
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
