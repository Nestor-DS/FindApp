package com.example.blog_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class PersonalFragment : Fragment() {

    private lateinit var txtname : TextView
    private lateinit var txtemail: TextView
    private lateinit var txtphone: TextView
    private lateinit var txtpassword: TextView
    private lateinit var txtconfipassword: TextView

    private  var userId: String? = null

    private var ipUrl: String? = "192.168.8.18"


    private lateinit var progressDialog: ProgressDialog

    // Sensor
    private var canAuthenticate = false
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var auth: Boolean = false

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
        val view = inflater.inflate(R.layout.fragment_personal, container, false)
        view.findViewById<View>(R.id.btnActualizar).setOnClickListener { btnActualizar(view) }

        txtname = view.findViewById(R.id.txtname)
        txtemail = view.findViewById(R.id.txtemail)
        txtphone = view.findViewById(R.id.txtphone)
        txtpassword = view.findViewById(R.id.txtpassword)
        txtconfipassword = view.findViewById(R.id.txtconfipassword)
        progressDialog = ProgressDialog(requireContext())

        userId?.let{
            progressDialog.setMessage("Please wait...")
            progressDialog.show()
            val url = "http://$ipUrl/findapp/viewUser.php?user_id=$it"

            val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    progressDialog.dismiss()

                    try {
                        val datosUsuario = JSONObject(response)
                        val name = datosUsuario.getString("name")
                        val email = datosUsuario.getString("email")
                        val phone = datosUsuario.getString("phone")
                        val pass=datosUsuario.getString("password")

                        txtname.text= name
                        txtemail.text= email
                        txtphone.text= phone
                        txtpassword.text=pass


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Error al procesar los datos de contacto $userId", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    progressDialog.dismiss()
                    error.printStackTrace()
                    Toast.makeText(requireContext(), "Error obteniendo los datos de contacto $userId", Toast.LENGTH_SHORT).show()
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

    private fun btnActualizar(view: View) {
        val url = "http://$ipUrl/findapp/updateUser.php"

        val parametros = HashMap<String, String>()
        parametros["name"] = txtname.text.toString()
        parametros["email"] = txtemail.text.toString()
        parametros["phone"] = txtphone.text.toString()
        parametros["password"] = txtpassword.text.toString()
        userId?.let { parametros["userId"] = it }

        val request = object : StringRequest(Method.PUT, url,
            Response.Listener {response ->
                Snackbar.make(view, "Felicidades, información actualizada exitosamente", Snackbar.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Snackbar.make(view, "Error $error", Snackbar.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                return parametros
            }
        }

        val queue = Volley.newRequestQueue(requireContext())
        queue.add(request)
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String?): PersonalFragment {
            val fragment = PersonalFragment()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }
}
