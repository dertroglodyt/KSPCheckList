package de.hdc.kspchecklist;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by DerTroglodyt on 2017-02-18 10:12.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

class DetailTouchHelper extends ItemTouchHelper.SimpleCallback {

    DetailTouchHelper(ItemDetailAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Remove item
        adapter.remove(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            adapter.setBGColor(((ItemDetailAdapter.ViewHolder) viewHolder).cb, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        adapter.setBGColor(((ItemDetailAdapter.ViewHolder) viewHolder).cb, false);
        super.clearView(recyclerView, viewHolder);
    }

    private final ItemDetailAdapter adapter;
}
