package lifecyclesurviveapi;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by arsentiykarpov on 11/11/15.
 */
public abstract class PresenterActivity<C extends HasPresenter<P>, P extends Presenter>
        extends ComponentCacheActivity {

    private ComponentControllerDelegate<C> mComponentDelegate = new ComponentControllerDelegate<>();

    private PresenterControllerDelegate<P> mPresenterDelegate = new PresenterControllerDelegate<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponentDelegate.onCreate(this, savedInstanceState, componentFactory);
        mPresenterDelegate.onCreate(getComponent().getPresenter(), savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mComponentDelegate.onSaveInstanceState(outState);
        mPresenterDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mComponentDelegate.onResume();
        mPresenterDelegate.onResume();
        mPresenterDelegate.onViewCreated(this);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            /**Support library does not redraw ActionBar if {@link android.view.WindowManager.LayoutParams#FLAG_SECURE FLAG_SECURE} is set.
             * So it is redrawn manually here*/
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenterDelegate.onDestroyView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterDelegate.onDestroy(isFinishing());
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
            return PresenterActivity.this.onCreateNonConfigurationComponent();
        }
    };
}
