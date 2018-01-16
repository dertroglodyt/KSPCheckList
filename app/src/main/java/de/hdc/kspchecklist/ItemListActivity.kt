package de.hdc.kspchecklist

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import com.google.firebase.analytics.FirebaseAnalytics
import de.hdc.kspchecklist.data.DataIO
import de.hdc.kspchecklist.data.ListItem
import java.util.*

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var items: ArrayList<ListItem>? = null
    private var adapter: ItemListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val dark = sharedPrefs.getBoolean(getString(R.string.preference_theme_key), true)
        if (dark) {
            setTheme(R.style.AppTheme_NoActionBar)
        } else {
            setTheme(R.style.AppThemeLight)
        }
        setContentView(R.layout.activity_item_list)
        val myToolbar = findViewById<View>(R.id.list_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        if (BuildConfig.DEBUG) {
            val params = Bundle()
            params.putString("app_started", "true")
            mFirebaseAnalytics!!.logEvent("share_image", params)
        }

        /**
         * If installed and run for the first time, copy assets files to local storage.
         */
        val FIRST_RUN = "FIRSTRUN"
        val wmbPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstRun = wmbPreference.getBoolean(FIRST_RUN, true)
        if (isFirstRun) {
            // Code to run once
            val editor = wmbPreference.edit()
            editor.putBoolean(FIRST_RUN, false)
            editor.apply()

            DataIO.copyAssetsFiles(applicationContext)
        }

        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //        fab.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                        .setAction("Action", null).show();
        //            }
        //        });

        // Construct the data source
        items = ArrayList()
        val list = DataIO.getDirList(this.applicationContext)
        Collections.sort(list)
        for (s in list) {
            items!!.add(ListItem.create(s))
        }
        // Create the adapter to convert the array to views
        adapter = ItemListAdapter(this, items!!)

        val listView = findViewById<View>(R.id.item_list) as ListView
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.onItemClickListener = this
        registerForContextMenu(listView)
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        val intent = Intent(this, ItemDetailActivity::class.java)
        intent.putExtra(DETAIL_MESSAGE, items!![i].name)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu items for use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }

            R.id.action_list_add -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("New checklist")

                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton("OK") { dialog, which ->
                    val name = input.text.toString()
                    DataIO.createLocalFile(applicationContext, name + ".txt")
                    items!!.add(ListItem.create(name))
                    Collections.sort(items!!)
                    adapter!!.notifyDataSetChanged()
                    val intent = Intent(application, ItemDetailActivity::class.java)
                    intent.putExtra(DETAIL_MESSAGE, name)
                    startActivity(intent)
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

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        if (v.id == R.id.item_list) {
            val info = menuInfo as AdapterView.AdapterContextMenuInfo
            menu.setHeaderTitle("Checklist: " + items!![info.position])
            val menuItems = resources.getStringArray(R.array.menu)
            for (i in menuItems.indices) {
                menu.add(Menu.NONE, i, i, menuItems[i])
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            0 -> {  // edit
                val builder = AlertDialog.Builder(this)
                builder.setTitle("New name")

                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton("OK") { dialog, which ->
                    val name = input.text.toString()
                    DataIO.renameLocalFile(applicationContext, items!![info.position].name + ".txt", name + ".txt")
                    items!![info.position] = ListItem.create(name)
                    adapter!!.notifyDataSetChanged()
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

                builder.show()
                return true
            }
            1 -> {  // delete
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete " + items!![info.position])
                        .setMessage("Do you really want to delete this checklist?")
                builder.setPositiveButton("OK") { dialog, which ->
                    DataIO.deleteLocalFile(applicationContext, items!![info.position].name + ".txt")
                    items!!.removeAt(info.position)
                    adapter!!.notifyDataSetChanged()
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

                builder.show()
                return true
            }
            else -> return false
        }
    }

    companion object {

        val DETAIL_MESSAGE = "DETAIL_MESSAGE"
    }

}
