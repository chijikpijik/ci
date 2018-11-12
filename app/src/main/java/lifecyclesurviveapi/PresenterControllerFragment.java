package lifecyclesurviveapi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class PresenterControllerFragment<C extends HasPresenter<P>, P extends Presenter>
        extends ComponentControllerFragment<C> {

    private PresenterControllerDelegate<P> mPresenterDelegate = new PresenterControllerDelegate<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenterDelegate.onCreate(getComponent().getPresenter(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenterDelegate.onViewCreated(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenterDelegate.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenterDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterDelegate.onDestroy(getActivity().isFinishing());
    }

    public P getPresenter() {
        return mPresenterDelegate.getPresenter();
    }
}
