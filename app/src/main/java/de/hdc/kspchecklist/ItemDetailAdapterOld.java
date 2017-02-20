package de.hdc.kspchecklist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.io.IOException;
import java.util.ArrayList;

import de.hdc.kspchecklist.data.CheckListItem;
import de.hdc.kspchecklist.data.DataIO;

/**
 * Created by DerTroglodyt on 2017-02-16 08:53.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

class ItemDetailAdapterOld extends ArrayAdapter<CheckListItem> implements View.OnClickListener {

    public ItemDetailAdapterOld(Context context, String fileName, ArrayList<CheckListItem> objects) {
        super(context, 0, objects);
        this.context = context;
        this.fileName = fileName;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final CheckListItem item = getItem(position);
        if (BuildConfig.DEBUG) {
            if (item == null) {
                throw new RuntimeException();
            }
        }
        ItemDetailAdapterOld.ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ItemDetailAdapterOld.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_detail_content, parent, false);
            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.cb.setOnClickListener(this);
//            viewHolder.cb.setOnLongClickListener(this);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ItemDetailAdapterOld.ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.cb.setText(item.name);
        viewHolder.cb.setChecked(item.checked);
        viewHolder.cb.setTag(position);
        if (item.checked) {
            viewHolder.cb.setBackgroundColor(Color.argb(64, 0, 255, 0));
        } else {
            viewHolder.cb.setBackgroundColor(Color.argb(80, 255, 255, 0));
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View view) {
        CheckBox cb = (CheckBox) view;
        CheckListItem cli = CheckListItem.create(cb.getText().toString(), cb.isChecked());
        Integer i = (Integer) view.getTag();
        list.set(i, cli);
        try {
            DataIO.writeLocalFile(context, fileName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    private final Context context;
    private final String fileName;
    private final ArrayList<CheckListItem> list;
    // View lookup cache
    private static class ViewHolder {
        CheckBox cb;
    }
}
