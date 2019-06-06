package net.pableras.sichefbeta.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.rowlocal.view.*
import net.pableras.sichefbeta.model.Receta

class CustomAdapterLocal (val context: Context,
                          val layout: Int) : RecyclerView.Adapter<CustomAdapterLocal.ViewHolder>() {

    private var dataList: ArrayList<Receta> = ArrayList()
    lateinit var recetasFS: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setRecetas(recetas: ArrayList<Receta>) {
        this.dataList = recetas
        notifyDataSetChanged()
    }

    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {

        var viewForeground: ConstraintLayout? = itemView.viewForeground

        fun bind(dataItem: Receta){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            itemView.tvTitleLocal.text = dataItem.title
            itemView.tvNumComensalesLocal.text = dataItem.comensales

            itemView.tag = dataItem

            //val id = context.resources.getIdentifier(dataItem.foto,"drawable",context.packageName)
            //itemView.ivrow.setImageResource(id)
            // Si la foto viene de Internet
            // implementation 'com.squareup.picasso:picasso:2.5.2'
            // Picasso.with(context).load(dataItem.foto).into(itemView.ivRow)
            //itemView.setTag(dataItem)
        }
    }
    //********************* <SWIPE> ******************************
    fun removeItem(dataItem: Receta, position: Int) {
        recetasFS = FirebaseFirestore.getInstance()
        // remove of recyclerview
        dataList.removeAt(position)
        notifyItemRemoved(position)
        //delete receta from firestore
        recetasFS.collection("recetas").document(dataItem.id)
            .delete()
            .addOnSuccessListener { Log.d("adios receta", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("adios receta", "Error deleting document", e) }
    }

    fun restoreItem(dataItem: Receta, position: Int) {
        recetasFS = FirebaseFirestore.getInstance()
        // add of recyclerview
        dataList.add(position, dataItem)
        notifyItemInserted(position)
        // add of firestore
        recetasFS.collection("recetas").document(dataItem.id)
            .set(dataItem)
            .addOnSuccessListener { Log.d("hola receta", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("hola receta", "Error deleting document", e) }
    }
    //********************* </SWIPE> ******************************
}