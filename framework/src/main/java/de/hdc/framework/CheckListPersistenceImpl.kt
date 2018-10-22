package de.hdc.framework

import android.content.*
import android.util.*
import de.hdc.kspchecklist.data.*
import de.hdc.kspchecklist.domain.*
import java.io.*

/**
 * Created by DerTroglodyt on 2018-10-22 13:22.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
class CheckListPersistenceImpl(private val appContext: Context) : CheckListPersistenceSource {

  override fun deleteList(name: String) {
    val f = File(appContext.filesDir.absolutePath + "/" + name)
    f.delete()
  }

  override fun renameList(oldName: String, newName: String) {
    val f = File(appContext.filesDir.absolutePath + "/" + oldName)
    val fn = File(appContext.filesDir.absolutePath + "/" + newName)
    f.renameTo(fn)
  }

  override fun copyAssetsFiles() {
    val files: Array<String>?
    try {
      files = appContext.assets.list("")
    } catch (e: IOException) {
      Log.e("tag", "Failed to get asset file list.", e)
      return
    }

    val buffer = ByteArray(1024)
    for (filename in files) {
      if (!filename.contains(".txt")) {
        continue
      }
      //            final String outFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename ;
      try {
        appContext.assets.open(filename).use { `in` ->
          appContext.openFileOutput(filename, Context.MODE_PRIVATE).use { out ->
            var read: Int = `in`.read(buffer)
            while (read != -1) {
              out.write(buffer, 0, read)
              read = `in`.read(buffer)
            }
            out.flush()
          }
        }
      } catch (e: Throwable) {
        Log.e("tag", "Failed to copy asset file: $filename", e)
      }

    }
  }

  override fun getLists(): ArrayList<CheckList> {
    val list = ArrayList<CheckList>()
    val dir = File(appContext.filesDir.absolutePath)
    for (f in dir.listFiles { _, s -> s.endsWith(".txt") }) {
      var s = f.name
      val x = s.lastIndexOf('.')
      if (x >= 0) {
        s = s.substring(0, x)
      }
      list.add(CheckList(s))
    }
    //        list.add(appContext.getFilesDir().getAbsolutePath());
    return list
  }

  override fun saveList(name: String, list: ArrayList<CheckListItem>) {
    BufferedWriter(FileWriter(
        File(appContext.filesDir.absolutePath + "/" + name))).use { w ->
      for (cle in list) {
        w.write(cle.name + "#" + java.lang.Boolean.toString(cle.checked))
        w.newLine()
      }
    }
  }

  override fun getCheckListItems(name: String): ArrayList<CheckListItem> {
    return readFile(FileInputStream(File(
        appContext.filesDir.absolutePath + "/" + name)))
  }

  override fun addCheckList(name: String) {
    val f = File(appContext.filesDir.absolutePath + "/" + name)
    try {
      f.createNewFile()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun readFile(stream: InputStream): java.util.ArrayList<CheckListItem> {
    val list = java.util.ArrayList<CheckListItem>(50)
    var line = 1

    BufferedReader(InputStreamReader(stream, "UTF-8")).use { r ->
      var s: String? = r.readLine()
      while (s != null) {
        if (s.trim { it <= ' ' }.isEmpty()) {
          s = r.readLine()
          continue
        }
        var b = false
        var x = s.indexOf("#")
        if (x >= 0) {
          b = java.lang.Boolean.valueOf(s.substring(x + 1).toUpperCase())
        } else {
          x = s.length
        }
        val cle = CheckListItem(s.substring(0, x), b)
        list.add(cle)

        line += 1
        s = r.readLine()
      }
    }
    return list
  }


}
