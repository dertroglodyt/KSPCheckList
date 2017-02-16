package de.hdc.kspchecklist.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by DerTroglodyt on 2017-02-13 12:52.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */
public class DataIOTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void readAssetFile() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        ArrayList<CheckListItem> list = DataIO.readAssetFile(appContext, "eins.txt");
    }

    @Test
    public void writeLocalFile() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        ArrayList<CheckListItem> list = new ArrayList<>();
        list.add(CheckListItem.create("eins", true));
        list.add(CheckListItem.create("zwei", false));
        list.add(CheckListItem.create("drei", true));
        DataIO.writeLocalFile(appContext, "eins.txt", list);

        ArrayList<CheckListItem> list2 = DataIO.readLocalFile(appContext, "eins.txt");
        Assert.assertTrue(list.size() == 3);
    }
}
