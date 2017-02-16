package de.hdc.kspchecklist.data;

/**
 * Created by DerTroglodyt on 2017-02-13 12:20.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class CheckListItem {

    public final String name;
    public final boolean checked;

    public static CheckListItem create(String name) {
        return new CheckListItem(name, false);
    }

    public static CheckListItem create(String name, boolean checked) {
        return new CheckListItem(name, false);
    }

    @Override
    public String toString() {
        return name + ": " + Boolean.toString(checked);
    }

    private CheckListItem(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }
}
