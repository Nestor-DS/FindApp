package com.example.blog_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import java.util.*

class Register : AppCompatActivity() {

    // Declare EditText fields
    private lateinit var txtNombre: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtPass: EditText
    private lateinit var txtConfirmPass: EditText

    // Define the IP address
    private var ipUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the EditText views
        initializeViews()
    }

    private fun initializeViews() {
        // Initialize EditText views using their IDs
        txtNombre = findViewById(R.id.nameET)
        txtEmail = findViewById(R.id.emailET)
        txtTelefono = findViewById(R.id.telefonoET)
        txtPass = findViewById(R.id.passwordET)
        txtConfirmPass = findViewById(R.id.conPasswordET)
    }

    fun clickBtnInsertar(view: View) {
        // Get user input from EditText fields
        val nombre = txtNombre.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val telefono = txtTelefono.text.toString().trim()
        val password = txtPass.text.toString().trim()
        val confirmarPassword = txtConfirmPass.text.toString().trim()

        // Validate user input
        if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
            Snackbar.make(view, "Please fill out all fields", Snackbar.LENGTH_LONG).show()
            return
        }

        if (!nombre.matches(Regex("^[a-zA-Z ]+\$"))) {
            Snackbar.make(view, "Name can only contain letters and spaces", Snackbar.LENGTH_LONG).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(view, "Invalid email address", Snackbar.LENGTH_LONG).show()
            return
        }

        if (telefono.length != 10) {
            Snackbar.make(view, "Phone number must have 10 digits", Snackbar.LENGTH_LONG).show()
            return
        }

        if (password.length < 8 || !password.matches(Regex(".*\\d.*")) || !password.matches(Regex(".*[A-Z].*"))) {
            Snackbar.make(view, "Password must be at least 8 characters long, contain a capital letter, and a number", Snackbar.LENGTH_LONG).show()
            return
        }

        if (password != confirmarPassword) {
            Snackbar.make(view, "Passwords do not match", Snackbar.LENGTH_LONG).show()
            return
        }

        // Define the URL for the HTTP request
        val url = "http://$ipUrl/findapp/insertUser.php"

        // Create a Volley request queue
        val queue = Volley.newRequestQueue(this)

        // Create a POST request to send user data to the server
        val resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                // If registration is successful, navigate to another activity
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Snackbar.make(view, "Congratulations, user added successfully", Snackbar.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->  Snackbar.make(view, "Error $error", Snackbar.LENGTH_LONG).show() }) {

            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "name" to txtNombre.text.toString(),
                    "email" to txtEmail.text.toString(),
                    "phone" to txtTelefono.text.toString(),
                    "password" to txtPass.text.toString()
                )
            }
        }
        queue.add(resultadoPost)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            val signUpButton: AppCompatButton = findViewById(R.id.singUpBtn)

            // Set the initial position of the button off the screen
            signUpButton.translationX = signUpButton.width.toFloat()

            // Create and configure the animation
            signUpButton.animate()
                .translationX(0f)
                .setDuration(1000)
                .withEndAction {
                    // Perform desired action after the animation, e.g., start another activity
                }
                .start() // Start the animation
        }
    }
}
