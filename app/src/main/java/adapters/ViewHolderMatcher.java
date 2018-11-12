package adapters;

import android.view.ViewGroup;

import com.example.akarpov.mainscreenplayground.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewHolderMatcher<T> {

    private ArrayList<FieldLayouter<T>> layouters = new ArrayList<>();

    private int incrementId = 0;

    private HashMap<FieldLayouter, Integer> layoutersIds = new HashMap<>();

    private void storeId(FieldLayouter layouter) {
        if (!layoutersIds.keySet().contains(layouter)) {
            layoutersIds.put(layouter, ++incrementId);
        }
    }

    private int getLayouterId(FieldLayouter layouter) {
        Integer res = layoutersIds.get(layouter);
        return res != null ? res : -1;
    }

    public ViewHolderMatcher add(FieldLayouter<T> layouter) {
        storeId(layouter);
        if (!layouters.contains(layouter)) {
            layouters.add(layouter);
        }
        return this;
    }

    public ViewHolderMatcher addAllToTop(ArrayList<FieldLayouter<T>> layouters) {
        Utils.apply(layouters, (iterator, val) -> storeId(val));
        if (!layouters.containsAll(this.layouters)) {
            layouters.addAll(0, this.layouters);
        }
        return this;
    }

    public ViewHolderMatcher addToTop(FieldLayouter<T> layouter) {
        storeId(layouter);
        if (!layouters.contains(layouter)) {
            layouters.add(0, layouter);
        }
        return this;
    }


    public int getMatchId(T content) {
        for (int i = 0; i < layouters.size(); i++) {
            FieldLayouter<T> layouter = layouters.get(i);
            if (layouter.isMatch(content)) {
                return getLayouterId(layouter);
            }
        }
        throw new RuntimeException("cant pick holder for " + content.toString());
    }

    public ViewHolder<T> onCreateViewHolder(int id, ViewGroup root) {
        if (id == -1) {
            return null;
        }

        FieldLayouter matchedLayouter = Utils.arrayListFirst(layouters,
                match -> Integer.valueOf(id).equals(layoutersIds.get(match)), null).getItem();
        return matchedLayouter != null ? matchedLayouter.onCreateViewHolder(root) : null;
    }

    public ArrayList<FieldLayouter<T>> getLayouters() {
        return layouters;
    }
}