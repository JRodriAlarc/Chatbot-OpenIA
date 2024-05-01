package com.example.ejemplo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class segundaInterfaz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interfaz_mensajes)

        // Obtener una instancia de FirebaseAuth
        val auth = FirebaseAuth.getInstance()

        //Crear una Vairable Global del Sistema
        val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
        val valorString = sharedPreferences.contains("logueado")

        val btnCloseSesion = findViewById<Button>(R.id.buttonCloseSesion)

        btnCloseSesion.setOnClickListener(){
            auth.signOut()

            // Eliminar los datos de inicio de sesión de las SharedPreferences
            val editor = sharedPreferences.edit()
            editor.remove("logueado")
            editor.remove("uid")
            editor.apply()

            // Redirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this, activity_interfaz_iniciar_sesion::class.java)
            startActivity(intent)
            finish()
        }
    }
}