package de.hdc.kspchecklist

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import de.hdc.kspchecklist.data.CheckListPersistenceSource
import de.hdc.kspchecklist.domain.CheckListItem
import de.hdc.kspchecklist.framework.CheckListPersistenceImpl
import kotlinx.android.synthetic.main.activity_item_detail.*
import java.io.IOException
import java.util.*

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class ItemDetailActivity : AppCompatActivity() {

    private val persistence: CheckListPersistenceSource by lazy {
        CheckListPersistenceImpl(applicationContext)
    }

    /*
        @Override
        public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
            if (menuInfo == null ) {
                return;
            }
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(list.get(info.position).name);
            String[] menuItems = getResources().getStringArray(R.array.menu_detail);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            if (item == null ) {
                return false;
            }
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
        }*/

    private var setChecked: Boolean = false
    private lateinit var fileName: String
    private lateinit var list: ArrayList<CheckListItem>
    //    private ItemDetailAdapter adapter;
    private lateinit var adapter: ItemDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        setSupportActionBar(detail_list_toolbar)

        val message = intent.getStringExtra(ItemListActivity.DETAIL_MESSAGE)
        fileName = "$message.txt"
        title = message

        // Construct the data source
        try {
            list = persistence.getCheckListItems(fileName)
//            list = DataIO.readLocalFile(this.applicationContext, fileName)
            // Create the adapter to convert the array to views
            //            adapter = new ItemDetailAdapter(this.getApplicationContext(), fileName, list);
            adapter = ItemDetailAdapter(this, persistence, fileName, list)

            item_detail_container.layoutManager = LinearLayoutManager(this)
            item_detail_container.setHasFixedSize(true)
            item_detail_container.adapter = adapter
            //            registerForContextMenu(listView);
            val helper = ItemTouchHelper(DetailTouchHelper(adapter))
            helper.attachToRecyclerView(item_detail_container)
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
                for (i in list.indices) {
                    val cli = list[i]
                    list[i] = CheckListItem(cli.name, setChecked)
                }
                setChecked = !setChecked
                saveList()
                adapter.notifyDataSetChanged()
                return true
            }

            R.id.action_add -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.new_item))

                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                    val cli = CheckListItem(input.text.toString(), false)
                    list.add(cli)
                    saveList()
                    adapter.notifyDataSetChanged()
                }
                builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }

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
            persistence.saveList(fileName, list)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
