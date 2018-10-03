package de.hdc.kspchecklist

import android.content.*
import android.graphics.*
import android.view.*
import android.widget.*
import de.hdc.kspchecklist.data.*
import java.io.*
import java.util.*

/**
 * Created by DerTroglodyt on 2017-02-16 08:53.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

internal class ItemDetailAdapterOld(private val aContext: Context, private val fileName: String, private val list: ArrayList<CheckListItem>)
    : ArrayAdapter<CheckListItem>(aContext, 0, list), View.OnClickListener {

    override fun getView(position: Int, aConvertView: View?, parent: ViewGroup): View {
        var convertView = aConvertView
        val item = getItem(position)
        if (BuildConfig.DEBUG) {
            if (item == null) {
                throw RuntimeException()
            }
        }
        val viewHolder: ItemDetailAdapterOld.ViewHolder // view lookup cache stored in tag

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = ItemDetailAdapterOld.ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.item_detail_content, parent, false)
            viewHolder.cb = convertView!!.findViewById(R.id.checkbox)
            viewHolder.cb!!.setOnClickListener(this)
            //            viewHolder.cb.setOnLongClickListener(this);
            // Cache the viewHolder object inside the fresh view
            convertView.tag = viewHolder
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = convertView.tag as ItemDetailAdapterOld.ViewHolder
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        assert(item != null)
        viewHolder.cb!!.text = item!!.name
        viewHolder.cb!!.isChecked = item.checked
        viewHolder.cb!!.tag = position
        if (item.checked) {
            viewHolder.cb!!.setBackgroundColor(Color.argb(64, 0, 255, 0))
        } else {
            viewHolder.cb!!.setBackgroundColor(Color.argb(80, 255, 255, 0))
        }

        // Return the completed view to render on screen
        return convertView
    }

    override fun onClick(view: View) {
        val cb = view as CheckBox
        val cli = CheckListItem(cb.text.toString(), cb.isChecked)
        val i = view.getTag() as Int
        list[i] = cli
        try {
            DataIO.writeLocalFile(aContext, fileName, list)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        notifyDataSetChanged()
    }

    // View lookup cache
    private class ViewHolder {
        internal var cb: CheckBox? = null
    }
}
