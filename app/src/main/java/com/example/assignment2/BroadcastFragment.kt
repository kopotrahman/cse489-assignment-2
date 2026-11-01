package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

/** Screen A: spinner + Proceed button. */
class BroadcastFragment : Fragment(R.layout.fragment_broadcast) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val btnProceed = view.findViewById<Button>(R.id.btnProceed)

        // Position 0 = Custom, position 1 = Battery
        val options = listOf("Custom broadcast receiver", "System battery notification receiver")
        spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        btnProceed.setOnClickListener {
            // An Intent = "please start this Activity". Extras = data we pass along.
            val intent = Intent(requireContext(), SecondActivity::class.java)
            intent.putExtra(SecondActivity.EXTRA_TYPE, spinner.selectedItemPosition)
            startActivity(intent)
        }
    }
}
