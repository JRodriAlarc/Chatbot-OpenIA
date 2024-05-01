package com.example.ejemplo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class activity_interfaz_mensajes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interfaz_mensajes)

        //Obtener una instancia de FirebaseRealtimeDataBase
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()

        //Obtener una instancia de FirebaseAuth
        val auth = FirebaseAuth.getInstance()

        //Crear una Vairable Global del Sistema
        val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
        val myuid:String = sharedPreferences.getString("uid", "").toString()
        val valMens = sharedPreferences.contains("mensajes")

        //Guardar mensajes del chat
        var messageList = mutableListOf<String>()

        if (valMens){
            val textMens:String = sharedPreferences.getString("mensajes", "").toString()
            messageList = textMens.split(";").toMutableList()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.viewMensajes)
        val btSend = findViewById<Button>(R.id.btSend)
        val etMensaje = findViewById<TextView>(R.id.etMensaje)

        recyclerView.layoutManager = LinearLayoutManager(this)

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

        class MessageAdapter(private val messages: MutableList<String>, val recyclerView: RecyclerView) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_interfaz_style_mensajes, parent, false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val message = messages[position]

                val MexTime:TimeZone = TimeZone.getTimeZone("America/Mexico_City")
                val formatter = SimpleDateFormat("HH:mm")
                formatter.timeZone = MexTime

                val now = Date()
                val fecha:String = formatter.format(now)

                holder.bindMessage(message, position)

                // Add text view for the message time
                val textHora = holder.itemView.findViewById<TextView>(R.id.textView3)
                val textHoraResp = holder.itemView.findViewById<TextView>(R.id.textView5)
                textHora.text = fecha
                textHoraResp.text = fecha

            }

            override fun getItemCount(): Int {
                return messages.size
            }


            inner class ViewHolder(View: View) : RecyclerView.ViewHolder(View) {
                private val llContent: LinearLayout = itemView.findViewById(R.id.misMensajes)
                private val textMens: TextView = itemView.findViewById(R.id.textView2)
                private val textHora: TextView = itemView.findViewById(R.id.textView3)
                private val llContentResp: LinearLayout = itemView.findViewById(R.id.lasRespuestas)
                private val textResp: TextView = itemView.findViewById(R.id.textView4)
                private val textHoraResp: TextView = itemView.findViewById(R.id.textView5)

                fun bindMessage(message: String, position: Int) {

                    if (message.substring(0,3)== "yo:"){
                        llContent.visibility = View.VISIBLE
                        llContentResp.visibility = View.GONE
                        textMens.text = message.substring(3,message.length)
                    } else {
                        llContent.visibility = View.GONE
                        llContentResp.visibility = View.VISIBLE
                        textResp.text = message.substring(3,message.length)
                    }
                }
            }
        }

        var adapter1 = MessageAdapter(messageList, recyclerView)
        recyclerView.adapter = adapter1

        adapter1.notifyDataSetChanged()
        recyclerView.scrollToPosition(messageList.size - 1)

        val leer = database.reference.child("ids_respuestas/"+myuid)
        val vEvLi = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mens_br = dataSnapshot.getValue().toString()

                if (!mens_br.contains("null")) {
                    val myRefResp: DatabaseReference = database.reference
                        .child("respuestas")
                        .child(mens_br.substring(5, mens_br.length - 1))
                        .child(myuid)

                    myRefResp.get().addOnSuccessListener { document ->
                        if (document != null) {
                            val my_resp = "tu:"+document.value.toString()
                            messageList.add(my_resp)

                            val mensAnter: String = sharedPreferences.getString("mensajes", "").toString()

                            val editor = sharedPreferences.edit()
                            if (mensAnter.length>0){
                                editor.putString("mensajes", mensAnter + ";" + my_resp)
                            } else {
                                editor.putString("mensajes", my_resp)
                            }
                            editor.apply()

                            adapter1 = MessageAdapter(messageList, recyclerView)
                            recyclerView.adapter = adapter1

                            adapter1.notifyDataSetChanged()
                            recyclerView.scrollToPosition(messageList.size - 1)

                            myRefResp.removeValue()                       //Eliminar el nodo
                                .addOnSuccessListener {
                                    // El nodo se eliminó exitosamente
                                    println("Nodo eliminado correctamente")
                                }
                                .addOnFailureListener { error ->
                                    // Ocurrió un error al intentar eliminar el nodo
                                    println("Error al eliminar el nodo: ${error.message}")
                                }
                        }
                    }.addOnFailureListener { exception ->
                        println("Error obteniendo los datos: $exception")
                    }
                }

                leer.removeValue()                       //Eliminar el nodo
                    .addOnSuccessListener {
                        // El nodo se eliminó exitosamente
                        println("Nodo eliminado correctamente")
                    }
                    .addOnFailureListener { error ->
                        // Ocurrió un error al intentar eliminar el nodo
                        println("Error al eliminar el nodo: ${error.message}")
                    }


                //leer.removeEventListener(this)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Este método se ejecuta si ocurre un error durante la lectura de datos
                println("Error al leer los datos: ${databaseError.message}")
            }
        }

        leer.addValueEventListener(vEvLi)

        btSend.setOnClickListener{
            val mensEnviar = etMensaje.text.toString()
            val my_mens = "yo:"+etMensaje.text.toString()

            messageList.add(my_mens)


            val mensAnter: String = sharedPreferences.getString("mensajes", "").toString()

            val editor = sharedPreferences.edit()
            if (mensAnter.length>0){
                editor.putString("mensajes", mensAnter + ";" + my_mens)
            } else {
                editor.putString("mensajes", my_mens)
            }
            editor.apply()

            val data: Map<String, Any> = mapOf(
                myuid to mensEnviar
            )
            val key_ale: String = database.reference.push().key.toString()
            val myRef: DatabaseReference = database.reference.child("preguntas").child(key_ale)
            myRef.setValue(data)
            etMensaje.text = ""
            adapter1 = MessageAdapter(messageList, recyclerView)
            recyclerView.adapter = adapter1

            adapter1.notifyDataSetChanged()
            recyclerView.scrollToPosition(messageList.size - 1)
        }

    }
}