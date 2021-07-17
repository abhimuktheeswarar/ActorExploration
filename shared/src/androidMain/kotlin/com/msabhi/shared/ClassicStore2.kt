package com.msabhi.shared


/*
actual class ClassicStore<S : State>(
    initialState: S,
    actual val reduce: (Action, S) -> S,
    private val stateChangeListeners: List<StateChangeListener<S>>,
    private val storeExecutor: Executor?,
)  {

    private val actions = LinkedBlockingQueue<Action>()

    @Volatile
    private var state: S = initialState

    @Synchronized
    override fun dispatch(action: Action) {
        actions.offer(action)
        storeExecutor?.execute {
            actions.poll()?.let {
                handle(it)
            }
        } ?: run {
            actions.poll()?.let {
                handle(it)
            }
        }
    }

    override fun state(): S = state

    private fun handle(action: Action) {
        val newState = reduce(action, state)
        if (state != newState) {
            state = newState
            stateChangeListeners.forEach { it.invoke(newState) }
        }
    }
}*/
