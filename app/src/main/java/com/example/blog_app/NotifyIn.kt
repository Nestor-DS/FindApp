package com.example.blog_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NotifyIn : Fragment() {

    private var userId: String? = null

    private var ipUrl: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(ARG_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_notify_in, container, false)

        val loginUrl = "http://$ipUrl:3000/back"

        val id = userId

        Toast.makeText(requireContext(), "El id: ${id.toString()}", Toast.LENGTH_SHORT).show()

        val keyCode = view.findViewById<EditText>(R.id.keyCodeInEditText)
        val btn = view.findViewById<Button>(R.id.btnIn)

        btn.setOnClickListener {

            val request = object : StringRequest(
                Method.POST, loginUrl,
                Response.Listener { response ->
                    if (response.contains("ok", ignoreCase = true)) {
                        notify(id)
                        Toast.makeText(requireContext(), "Notificado y agrega la navegacion a tu home.", Toast.LENGTH_SHORT).show()
                    }
                    if (response.contains("missmatch", ignoreCase = true)){
                        Toast.makeText(requireContext(), "No coincide.", Toast.LENGTH_SHORT).show()
                    }
                    if (response.contains("already in", ignoreCase = true)){
                        Toast.makeText(requireContext(), "Ya notificaste tu regreso.", Toast.LENGTH_SHORT).show()
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

    private fun notify(userId: String?) {
        val notifyUrl = "http://$ipUrl:3000/send"
        val queue = Volley.newRequestQueue(requireContext())
        val stringRequest = object : StringRequest(
            Request.Method.POST, notifyUrl,
            Response.Listener {
                // Procesar la respuesta del servidor (si es necesario)
                Toast.makeText(requireContext(), "Notificado y agrega la navegacion a tu home.", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Error al enviar notificación.", Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }

            override fun getBody(): ByteArray {
                val json = JSONObject()
                json.put("id", userId ?: "")
                json.put("coords", "Regreso a la zona delimitada")
                return json.toString().toByteArray(Charsets.UTF_8)
            }
        }
        queue.add(stringRequest)
    }

    companion object {
        private const val ARG_USER_ID = "user_id"
        fun newInstance(userId: String?): NotifyIn {
            val fragment = NotifyIn()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }
}
