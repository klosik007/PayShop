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
import com.pklos.payshop.activities.MainActivity
import com.pklos.payshop.data.Category
import com.pklos.payshop.data.FirebaseData
import com.pklos.payshop.data.Item
import com.pklos.payshop.databinding.FilterSearchFragmentBinding
import com.pklos.payshop.databinding.FilterSortPopupMenuBinding
import com.pklos.payshop.utils.DialogInterfaceListener
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.filter_sort_popup_menu.view.*
import java.lang.IllegalStateException

class FiltersDialogWindowFragment(var listner: DialogInterfaceListener) : DialogFragment() {

    private lateinit var binding: FilterSortPopupMenuBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            binding = FilterSortPopupMenuBinding.inflate(inflater)
            val view = binding.root

            with (view){
                sortBy_categoryDescending.setOnClickListener {
                    listner.firebaseSortByCategoryNameDescending()
                }

                sortBy_categoryAscending.setOnClickListener {
                    listner.firebaseSortByCategoryNameAscending()
                }

                sortBy_priceDescending.setOnClickListener {
                    listner.firebaseSortPriceDescending()
                }

                sortBy_priceAscending.setOnClickListener {
                    listner.firebaseSortPriceAscending()
                }

                sortBy_nameDescending.setOnClickListener {
                    listner.firebaseSortNameDescending()
                }

                sortBy_nameAscending.setOnClickListener {
                    listner.firebaseSortNameAscending()
                }

                sportCategory.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){
                        FirebaseData.checkboxFilters.add { filter -> filter.category == Category.SPORT}
                    }else FirebaseData.checkboxFilters.remove { filter -> filter.category == Category.SPORT }
                }

                homeCategory.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){
                        FirebaseData.checkboxFilters.add { filter -> filter.category == Category.HOME}
                    }else FirebaseData.checkboxFilters.remove { filter -> filter.category == Category.HOME }
                }

                foodCategory.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){
                        FirebaseData.checkboxFilters.add { filter -> filter.category == Category.FOOD}
                    }else FirebaseData.checkboxFilters.remove { filter -> filter.category == Category.FOOD }
                }

                sortApplyBtn.setOnClickListener {
                    var priceFrom = 0
                    var priceTo: Int? = 0

                    if (priceFromEditText.text.toString().isEmpty() && priceToEditText.text.toString().isEmpty()){
                        priceTo = null
                    }

                    if (priceFromEditText.text.toString().isNotEmpty() && priceToEditText.text.toString().isNotEmpty()){
                        priceFrom = priceFromEditText.text.toString().toInt()
                        priceTo = priceToEditText.text.toString().toInt()
                    }

                    if (priceFromEditText.text.toString().isEmpty() && priceToEditText.text.toString().isNotEmpty()){
                        priceFrom = 0
                    }

                    if (priceFromEditText.text.toString().isNotEmpty() && priceToEditText.text.toString().isEmpty()){
                        priceTo = null
                    }

                    FirebaseData.priceFrom = priceFrom
                    FirebaseData.priceTo = priceTo
                    listner.firebaseApplyFilters()

                }
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cant be null")
    }
}