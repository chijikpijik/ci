package lifecyclesurviveapi;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class ViewDependantHelper {

    private BehaviorSubject<Boolean> mIsViewAvailableSubject = BehaviorSubject.create();

    public ViewDependantHelper() {
    }

    public class SimpleWrapper<T> {

        private T t;

        private boolean isCleared = false;

        public SimpleWrapper(T t) {
            this.t = t;
        }

        public void clear() {
            t = null;
            isCleared = true;
        }

        public T get() {
            if (!isCleared) {
                return t;
            } else {
                throw new IllegalStateException("SimpleWrapper has cleared");
            }
        }

    }

    public void bind() {
        mIsViewAvailableSubject.onNext(true);
    }

    public void unbind() {
        mIsViewAvailableSubject.onNext(false);
    }

    public <E> Observable.Operator<E, E> liftToViewDependant() {
        final CompositeSubscription viewDependantSubscriptions = new CompositeSubscription();
        final PublishSubject<Boolean> isComplete = PublishSubject.create();
        final PublishSubject<Boolean> isError = PublishSubject.create();
        return new Observable.Operator<E, E>() {
            @Override
            public Subscriber<? super E> call(final Subscriber<? super E> subscriber) {
                return new Subscriber<E>() {
                    @Override
                    public void onCompleted() {
                        viewDependantSubscriptions.add(Observable
                                .combineLatest(mIsViewAvailableSubject, isComplete,
                                        new Func2<Boolean, Boolean, Boolean>() {
                                            @Override
                                            public Boolean call(Boolean aBoolean,
                                                    Boolean aBoolean2) {
                                                return aBoolean && aBoolean2;
                                            }
                                        }).filter(new Func1<Boolean, Boolean>() {
                                    @Override
                                    public Boolean call(Boolean b) {
                                        return b;
                                    }
                                }).subscribe(new Observer<Boolean>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }

                                    @Override
                                    public void onNext(Boolean e) {
                                        subscriber.onCompleted();
                                        viewDependantSubscriptions.clear();
                                    }
                                }));
                        isComplete.onNext(true);
                    }

                    @Override
                    public void onError(final Throwable e) {
                        viewDependantSubscriptions.add(Observable
                                .combineLatest(mIsViewAvailableSubject, isError,
                                        (aBoolean, aBoolean2) -> {
                                            if (aBoolean && aBoolean2) {
                                                return e;
                                            }
                                            return null;
                                        }).filter(e1 -> e1 != null).subscribe(new Observer<Throwable>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Throwable e) {
                                        subscriber.onError(e);
                                        viewDependantSubscriptions.clear();
                                    }
                                }));
                        isError.onNext(true);
                    }

                    @Override
                    public void onNext(final E e) {
                        final ViewDependantHelper.SimpleWrapper<E> itemWrapper
                                = new ViewDependantHelper.SimpleWrapper<>(e);//Cause e could be null
                        viewDependantSubscriptions.add(Observable
                                .combineLatest(mIsViewAvailableSubject, Observable.just(true),
                                        new Func2<Boolean, Boolean, ViewDependantHelper.SimpleWrapper<E>>() {
                                            @Override
                                            public ViewDependantHelper.SimpleWrapper<E> call(
                                                    Boolean aBoolean, Boolean aBoolean2) {
                                                if (aBoolean && aBoolean2) {
                                                    return itemWrapper;
                                                }
                                                return null;
                                            }
                                        })
                                .filter(new Func1<ViewDependantHelper.SimpleWrapper<E>, Boolean>() {
                                    @Override
                                    public Boolean call(
                                            ViewDependantHelper.SimpleWrapper<E> itemWrapper) {
                                        return itemWrapper != null && !itemWrapper.isCleared;
                                    }
                                }).subscribe(new Observer<ViewDependantHelper.SimpleWrapper<E>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(ViewDependantHelper.SimpleWrapper<E> e) {
                                        subscriber.onNext(e.get());
                                        itemWrapper.clear();
                                    }

                                }));
                    }
                };
            }
        };
    }

}
