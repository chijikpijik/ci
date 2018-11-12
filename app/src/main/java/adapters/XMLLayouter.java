package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by gputintsev on 25.11.16.
 */
public class XMLLayouter<T> implements FieldLayouter<T> {

    private final Matcher<T> mMatcher;

    private final Creator<T> mCreator;

    private final int layoutId;

    public XMLLayouter(Matcher<T> matcher, Creator<T> creator, int layoutId) {
        mMatcher = matcher;
        this.layoutId = layoutId;
        mCreator = creator;
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
        View res = LayoutInflater.from(root.getContext()).inflate(layoutId, root, false);
        return mCreator.create(res, root);
    }

    public interface Matcher<T> {

        boolean match(T data);
    }

    public interface Creator<T> {

        ViewHolder<? extends T> create(View v, ViewGroup root);
    }
}