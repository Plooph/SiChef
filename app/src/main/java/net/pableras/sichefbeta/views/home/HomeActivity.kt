package net.pableras.sichefbeta.views.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.support.v7.widget.SearchView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.adapters.CustomAdapterLocal
import net.pableras.sichefbeta.model.Receta
import net.pableras.sichefbeta.model.User
import net.pableras.sichefbeta.utils.helper.RecyclerItemTouchHelper
import net.pableras.sichefbeta.views.auth.LogActivity
import net.pableras.sichefbeta.views.detail.DetailActivity
import net.pableras.sichefbeta.views.nuevareceta.NewRecetaActivity
import net.pableras.sichefbeta.views.recetasnube.RecetasNubeActivity
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, SearchView.OnQueryTextListener {
    companion object {
        const val TAG = "pablerasChef"
    }

    private lateinit var searchView: SearchView
    private lateinit var recetasAL: ArrayList<Receta>
    private lateinit var adapter: CustomAdapterLocal
    lateinit var recetasFS: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private lateinit var user: User
    private lateinit var recetas: ArrayList<Receta>
    //header box
    private lateinit var tvnickcajon: TextView
    private lateinit var tvemailcajon: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        nav_view.itemIconTintList = null
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { newReceta(user) }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        /***************************** MI CODIGO ********************************/
        auth = FirebaseAuth.getInstance()
        recetasFS = FirebaseFirestore.getInstance()

        user = User()
        user = intent.getSerializableExtra("loguser") as User
        sharedUser(user)

        //Hay que acceder a los elementos del header
        tvnickcajon = nav_view.getHeaderView(0).tvnickcajon
        tvemailcajon = nav_view.getHeaderView(0).tvemailcajon
        refresCajon(user)

        recetas = ArrayList()
        showRecetas()
        leerRecetas(user)

        /***************************** MI CODIGO ********************************/
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        /***************************** FILTRO ********************************/
        val searchItem = menu.findItem(R.id.app_bar_search)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setOnQueryTextListener(this)
        /***************************** FILTRO ********************************/
        return true
    }
    /***************************** FILTRO ********************************/
    override fun onQueryTextChange(query: String): Boolean {
        recetas.clear()
        recetas.addAll(recetasAL.filter { p -> p.title.contains(query) })
        adapter.notifyDataSetChanged()
        return false
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        return false
    }
    /***************************** FILTRO ********************************/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.app_bar_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                //sacar los datos del usuario.
            }
            R.id.nav_nube -> {
                goRecetasNube(user)
            }
            R.id.nav_slideshow -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LogActivity::class.java))
                deleteshared()
                finish()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    /***************************** MI CODIGO ********************************/

    /***************************** REFRESCAR CAJON ********************************/
    private fun refresCajon(user: User){
        //Log.d(TAG, "vacio ${user.nick}")
        toast("Hola ${user.nick}")

        tvnickcajon.text = user.nick
        tvemailcajon.text = user.email
    }
    /***************************** REFRESCAR CAJON ********************************/

    /***************************** PINTAR RECETAS ********************************/
    private fun leerRecetas(user: User){
        val docRef = recetasFS.collection("recetas")
            .whereEqualTo("uid", user.id)
        docRef.addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@EventListener
            }

            recetas = ArrayList()
            recetasAL = ArrayList()
            for (doc in value!!) {
                val receta = doc.toObject(Receta::class.java)
                recetas.add(receta)
                recetasAL.add(receta)
                //Log.d(TAG, recetas.toString())
            }
            pintarArray(recetas)
        })
    }

    private fun showRecetas(){
        adapter = CustomAdapterLocal(this, R.layout.rowlocal)
        rvRecetLocal.layoutManager = LinearLayoutManager(this)
        rvRecetLocal.adapter = adapter

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvRecetLocal)
    }

    fun pintarArray(recetas: ArrayList<Receta>) {
        //llama a la funcion que actualiza el RV
        adapter.setRecetas(recetas)
    }

    /***************************** PINTAR RECETAS ********************************/

    private fun newReceta(user: User) {
        val intent = Intent(this, NewRecetaActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    fun onClickReceta(v: View){
        val receta = v.tag as Receta
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("receta", receta)
        startActivity(intent)
    }

    private fun goRecetasNube(user: User) {
        val intent = Intent(this, RecetasNubeActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    /***************************** SWIPE ********************************/
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is CustomAdapterLocal.ViewHolder) {

            // Guardar el objeto amigo por si nos deshacen
            val deletedIndex = viewHolder.adapterPosition

            //aqui salta el error
            val deletedItem = recetas[deletedIndex]
            toast("la receta en la posicion ${deletedItem.title}")

            // Obtener el nombre de la receta que nos vamos a calzar
            val name = deletedItem.title

            // Nos lo calzamos del recycler
            adapter.removeItem(deletedItem, deletedIndex)

            // Mostamos la tostada Snackbar por si desean deshacer
            // Este coordinatorLayout es el contenedor principal del activity_main
            // al que le pusimos este @+id/
            val snackbar = Snackbar
                .make(drawer_layout, "$name, borrado de la carta!", Snackbar.LENGTH_LONG)
            snackbar.setAction("Deshacer", View.OnClickListener {
                // Si pulsan sobre deshacer restauramos el amigo eliminado anteriormente
                adapter.restoreItem(deletedItem, deletedIndex)
            })
            snackbar.setActionTextColor(Color.YELLOW) // Deshacer sale amarillo
            snackbar.show()
        }
    }
    /***************************** SWIPE ********************************/

    private fun sharedUser(user: User) {
        val preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE)

        val uid = preferencias.getString("usuarioSP","nosta")

        if (uid == "nosta") {
            val jsonUser: String = Gson().toJson(user)
            val editor = preferencias.edit()
            editor.putString("usuarioSP", jsonUser)
            editor.apply()
        }
    }

    private fun deleteshared() {
        val preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE)

        val editor = preferencias.edit()
        editor.clear()
        editor.apply()
    }

    /***************************** MI CODIGO ********************************/
}
