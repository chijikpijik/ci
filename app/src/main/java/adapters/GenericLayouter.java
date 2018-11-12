package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by gputintsev on 25.11.16.
 */
public class GenericLayouter<T> implements FieldLayouter<T> {

    private final Matcher<T> mMatcher;

    private final Creator<T> mCreator;

    private final ViewCreator mViewCreator;

    public GenericLayouter(Matcher<T> matcher, ViewCreator viewCreator, Creator<T> creator) {
        mMatcher = matcher;
        mCreator = creator;
        mViewCreator = viewCreator;
    }

    @Override
    public boolean isMatch(T data) {
        try {
            return mMatcher.match(data);
        } catch (ClassCastException cce) {
            cce.printStackTrace();
            return false;
        }
    }

    @Override
    public ViewHolder<? extends T> onCreateViewHolder(ViewGroup root) {
        return mCreator.create(mViewCreator.create(root.getContext()), root);
    }

    public interface Matcher<T> {

        boolean match(T data);
    }

    public interface Creator<T> {

        ViewHolder<? extends T> create(View v, ViewGroup root);
    }

    public interface ViewCreator {

        View create(Context context);
    }
}