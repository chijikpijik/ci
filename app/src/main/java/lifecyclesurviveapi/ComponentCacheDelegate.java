package lifecyclesurviveapi;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ComponentCacheDelegate {
    private static final String NEXT_ID_KEY = "next-presenter-id";

    private NonConfigurationInstance mNonConfigurationInstance;

    public void onCreate(Bundle savedInstanceState, Object nonConfigurationInstance) {
        if (nonConfigurationInstance == null) {
            long seed = 0;
            if (savedInstanceState != null) {
                seed = savedInstanceState.getLong(NEXT_ID_KEY);
            }
            this.mNonConfigurationInstance = new NonConfigurationInstance(seed);
        } else {
            this.mNonConfigurationInstance = (NonConfigurationInstance)nonConfigurationInstance;
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(NEXT_ID_KEY, mNonConfigurationInstance.nextId.get());
    }

    public Object onRetainCustomNonConfigurationInstance() {
        return mNonConfigurationInstance;
    }

    public long generateId() {
        return mNonConfigurationInstance.nextId.getAndIncrement();
    }

    @SuppressWarnings("unchecked")
    public final <C> C getComponent(long index) {
        return (C) mNonConfigurationInstance.components.get(index);
    }

    public void setComponent(long index, Object component) {
        mNonConfigurationInstance.components.put(index, component);
    }

    private static class NonConfigurationInstance {
        private Map<Long, Object> components;
        private AtomicLong nextId;
        public NonConfigurationInstance(long seed) {
            components = new HashMap<>();
            nextId = new AtomicLong(seed);
        }
    }
}
