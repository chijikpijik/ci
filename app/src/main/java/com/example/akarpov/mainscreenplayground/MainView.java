package com.example.akarpov.mainscreenplayground;

import io.reactivex.Observable;
import kotlin.Unit;
import mvi.BasePresenterMVI;

/**
 * Created by a.karpov on 06.11.2018.
 */

public interface MainView extends BasePresenterMVI.ViewStateConsumer<MainViewState> {


    class Load implements BasePresenterMVI.Action<Unit> {
        @Override
        public Observable<Unit> bind() {
            return Observable.just(Unit.INSTANCE);
        }

    }

    class AddItem implements BasePresenterMVI.Action<String> {

        public String title;

        public AddItem(String title) {
            this.title = title;
        }

        @Override
        public Observable<String> bind() {
            return Observable.just(title);
        }
    }

}
