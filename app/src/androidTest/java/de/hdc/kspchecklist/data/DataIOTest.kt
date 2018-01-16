package de.hdc.kspchecklist.data

import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Created by DerTroglodyt on 2017-02-13 12:52.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */
class DataIOTest {
    @Before
    @Throws(Exception::class)
    fun setUp() {

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {

    }

    @Test
    @Throws(Exception::class)
    fun readAssetFile() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        DataIO.readAssetFile(appContext.assets, "Simple.txt")
    }

    @Test
    @Throws(Exception::class)
    fun writeLocalFile() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val list = ArrayList<CheckListItem>()
        list.add(CheckListItem("eins", true))
        list.add(CheckListItem("zwei", false))
        list.add(CheckListItem("drei", true))
        DataIO.writeLocalFile(appContext, "eins.txt", list)

        val list2 = DataIO.readLocalFile(appContext, "eins.txt")
        Assert.assertTrue(list2.size == 3)
    }
}
