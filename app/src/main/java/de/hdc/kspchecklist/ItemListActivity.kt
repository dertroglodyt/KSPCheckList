package de.hdc.kspchecklist

import android.content.*
import android.os.*
import android.preference.*
import android.text.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import com.google.firebase.analytics.*
import de.hdc.framework.*
import de.hdc.kspchecklist.data.*
import de.hdc.kspchecklist.domain.*
import de.hdc.kspchecklist.framework.*
import kotlinx.android.synthetic.main.activity_item_list.*
import java.util.*

private const val FIRST_RUN = "FIRSTRUN"

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var items: ArrayList<CheckList>
    private lateinit var adapter: ItemListAdapter
    private val persistence: CheckListPersistenceSource by lazy {
      CheckListPersistenceImpl(applicationContext)
    }

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
        setSupportActionBar(list_toolbar)

        // Obtain the FirebaseAnalytics instance.
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        if (BuildConfig.DEBUG) {
            val params = Bundle()
            params.putString("app_started", "true")
            mFirebaseAnalytics.logEvent("share_image", params)
        }

        /**
         * If installed and run for the first time, copy assets files to local storage.
         */
        val wmbPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstRun = wmbPreference.getBoolean(FIRST_RUN, true)
        if (isFirstRun) {
            // Code to run once
            val editor = wmbPreference.edit()
            editor.putBoolean(FIRST_RUN, false)
            editor.apply()

            persistence.copyAssetsFiles()
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
        items = persistence.getLists()
        items.sort()
        // Create the adapter to convert the array to views
        adapter = ItemListAdapter(this, items)

        item_list.adapter = adapter
        item_list.choiceMode = ListView.CHOICE_MODE_SINGLE
        item_list.onItemClickListener = this
        registerForContextMenu(item_list)
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        val intent = Intent(this, ItemDetailActivity::class.java)
        intent.putExtra(DETAIL_MESSAGE, items[i].name)
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
                builder.setTitle(getString(R.string.new_checklist))

                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                    val name = input.text.toString()
                    persistence.addCheckList("$name.txt")
//                    DataIO.createLocalFile(applicationContext, "$name.txt")
                    items.add(CheckList(name))
                    items.sort()
                    adapter.notifyDataSetChanged()
                    val intent = Intent(application, ItemDetailActivity::class.java)
                    intent.putExtra(DETAIL_MESSAGE, name)
                    startActivity(intent)
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

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        if (v.id == R.id.item_list) {
            val info = menuInfo as AdapterView.AdapterContextMenuInfo
            menu.setHeaderTitle("Checklist: " + items[info.position])
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
                builder.setTitle(getString(R.string.new_name))

                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                    val name = input.text.toString()
                    persistence.renameList(items[info.position].name + ".txt", "$name.txt")
                    items[info.position] = CheckList(name)
                    adapter.notifyDataSetChanged()
                }
                builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }

                builder.show()
                return true
            }
            1 -> {  // delete
                val builder = AlertDialog.Builder(this)
                builder.setTitle(items[info.position].toString())
                        .setMessage(getString(R.string.confirm_delete))
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                    persistence.deleteList(items[info.position].name + ".txt")
                    items.removeAt(info.position)
                    adapter.notifyDataSetChanged()
                }
                builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }

                builder.show()
                return true
            }
            else -> return false
        }
    }

    companion object {

        const val DETAIL_MESSAGE = "DETAIL_MESSAGE"
    }

}
