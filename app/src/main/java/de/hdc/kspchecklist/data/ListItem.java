package de.hdc.kspchecklist.data;

/**
 * Created by DerTroglodyt on 2017-02-13 12:20.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class ListItem {

    public final String name;

    public static ListItem create(String name) {
        return new ListItem(name);
    }

    @Override
    public String toString() {
        return name;
    }

    private ListItem(String name) {
        this.name = name;
    }
}
