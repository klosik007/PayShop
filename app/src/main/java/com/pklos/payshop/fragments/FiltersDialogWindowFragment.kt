package com.pklos.payshop.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pklos.payshop.R
import com.pklos.payshop.utils.DialogInterfaceListener
import kotlinx.android.synthetic.*
import java.lang.IllegalStateException

class FiltersDialogWindowFragment(var listner: DialogInterfaceListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view: View = inflater.inflate(R.layout.filter_sort_popup_menu, null, false)

            val sortCategoryDescending: Button = view.findViewById(R.id.sortBy_categoryDescending)
            sortCategoryDescending.setOnClickListener {
               listner.firebaseSortByCategoryNameDescending()
            }

            val sortCategoryAscending: Button = view.findViewById(R.id.sortBy_categoryAscending)
            sortCategoryAscending.setOnClickListener {
                listner.firebaseSortByCategoryNameAscending()
            }

            val sortPriceDescending: Button = view.findViewById(R.id.sortBy_priceDescending)
            sortPriceDescending.setOnClickListener {
                listner.firebaseSortPriceDescending()
            }

            val sortPriceAscending: Button = view.findViewById(R.id.sortBy_priceAscending)
            sortPriceAscending.setOnClickListener {
                listner.firebaseSortPriceAscending()
            }

            val sortNameDescending: Button = view.findViewById(R.id.sortBy_nameDescending)
            sortNameDescending.setOnClickListener {
                listner.firebaseSortNameDescending()
            }

            val sortNameAscending: Button = view.findViewById(R.id.sortBy_nameAscending)
            sortNameAscending.setOnClickListener {
                listner.firebaseSortNameAscending()
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cant be null")
    }
}