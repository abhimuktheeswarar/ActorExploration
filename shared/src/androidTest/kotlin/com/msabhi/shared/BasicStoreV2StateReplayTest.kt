package com.msabhi.shared

import com.msabhi.shared.basic.BasicStoreV2
import com.msabhi.shared.common.counterStateReduce
import com.msabhi.shared.entities.CounterAction
import com.msabhi.shared.entities.CounterState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList
import org.junit.Assert
import org.junit.Test


class BasicStoreV2StateReplayTest {

    @Test
    fun replayTest() = runBlocking {
        repeat(1) {
            singleReplayTestIteration(N = 200, subscribers = 1)
        }
        Unit
    }

    @Test
    fun replayLargeTest() = runBlocking {
        singleReplayTestIteration(N = 100_000, subscribers = 10)
        Unit
    }

    /**
     * Tests consistency of produced flow. E.g. for just increment reducer output must be
     * 1,2,3,4,5
     * not 1,3,4,5 (value missing)
     * or 4,3,4,5 (incorrect order)
     * or 3,3,4,5 (duplicate value)
     */
    private suspend fun singleReplayTestIteration(N: Int, subscribers: Int) =
        withContext(Dispatchers.Default) {
            val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
            val store = BasicStoreV2(CounterState(), ::counterStateReduce, scope)

            launch {
                repeat(N) {
                    store.dispatch(CounterAction.IncrementAction)
                }
            }

            // One more scope for subscribers, to ensure subscribers are finished before cancelling store scope
            coroutineScope {
                repeat(subscribers) {
                    launch {
                        // Since only increase by 1 reducers are applied
                        // it's expected to see monotonously increasing sequence with no missing values
                        store.states.takeWhile { it.counter < N }.toList().zipWithNext { a, b ->
                            Assert.assertEquals(a.counter + 1, b.counter)
                        }
                    }
                }
            }
            scope.cancel()
        }
}