package com.example.blog_app

import androidx.appcompat.app.AppCompatActivity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Login: AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var strEmail: String
    private lateinit var strPassword: String
    private val url = "http://192.168.74.1/findapp/login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.emailEditText)
        password = findViewById(R.id.passwordEditText)
    }

    fun login(view: View) {
        if (email.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
        } else if (password.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
        } else {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Por favor espera...")
            progressDialog.show()

            strEmail = email.text.toString().trim()
            strPassword = password.text.toString().trim()

            val request = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    progressDialog.dismiss()
                    if (response.equals("Ingreso correctamente", ignoreCase = true)) {
                        email.setText("")
                        password.setText("")
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        Toast.makeText(this@Login, response, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Login, response, Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    progressDialog.dismiss()
                    Toast.makeText(this@Login, error.message.toString(), Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = strEmail
                    params["password"] = strPassword
                    return params
                }
            }

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(request)
        }
    }

    fun clickBtnRegister (view: View) {
        val intent = Intent (this, Register::class.java)
        startActivity(intent)
    }
}
