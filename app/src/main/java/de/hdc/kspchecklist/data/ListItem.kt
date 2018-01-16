package de.hdc.kspchecklist.data

/**
 * Created by DerTroglodyt on 2017-02-13 12:20.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

class ListItem private constructor(val name: String) : Comparable<Any> {

    override fun toString(): String {
        return name
    }

    override operator fun compareTo(other: Any): Int {
        return this.name.compareTo(other.toString())
    }

    companion object {

        fun create(name: String): ListItem {
            return ListItem(name)
        }
    }
}
