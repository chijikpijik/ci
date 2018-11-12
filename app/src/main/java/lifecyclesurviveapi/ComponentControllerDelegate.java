package lifecyclesurviveapi;

import android.os.Bundle;

public class ComponentControllerDelegate<C> {
    private static final String PRESENTER_INDEX_KEY = "presenter-index";

    private C mComponent;
    private ComponentCache mCache;
    private long mComponentId;
    private boolean mIsDestroyedBySystem;

    public void onCreate(ComponentCache cache, Bundle savedInstanceState,
                         ComponentFactory<C> componentFactory) {
        this.mCache = cache;
        if (savedInstanceState == null) {
            mComponentId = cache.generateId();
        } else {
            mComponentId = savedInstanceState.getLong(PRESENTER_INDEX_KEY);
        }
        mComponent = cache.getComponent(mComponentId);
        if (mComponent == null) {
            mComponent = componentFactory.createComponent();
            cache.setComponent(mComponentId, mComponent);
        }
    }

    public void onResume() {
        mIsDestroyedBySystem = false;
    }

    public void onSaveInstanceState(Bundle outState) {
        mIsDestroyedBySystem = true;
        outState.putLong(PRESENTER_INDEX_KEY, mComponentId);
    }

    public void onDestroy() {
        if (!mIsDestroyedBySystem) {
            mCache.setComponent(mComponentId, null);
        }
    }

    public C getComponent() {
        return mComponent;
    }
}
