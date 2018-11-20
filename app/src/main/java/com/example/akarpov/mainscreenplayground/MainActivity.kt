package com.example.akarpov.mainscreenplayground

import adapters.AwesomeAdapter
import adapters.Diffable
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import lifecyclesurviveapi.HasPresenter
import lifecyclesurviveapi.PresenterActivity
import playground.ItemHolder

class MainActivity : PresenterActivity<MainActivity.MainComponent, MainPresenter>(), MainView {

    override fun accept(viewState: MainViewState) {
        val old = adapter.list.toMutableList();
        val new = viewState.data?.toMutableList();
        adapter.list=new
        val d : DiffUtil.DiffResult = DiffUtil.calculateDiff(MainDiffUtil(old, new!!))
        d.dispatchUpdatesTo(adapter)
    }

    private lateinit var recyclerView : RecyclerView

    private lateinit var adapter: AwesomeAdapter<Diffable<*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = AwesomeAdapter()
        adapter.addHolder(ItemHolder.ItemData::class.java,
                { itemView : View, root : ViewGroup -> ItemHolder(itemView, root, {
                    presenter.sendAction(MainView.AddItem(" ЕЩЕ ОДИН НАХ!!!"))
                }) },
                R.layout.item_holder)
        recyclerView = findViewById(R.id.main_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(MainItemDecoration())
        recyclerView.adapter = adapter
    }


    override fun onCreateNonConfigurationComponent(): MainComponent {
        return object : MainComponent {
            override fun getPresenter(): MainPresenter = presenter1
        }

    }

    val presenter1: MainPresenter by lazy {
        MainPresenter()
    }

    interface MainComponent: HasPresenter<MainPresenter>

    class MainDiffUtil(val o:List<Diffable<*>>, val n:List<Diffable<*>>) : DiffUtil.Callback() {


        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return false
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return o.get(oldItemPosition).diffId.equals(n.get(newItemPosition).diffId)
        }

        override fun getOldListSize(): Int {
            return o.size
        }

        override fun getNewListSize(): Int {
            return n.size
        }

    }
}
