package com.pklos.payshop.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pklos.payshop.R
import java.lang.IllegalStateException

class FiltersDialogWindowFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.sort_by)
                .setItems(R.array.sortingBy, DialogInterface.OnClickListener { dialog, index ->
                    when (index) {
                        0 -> Toast.makeText(context, "1 klik!", Toast.LENGTH_LONG).show()
                        1 -> Toast.makeText(context, "2 klik!", Toast.LENGTH_LONG).show()
                        2 -> Toast.makeText(context, "3 klik!", Toast.LENGTH_LONG).show()
                        3 -> Toast.makeText(context, "4 klik!", Toast.LENGTH_LONG).show()
                    }
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cant be null")
    }
}