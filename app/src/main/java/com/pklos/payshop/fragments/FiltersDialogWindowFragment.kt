package com.pklos.payshop.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.pklos.payshop.R
import com.pklos.payshop.utils.DialogInterfaceListener
import java.lang.IllegalStateException

class FiltersDialogWindowFragment(var listener: DialogInterfaceListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.sort_by)
                .setItems(R.array.sortingBy) { _, index ->
                    listener.filterItemClicked(index)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cant be null")
    }
}