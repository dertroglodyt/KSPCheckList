package de.hdc.kspchecklist.domain

/**
 * Created by DerTroglodyt on 2018-10-22 12:18.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
data class CheckListItem(val name: String, val checked: Boolean) : Comparable<Any> {

    override fun toString(): String {
        return "$name: $checked"
    }

    override operator fun compareTo(other: Any): Int {
        return this.name.compareTo(other.toString())
    }

}
