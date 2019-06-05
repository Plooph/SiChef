package net.pableras.sichefbeta.views.detail.fragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_item.view.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.model.Comentario

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */

class MyItemRecyclerViewAdapter : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private var mValues: ArrayList<Comentario> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    internal fun setComentarios(comentarios: ArrayList<Comentario>) {
        this.mValues = comentarios
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_nick
        val mContentView: TextView = mView.content
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.nick
        holder.mContentView.text = item.content
    }
}
