package lifecyclesurviveapi;

import android.support.annotation.NonNull;

public interface ComponentFactory<C> {
    @NonNull
    C createComponent();
}
