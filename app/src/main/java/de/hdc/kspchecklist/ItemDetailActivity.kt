package de.hdc.kspchecklist

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import de.hdc.kspchecklist.data.CheckListItem
import de.hdc.kspchecklist.data.DataIO
import java.io.IOException
import java.util.*

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class ItemDetailActivity : AppCompatActivity() {

    //    @Override
    //    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
    //        if (menuInfo == null ) {
    //            return;
    //        }
    //        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    //        menu.setHeaderTitle(list.get(info.position).name);
    //        String[] menuItems = getResources().getStringArray(R.array.menu_detail);
    //        for (int i = 0; i<menuItems.length; i++) {
    //            menu.add(Menu.NONE, i, i, menuItems[i]);
    //        }
    //    }
    //
    //    @Override
    //    public boolean onContextItemSelected(MenuItem item) {
    //        if (item == null ) {
    //            return false;
    //        }
    //        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    //        switch (item.getItemId()) {
    //            case 0: {  // edit
    //                AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //                builder.setTitle("New name");
    //
    //                final EditText input = new EditText(this);
    //                input.setInputType(InputType.TYPE_CLASS_TEXT);
    //                builder.setView(input);
    //                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    //                    @Override
    //                    public void onClick(DialogInterface dialog, int which) {
    //                        String name = input.getText().toString();
    //                        CheckListItem cli = CheckListItem.create(name, list.get(info.position).checked);
    //                        list.set(info.position, cli);
    //                        try {
    //                            DataIO.writeLocalFile(getApplicationContext(), fileName, list);
    //                        } catch (IOException e) {
    //                            e.printStackTrace();
    //                        }
    //                        adapter.notifyDataSetChanged();
    //                    }
    //                });
    //                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    //                    @Override
    //                    public void onClick(DialogInterface dialog, int which) {
    //                        dialog.cancel();
    //                    }
    //                });
    //
    //                builder.show();
    //                return true;
    //            }
    //            case 1: {  // delete
    //                list.remove(info.position);
    //                adapter.notifyDataSetChanged();
    //                return true;
    //            }
    //            default: return false;
    //        }
    //    }

    private var setChecked: Boolean = false
    private var fileName: String? = null
    private var list: ArrayList<CheckListItem>? = null
    //    private ItemDetailAdapter adapter;
    private var adapter: ItemDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val myToolbar = findViewById<View>(R.id.list_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        val intent = intent
        val message = intent.getStringExtra(ItemListActivity.DETAIL_MESSAGE)
        fileName = message + ".txt"
        title = message

        // Construct the data source
        try {
            list = DataIO.readLocalFile(this.applicationContext, fileName ?: "")
            // Create the adapter to convert the array to views
            //            adapter = new ItemDetailAdapter(this.getApplicationContext(), fileName, list);
            adapter = ItemDetailAdapter(this, fileName ?: "", list!!)
            val listView = findViewById<View>(R.id.item_detail_container) as RecyclerView
            listView.layoutManager = LinearLayoutManager(this)
            listView.setHasFixedSize(true)
            listView.adapter = adapter
            //            registerForContextMenu(listView);
            val helper = ItemTouchHelper(DetailTouchHelper(adapter!!))
            helper.attachToRecyclerView(listView)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu items for use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_check_all -> {
                for (i in list!!.indices) {
                    val cli = list!![i]
                    list!![i] = CheckListItem(cli.name, setChecked)
                }
                setChecked = !setChecked
                saveList()
                adapter!!.notifyDataSetChanged()
                return true
            }

            R.id.action_add -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("New item")

                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton("OK") { dialog, which ->
                    val cli = CheckListItem(input.text.toString(), false)
                    list!!.add(cli)
                    saveList()
                    adapter!!.notifyDataSetChanged()
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

                builder.show()

                return true
            }

            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }

    private fun saveList() {
        try {
            if (fileName != null && list != null) {
                DataIO.writeLocalFile(applicationContext, fileName!!, list!!)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
