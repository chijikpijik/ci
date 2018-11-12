package com.example.akarpov.mainscreenplayground

import adapters.Diffable
import mvi.BaseViewState

/**
 * Created by a.karpov on 06.11.2018.
 */
data class MainViewState(
        val data: List<Diffable<*>>?,
        override val isLoading: Boolean,
        override val error: Throwable?
): BaseViewState(isLoading, error)
