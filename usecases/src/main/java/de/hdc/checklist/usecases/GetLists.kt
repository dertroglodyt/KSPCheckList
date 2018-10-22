package de.hdc.checklist.usecases

import de.hdc.kspchecklist.data.*
import de.hdc.kspchecklist.domain.*

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
