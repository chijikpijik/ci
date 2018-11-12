package mvi

abstract class BaseViewState(
    open val isLoading: Boolean,
    open val error: Throwable?
)
