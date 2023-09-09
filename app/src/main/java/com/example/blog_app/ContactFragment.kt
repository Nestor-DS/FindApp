package com.example.blog_app

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class ContactFragment : Fragment() {
    private lateinit var nameText: TextView
    private lateinit var addressText: TextView
    private lateinit var emailText: TextView
    private lateinit var phoneText: TextView
    private var userId: String? = null
    private lateinit var progressDialog: ProgressDialog
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
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        nameText = view.findViewById(R.id.nameTxt)
        addressText = view.findViewById(R.id.addressTxt)
        emailText = view.findViewById(R.id.emailTxt)
        phoneText = view.findViewById(R.id.numTxt)
        progressDialog = ProgressDialog(requireContext())

        userId?.let {
            progressDialog.setMessage("Please wait...")
            progressDialog.show()
            val url = "http://$ipUrl/findapp/contact/viewContact.php?id_user=$it"

            val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    progressDialog.dismiss()

                    try {
                        val datosUsuario = JSONObject(response)
                        val name = datosUsuario.getString("name")
                        val address = datosUsuario.getString("address")
                        val email = datosUsuario.getString("email")
                        val phone = datosUsuario.getString("phone")

                        nameText.text = name
                        addressText.text = address
                        emailText.text = email
                        phoneText.text = phone

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
        } ?: run {
            Toast.makeText(requireContext(), "No se encontró el ID del usuario", Toast.LENGTH_SHORT).show()
        }

        val editContactButton: FloatingActionButton = view.findViewById(R.id.editContact)
        editContactButton.setOnClickListener {
            editContact()
        }

        return view
    }

    private fun editContact() {
        val newName = nameText.text.toString()
        val newAddress = addressText.text.toString()
        val newEmail = emailText.text.toString()
        val newPhone = phoneText.text.toString()


        // Verificar si todos los campos están vacíos
        if (newName.isEmpty() && newAddress.isEmpty() && newEmail.isEmpty() && newPhone.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese datos de contacto", Toast.LENGTH_SHORT).show()
            return
        }

        if (newName.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese un nombre de contacto", Toast.LENGTH_SHORT).show()
            return
        }

        if (newAddress.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese una dirección de contacto", Toast.LENGTH_SHORT).show()
            return
        }

        if (newEmail.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese un email de contacto", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPhone.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese un numero de contacto", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de datos

        if (!newName.matches(Regex("^[a-zA-Z ]+\$"))) {
            Toast.makeText(requireContext(), "El nombre no puede contener simbolos", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            Toast.makeText(requireContext(), "Correo electronico no valido", Toast.LENGTH_SHORT).show()
            return
        }
        if (newPhone.length != 10){
            Toast.makeText(requireContext(),"Telefono no valido", Toast.LENGTH_SHORT).show()
        }


        val updateUrl = "http://$ipUrl/findapp/contact/updateContact.php"

        val stringRequest = object : StringRequest(Request.Method.PUT, updateUrl,
            Response.Listener {
                // Procesar la respuesta del servidor (si es necesario)
                Toast.makeText(requireContext(), "Datos de contacto actualizados exitosamente.", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Error al actualizar los datos de contacto.", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id_user"] = userId ?: ""
                params["name"] = newName
                params["address"] = newAddress
                params["email"] = newEmail
                params["phone"] = newPhone
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(requireContext())
        stringRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            3,  // Intentar 3 veces antes de mostrar el error
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String): ContactFragment {
            return ContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                }
            }
        }
    }
}
