package lifecyclesurviveapi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T> implements Presenter<T> {

    protected T mView;

    ViewDependantHelper viewDependantHelper = new ViewDependantHelper();

    protected CompositeSubscription mMainCompositeSubscription;

    public static final String PRESENTER_HASH = "presenter_hash";

    public CompositeSubscription getCompositeSubscription() {
        if (mMainCompositeSubscription == null) {
            mMainCompositeSubscription = new CompositeSubscription();
        }
        return mMainCompositeSubscription;
    }

    public void addSubscription(Subscription subscription) {
        getCompositeSubscription().add(subscription);
    }

    @Override
    public void bindView(final T view) {
        this.mView = view;
        viewDependantHelper.bind();
    }

    @Override
    public void unbindView() {
        this.mView = null;
        viewDependantHelper.unbind();
    }

    protected void onFirstViewBound() {

    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        if (bundle == null || checkHashChanged(bundle)) {//TODO: ADD PROPER HANDLING

            addSubscription(Observable.just(true).lift(liftToViewDependant())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(b -> {
                        if (checkRecreated(bundle)) {
                            onRecreated();
                        } else {
                            onFirstViewBound();
                        }
                    }));
        }
    }

    protected boolean checkRecreated(PresenterBundle bundle) {
        return checkHashChanged(bundle);
    }

    @Override
    public void onDestroy() {
        getCompositeSubscription().unsubscribe();
    }

    @Override
    public void onSaveInstanceState(@NonNull PresenterBundle bundle) {
        bundle.putInt(PRESENTER_HASH, hashCode());
    }

    public <E> Observable.Operator<E, E> liftToViewDependant() {
        return viewDependantHelper.liftToViewDependant();
    }

    protected void onRecreated() {
    }

    private boolean checkHashChanged(PresenterBundle bundle) {
        if (bundle != null && bundle.containsKey(PRESENTER_HASH)) {
            return hashCode() != bundle.getInt(PRESENTER_HASH);
        }
        return false;
    }
}
