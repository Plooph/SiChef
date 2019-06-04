package net.pableras.sichefbeta.views.detail

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.app_bar_home.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.model.Comentario
import net.pableras.sichefbeta.model.Receta
import net.pableras.sichefbeta.model.User
import net.pableras.sichefbeta.views.detail.fragment.ItemFragment
import net.pableras.sichefbeta.views.detail.fragment.RecetaFragment
import net.pableras.sichefbeta.views.detail.ui.main.SectionsPagerAdapter
import net.pableras.sichefbeta.views.nuevareceta.NewRecetaActivity
import org.jetbrains.anko.*


class DetailActivity : AppCompatActivity() {

    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    lateinit var receta: Receta
    lateinit var recetasFS: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        recetasFS = FirebaseFirestore.getInstance()
        receta = Receta()
        receta = intent.getSerializableExtra("receta") as Receta
        //getComentarios(receta)

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        //viewPager.adapter = sectionsPagerAdapter
        detalleReceta(viewPager)

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

            fab.setOnClickListener { addComentario() }

    }

    private fun getComentarios(receta: Receta) {
        //listcomentarios = ArrayList()

        val idcomntarios = receta.cid
        for (cid in idcomntarios){
            val docRef = recetasFS.collection("comentarios").document(cid)
            docRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
                if (e != null) {
                    Log.w("error", "Listen failed.", e)
                    return@EventListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val comentario = snapshot.toObject(Comentario::class.java)
                    if (comentario != null) {
                        //listcomentarios.add(comentario)
                    }
                } else {
                    Log.d("NoComents", "sin comentarios")
                }
            })
        }
    }

    private fun detalleReceta(viewPager: ViewPager) {

        val recet = Bundle()
        recet.putSerializable("receta", receta)
        val fragmentRecetas = RecetaFragment.newInstance(receta)
        fragmentRecetas.arguments = recet
        sectionsPagerAdapter.addFragment(fragmentRecetas)

//        val coment = Bundle()
//        coment.putSerializable("comentarios", listcomentarios)
//        val fragmentComentarios = ComentariosFragment.newInstance(listcomentarios)
//        fragmentComentarios.arguments = coment
        sectionsPagerAdapter.addFragment(ItemFragment())

        viewPager.adapter = sectionsPagerAdapter
    }

    private fun addComentario() {

        val preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE)
        val jsonUser = preferencias.getString("usuarioSP","nosta")
        val usuario: User = Gson().fromJson(jsonUser, User::class.java)
        val nick = usuario.nick

        alert {
            title = receta.title
            customView {
                verticalLayout {
                    lparams(width = wrapContent, height = wrapContent)
                    val etContent = editText {
                        hint = "Comente"
                        padding = dip(16)
                    }
                    positiveButton("Aceptar") {
                        if (etContent.text.toString().isEmpty())
                            toast("Campos Obligatorios")
                        else {
                            val contenido = etContent.text.toString()
                                saveComentario(Comentario("", nick, receta.id, contenido))
                        }
                    }
                }
            }
        }.show()
    }

    private fun saveComentario(comentario: Comentario) {
        comentario.id = recetasFS.collection("comentarios").document().id

        recetasFS.collection("comentarios").document(comentario.id)
            .set(comentario)
            .addOnSuccessListener { Log.d("El comentario", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(NewRecetaActivity.TAG, "Error writing document", e)
                error("No se ha podido guardar la receta") }
    }
}
