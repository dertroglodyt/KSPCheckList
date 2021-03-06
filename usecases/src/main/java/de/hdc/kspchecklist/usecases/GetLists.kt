package de.hdc.kspchecklist.usecases

import de.hdc.kspchecklist.data.CheckListRepository
import de.hdc.kspchecklist.domain.CheckList
import de.hdc.kspchecklist.domain.Result

/**
 * Created by DerTroglodyt on 2018-10-22 12:20.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
class GetLists
constructor(private val checklistRepository: CheckListRepository) :
    UseCase<List<CheckList>, UseCase.NoParms>() {

    override suspend fun run(params: NoParms) =
        Result.ok(checklistRepository.getLists())
}
