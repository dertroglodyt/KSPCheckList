package de.hdc.kspchecklist.data

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.*
import java.util.*

/**
 * Created by DerTroglodyt on 2017-02-13 12:17.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

object DataIO {

    fun copyAssetsFiles(appContext: Context) {
        val files: Array<String>
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
                Log.e("tag", "Failed to copy asset file: " + filename, e)
            }

        }
    }

    fun deleteLocalFile(appContext: Context, fileName: String) {
        val f = File(appContext.filesDir.absolutePath + "/" + fileName)
        f.delete()
    }

    fun renameLocalFile(appContext: Context, oldFilename: String, newFilename: String) {
        val f = File(appContext.filesDir.absolutePath + "/" + oldFilename)
        val fn = File(appContext.filesDir.absolutePath + "/" + newFilename)
        f.renameTo(fn)
    }

    fun createLocalFile(appContext: Context, fileName: String) {
        val f = File(appContext.filesDir.absolutePath + "/" + fileName)
        try {
            f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    internal fun readAssetFile(assetManager: AssetManager, fileName: String): ArrayList<CheckListItem> {
        return readFile(assetManager.open(fileName))
    }

    @Throws(IOException::class)
    fun readLocalFile(appContext: Context, fileName: String): ArrayList<CheckListItem> {
        return readFile(FileInputStream(File(
                appContext.filesDir.absolutePath + "/" + fileName)))
    }

    @Throws(IOException::class)
    fun writeLocalFile(appContext: Context, fileName: String, list: ArrayList<CheckListItem>) {
        BufferedWriter(FileWriter(
                File(appContext.filesDir.absolutePath + "/" + fileName))).use { w ->
            for (cle in list) {
                w.write(cle.name + "#" + java.lang.Boolean.toString(cle.checked))
                w.newLine()
            }
        }
    }

    fun getDirList(appContext: Context): ArrayList<String> {
        val list = ArrayList<String>()
        val dir = File(appContext.filesDir.absolutePath)
        for (f in dir.listFiles { file, s -> s.endsWith(".txt") }) {
            var s = f.name
            val x = s.lastIndexOf('.')
            if (x >= 0) {
                s = s.substring(0, x)
            }
            list.add(s)
        }
        //        list.add(appContext.getFilesDir().getAbsolutePath());
        return list
    }

    @Throws(IOException::class)
    private fun readFile(stream: InputStream): ArrayList<CheckListItem> {
        val list = ArrayList<CheckListItem>(50)
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
                    b = java.lang.Boolean.valueOf(s.substring(x + 1).toUpperCase())!!
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
