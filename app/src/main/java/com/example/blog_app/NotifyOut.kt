package com.example.blog_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class NotifyOut : Fragment() {

    private var userId: String? = null

    private var ipUrl: String? = "192.168.8.18"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(NotifyOut.ARG_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notify_out, container, false)

        val loginUrl = "http://$ipUrl:3000/notify"
        val id = userId

        Toast.makeText(requireContext(), "El id: ${id.toString()}", Toast.LENGTH_SHORT).show()

        val keyCode = view.findViewById<EditText>(R.id.keyCodeOutEditText)
        val btn = view.findViewById<Button>(R.id.btnOut)

        btn.setOnClickListener {

            val request = object : StringRequest(
                Method.POST, loginUrl,
                Response.Listener { response ->
                    if (response.contains("ok", ignoreCase = true)){
                        Toast.makeText(requireContext(), "Notificado y agrega la navegacion a tu home.", Toast.LENGTH_SHORT).show()
                    }
                    if (response.contains("missmatch", ignoreCase = true)){
                        Toast.makeText(requireContext(), "No coincide.", Toast.LENGTH_SHORT).show()
                    }
                    if (response.contains("already out", ignoreCase = true)){
                        Toast.makeText(requireContext(), "Ya notificaste tu salida.", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(
                        requireContext(),
                        error.message,
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["keycode"] = keyCode.text.toString()
                    params["id"] = id.toString()
                    return params
                }
            }
            val queue = Volley.newRequestQueue(requireContext())
            queue.add(request)
        }

        return view
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String?): NotifyOut {
            val fragment = NotifyOut()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

}
