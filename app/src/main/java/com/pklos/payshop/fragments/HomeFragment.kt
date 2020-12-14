package com.pklos.payshop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pklos.payshop.R
import com.pklos.payshop.utils.AppUtils

class HomeFragment: Fragment() {
    private lateinit var welcomeTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showWelcomeMessageOnDayTime(view)
    }

    private fun showWelcomeMessageOnDayTime(view: View) {
        val dayTime = AppUtils.printWelcomeMessageOnDayTime()
        welcomeTextView = view.findViewById(R.id.welcomeTextView)
        welcomeTextView.text = "${getString(R.string.good)} $dayTime"
    }

    private fun showItemsOnScreen(){

    }
}