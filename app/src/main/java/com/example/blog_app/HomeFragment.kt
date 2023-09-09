package com.example.blog_app

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var txtclave: TextView
    private lateinit var progressDialog: ProgressDialog
    private var userId: String? = null

    private var ipUrl: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        txtclave = view.findViewById(R.id.txtclave)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please wait...")

        val btnguardar: Button = view.findViewById(R.id.btnguardar)
        btnguardar.setOnClickListener { btnguardar() }

        userId?.let {
            progressDialog.show()

            val encodedUserId = Uri.encode(it)
            val url = "http://$ipUrl/findapp/board/viewBoard.php?user_id=$encodedUserId"

            val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    progressDialog.dismiss()

                    try {
                        val datosUsuario = JSONObject(response)
                        val id_gps = datosUsuario.getString("id_gps")

                        txtclave.text = id_gps // Display the id_gps in TextView

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        showToast("Error al procesar los datos de contacto $userId")
                    }
                },
                { error ->
                    progressDialog.dismiss()
                    error.printStackTrace()
                    showToast("Error obteniendo los datos de contacto $userId")
                })

            val requestQueue = Volley.newRequestQueue(requireContext())
            stringRequest.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                3,  // Intentar 3 veces antes de mostrar el error
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(stringRequest)
        }
        return view
    }

    private fun btnguardar() {
        val clave = txtclave.text.toString().trim()

        // Validaciones
        if (clave.isEmpty()) {
            Snackbar.make(requireView(), "Por favor ingresa una clave", Snackbar.LENGTH_LONG).show()
            return
        }

        val url = "http://$ipUrl/findapp/board/insertBoard.php"
        val queue = Volley.newRequestQueue(requireContext())

        val resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                Snackbar.make(requireView(), "Clave guardada exitosamente", Snackbar.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Snackbar.make(
                    requireView(),
                    "Error al guardar la clave: $error",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id_user"] = userId ?: "" // Pass the userId along with id_gps
                params["id_gps"] = clave
                return params
            }
        }

        queue.add(resultadoPost)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String): HomeFragment {
            return HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                }
            }
        }
    }
}
