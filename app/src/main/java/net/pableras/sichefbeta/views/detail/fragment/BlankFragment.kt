package net.pableras.sichefbeta.views.detail.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_blank.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.adapters.CustomAdapterComentarios
import net.pableras.sichefbeta.model.Comentario
import org.jetbrains.anko.support.v4.find

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BlankFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BlankFragment : Fragment() {
    private lateinit var adapter: CustomAdapterComentarios
    private lateinit var idrecet: String
    private lateinit var comentarios: ArrayList<Comentario>
    private lateinit var data: Bundle
    lateinit var recetasFS: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recetasFS = FirebaseFirestore.getInstance()
        comentarios = ArrayList()


        if (this.arguments != null){
            data = this.arguments!!
            idrecet = data.getString("comentarios")
            //Log.d("AATengoId",  idrecet)
            getComentarios(idrecet)
        }else{
            Log.d("elBundledelfragment", "el data está vacio")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank, container, false)

        val contesto = activity as Context

        adapter = CustomAdapterComentarios(contesto, R.layout.rowcomentario)
        val rvC = view.findViewById<RecyclerView>(R.id.rvComentarios)
        rvC.layoutManager = LinearLayoutManager(contesto)
        rvC.adapter = adapter

        return view
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */

    companion object {

        const val ARG_RECETA_ID = "comentarios"

        @JvmStatic
        fun newInstance(recetaId: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RECETA_ID, recetaId)
                }
            }
    }

    fun getComentarios(idrecet: String?) {
        val docRef = recetasFS.collection("comentarios")
        docRef.addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.w("ERROR", "Listen failed.", e)
                return@EventListener
            }

            comentarios  = ArrayList()
            for (doc in value!!) {
                val comentario = doc.toObject(Comentario::class.java)
                if (idrecet == comentario.rid) {
                    comentarios.add(comentario)
                }
            }
            //Log.d("AALosTengo",  comentarios.toString())
            adapter.setComentarios(comentarios)
        })
    }
}
