package net.pableras.sichefbeta.views.detail.fragment

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
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.model.Comentario

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemFragment.OnListFragmentInteractionListener] interface.
 */

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ItemFragment : Fragment() {

    //private lateinit var adapter: MyItemRecyclerViewAdapter
    private var columnCount = 1
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
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyItemRecyclerViewAdapter(comentarios)
            }
        }

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
            ItemFragment().apply {
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
        })
    }
}
