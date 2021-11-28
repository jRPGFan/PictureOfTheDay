package com.example.pictureoftheday.recycler

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureoftheday.R
import kotlinx.android.synthetic.main.recycler_item_earth.view.*
import kotlinx.android.synthetic.main.recycler_item_mars.view.*

class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Pair<RecyclerData, Boolean>>,
    private val dragListener: OnStartDragListener
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_EARTH -> EarthViewHolder(
                inflater.inflate(R.layout.recycler_item_earth, parent, false) as View
            )
            TYPE_MARS -> MarsViewHolder(
                inflater.inflate(R.layout.recycler_item_mars, parent, false) as View
            )
            else -> HeaderViewHolder(
                inflater.inflate(R.layout.recycler_header, parent, false) as View
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChanges =
                createCombinedPayload(payloads as List<Changes<Pair<RecyclerData, Boolean>>>)
            val oldData = combinedChanges.oldData
            val newData = combinedChanges.newData

            if (newData.first.text != oldData.first.text)
                holder.itemView.marsTextView.text = data[position].first.text
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            data[position].first.description.isNullOrBlank() -> TYPE_MARS
            else -> TYPE_EARTH
        }
    }

    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() = Pair(RecyclerData(1, "Mars", ""), false)

    fun setItems(newItems: List<Pair<RecyclerData, Boolean>>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(data, newItems))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newItems)
    }

    inner class DiffUtilCallback(
        private var oldItems: List<Pair<RecyclerData, Boolean>>,
        private var newItems: List<Pair<RecyclerData, Boolean>>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.id == newItems[newItemPosition].first.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.text == newItems[newItemPosition].first.text

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Changes(oldItem, newItem)
        }
    }

    inner class EarthViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Pair<RecyclerData, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.descriptionTextView.text = data.first.description
                itemView.wikiImageView.setOnClickListener {
                    onListItemClickListener.onItemClick(data.first)
                }
            }
        }
    }

    inner class MarsViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {
        @SuppressLint("ClickableViewAccessibility")
        override fun bind(data: Pair<RecyclerData, Boolean>) {
            itemView.marsImageView.setOnClickListener {
                onListItemClickListener.onItemClick(data.first)
            }
            itemView.addMarsItemImageView.setOnClickListener { addItem() }
            itemView.removeMarsItemImageView.setOnClickListener { removeItem() }
            itemView.moveMarsItemUp.setOnClickListener { moveUp() }
            itemView.moveMarsItemDown.setOnClickListener { moveDown() }
            itemView.marsDescription.visibility = if (data.second) View.VISIBLE else View.GONE
            itemView.marsTextView.setOnClickListener { toggleText() }
            itemView.marsDragHandle.setOnTouchListener { _, motionEvent ->
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN)
                    dragListener.onStartDrag(this)
                false
            }
        }

        private fun addItem() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(dataItem: Pair<RecyclerData, Boolean>) {
            itemView.setOnClickListener {
                onListItemClickListener.onItemClick(dataItem.first)
//                data[1] = Pair(Data("Jupiter", ""), false)
//                notifyItemChanged(1, Pair(Data("", ""), false))
            }
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: RecyclerData)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
    }
}