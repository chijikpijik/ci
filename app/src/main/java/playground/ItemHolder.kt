package playground

import adapters.Diffable
import adapters.ViewHolder
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.akarpov.mainscreenplayground.R

/**
 * Created by a.karpov on 09.11.2018.
 */
class ItemHolder(itemView: View,
                 root: ViewGroup,
                 private val onItemClicked: (data: ItemData) -> Unit
): ViewHolder<ItemHolder.ItemData>(itemView, root) {

    var titleTv: TextView;

    init {
        titleTv = itemView.findViewById(R.id.title)
        itemView.setOnClickListener(View.OnClickListener {onItemClicked(data)})
    }

    data class ItemData(val title:String):Diffable<String> {
        override fun getDiffId(): String = title
    }

    override fun performBind(data: ItemData?) {
        super.performBind(data)
        titleTv.text = data?.title
    }
}