package lifecyclesurviveapi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class ComponentControllerFragment<C> extends Fragment {
    private ComponentCache mComponentCache;
    private ComponentControllerDelegate<C> mComponentDelegate = new ComponentControllerDelegate<>();

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof ComponentCache) {
            mComponentCache = (ComponentCache)activity;
        } else {
            throw new RuntimeException(getClass().getSimpleName() + " must be attached to " +
                    "an Activity that implements " + ComponentCache.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponentDelegate.onCreate(mComponentCache, savedInstanceState, componentFactory);
    }

    @Override
    public void onResume() {
        super.onResume();
        mComponentDelegate.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mComponentDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mComponentDelegate.onDestroy();
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
}