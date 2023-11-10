package com.example.buscaminas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private var dificultadSeleccionada: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar =  findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.game_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.nuevoJuego -> {
                //nuevoJuego()
                true
            }
            R.id.instrucciones -> {
                instrucciones(this)
                true
            }
            R.id.configuracion -> {
                seleccionarDificultad()
                true
            }
            R.id.seleccionarPersonaje -> {
                //seleccionarPersonaje()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //funcion que muestra las instrucciones del juego al que se la pasa como contexto el activityMain
    private fun instrucciones(context: Context) {

        //alertDialog que saque un texto con las intrucciones de juego
        //y un boton de aceptar
        //y que se pueda cerrar con el boton de atras

        val builder = AlertDialog.Builder(context)
        val mensaje = R.string.textoInstrucciones

        //aqui se le pasa el mensaje a msotrar en el alertDialog
        builder.setMessage(mensaje)
        //cuadno se le da al boton de aceptar se cierra el alertDialog
        builder.setPositiveButton("Aceptar") { dialog, which ->
            dialog.dismiss()
        }
        //se crea el alertDialog con el builder que contiene el mensaje y el boton de aceptar
        val alertDialog = builder.create()
        alertDialog.show()

    }

    private fun seleccionarDificultad() {

        //builder de alertdialog para mostrar los niveles de dificultad con radiobuton
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione la dificultad")

        val radioGroup = RadioGroup(this)
        radioGroup.orientation = RadioGroup.VERTICAL

        val principiante = RadioButton(this)
        principiante.text = "Principiante"
        radioGroup.addView(principiante)
        principiante.isChecked = true

        val amateur = RadioButton(this)
        amateur.text = "Amateur"
        radioGroup.addView(amateur)

        val avanzado = RadioButton(this)
        avanzado.text = "Avanzado"
        radioGroup.addView(avanzado)
        //se muestra el rediogroup
        builder.setView(radioGroup)

        //cuando se le da al boton de aceptar se cierra el alertDialog dando valor a dificultadSeleccionada, que por defecto extaria inicializada como Principiante
        builder.setPositiveButton("Aceptar") { dialog, which ->

            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            if (checkedRadioButtonId != -1) {
                // Al menos un RadioButton está seleccionado
                val radioButton: RadioButton = radioGroup.findViewById(checkedRadioButtonId)
                dificultadSeleccionada = radioButton.text.toString()

                // Puedes realizar acciones específicas según la dificultad seleccionada aquí
                when (dificultadSeleccionada) {
                    "Principiante" -> {
                        // Lógica para el nivel Principiante
                    }
                    "Amateur" -> {
                        // Lógica para el nivel Amateur
                    }
                    "Avanzado" -> {
                        // Lógica para el nivel Avanzado
                    }
                }
            }
            dialog.dismiss()
        }

        builder.setOnCancelListener {
            // Código a ejecutar cuando se cancela el diálogo
            dificultadSeleccionada = null
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


}