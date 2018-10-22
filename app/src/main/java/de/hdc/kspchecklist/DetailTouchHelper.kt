package de.hdc.kspchecklist

import androidx.recyclerview.widget.*

/**
 * Created by DerTroglodyt on 2017-02-18 10:12.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

internal class DetailTouchHelper(private val adapter: ItemDetailAdapter)
    : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        adapter.swap(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //Remove item
        adapter.remove(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            adapter.setBGColor((viewHolder as ItemDetailAdapter.ViewHolder).cb, true)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        adapter.setBGColor((viewHolder as ItemDetailAdapter.ViewHolder).cb, false)
        super.clearView(recyclerView, viewHolder)
    }
}
