package com.pklos.payshop.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pklos.payshop.R
import com.pklos.payshop.activities.MainActivity
import com.pklos.payshop.playground.Data
import com.pklos.payshop.playground.Item
import com.pklos.payshop.playground.inflate
import com.pklos.payshop.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.recycleview_item_row.view.*
import org.w3c.dom.Text

class SearchFragment: Fragment() {
    private var dataItems: List<Item> = Data.dataList
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: RecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        linearLayoutManager = LinearLayoutManager(context)
        search_results_recycler_view.layoutManager = linearLayoutManager
        updateUI()
        return view
    }

//    override fun onStart() {
//        super.onStart()
//        if(dataItems.isEmpty()){
//
//        }
//    }

    private fun updateUI(){
        mAdapter = RecyclerAdapter(dataItems)
        search_results_recycler_view.adapter = mAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
    }

    class RecyclerAdapter(private val items: List<Item>) : RecyclerView.Adapter<RecyclerAdapter.SearchItemHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.SearchItemHolder {
            val inflatedView = parent.inflate(R.layout.recycleview_item_row, false)
            return SearchItemHolder(inflatedView)
        }

        override fun onBindViewHolder(holder: RecyclerAdapter.SearchItemHolder, position: Int) {
            val itemData = items[position]
            holder.bindItem(itemData)
        }

        override fun getItemCount(): Int  = items.size //how many items are displayed

        class SearchItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            private var view: View = itemView
            private var item: Item? = null

            fun bindItem(item: Item){
                this.item = item
                view.name.text = item.name
                view.category.text = item.category.toString()
                view.price.text = item.price.toString()
                view.isFavorite.text = item.isFavorite.toString()
            }
        }
    }
}