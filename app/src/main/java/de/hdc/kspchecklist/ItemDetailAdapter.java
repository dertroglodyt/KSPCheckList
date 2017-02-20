package de.hdc.kspchecklist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import de.hdc.kspchecklist.data.CheckListItem;
import de.hdc.kspchecklist.data.DataIO;

/**
 * Created by DerTroglodyt on 2017-02-18 09:33.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder>
        implements View.OnClickListener {   //}, View.OnCreateContextMenuListener {

    ItemDetailAdapter(Context context, String fileName, ArrayList<CheckListItem> objects) {
//        super(context, 0, objects);
        this.context = context;
        this.fileName = fileName;
        this.list = objects;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_content, parent, false);
        //CheckBox v = (CheckBox) test.findViewById(R.id.checkbox);
//        CheckBox v = (CheckBox) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_detail_content, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cb.setText(list.get(position).name);
        holder.cb.setChecked(list.get(position).checked);
        holder.cb.setTag(position);
        setBGColor(holder.cb, false);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    void remove(int position) {
        list.remove(position);
        try {
            DataIO.writeLocalFile(context, fileName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyItemRemoved(position);
    }

    void swap(int firstPosition, int secondPosition){
        Collections.swap(list, firstPosition, secondPosition);
        try {
            DataIO.writeLocalFile(context, fileName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyItemMoved(firstPosition, secondPosition);
    }

    void setBGColor(CheckBox cb, boolean isDragging) {
        if (isDragging) {
            cb.setBackgroundColor(Color.CYAN);
        } else {
            if (cb.isChecked()) {
                cb.setBackgroundColor(Color.argb(64, 0, 255, 0));
            } else {
                cb.setBackgroundColor(Color.argb(128, 255, 255, 0));
            }
        }
    }

    @Override
    public void onClick(View view) {
        CheckBox cb = (CheckBox) view;
        setBGColor(cb, false);
        CheckListItem cli = CheckListItem.create(cb.getText().toString(), cb.isChecked());
        Integer i = (Integer) view.getTag();
        list.set(i, cli);
        try {
            DataIO.writeLocalFile(context, fileName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        notifyDataSetChanged();
    }

//    @Override
//    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
//        if (menuInfo == null ) {
//            return;
//        }
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        menu.setHeaderTitle(list.get(info.position).name);
//        String[] menuItems = context.getResources().getStringArray(R.array.menu_detail);
//        for (int i = 0; i<menuItems.length; i++) {
//            menu.add(Menu.NONE, i, i, menuItems[i]);
//        }
//    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        final CheckBox cb;

        ViewHolder(View v, ItemDetailAdapter adapter) {
            super(v);
            cb = (CheckBox) v.findViewById(R.id.checkbox);
            cb.setOnClickListener(adapter);
//            cb.setOnCreateContextMenuListener(adapter);
        }
    }

    private final Context context;
    private final String fileName;
    private final ArrayList<CheckListItem> list;
}
