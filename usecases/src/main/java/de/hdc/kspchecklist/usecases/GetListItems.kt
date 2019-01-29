package de.hdc.kspchecklist.usecases

import de.hdc.kspchecklist.data.CheckListRepository
import de.hdc.kspchecklist.domain.CheckListItem
import de.hdc.kspchecklist.domain.Result

/**
 * Created by DerTroglodyt on 2018-10-22 12:20.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
class GetListItems
constructor(private val checklistRepository: CheckListRepository, val checkListName: String) :
    UseCase<List<CheckListItem>, UseCase.NoParms>() {

    override suspend fun run(params: NoParms) = Result.ok(checklistRepository.getCheckListItems(checkListName))
}
