package net.pableras.sichefbeta.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.rowlocal.view.*
import kotlinx.android.synthetic.main.rownube.view.*
import net.pableras.sichefbeta.model.Receta

class CustomAdapterNube (val context: Context,
                         val layout: Int): RecyclerView.Adapter<CustomAdapterNube.ViewHolder>()
{
    private var dataList: List<Receta> = emptyList()

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

    internal fun setRecetasNube(recetas: List<Receta>) {
        this.dataList = recetas
        notifyDataSetChanged()
    }

    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Receta){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem

            itemView.tvTitleNube.text = dataItem.title
            itemView.tvNumComensalesNube.text = dataItem.comensales

            itemView.tag = dataItem

            //val id = context.resources.getIdentifier(dataItem.foto,"drawable",context.packageName)
            //itemView.ivrow.setImageResource(id)
            // Si la foto viene de Internet
            // implementation 'com.squareup.picasso:picasso:2.5.2'
            // Picasso.with(context).load(dataItem.foto).into(itemView.ivRow)
            //itemView.setTag(dataItem)
        }

    }
}