package de.hdc.kspchecklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import de.hdc.kspchecklist.data.ListItem
import java.util.*

/**
 * Created by DerTroglodyt on 2017-02-16 08:53.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

internal class ItemListAdapter(context: Context, objects: ArrayList<ListItem>)
    : ArrayAdapter<ListItem>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder // view lookup cache stored in tag

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.item_list_content, parent, false)
            viewHolder = ViewHolder(view.findViewById<View>(R.id.content) as TextView)
            // Cache the viewHolder object inside the fresh view
            view.tag = viewHolder
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.text = getItem(position).name

        return view
    }

    // View lookup cache
    private class ViewHolder(val name: TextView)
}
