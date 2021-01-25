package com.pklos.payshop.fragments

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pklos.payshop.R
import com.pklos.payshop.data.FirebaseData
import com.pklos.payshop.data.Item
import com.pklos.payshop.data.MyCallback
import com.pklos.payshop.data.inflate
import com.pklos.payshop.utils.AppUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycleview_item_row.view.*

class SearchFragment: Fragment() {
    private lateinit var firebaseItems: List<Item>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var itemRecyclerView: RecyclerView

    private var isSlided: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
//        FirebaseData.firebaseDataDownload(object : MyCallback {
//            override fun onCallback(value: List<Item>) {
//                firebaseItems = value
//                linearLayoutManager = LinearLayoutManager(context)
//                itemRecyclerView = view.findViewById(R.id.search_results_recycler_view)
//                itemRecyclerView.layoutManager = linearLayoutManager
//                updateUI(view)
//            }
//        })

        val filterTextView: TextView = view.findViewById(R.id.filterTextView)
        filterTextView.setOnClickListener{
            val filterSortView: View = view.findViewById(R.id.filterSortMenu)
            val recyclerView: View = view.findViewById(R.id.search_results_recycler_view)
            onFilterSortClick(filterSortView, recyclerView)
        }

        val itemSearchBar: EditText = view.findViewById(R.id.itemEditText)
        itemSearchBar.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val searchBarText: String = itemSearchBar.text.toString()

                    FirebaseData.firebaseSearchByName(searchBarText, object : MyCallback {
                        override fun onCallback(value: List<Item>) {
                            firebaseItems = value
                            linearLayoutManager = LinearLayoutManager(context)
                            itemRecyclerView = view.findViewById(R.id.search_results_recycler_view)
                            itemRecyclerView.layoutManager = linearLayoutManager
                            updateUI(view)
                        }
                    })
                    true
                }
                else -> false
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
    }

    private fun updateUI(view: View){
        mAdapter = RecyclerAdapter(firebaseItems)
        itemRecyclerView.adapter = mAdapter

        val searchTextView: TextView = view.findViewById(R.id.searchResultsTextView)
        val itemsCount = mAdapter.itemCount
        searchTextView.text = resources.getString(R.string.searchResults, itemsCount)
    }

    private fun onFilterSortClick(vararg view: View){
        if (!isSlided){
            AppUtils.slideFromRightToLeft(view[0])
            AppUtils.slideFromLeftToRight(view[1])
        }else{
            AppUtils.slideFromLeftToRight(view[0])
            AppUtils.slideFromRightToLeft(view[1])
        }

        isSlided= !isSlided
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

        class SearchItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
            private var view: View = itemView
            private var item: Item? = null

            fun bindItem(item: Item){
                var isFavoriteChoice = 0

                this.item = item
                view.name.text = item.name
                view.category.text = view.context.getString(
                    R.string.category,
                    item.category.toString()
                )
                view.price.text = item.price.toString()
                //view.isFavorite.text = item.isFavorite.toString()
                isFavoriteChoice = if (!item.isFavorite) {
                    view.isFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24_red)
                    R.drawable.ic_baseline_favorite_border_24_red
                } else {
                    view.isFavorite.setImageResource(R.drawable.ic_baseline_favorite_24_red)
                    R.drawable.ic_baseline_favorite_24_red
                }

                view.isFavorite.setOnClickListener{
                    isFavoriteChoice =
                        if (isFavoriteChoice == R.drawable.ic_baseline_favorite_border_24_red) {
                            view.isFavorite.setImageResource(R.drawable.ic_baseline_favorite_24_red)
                            R.drawable.ic_baseline_favorite_24_red
                        } else {
                            view.isFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24_red)
                            R.drawable.ic_baseline_favorite_border_24_red
                        }

                    FirebaseData.firebaseUpdateFavoriteStatus(item.name)
                }

                Picasso.get().load(item.imageUrl).resize(140, 140).centerCrop().into(view.itemImage)
                view.itemDescription.text = item.itemDescription.replace("\\n", "\n")
                view.setOnClickListener(this)//relevant if setting onClick listener!!!
            }

            override fun onClick(v: View?) {
                val context = view.context
                Toast.makeText(context, "${item?.name} klik!", Toast.LENGTH_LONG).show()
            }
        }
    }
}