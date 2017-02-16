package de.hdc.kspchecklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import de.hdc.kspchecklist.data.CheckListItem;
import de.hdc.kspchecklist.data.DataIO;

import static de.hdc.kspchecklist.ItemListActivity.DETAIL_MESSAGE;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();
        String message = intent.getStringExtra(DETAIL_MESSAGE);
        fileName = message + ".txt";
        TextView header = (TextView) findViewById(R.id.header);
        header.setText(message);

        // Construct the data source
        try {
            list = DataIO.readLocalFile(this.getApplicationContext(), fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create the adapter to convert the array to views
        ItemDetailAdapter adapter = new ItemDetailAdapter(this.getApplicationContext(), list);
        ListView listView = (ListView) findViewById(R.id.item_detail_container);
        listView.setAdapter(adapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    /** Called when the user clicks the checkbox */
    public void setChecked(View view) {
        CheckBox cb = (CheckBox) view;
        CheckListItem cli = CheckListItem.create(cb.getText().toString(), cb.isChecked());
        list.set((int) view.getTag(), cli);
        try {
            DataIO.writeLocalFile(getApplicationContext(), fileName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<CheckListItem> list;
    private String fileName;
}
