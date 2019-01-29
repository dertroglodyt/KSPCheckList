package de.hdc.kspchecklist

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import de.hdc.kspchecklist.data.CheckListPersistenceSource
import de.hdc.kspchecklist.domain.CheckListItem
import java.io.IOException
import java.util.*

/**
 * Created by DerTroglodyt on 2017-02-18 09:33.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

internal class ItemDetailAdapter(
    private val context: Context,
    private val persistence: CheckListPersistenceSource,
    private val fileName: String,
    private val list: ArrayList<CheckListItem>
) : RecyclerView.Adapter<ItemDetailAdapter.ViewHolder>(), View.OnClickListener {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDetailAdapter.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_content, parent, false)
        //CheckBox v = (CheckBox) test.findViewById(R.id.checkbox);
        //        CheckBox v = (CheckBox) LayoutInflater.from(parent.getContext())
        //                .inflate(R.layout.item_detail_content, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v, this)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cb.text = list[position].name
        holder.cb.isChecked = list[position].checked
        holder.cb.tag = position
        setBGColor(holder.cb, false)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return list.size
    }

    fun remove(position: Int) {
        list.removeAt(position)
        try {
            persistence.saveList(fileName, list)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        notifyItemRemoved(position)
    }

    fun swap(firstPosition: Int, secondPosition: Int) {
        Collections.swap(list, firstPosition, secondPosition)
        try {
            persistence.saveList(fileName, list)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        notifyItemMoved(firstPosition, secondPosition)
    }

    fun setBGColor(cb: CheckBox, isDragging: Boolean) {
        if (isDragging) {
            cb.setBackgroundColor(Color.CYAN)
        } else {
            if (cb.isChecked) {
                cb.setBackgroundColor(Color.argb(64, 0, 255, 0))
            } else {
                cb.setBackgroundColor(Color.argb(128, 255, 255, 0))
            }
        }
    }

    override fun onClick(view: View) {
        val cb = view as CheckBox
        setBGColor(cb, false)
        val cli = CheckListItem(cb.text.toString(), cb.isChecked)
        val i = view.getTag() as Int
        list[i] = cli
        try {
            persistence.saveList(fileName, list)
        } catch (e: IOException) {
            e.printStackTrace()
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
    internal class ViewHolder(v: View, adapter: ItemDetailAdapter) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        val cb: CheckBox = v.findViewById<View>(R.id.checkbox) as CheckBox

        init {
            cb.setOnClickListener(adapter)
            //            cb.setOnCreateContextMenuListener(adapter);
        }
    }
}
