package com.msabhi.shared

import kotlinx.coroutines.runBlocking


class ClassicStoreStateReplayTest {

    //@Test
    fun replayTest() = runBlocking {
        repeat(1) {
            //singleReplayTestIteration(N = 200, subscribers = 1)
        }
    }

    //@Test
    fun replayLargeTest() = runBlocking {
        //singleReplayTestIteration(N = 500, subscribers = 10)
    }

    /**
     * Tests consistency of produced flow. E.g. for just increment reducer output must be
     * 1,2,3,4,5
     * not 1,3,4,5 (value missing)
     * or 4,3,4,5 (incorrect order)
     * or 3,3,4,5 (duplicate value)
     */

    /*private fun singleReplayTestIteration(N: Int, subscribers: Int) = runBlocking {

        val states = MutableSharedFlow<CounterState>(replay = 1).apply { tryEmit(CounterState()) }

        val stateChangeListeners = arrayListOf<StateChangeListener<CounterState>>()
        for (i in 0 until subscribers) {
            stateChangeListeners.add { states.tryEmit(it) }
        }

        val store = ClassicStore(CounterState(),
            ::counterStateReduce,
            stateChangeListeners,
            Executors.newSingleThreadExecutor())

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
                    states.takeWhile { it.counter < N }.toList().zipWithNext { a, b ->
                        Assert.assertEquals(a.counter + 1, b.counter)
                    }
                }
            }
        }

    }*/
}