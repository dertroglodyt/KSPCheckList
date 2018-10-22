package de.hdc.kspchecklist.data

import de.hdc.kspchecklist.domain.*

/**
 * Created by DerTroglodyt on 2018-10-22 12:26.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
class CheckListRepository(private val checkListPersistenceSource: CheckListPersistenceSource) {

  fun getCheckListItems(name: String): List<CheckListItem> = checkListPersistenceSource.getCheckListItems(name)

  fun getLists(): List<CheckList> = checkListPersistenceSource.getLists()

  fun addCheckList(name: String) = checkListPersistenceSource.addCheckList(name)
}
