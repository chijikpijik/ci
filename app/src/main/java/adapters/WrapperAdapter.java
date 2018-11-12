package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by gputintsev on 12.12.16.
 */

public class WrapperAdapter extends RecyclerView.Adapter {

    //640k is enough for anyone (c)
    private static final int RESERVED = 200;

    private final RecyclerView.Adapter source;

    private ArrayList<WrapperHolder> headers;

    private ArrayList<WrapperHolder> footers;

    public WrapperAdapter(RecyclerView.Adapter source) {
        this.source = source;
        headers = new ArrayList<>();
        footers = new ArrayList<>();
        RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                notifyItemRangeChanged(positionStart + headers.size(), itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                notifyItemRangeInserted(positionStart + headers.size(), itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                notifyItemRangeRemoved(positionStart + headers.size(), itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                int headersCount = headers.size();
                for (int i = 0; i < itemCount; i++) {
                    notifyItemMoved(fromPosition + headersCount + i, toPosition + headersCount + i);
                }
            }
        };
        source.registerAdapterDataObserver(dataObserver);
        setHasStableIds(source.hasStableIds());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType >= RESERVED) {
            return source.onCreateViewHolder(parent, viewType - RESERVED);
        } else {
            if (viewType < RESERVED / 2) {
                return headers.get(viewType);
            } else {
                return footers.get(viewType - RESERVED / 2);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int headersCount = headers.size();
        if (position < headersCount) {
            return position;
        } else if (position < headersCount + source.getItemCount()) {
            return source.getItemViewType(position - headersCount) + RESERVED;
        } else {
            return position - headersCount - source.getItemCount() + RESERVED / 2;
        }
    }

    @Override
    public long getItemId(int position) {
        int headersCount = headers.size();
        if (position < headersCount) {
            return position;
        } else if (position < headersCount + source.getItemCount()) {
            return source.getItemId(position - headersCount) + RESERVED;
        } else {
            return position - headersCount - source.getItemCount() + RESERVED / 2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headersCount = headers.size();
        if (position >= headersCount && position < source.getItemCount() + headersCount) {
            source.onBindViewHolder(holder, position - headersCount);
        }
    }

    @Override
    public int getItemCount() {
        return source.getItemCount() + headers.size() + footers.size();
    }

    public void addHeader(View v) {
        if (headers.size() >= RESERVED / 2) {
            throw new IllegalStateException("Can't add more than " + RESERVED / 2 + " headers");
        }
        headers.add(new WrapperHolder(v));
        notifyDataSetChanged();
    }

    public void addFooter(View v) {
        if (footers.size() >= RESERVED / 2) {
            throw new IllegalStateException("Can't add more than " + RESERVED / 2 + " footers");
        }
        footers.add(new WrapperHolder(v));
        notifyDataSetChanged();
    }

    private static class WrapperHolder extends RecyclerView.ViewHolder {

        public WrapperHolder(View itemView) {
            super(fixParams(itemView));
        }

        private static View fixParams(View itemView) {
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                itemView.setLayoutParams(params);
            }
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            return itemView;
        }
    }

    public int getHeadersCount() {
        return headers.size();
    }

    public int getFootersCount() {
        return footers.size();
    }
}
