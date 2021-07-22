package com.example.pictureoftheday.recycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureoftheday.R
import kotlinx.android.synthetic.main.activity_explode_animation.*
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.recycler_item_earth.view.*
import kotlinx.android.synthetic.main.recycler_item_mars.view.*

class RecyclerActivity : AppCompatActivity() {

    private lateinit var adapter: RecyclerActivityAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var isNewList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        val data = arrayListOf(
            Pair(RecyclerData(1, "Mars", ""), false)
        )
        data.add(0, Pair(RecyclerData(0, "Header"), false))
        adapter = RecyclerActivityAdapter(
            object : RecyclerActivityAdapter.OnListItemClickListener {
                override fun onItemClick(data: RecyclerData) {
                    Toast.makeText(
                        this@RecyclerActivity, data.text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            data,
            object : RecyclerActivityAdapter.OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
        )
        notesRecyclerView.adapter = adapter
        recyclerFAB.setOnClickListener { adapter.appendItem() }
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)
        recyclerDiffUtilFAB.setOnClickListener { changeAdapterData() }
    }

    private fun changeAdapterData(){
        adapter.setItems(createItemList(isNewList).map { it })
        isNewList = !isNewList
    }

    private fun createItemList(instanceNumber: Boolean): List<Pair<RecyclerData,Boolean>> {
        return when (instanceNumber) {
            false -> listOf(
                Pair(RecyclerData(0, "Header"), false),
                Pair(RecyclerData(1, "Mars", ""), false),
                Pair(RecyclerData(2, "Mars", ""), false),
                Pair(RecyclerData(3, "Mars", ""), false),
                Pair(RecyclerData(4, "Mars", ""), false),
                Pair(RecyclerData(5, "Mars", ""), false),
                Pair(RecyclerData(6, "Mars", ""), false)
            )
            true -> listOf(
                Pair(RecyclerData(0, "Header"), false),
                Pair(RecyclerData(1, "Mars", ""), false),
                Pair(RecyclerData(2, "Jupiter", ""), false),
                Pair(RecyclerData(3, "Mars", ""), false),
                Pair(RecyclerData(4, "Neptune", ""), false),
                Pair(RecyclerData(5, "Saturn", ""), false),
                Pair(RecyclerData(6, "Mars", ""), false)
            )
        }
    }
}
