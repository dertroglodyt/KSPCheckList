package de.hdc.checklist.usecases

import de.hdc.kspchecklist.data.*
import de.hdc.kspchecklist.domain.*

/**
 * Created by DerTroglodyt on 2018-10-22 12:08.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
class AddCheckList
constructor(private val checklistRepository: CheckListRepository, private val name: String) :
    UseCase<Unit, UseCase.NoParms>() {

  override suspend fun run(params: NoParms) =
      Result.ok(checklistRepository.addCheckList(name))
}
