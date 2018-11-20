package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by gputintsev on 25.11.16.
 */

public class ViewHolder<T> extends RecyclerView.ViewHolder {

    protected T data;

    protected ViewGroup root;

    private boolean preventRebindEqual = true;

    private Touple<T, T> mNeighborhood;

    private boolean mFirst = false;

    public ViewHolder(View itemView, ViewGroup root) {
        super(preInit(itemView));
        this.root = root;
    }

    private static View preInit(View itemView) {
        if (itemView.getLayoutParams() == null) {
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return itemView;
    }

    public final void bind(T data, boolean isFirst, Touple<T, T> neighborhood) {
        repeatableBind(data);
        if (preventRebindEqual && data.equals(this.data)) {
            return;
        }
        unbind();
        internalBind(data, isFirst, neighborhood);
        this.data = data;
    }

    private void internalBind(T data, boolean first, Touple<T, T> isNextHasTheSameType) {
        mNeighborhood = isNextHasTheSameType;
        mFirst = first;
        setupFirst(mFirst);
        performBind(data);
    }

    public boolean isFirst() {
        return mFirst;
    }

    protected void setupFirst(boolean isFirst) {

    }

    protected Touple<? super T, ? super T> getNeighborhood() {
        return mNeighborhood;
    }

    protected void performBind(T data) {

    }

    protected void repeatableBind(T data) {

    }

    public void unbind() {

    }

    public ViewHolder<T> setPreventRebindEqual(boolean preventRebindEqual) {
        this.preventRebindEqual = preventRebindEqual;
        return this;
    }
}