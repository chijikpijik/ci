package lifecyclesurviveapi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ComponentCacheActivity extends AppCompatActivity implements ComponentCache {

    public static final String KEY_INTERNAL = "flag_internal";


    private ComponentCacheDelegate mDelegate = new ComponentCacheDelegate();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDelegate.onCreate(savedInstanceState, getLastCustomNonConfigurationInstance());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mDelegate.onRetainCustomNonConfigurationInstance();
    }

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

    @Override
    public void startActivity(Intent intent) {
        if (!intent.hasExtra(KEY_INTERNAL)) {
            intent.putExtra(KEY_INTERNAL, true);
        }
        PackageManager pm = getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            super.startActivity(intent);
        }
    }
}
