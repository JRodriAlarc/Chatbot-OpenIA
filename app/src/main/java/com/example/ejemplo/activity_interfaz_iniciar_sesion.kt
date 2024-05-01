package com.example.ejemplo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth

class activity_interfaz_iniciar_sesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interfaz_iniciar_sesion)

        // Obtener una instancia de FirebaseAuth
        val auth = FirebaseAuth.getInstance()

        //Crear una Vairable Global del Sistema
        val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
        val valorString = sharedPreferences.contains("logueado")


        if (valorString){
            val intent = Intent(this, activity_interfaz_mensajes::class.java)
            startActivity(intent)
            finish()
        }


        val nameUser = findViewById<TextView>(R.id.editTextNameUser)
        val emailUser = findViewById<TextView>(R.id.editTextCorreo)
        val passUser = findViewById<TextView>(R.id.editTextPassword)
        val confPass = findViewById<TextView>(R.id.editTextConfPassword)
        val mesageUser = findViewById<TextView>(R.id.tv_mensaje)

        val btnIniSesion = findViewById<Button>(R.id.buttonInicSes)
        val btnCrearUser = findViewById<Button>(R.id.buttonCrearUser)

        nameUser.visibility = View.GONE
        confPass.visibility = View.GONE

        btnCrearUser.setOnClickListener(){
            if (btnCrearUser.text == "CREAR CUENTA"){
                btnCrearUser.text = "INICIAR SESIÓN"
                btnIniSesion.text = "CREAR CUENTA"
                nameUser.visibility = View.VISIBLE
                confPass.visibility = View.VISIBLE
                mesageUser.text ="Si ya tienes una cuenta, solo inicia sesión"
            } else {
                btnCrearUser.text = "CREAR CUENTA"
                btnIniSesion.text = "INICIAR SESIÓN"
                nameUser.visibility = View.GONE
                confPass.visibility = View.GONE
                mesageUser.text ="Si aún no te registras, crea una cuenta."
            }
        }

        btnIniSesion.setOnClickListener(){

            // Obtener los datos del usuario
            val textEmailUser = emailUser.text.toString()
            val textPasswUser = passUser.text.toString()

            if (btnIniSesion.text == "CREAR CUENTA"){

                val textNameUser = nameUser.text
                val confirmPassw = confPass.text

                if (textNameUser.length > 0 && textEmailUser.length > 0 && textPasswUser.length > 0 && confirmPassw.length > 0) {
                    if (textPasswUser.length == confirmPassw.length) {
                        auth.createUserWithEmailAndPassword(textEmailUser, textPasswUser)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // El usuario se creó exitosamente
                                    val user = auth.currentUser
                                    val editor = sharedPreferences.edit()
                                    editor.putString("logeado", "sí")
                                    editor.apply()
                                    editor.putString("uid", user?.uid)
                                    editor.apply()
                                    val intent = Intent(this, activity_interfaz_mensajes::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // Ocurrió un error durante la creación del usuario
                                    Toast.makeText(
                                        this,
                                        "Error al Crear el Usuario",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Las Contraseñas no coinciden", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else {
                        Toast.makeText(this, "Usuario o Contraseña Invalidos", Toast.LENGTH_SHORT).show()
                    }

                } else {
                if (textEmailUser.length > 0 && textPasswUser.length > 0){
                 auth.signInWithEmailAndPassword(textEmailUser, textPasswUser).addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         // El usuario se creó exitosamente
                         val user = auth.currentUser
                         val editor = sharedPreferences.edit()
                         editor.putString("logeado", "sí")
                         editor.apply()
                         editor.putString("uid", user?.uid)
                         editor.apply()
                         val intent = Intent(this, activity_interfaz_mensajes::class.java)
                         startActivity(intent)
                         finish()
                     } else {
                         // Ocurrió un error durante la creación del usuario
                         val type_error = task.exception.toString()
                         var text_error = ""

                         if  ( type_error == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted."){
                             text_error = "La dirección de correo electrónico está mal formateada."
                         } else if ( type_error == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password."){
                             text_error = "La contraseña no es válida o el usuario no tiene contraseña."
                         } else {
                             text_error = "Ocurrió un error al iniciar sesión"
                         }

                         Toast.makeText(this, text_error, Toast.LENGTH_SHORT).show()
                     }
                 }
             }

            }


        }

    }
}