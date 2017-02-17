package de.hdc.kspchecklist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        String message = intent.getStringExtra(DETAIL_MESSAGE);
        fileName = message + ".txt";
        setTitle(message);

        // Construct the data source
        try {
            list = DataIO.readLocalFile(this.getApplicationContext(), fileName);
            // Create the adapter to convert the array to views
            adapter = new ItemDetailAdapter(this.getApplicationContext(), fileName, list);
            ListView listView = (ListView) findViewById(R.id.item_detail_container);
            listView.setAdapter(adapter);
            registerForContextMenu(listView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check_all:
                for (int i = 0; i < list.size(); i++) {
                    CheckListItem cli = list.get(i);
                    list.set(i, CheckListItem.create(cli.name, setChecked));
                }
                setChecked = !setChecked;
                saveList();
                adapter.notifyDataSetChanged();
                return true;

            case R.id.action_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New item");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckListItem cli = CheckListItem.create(input.getText().toString());
                        list.add(cli);
                        saveList();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(list.get(info.position).name);
        String[] menuItems = getResources().getStringArray(R.array.menu_detail);
        for (int i = 0; i<menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case 0: {  // edit
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New name");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        CheckListItem cli = CheckListItem.create(name, list.get(info.position).checked);
                        list.set(info.position, cli);
                        try {
                            DataIO.writeLocalFile(getApplicationContext(), fileName, list);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            }
            case 1: {  // delete
                list.remove(info.position);
                adapter.notifyDataSetChanged();
                return true;
            }
            default: return false;
        }
    }

    private boolean setChecked;
    private String fileName;
    private ArrayList<CheckListItem> list;
    private ItemDetailAdapter adapter;

    private void saveList() {
        try {
            DataIO.writeLocalFile(getApplicationContext(), fileName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
