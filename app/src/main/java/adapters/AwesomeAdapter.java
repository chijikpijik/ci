package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gputintsev on 25.11.16.
 */

public class AwesomeAdapter<T> extends RecyclerView.Adapter<ViewHolder<T>> {

    private List<T> list = new ArrayList<>();

    private ViewHolderMatcher<T> mMatcher;

    public AwesomeAdapter() {
        this.mMatcher = new ViewHolderMatcher<>();
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void update(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        return mMatcher.getMatchId(list.get(position));
    }

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return mMatcher.onCreateViewHolder(viewType, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        Touple<T, T> neighborhood = new Touple<>(position > 0 ? list.get(position - 1) : null,
                position < list.size() - 1 ? list.get(position + 1) : null);
        holder.bind(list.get(position), position == 0, neighborhood);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ViewHolderMatcher<T> getMatcher() {
        return mMatcher;
    }

    public void addHolder(Class<? extends T> data, XMLLayouter.Creator<T> creator, int layout) {
        mMatcher.add(new XMLLayouter<>(d -> d.getClass().isAssignableFrom(data), creator, layout));
    }

    public void addHolder(GenericLayouter.Matcher<T> matcher,
            GenericLayouter.ViewCreator viewCreator, GenericLayouter.Creator<T> creator) {
        mMatcher.add(new GenericLayouter<>(matcher, viewCreator, creator));
    }

    public void addHolder(Class<? extends T> data, GenericLayouter.ViewCreator viewCreator,
            GenericLayouter.Creator<T> creator) {
        mMatcher.add(new GenericLayouter<>(d -> d.getClass().isAssignableFrom(data), viewCreator,
                creator));
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder<T> holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }
}