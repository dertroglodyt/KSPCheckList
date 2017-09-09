package de.hdc.kspchecklist.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by DerTroglodyt on 2017-02-13 12:17.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */

public class DataIO {

    public static void copyAssetsFiles(Context appContext) throws IOException {
        String[] files;
        try {
            files = appContext.getAssets().list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
            return;
        }

        byte[] buffer = new byte[1024];
        for(String filename : files) {
            if (!filename.contains(".txt")) {
                continue;
            }
//            final String outFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename ;
            try (InputStream in = appContext.getAssets().open(filename); OutputStream out = appContext.openFileOutput(filename, Context.MODE_PRIVATE)) {
                int read;
                while((read = in.read(buffer)) != -1){
                    out.write(buffer, 0, read);
                }
                out.flush();
//            } catch(Throwable e) {
//                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }

    public static void deleteLocalFile(Context appContext, String fileName) {
        File f = new File(appContext.getFilesDir().getAbsolutePath() + "/" + fileName);
        f.delete();
    }

    public static void renameLocalFile(Context appContext, String oldFilename, String newFilename) {
        File f = new File(appContext.getFilesDir().getAbsolutePath() + "/" + oldFilename);
        File fn = new File(appContext.getFilesDir().getAbsolutePath() + "/" + newFilename);
        f.renameTo(fn);
    }

    public static void createLocalFile(Context appContext, String fileName) {
        File f = new File(appContext.getFilesDir().getAbsolutePath() + "/" + fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<CheckListItem> readAssetFile(AssetManager assetManager, String fileName)
            throws IOException {
        return readFile(assetManager.open(fileName));
    }

    public static ArrayList<CheckListItem> readLocalFile(Context appContext, String fileName)
            throws IOException {
        return readFile(new FileInputStream(new File(
                appContext.getFilesDir().getAbsolutePath() + "/" + fileName)));
    }

    public static void writeLocalFile(Context appContext, String fileName, ArrayList<CheckListItem> list)
            throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(
                new File(appContext.getFilesDir().getAbsolutePath()+"/" + fileName)))) {
            for (CheckListItem cle : list) {
                w.write(cle.name + "#" + Boolean.toString(cle.checked));
                w.newLine();
            }
        }
    }

    public static ArrayList<String> getDirList(Context appContext) {
        ArrayList<String> list = new ArrayList<>();
        File dir = new File(appContext.getFilesDir().getAbsolutePath());
        for (File f : dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return (s.endsWith(".txt"));
                    }
                })) {
            String s = f.getName();
            int x = s.lastIndexOf('.');
            if (x >= 0) {
                s = s.substring(0, x);
            }
            list.add(s);
        }
//        list.add(appContext.getFilesDir().getAbsolutePath());
        return list;
    }

    private static ArrayList<CheckListItem> readFile(InputStream stream)
            throws IOException {
        ArrayList<CheckListItem> list = new ArrayList<>(50);
        int line = 1;

        try (BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
            String s = r.readLine();
            while (s != null) {
                if (s.trim().isEmpty()) {
                    s = r.readLine();
                    continue;
                }
                boolean b = false;
                int x = s.indexOf("#");
                if (x >= 0) {
                    b = Boolean.valueOf(s.substring(x+1).toUpperCase());
                } else {
                    x = s.length();
                }
                CheckListItem cle = CheckListItem.create(s.substring(0, x), b);
                list.add(cle);

                line += 1;
                s = r.readLine();
            }
        }
        return list;
    }

    private DataIO(){
    }

}
