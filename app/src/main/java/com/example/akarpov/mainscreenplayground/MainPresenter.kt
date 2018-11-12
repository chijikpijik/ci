package com.example.akarpov.mainscreenplayground

import adapters.Diffable
import io.reactivex.Observable
import mvi.BasePresenterMVI
import playground.ItemHolder
import java.util.*

/**
 * Created by a.karpov on 06.11.2018.
 */
class MainPresenter: BasePresenterMVI<MainView, MainViewState>() {

    override fun getViewStateConsumer(): ViewStateConsumer<MainViewState> {
        return mView
    }

    override fun actionsHasBound() {
        super.actionsHasBound()
        sendAction(MainView.Load())
    }

    override fun bindActions() {
        val load:Observable<MainViewState> = bindAction(MainView.Load::class.java).map {
            MainViewState(arrayListOf(ItemHolder.ItemData("ПЕРВЫЙ НАХ")),false,null)
        }
        val newitem:Observable<MainViewState> = bindAction(MainView.AddItem::class.java).map {
            MainViewState(arrayListOf(ItemHolder.ItemData(it)),false,null)
        }

        val reduce:Observable<MainViewState> = Observable.merge(load, newitem).scan(initViewState(),
        { prev, new ->
            val newlist:MutableList<Diffable<*>> ?= prev.data?.toMutableList();
            newlist?.addAll(new.data!!)
            MainViewState(newlist, false, null)
        })

        subscribeToViewState(reduce)
    }

    private fun initViewState(): MainViewState {
        return  MainViewState(Collections.emptyList(), true, null)
    }
}