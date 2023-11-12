package com.example.buscaminas

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.R.color.material_blue_grey_800
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var dificultadSeleccionada: String? = null
    private lateinit var tablero: Array<Array<Char>>
    companion object {
        const val MINAS_PRINCIPIANTE = 10
        const val MINAS_AMATEUR = 30
        const val MINAS_EXPERTO = 40
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar =  findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tablero = generarTablero("principiante")
        mostrarTableroEnPantalla(tablero)





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.game_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.instrucciones -> {
                instrucciones(this)
                true
            }

            R.id.nuevoJuego -> {
                //nuevoJuego()
                true
            }
            R.id.configuracion -> {
                seleccionarDificultad()
                true
            }
            R.id.seleccionarPersonaje -> {
                seleccionarPersonaje()
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

        //cuando se le da al boton de aceptar se cierra el alertDialog dando valor a dificultadSeleccionada, que por defecto eataria inicializada como Principiante
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


    // funcion que muestra un alertDialog con los personajes disponibles para seleccionar y que devuelve el personaje seleccionado, las opciones sera personaje 1, personje2 y personaje3 en un spinner
    private fun seleccionarPersonaje() {

        //builder de alertdialog para mostrar los personajes con spinner
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione el personaje")

        //se crea el spinner con los personajes
        val spinner = Spinner(this)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOf("Personaje 1", "Personaje 2", "Personaje 3"))

        //se muestra el spinner
        builder.setView(spinner)

        //cuando se le da al boton de aceptar se cierra el alertDialog dando valor a personajeSeleccionado, que por defecto eataria inicializada como Personaje 1
        builder.setPositiveButton("Aceptar") { dialog, which ->

            var personajeSeleccionado = spinner.selectedItem.toString()

            // Puedes realizar acciones específicas según el personaje seleccionado aquí
            when (personajeSeleccionado) {
                "Personaje 1" -> {
                    // Lógica para el personaje 1
                }
                "Personaje 2" -> {
                    // Lógica para el personaje 2
                }
                "Personaje 3" -> {
                    // Lógica para el personaje 3
                }
            }
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun generarTablero(nivelDificultad: String): Array<Array<Char>> {
        val (filas, columnas, minas) = when (nivelDificultad) {
            "principiante" -> Triple(8, 8, Companion.MINAS_PRINCIPIANTE)
            "amateur" -> Triple(1, 12, MINAS_AMATEUR)
            "experto" -> Triple(16, 16, MINAS_EXPERTO)
            else -> throw IllegalArgumentException("Nivel de dificultad no válido")
        }

        val tablero = Array(filas) { Array(columnas) { ' ' } }
        colocarMinas(tablero, minas)
        calcularNumeros(tablero)

        return tablero
    }

    private fun colocarMinas(tablero: Array<Array<Char>>, cantidadMinas: Int) {
        val random = Random.Default

        var minasRestantes = cantidadMinas

        while (minasRestantes > 0) {
            val fila = random.nextInt(tablero.size)
            val columna = random.nextInt(tablero[0].size)

            if (tablero[fila][columna] != '*') {
                tablero[fila][columna] = '*'
                minasRestantes--
            }
        }
    }

    private fun calcularNumeros(tablero: Array<Array<Char>>) {
        for (i in tablero.indices) {
            for (j in tablero[0].indices) {
                if (tablero[i][j] != '*') {
                    val minasAlrededor = contarMinasAlrededor(tablero, i, j)
                    tablero[i][j] = Character.forDigit(minasAlrededor, 10)
                }
            }
        }
    }

    private fun contarMinasAlrededor(tablero: Array<Array<Char>>, fila: Int, columna: Int): Int {
        var minas = 0

        for (i in fila - 1..fila + 1) {
            for (j in columna - 1..columna + 1) {
                if (i in 0 until tablero.size && j in 0 until tablero[0].size && tablero[i][j] == '*') {
                    minas++
                }
            }
        }

        return minas
    }

    private fun mostrarTableroEnPantalla(tablero: Array<Array<Char>>) {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.removeAllViews()  // Eliminar vistas existentes antes de agregar nuevas

        val columnCount = tablero[0].size

        // Configurar el GridLayout
        gridLayout.columnCount = columnCount
        gridLayout.rowCount = tablero.size
        gridLayout.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        for (i in tablero.indices) {
            for (j in tablero[0].indices) {
                val button = Button(this)
                button.text = "" // No mostrar el valor inicialmente

                // Establecer el fondo con borde gris y fondo gris más oscuro
                //val drawable = ContextCompat.getDrawable(this, R.drawable.botones_personalizados)
                //button.background = drawable

                // Configurar el botón para que ocupe el espacio disponible en cada celda
                val params = GridLayout.LayoutParams()
                params.rowSpec = GridLayout.spec(i)
                params.columnSpec = GridLayout.spec(j)
                button.layoutParams = params

                // Asignar un OnClickListener a cada botón
                button.setOnClickListener {
                    // Lógica a realizar al hacer clic en el botón
                    realizarAccion(i, j)
                }

                gridLayout.addView(button)
            }
        }
    }

    private fun realizarAccion(fila: Int, columna: Int) {
        val valorCelda = obtenerValorCeldaEnPosicion(fila, columna)

        // Implementar la lógica específica según el valor de la celda
        when (valorCelda) {
            ' ' -> {
                // Lógica cuando el valor es un espacio vacío
            }
            '*' -> {
                // Lógica cuando el valor es una mina
            }
            // Puedes agregar más casos según tus necesidades
            else -> {
                // Lógica por defecto o para otros valores específicos
            }
        }
    }

    private fun obtenerValorCeldaEnPosicion(fila: Int, columna: Int): Char {
        return tablero[fila][columna]
    }


}




