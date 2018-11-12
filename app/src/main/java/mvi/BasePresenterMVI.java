package mvi;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import lifecyclesurviveapi.BasePresenter;

/**
 * Created by a.karpov on 27.02.2018.
 */

public abstract class BasePresenterMVI<T, VS> extends BasePresenter<T> {

    private Map<Class, PublishSubject> actions = new HashMap<>();

    private Disposable viewStateDisposable;

    private Disposable viewRelayConsumerDisposable;

    private final BehaviorSubject<VS> viewStateBehaviorSubject;

    private ViewStateConsumer<VS> viewStateConsumer;

    public BasePresenterMVI() {
        this.viewStateBehaviorSubject = BehaviorSubject.create();
    }

    public <I> void sendAction(Action<I> action) {
        PublishSubject<Observable<I>> actionPipe = actions.get(action.getClass());
        if (actionPipe != null) {
            actionPipe.onNext(action.bind());
        }
    }

    public <I> void cancelAction(Class<? extends Action<I>> actionType){
        PublishSubject<Observable<I>> actionPipe = actions.get(actionType);
        if (actionPipe != null) {
            actionPipe.onNext(Observable.never());
        }
    }

    @Override
    public void bindView(T view) {
        super.bindView(view);

        viewStateConsumer = getViewStateConsumer();

        if (actions.isEmpty()) {
            bindActions();
            actionsHasBound();
        }

        if (viewStateConsumer != null) {
            viewRelayConsumerDisposable = viewStateBehaviorSubject.subscribe(vs -> {
                Log.d("STATE:", vs.toString());
                viewStateConsumer.accept(vs);
            });
        }
    }

    @Override
    public void unbindView() {
        super.unbindView();
        if (viewRelayConsumerDisposable != null) {
            viewRelayConsumerDisposable.dispose();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (viewStateDisposable != null) {
            viewStateDisposable.dispose();
        }
    }

    /**
     *  Create Observable on an @actionType event and add such action into the actions set.
     *
     * @param actionType Will be created event observable on such action type
     * @param <I> Event observable data type
     * @return Observable of a date witch we get by action
     */

    protected <I> Observable<I> bindAction(Class<? extends Action<I>> actionType) {
        PublishSubject<Observable<I>> actionPipe = PublishSubject.create();
        Observable<I> binder = Observable.switchOnNext(actionPipe);
        actions.put(actionType, actionPipe);
        return binder;
    }

    /**
     *
     * @param observable result Observable that will be emit into ViewStateConsumer
     */

    protected void subscribeToViewState(Observable<VS> observable) {
        viewStateDisposable = observable.subscribeWith(new DisposableViewStateObserver<>(viewStateBehaviorSubject));
    }


    /**
     * @return Consumer object, that needs in viewstate: "View" finally
     */
    protected abstract ViewStateConsumer<VS> getViewStateConsumer();

    /**
     *  Bind all actions, which are used to presenter and which are sent by View
     */
    protected abstract void bindActions();

    /**
     *  invoke immediately after bindActions()
     */
    protected void actionsHasBound(){}


    public static class DisposableViewStateObserver<VS> extends DisposableObserver<VS> {
        private final BehaviorSubject<VS> subject;

        public DisposableViewStateObserver(BehaviorSubject<VS> subject) {
            this.subject = subject;
        }

        @Override
        public void onNext(VS value) {
            subject.onNext(value);
        }

        @Override
        public void onError(Throwable e) {
            throw new IllegalStateException(
                "ViewState observable must not reach error state - onError()", e);
        }

        @Override
        public void onComplete() {
            // ViewState observable never completes so ignore any complete event
        }
    }

    /**
     *  "View" must implement one.
     *  New state of a screen will be send on "View" thought method @accept.
     *
     *  Also "View" should to have implementation of {@link Action<>}.
     * @param <VS> ViewState-type which contains state of a "View" (data for show)
     */
    public interface ViewStateConsumer<VS> {
        void accept(@NonNull VS viewState);
    }

    /**
     *  Any actions are required by you for getting data.
     *  This is needed for communication from "View" to Presenter
     * @param <I>
     */
    public interface Action<I> {
        Observable<I> bind();
    }

}
