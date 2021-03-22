package com.pklos.payshop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pklos.payshop.R
import com.pklos.payshop.data.FirebaseData
import com.pklos.payshop.data.Item
import com.pklos.payshop.data.MyCallback
import com.pklos.payshop.data.inflate
import com.pklos.payshop.utils.DialogInterfaceListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycleview_item_row.view.*

class SearchFragment: Fragment(), DialogInterfaceListener {
    private lateinit var firebaseItems: List<Item>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var searchFragmentRelativeLayout: RelativeLayout

    val FILTER_TAG = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)

        searchFragmentRelativeLayout = view.findViewById(R.id.searchFragmentLayout)

        setDefaultRecyclerViewOnStart(view)

        val filterTextView: TextView = view.findViewById(R.id.filterTextView)
        filterTextView.setOnClickListener{
            val manager: FragmentManager? = fragmentManager
            val dialog: FiltersDialogWindowFragment = FiltersDialogWindowFragment(this)
            dialog.setTargetFragment(this, FILTER_TAG)
            if (manager != null) {
                dialog.show(manager, "DIALOG_DATE")
            }
        }

        val itemSearchBar: EditText = view.findViewById(R.id.itemEditText)
        itemSearchBar.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val searchBarText: String = itemSearchBar.text.toString()

                    FirebaseData.firebaseSearchByName(searchBarText, object : MyCallback {
                        override fun onCallback(value: List<Item>) {
                            firebaseItems = value
                            linearLayoutManager = LinearLayoutManager(context)
                            itemRecyclerView = view.findViewById(R.id.search_results_recycler_view)
                            itemRecyclerView.layoutManager = linearLayoutManager
                            updateRecyclerView()
                            setSearchItemCount(view)
                        }
                    })
                    true
                }
                else -> false
            }
        }

        return view
    }

    override fun sortItemClicked(index: Int) {
        when (index) {
            0 -> Toast.makeText(activity, "1 klik!", Toast.LENGTH_LONG).show()
            1 -> Toast.makeText(activity, "2 klik!", Toast.LENGTH_LONG).show()
            2 -> Toast.makeText(activity, "3 klik!", Toast.LENGTH_LONG).show()
            3 -> Toast.makeText(activity, "4 klik!", Toast.LENGTH_LONG).show()
            4 -> Toast.makeText(activity, "5 klik!", Toast.LENGTH_LONG).show()
            else -> Toast.makeText(activity, "else klik!", Toast.LENGTH_LONG).show()
        }
    }

    override fun filterItemClicked(index: Int) {
        when (index){
            0 ->  Toast.makeText(activity, "category filter!", Toast.LENGTH_LONG).show()
            1 ->  Toast.makeText(activity, "price filter!", Toast.LENGTH_LONG).show()
            else -> Toast.makeText(activity, "else klik!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
    }

    private fun setDefaultRecyclerViewOnStart(view: View){
        firebaseItems = listOf()
        linearLayoutManager = LinearLayoutManager(context)
        itemRecyclerView = view.findViewById(R.id.search_results_recycler_view)
        itemRecyclerView.layoutManager = linearLayoutManager
        updateRecyclerView()
        setSearchItemCount(view)
    }

    private fun updateRecyclerView(){
        mAdapter = RecyclerAdapter(firebaseItems)
        itemRecyclerView.adapter = mAdapter
    }

    private fun setSearchItemCount(view: View){
        val searchTextView: TextView = view.findViewById(R.id.searchResultsTextView)
        val itemsCount = mAdapter.itemCount
        searchTextView.text = resources.getString(R.string.searchResults, itemsCount)
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

                this.item = item
                view.name.text = item.name
                view.category.text = view.context.getString(
                    R.string.category,
                    item.category.toString()
                )
                view.price.text = item.price.toString()
                //view.isFavorite.text = item.isFavorite.toString()
                var isFavoriteChoice = if (!item.isFavorite) {
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