package lifecyclesurviveapi;

import android.os.Bundle;

public class PresenterControllerDelegate<P extends Presenter> {
    private boolean mIsDestroyedBySystem;
    private P mPresenter;

    public void onCreate(P presenter, Bundle savedInstanceState) {
        this.mPresenter = presenter;
        PresenterBundle bundle = PresenterBundleUtils.getPresenterBundle(savedInstanceState);
        presenter.onCreate(bundle);
    }

    @SuppressWarnings("unchecked")
    public void onViewCreated(Object view) {
        try {
            mPresenter.bindView(view);
        } catch (ClassCastException e) {
            throw new RuntimeException("The mView provided does not implement the mView interface " +
                    "expected by " + mPresenter.getClass().getSimpleName() + ".", e);
        }
    }

    public void onResume() {
        mIsDestroyedBySystem = false;
    }

    public void onSaveInstanceState(Bundle outState) {
        mIsDestroyedBySystem = true;
        PresenterBundle bundle = new PresenterBundle();
        mPresenter.onSaveInstanceState(bundle);
        PresenterBundleUtils.setPresenterBundle(outState, bundle);
    }

    public void onDestroyView() {
        mPresenter.unbindView();
    }

    /**
     *
     * @param forceKill Sometimes there needs to destroy the presenter, but {@link #mIsDestroyedBySystem}
     *                  is true. Example, when we use startActivityForResult onResume() isn't called by Activity that was started.
     */
    public void onDestroy(boolean forceKill) {
        if (forceKill || !mIsDestroyedBySystem) {
            mPresenter.onDestroy();
        }
    }

    public P getPresenter() {
        return mPresenter;
    }
}
