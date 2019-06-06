package net.pableras.sichefbeta.views.detail.fragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.rowcomentario.view.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.model.Comentario

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */

class MyItemRecyclerViewAdapter(private var mValues: ArrayList<Comentario>) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rowcomentario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.bind(item)
    }

    internal fun setComentarios(comentarios: ArrayList<Comentario>) {
        this.mValues = comentarios
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mValues.size

    class ViewHolder(viewlayout: View) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Comentario){

            itemView.tvnick.text = dataItem.nick
            itemView.tvcontent.text = dataItem.content

        }
    }

}
