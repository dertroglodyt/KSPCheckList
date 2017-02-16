package de.hdc.kspchecklist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdc.kspchecklist.data.DataIO;
import de.hdc.kspchecklist.data.ListItem;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    public final static String DETAIL_MESSAGE = "DETAIL_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        setTitle("KSP Checklist");

        /**
         * If installed and run for the first time, copy assets files to local storage.
         */
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun) {
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.apply();

            DataIO.copyAssetsFiles(getApplicationContext());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Construct the data source
        ArrayList<ListItem> items = new ArrayList<>();
        List<String> list = DataIO.getDirList(this.getApplicationContext());
        for (String s : list) {
            items.add(ListItem.create(s));
        }
        // Create the adapter to convert the array to views
        ItemListAdapter adapter = new ItemListAdapter(this, items);

        ListView listView = (ListView) findViewById(R.id.item_list);
        assert listView != null;
        listView.setAdapter(adapter);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
          Intent intent = new Intent(this, ItemDetailActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
        intent.putExtra(DETAIL_MESSAGE, ((TextView) view).getText());
        startActivity(intent);
    }

}
