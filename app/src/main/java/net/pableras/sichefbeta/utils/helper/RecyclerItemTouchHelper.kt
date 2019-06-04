//********************* <SWIPE> ******************************
// Clase totalmente portable a otras Apps
// solo tenéis que tener en cuenta que hay que poner:
// @+id/viewForeground y @+id/viewBackground a los dos RelativeLayout
// que hay en el FrameLayout del diseño de la rowcomentarios uno encima del otro.
// El viewBackground es el RelativeLayout rojo con la papelera que hay en el fondo
// El viewForeground es el RelativeLayout con el diseño de las views de la rowcomentarios del primer plano
package net.pableras.sichefbeta.utils.helper

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import net.pableras.sichefbeta.adapters.CustomAdapterLocal

/**
 * Created by pacopulido on 02/11/17.
 */

class RecyclerItemTouchHelper(dragDirs: Int,
                              swipeDirs: Int,
                              private val listener: RecyclerItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as CustomAdapterLocal.ViewHolder).viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                 viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                 actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as CustomAdapterLocal.ViewHolder).viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as CustomAdapterLocal.ViewHolder).viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as CustomAdapterLocal.ViewHolder).viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d("pabloChef", "${viewHolder.adapterPosition} $direction $viewHolder")
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}
//********************* <SWIPE> ******************************
