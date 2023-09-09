package com.example.blog_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class NotificationFragment : Fragment() {

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(NotificationFragment.ARG_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        val btnNotifyIn = view.findViewById<Button>(R.id.btnNotifyIn)
        val btnNotifyOut = view.findViewById<Button>(R.id.btnNotifyOut)

        // Toast.makeText(requireContext(), "Usuario $userId", Toast.LENGTH_SHORT).show()

        // Go to the fragment NotifyIn
        btnNotifyIn.setOnClickListener {
            // val destinationFragment = NotifyIn.newInstance(userId)
            val destinationFragment = NotifyIn.newInstance(userId)
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragment_container, destinationFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        // Go to the fragment NotifyOut
        btnNotifyOut.setOnClickListener {
            val destinationFragment = NotifyOut.newInstance(userId)
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragment_container, destinationFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String?): NotificationFragment {
            val fragment = NotificationFragment()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }
}
