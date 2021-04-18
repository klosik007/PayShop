package com.pklos.payshop.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pklos.payshop.R
import com.pklos.payshop.data.Category
import com.pklos.payshop.data.FirebaseData
import com.pklos.payshop.data.Item
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

            val sportCategoryCheckbox: CheckBox = view.findViewById(R.id.sportCategory)
            sportCategoryCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    FirebaseData.checkboxFilters.add { filter -> filter.category == Category.SPORT}
                }else FirebaseData.checkboxFilters.remove { filter -> filter.category == Category.SPORT }
            }

            val homeCategoryCheckbox: CheckBox = view.findViewById(R.id.homeCategory)
            homeCategoryCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    FirebaseData.checkboxFilters.add { filter -> filter.category == Category.HOME}
                }else FirebaseData.checkboxFilters.remove { filter -> filter.category == Category.HOME }
            }

            val foodCategoryCheckbox: CheckBox = view.findViewById(R.id.foodCategory)
            foodCategoryCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    FirebaseData.checkboxFilters.add { filter -> filter.category == Category.FOOD}
                }else FirebaseData.checkboxFilters.remove { filter -> filter.category == Category.FOOD }
            }

            val editTextPriceFrom: EditText = view.findViewById(R.id.priceFromEditText)
            //FirebaseData.priceFrom = editTextPriceFrom.text

            val applyFiltersBtn: Button = view.findViewById(R.id.sortApplyBtn)
            applyFiltersBtn.setOnClickListener {
                listner.firebaseApplyFilters()
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cant be null")
    }
}