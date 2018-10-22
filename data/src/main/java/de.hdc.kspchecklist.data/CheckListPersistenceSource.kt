package de.hdc.kspchecklist.data

import de.hdc.kspchecklist.domain.*

/**
 * Created by DerTroglodyt on 2018-10-22 12:59.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
interface CheckListPersistenceSource {
  fun copyAssetsFiles()
  fun getLists(): ArrayList<CheckList>
  fun saveList(name: String, list: ArrayList<CheckListItem>)
  fun renameList(oldName: String, newName: String)
  fun deleteList(name: String)
  fun getCheckListItems(name: String): ArrayList<CheckListItem>
  fun addCheckList(name: String)
}
