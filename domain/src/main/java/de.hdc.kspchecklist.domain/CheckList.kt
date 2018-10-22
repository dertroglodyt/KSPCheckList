package de.hdc.kspchecklist.domain

/**
 * Created by DerTroglodyt on 2018-10-22 12:19.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
data class CheckList(val name: String): Comparable<Any> {

  override fun toString(): String {
    return name
  }

  override operator fun compareTo(other: Any): Int {
    return this.name.compareTo(other.toString())
  }
}
