package adapters;

import android.view.ViewGroup;


/**
 * Created by gputintsev on 25.11.16.
 */

public interface FieldLayouter<T> {

    boolean isMatch(T data);

    ViewHolder<? extends T> onCreateViewHolder(ViewGroup root);

}