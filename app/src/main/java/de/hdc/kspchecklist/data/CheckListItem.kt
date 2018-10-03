package de.hdc.kspchecklist.data

/**
 * Created by DerTroglodyt on 2017-02-13 12:20.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

class CheckListItem constructor(val name: String, val checked: Boolean = false) {

    override fun toString(): String {
        return "$name: $checked"
    }

}
