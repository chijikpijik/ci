package adapters;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by m.artamonov on 16.05.2018.
 */

public class AwesomeDiffUtils<T extends Diffable> extends DiffUtil.Callback  {

    protected final List<T> mOldList;

    protected final List<T> mNewList;

    public AwesomeDiffUtils(List<T> oldList, List<T> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getDiffId()
                .equals(mNewList.get(newItemPosition).getDiffId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
    }
}
