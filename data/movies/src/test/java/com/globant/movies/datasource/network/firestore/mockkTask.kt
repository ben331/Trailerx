package com.globant.movies.datasource.network.firestore

import com.google.android.gms.tasks.Task
import io.mockk.every
import io.mockk.mockk

/**
 * Mocks the simplest behaviour of a task so .await() can return task or throw exception
 * See more on [await] and inside of that on awaitImpl
 */
inline fun <reified T> mockTask(result: T?, exception: Exception? = null): Task<T> {
    val task: Task<T> = mockk(relaxed = true)
    every { task.isComplete } returns true
    every { task.exception } returns exception
    every { task.isCanceled } returns false
    val relaxedT: T = mockk(relaxed = true)
    every { task.result } returns result
    return task
}