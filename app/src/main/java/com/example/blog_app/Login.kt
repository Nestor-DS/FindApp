package com.example.blog_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import androidx.fragment.app.DialogFragment

class Login : AppCompatActivity() {

    // Views
    private lateinit var email: EditText
    private lateinit var password: EditText

    // Sensor
    private var canAuthenticate = false
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var auth: Boolean = false

    // ProgressDialog for showing loading spinner
    private lateinit var progressDialog: ProgressDialog

    // URL for the login API
    private var ipUrl: String? = "192.168.8.18"

    private val loginUrl = "http://$ipUrl/findapp/login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        email = findViewById(R.id.emailEditText)
        password = findViewById(R.id.passwordEditText)
        progressDialog = ProgressDialog(this)


        // Check if the user is already logged in
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val userId = sharedPreferences.getString("user_id", "")
            userId?.let {
                startMainActivity(it)
            }
        }
        // Setup
        setupAuth()
    }

    private fun setupAuth() {
        if (androidx.biometric.BiometricManager.from(this).canAuthenticate(
                androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
        ) {

            canAuthenticate = true

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setSubtitle("Autenticarse usando biometría")
                .setAllowedAuthenticators(
                    androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build()
        }
    }

    private fun authenticate(auth: (auth: Boolean) -> Unit) {
        if (canAuthenticate) {
            BiometricPrompt(
                this, ContextCompat.getMainExecutor(this),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        auth(true)
                    }
                }
            ).authenticate(promptInfo)
        } else {
            auth(true)
        }
    }

    fun login(view: View) {
        // Get input data
        val strEmail = email.text.toString().trim()
        val strPassword = password.text.toString().trim()

        // Validate the email and password fields
        if (strEmail.isEmpty()) {
            showToast("Enter Email")
            return
        }

        if (strPassword.isEmpty()) {
            showToast("Enter Password")
            return
        }

        // Show ProgressDialog while making the login request
        progressDialog.setMessage("Please wait...")
        progressDialog.show()

        // Create a POST request using Volley
        val request = object : StringRequest(
            Request.Method.POST, loginUrl,
            Response.Listener { response ->
                progressDialog.dismiss()
                if (response.contains("ingreso correctamente", ignoreCase = true)) {
                    password.text.clear()

                    val userId = response.split("ID:")[1].trim() // Extract the user ID from the response

                    // Save the login state and user ID in SharedPreferences
                    val sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("user_id", userId)
                    editor.apply()

                    if (!auth) {
                        authenticate { authenticated ->
                            auth = authenticated
                            if (auth) {
                                startMainActivity(userId)
                            } else {
                                showToast("Biometric authentication failed.")
                            }
                        }
                    } else {
                        startMainActivity(userId)
                    }

                } else {
                    showToast(response)
                }
            },
            Response.ErrorListener { error ->
                progressDialog.dismiss()
                showToast(error.message.toString())
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

    private fun startMainActivity(userId: String) {
        val intent = Intent(this@Login, MainActivity::class.java)
        intent.putExtra("user_id", userId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        showToast("User $userId logged in successfully")
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun clickBtnRegister(view: View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

    fun clickBtnOlvide(view: View) {
        val intent = Intent(this, RecoverPassword::class.java)
        startActivity(intent)
    }

}
