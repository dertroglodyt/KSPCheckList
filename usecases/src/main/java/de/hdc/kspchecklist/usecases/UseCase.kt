package de.hdc.checklist.usecases

import de.hdc.kspchecklist.domain.*
import kotlinx.coroutines.*

/**
 * Created by DerTroglodyt on 2018-10-18 17:27.
 * Email dertroglodyt@gmail.com
 * Copyright by HDC, Germany
 */
abstract class UseCase<out Type, in Params> where Type: Any {

  abstract suspend fun run(params: Params): Result<Type, Any>

  operator fun invoke(params: Params, onResult: (Result<Type, Any>) -> Unit = {}) {
    val job =
        GlobalScope.async(Dispatchers.Default, CoroutineStart.DEFAULT, { run(params) })
    GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT, { onResult(job.await()) })
  }

  class NoParms
}
