package lifecyclesurviveapi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class PresenterFragment<C extends HasPresenter<P>, P extends Presenter>
        extends Fragment implements ComponentCache {

    private ComponentControllerDelegate<C> mComponentDelegate = new ComponentControllerDelegate<>();

    private PresenterControllerDelegate<P> mPresenterDelegate = new PresenterControllerDelegate<>();

    private ComponentCacheDelegate mDelegate = new ComponentCacheDelegate();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState, mDelegate.onRetainCustomNonConfigurationInstance());
        mComponentDelegate.onCreate(this, savedInstanceState, componentFactory);
        mPresenterDelegate.onCreate(getComponent().getPresenter(), savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mComponentDelegate.onSaveInstanceState(outState);
        mPresenterDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mComponentDelegate.onResume();
        mPresenterDelegate.onResume();
        mPresenterDelegate.onViewCreated(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenterDelegate.onDestroyView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenterDelegate.onDestroy(getActivity().isFinishing());
        mComponentDelegate.onDestroy();
    }

    public P getPresenter() {
        return mPresenterDelegate.getPresenter();
    }

    public C getComponent() {
        return mComponentDelegate.getComponent();
    }

    protected abstract C onCreateNonConfigurationComponent();

    private ComponentFactory<C> componentFactory = new ComponentFactory<C>() {
        @NonNull
        @Override
        public C createComponent() {
            return onCreateNonConfigurationComponent();
        }
    };

    @Override
    public long generateId() {
        return mDelegate.generateId();
    }

    @Override
    public final <C> C getComponent(long index) {
        return mDelegate.getComponent(index);
    }

    @Override
    public void setComponent(long index, Object component) {
        mDelegate.setComponent(index, component);
    }


}
