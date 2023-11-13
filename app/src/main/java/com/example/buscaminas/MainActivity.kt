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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.gridlayout.widget.GridLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var dificultadSeleccionada: String? = null

    companion object {
        const val MINAS_PRINCIPIANTE = 10
        const val MINAS_AMATEUR = 30
        const val MINAS_AVANZADO = 60
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tablero = generarTablero("@string/principiante")
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
                nuevoJuego(dificultadSeleccionada ?: "@string/principiante")
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

    private fun nuevoJuego(dificultad: String) {
        val tablero = generarTablero(dificultad)
        mostrarTableroEnPantalla(tablero)
    }

    private fun instrucciones(context: Context) {

        //alertDialog que saque un texto con las intrucciones de juego
        //y un boton de @string/aceptar
        //y que se pueda cerrar con el boton de atras

        val builder = AlertDialog.Builder(context)
        val mensaje = R.string.textoInstrucciones

        //aqui se le pasa el mensaje a msotrar en el alertDialog
        builder.setMessage(mensaje)
        //cuadno se le da al boton de @string/aceptar se cierra el alertDialog
        builder.setPositiveButton("@string/aceptar") { dialog, which ->
            dialog.dismiss()
        }
        //se crea el alertDialog con el builder que contiene el mensaje y el boton de @string/aceptar
        val alertDialog = builder.create()
        alertDialog.show()

    }

    private fun seleccionarDificultad() {

        //builder de alertdialog para mostrar los niveles de dificultad con radiobuton
        val builder = AlertDialog.Builder(this)
        builder.setTitle("@string/seleccionaDificultad")

        val radioGroup = RadioGroup(this)
        radioGroup.orientation = RadioGroup.VERTICAL

        val principiante = RadioButton(this)
        principiante.text = "@string/principiante"
        radioGroup.addView(principiante)
        principiante.isChecked = true

        val amateur = RadioButton(this)
        amateur.text = "@string/amateur"
        radioGroup.addView(amateur)

        val avanzado = RadioButton(this)
        avanzado.text = "@string/avanzado"
        radioGroup.addView(avanzado)
        //se muestra el rediogroup
        builder.setView(radioGroup)

        //cuando se le da al boton de aceptar se cierra el alertDialog dando valor a dificultadSeleccionada, que por defecto eataria inicializada como Principiante
        builder.setPositiveButton("@string/aceptar") { dialog, which ->

            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            if (checkedRadioButtonId != -1) {
                // Al menos un RadioButton está seleccionado
                val radioButton: RadioButton = radioGroup.findViewById(checkedRadioButtonId)
                dificultadSeleccionada = radioButton.text.toString()

                // aqui se llama a la funcion nuevoJuego con la dificultad seleccionada
                when (dificultadSeleccionada) {
                    "@string/principiante" -> {
                        // Lógica para el nivel Principiante
                        nuevoJuego(dificultadSeleccionada ?: "principiante")

                    }

                    "@string/amateur" -> {
                        // Lógica para el nivel Amateur
                        nuevoJuego(dificultadSeleccionada ?: "amateur")
                    }

                    "@string/avanzado" -> {
                        // Lógica para el nivel Avanzado
                        nuevoJuego(dificultadSeleccionada ?: "avanzado")
                    }
                }
            }
            dialog.dismiss()
        }

        builder.setOnCancelListener {
            // Código a ejecutar cuando se cancela
            dificultadSeleccionada = null
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    // funcion que muestra un alertDialog con los personajes disponibles para seleccionar y que devuelve el personaje seleccionado, las opciones sera personaje 1, personje2 y personaje3 en un spinner
    private fun seleccionarPersonaje() {

        //builder de alertdialog para mostrar los personajes con spinner
        val builder = AlertDialog.Builder(this)
        builder.setTitle("@string/seleccionaPersonaje")

        //se crea el spinner con los personajes
        val spinner = Spinner(this)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            arrayOf("Personaje 1", "Personaje 2", "Personaje 3")
        )

        //se muestra el spinner con lso distintos personajes
        builder.setView(spinner)

        //cuando se le da al boton de aceptar se cierra el alertDialog dando valor a personajeSeleccionado, que por defecto eataria inicializada como Personaje 1
        builder.setPositiveButton("@string/aceptar") { dialog, which ->

            var personajeSeleccionado = spinner.selectedItem.toString()

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

    fun generarTablero(nivelDificultad: String): Array<Array<Int>> {

        // Genera un tablero de acuerdo al nivel de dificultad seleccionado
        val (filas, columnas, minas) = when (nivelDificultad.lowercase()) {
            "@string/principiante" -> Triple(8, 8, Companion.MINAS_PRINCIPIANTE)
            "@string/amateur" -> Triple(12, 12, MINAS_AMATEUR)
            "@string/avanzado" -> Triple(16, 16, MINAS_AVANZADO)
            //si por alguna razon no hay una dificultad seleccionada de las 3
            //se lanza esta exception que se vera en el Logcat
            else -> throw IllegalArgumentException("Nivel de dificultad no válido")
        }
        // Crea un tablero de acuerdo a las dimensiones y cantidad de minas
        //al crear el tablero todas las celdas tienen como valor 0
        //y este valor se sustituira en celdas aleatorias por -1, que seran las minas.
        val tablero = Array(filas) { Array(columnas) { 0 } }
        colocarMinas(tablero, minas)
        calcularNumeros(tablero)

        return tablero
    }

    private fun colocarMinas(tablero: Array<Array<Int>>, cantidadMinas: Int) {
        val random = Random.Default

        var minasRestantes = cantidadMinas
        //se genera un numero aleatorio entre 0 y el numero de filas y otro entre 0 y el numero de columnas
        //para colocar las minas aleatoriamente en el mapa, y se realizara este bucle hasta que
        //se hayan colocado todas las minas como las pasadas como parametro
        while (minasRestantes > 0) {
            val fila = random.nextInt(tablero.size)
            val columna = random.nextInt(tablero[0].size)

            if (tablero[fila][columna] != -1) {
                tablero[fila][columna] = -1
                minasRestantes--
            }
        }
    }

    private fun calcularNumeros(tablero: Array<Array<Int>>) {
        for (i in tablero.indices) {
            for (j in tablero[0].indices) {
                if (tablero[i][j] != -1) {
                    val minasAlrededor = contarMinasAlrededor(tablero, i, j)
                    tablero[i][j] = minasAlrededor
                }
            }
        }
    }

    private fun contarMinasAlrededor(tablero: Array<Array<Int>>, fila: Int, columna: Int): Int {
        var minas = 0

        for (i in fila - 1..fila + 1) {
            for (j in columna - 1..columna + 1) {
                if (i in 0 until tablero.size && j in 0 until tablero[0].size && tablero[i][j] == -1) {
                    minas++
                }
            }
        }

        return minas
    }

    private fun mostrarTableroEnPantalla(tablero: Array<Array<Int>>) {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.removeAllViews()  // Elimina vistas existentes antes de agregar nuevas

        val columnCount = tablero[0].size

        // Configura el GridLayout
        gridLayout.columnCount = columnCount
        gridLayout.rowCount = tablero.size

        for (i in tablero.indices) {
            for (j in tablero[0].indices) {
                val button = Button(this)

                // Configura el texto del botón con el valor del tablero en esa posición
                button.text = tablero[i][j].toString()

                button.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }

                // Asigna un OnClickListener a cada botón
                button.setOnClickListener {
                    // Lógica al ahcer clic en el boton
                    realizarAccion(i, j, button,tablero)
                }

                gridLayout.addView(button)
            }
        }
    }



    private fun realizarAccion(fila: Int, columna: Int, button: Button,tablero: Array<Array<Int>>) {
        val valorCelda = button.text.toString().toInt()

        // Implementa la logica segun el valor de la celda
        if (valorCelda == -1) {
            button.setBackgroundColor(Color.RED)
            mostrarMensaje("@string/partidaPerdida")

        }else if(valorCelda == 0){
            cambiarColorBoton(fila, columna, Color.GREEN)
            revelarBotonesAdyacentes(tablero, fila, columna)
        }else{
            cambiarColorBoton(fila, columna, Color.GREEN)
        }
    }

    private fun obtenerValorCeldaEnPosicion(fila: Int, columna: Int): Any {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val button = gridLayout.getChildAt(fila * gridLayout.columnCount + columna) as Button
        return button.text
    }
    private fun revelarBotonesAdyacentes(tablero: Array<Array<Int>>, fila: Int, columna: Int) {
        // Verifica si el valor en la posición actual es 0
        if (tablero[fila][columna] == 0) {
            // Crea una lista para almacenar las posiciones de los botones adyacentes
            val posicionesARevelar = mutableListOf<Pair<Int, Int>>()

            // Llams a una función recursiva para explorar las posiciones adyacentes
            revelarBotonesAdyacentesRecursivo(tablero, fila, columna, posicionesARevelar)

            // Itera sobre las posiciones y cambiar el color de los botones
            for ((fila, columna) in posicionesARevelar) {
                cambiarColorBoton(fila, columna, Color.GREEN)
            }
        }
    }

    private fun revelarBotonesAdyacentesRecursivo(tablero: Array<Array<Int>>, fila: Int, columna: Int, posicionesARevelar: MutableList<Pair<Int, Int>>) {
        // Verifica si la posición está dentro de los límites del tablero
        if (fila in 0 until tablero.size && columna in 0 until tablero[0].size) {
            // Verifica si la posición ya fue revelada
            if (Pair(fila, columna) !in posicionesARevelar) {
                // Agrega la posición a la lista de posiciones a revelar
                posicionesARevelar.add(Pair(fila, columna))

                // Verifica si la posición tiene un valor de 0 (sin bomba)
                if (tablero[fila][columna] == 0) {
                    // Llama recursivamente a las posiciones adyacentes
                    for (i in -1..1) {
                        for (j in -1..1) {
                            revelarBotonesAdyacentesRecursivo(tablero, fila + i, columna + j, posicionesARevelar)
                        }
                    }
                }
            }
        }
    }

    private fun cambiarColorBoton(fila: Int, columna: Int, color: Int) {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val button = gridLayout.getChildAt(fila * gridLayout.columnCount + columna) as Button
        button.setBackgroundColor(color)
    }

    private fun mostrarMensaje(s: String) {
        val builder = AlertDialog.Builder(this)
        val mensaje = R.string.partidaPerdida

        //aqui se le pasa el mensaje a msotrar en el alertDialog
        builder.setMessage(mensaje)
        //cuadno se le da al boton de @string/aceptar se cierra el alertDialog
        builder.setPositiveButton("@string/aceptar") { dialog, which ->
            dialog.dismiss()
        }
        //se crea el alertDialog con el builder que contiene el mensaje y el boton de @string/aceptar
        val alertDialog = builder.create()
        alertDialog.show()
    }


}





