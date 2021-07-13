package com.msabhi.shared

interface Action

interface EventAction : Action

interface NavigateAction : Action

interface ErrorAction : Action {
    val exception: Exception
}

fun Action.name(): String = this::class.simpleName ?: "Action"

interface State