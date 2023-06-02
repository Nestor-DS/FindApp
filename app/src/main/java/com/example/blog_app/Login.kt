package com.example.blog_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            val signUpButton: AppCompatButton = findViewById(R.id.BtnLogin)

            // Establece la posición inicial del botón fuera de la pantalla
            signUpButton.translationX = -signUpButton.width.toFloat()

            // Crea y configura la animación
            signUpButton.animate()
                .translationX(0f)
                .setDuration(1000)
                .withEndAction {
                    // Realiza la acción deseada después de la animación, por ejemplo, iniciar otra actividad
                }
                .start() // Inicia la animación
            }

        }

    fun clickBtnRegister (view: View) {
        val intent = Intent (this, Register::class.java)
        startActivity(intent)
    }


}