package com.example.buscaminas

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.gridlayout.widget.GridLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var dificultadSeleccionada: String? = null
    var banderasColocadas: Int = 0
    var indicePersonajeSeleccionado: Int = 0

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
        val tablero = generarTablero(getString(R.string.principiante))
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
                instrucciones()
                true
            }

            R.id.nuevoJuego -> {
                nuevoJuego(dificultadSeleccionada ?: getString(R.string.principiante))
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
        banderasColocadas = 0
        mostrarTableroEnPantalla(tablero)
    }

    private fun instrucciones() {

        //alertDialog que saque un texto con las intrucciones de juego
        //y un boton de @string/aceptar
        //y que se pueda cerrar con el boton de atras

        val builder = AlertDialog.Builder(this)
        val mensaje = R.string.textoInstrucciones

        //aqui se le pasa el mensaje a msotrar en el alertDialog
        builder.setMessage(mensaje)
        //cuadno se le da al boton de @string/aceptar se cierra el alertDialog
        builder.setPositiveButton(R.string.aceptar) { dialog, which ->
            dialog.dismiss()
        }
        //se crea el alertDialog con el builder que contiene el mensaje y el boton de @string/aceptar
        val alertDialog = builder.create()
        alertDialog.show()

    }

    private fun seleccionarDificultad() {

        //builder de alertdialog para mostrar los niveles de dificultad con radiobuton
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.tituloDificultad)

        val radioGroup = RadioGroup(this)
        radioGroup.orientation = RadioGroup.VERTICAL

        val principiante = RadioButton(this)
        principiante.text = getString(R.string.principiante)
        radioGroup.addView(principiante)
        principiante.isChecked = true

        val amateur = RadioButton(this)
        amateur.text = getString(R.string.amateur)
        radioGroup.addView(amateur)

        val avanzado = RadioButton(this)
        avanzado.text = getString(R.string.avanzado)
        radioGroup.addView(avanzado)
        //se muestra el rediogroup
        builder.setView(radioGroup)

        //cuando se le da al boton de aceptar se cierra el alertDialog dando valor a dificultadSeleccionada, que por defecto eataria inicializada como Principiante
        builder.setPositiveButton(R.string.aceptar) { dialog, which ->

            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            if (checkedRadioButtonId != -1) {
                // Al menos un RadioButton está seleccionado
                val radioButton: RadioButton = radioGroup.findViewById(checkedRadioButtonId)
                dificultadSeleccionada = radioButton.text.toString()

                // aqui se llama a la funcion nuevoJuego con la dificultad seleccionada
                // aquí se llama a la función nuevoJuego con la dificultad seleccionada
                when (dificultadSeleccionada) {
                    getString(R.string.principiante) -> {
                        // Lógica para el nivel Principiante
                        nuevoJuego(dificultadSeleccionada ?: getString(R.string.principiante))
                    }

                    getString(R.string.amateur) -> {
                        // Lógica para el nivel Amateur
                        nuevoJuego(dificultadSeleccionada ?: getString(R.string.amateur))
                    }

                    getString(R.string.avanzado) -> {
                        // Lógica para el nivel Avanzado
                        nuevoJuego(dificultadSeleccionada ?: getString(R.string.avanzado))
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
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.seleccionarPersonaje))

        // Lista de nombres de personajes y recursos de imagen
        val nombresPersonajes = listOf("Tu primo", "Biden", "Putin")
        val imagenesPersonajes = listOf(R.drawable.terrorista, R.drawable.biden, R.drawable.putin)

        // Crear un ArrayAdapter personalizado
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombresPersonajes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Crear la vista personalizada del AlertDialog
        val customLayout = layoutInflater.inflate(R.layout.item_personaje, null)
        val spinner = customLayout.findViewById<Spinner>(R.id.spinnerPersonajes)
        val imageViewPersonaje = customLayout.findViewById<ImageView>(R.id.imageViewPersonaje)

        // Configurar el Spinner y su adaptador
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Obtener la posición seleccionada y cargar la imagen correspondiente
                val imagenPersonaje = imagenesPersonajes[position]
                imageViewPersonaje.setImageResource(imagenPersonaje)

                indicePersonajeSeleccionado = position
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Hacer algo si nada está seleccionado (opcional)
            }
        }

        builder.setView(customLayout)

        // Manejar el evento de botón positivo
        builder.setPositiveButton(getString(R.string.aceptar)) { _, _ ->
            // Obtener el índice seleccionado y el nombre del personaje
            val indiceSeleccionado = spinner.selectedItemPosition
            val nombrePersonaje = nombresPersonajes[indiceSeleccionado]

            // También puedes mostrar un mensaje aquí si lo necesitas
            Toast.makeText(this, "Personaje seleccionado: $nombrePersonaje", Toast.LENGTH_SHORT).show()
        }

        // Manejar el evento de botón negativo (opcional)
        builder.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
            // Lógica para manejar la cancelación
        }

        // Crear y mostrar el AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun generarTablero(nivelDificultad: String): Array<Array<Celda>> {

        // Genera un tablero de acuerdo al nivel de dificultad seleccionado
        val (filas, columnas, minas) = when (nivelDificultad) {
            getString(R.string.principiante) -> Triple(8, 8, MainActivity.MINAS_PRINCIPIANTE)
            getString(R.string.amateur) -> Triple(12, 12, MainActivity.MINAS_AMATEUR)
            getString(R.string.avanzado) -> Triple(16, 16, MainActivity.MINAS_AVANZADO)
            //si por alguna razon no hay una dificultad seleccionada de las 3
            //se lanza esta exception que se vera en el Logcat
            else -> throw IllegalArgumentException("Nivel de dificultad no válido")
        }
        // Crea un tablero de acuerdo a las dimensiones y cantidad de minas
        //al crear el tablero todas las celdas tienen como valor 0
        //y este valor se sustituira en celdas aleatorias por -1, que seran las minas.
        val tab: Tablero

        tab = Tablero(filas, columnas, minas)
        tab.colocarMinas(tab.celdas, minas)
        tab.calcularNumeros(tab.celdas)

        return tab.celdas.map { row ->
            row.map { contenido ->
                Celda(false, contenido)
            }.toTypedArray()
        }.toTypedArray()
    }
    private fun descubirCelda(fila: Int, columna: Int, button: Button, tablero: Array<Array<Celda>>) {
        val celda = tablero[fila][columna]

        // Verifica si ya se ha hecho clic en este botón
        if (celda.descubierta) {
            return
        }

        if (celda.contenido == -1) {
            // Mina encontrada
            descubrirMinas(button, tablero)
            mostrarMensaje(getString(R.string.partidaPerdida))
        } else if(celda.contenido == 0) {
            // No hay minas alrededor
            revelarBotonesAdyacentes(tablero, fila, columna)
        } else {
            // Hay minas alrededor
            cambiarTextoBoton(tablero, fila, columna)
        }

        // Marcar la celda como descubierta
        celda.descubierta = true
    }


    private fun revelarBotonesAdyacentes(tablero: Array<Array<Celda>>, fila: Int, columna: Int) {
        // Verifica si el valor de la celda es 0 (sin bomba)
        if (tablero[fila][columna].contenido == 0 && !tablero[fila][columna].descubierta) {
            // Crea una lista para almacenar las posiciones de los botones adyacentes
            val posicionesARevelar = mutableListOf<Pair<Int, Int>>()

            // Llama a una función recursiva para explorar las posiciones adyacentes
            revelarBotonesAdyacentesRecursivo(tablero, fila, columna, posicionesARevelar)

            // Itera sobre las posiciones y cambia el color de los botones
            for ((fila, columna) in posicionesARevelar) {
                //cambiarColorBoton(fila, columna, Color.GREEN)
                cambiarTextoBoton(tablero, fila, columna)
            }

            // Verifica si todas las celdas sin bomba han sido descubiertas después de revelar las adyacentes
            if (posicionesARevelar.size == tablero.size * tablero[0].size - tablero.size) {
                mostrarMensaje(getString(R.string.partidaGanada))
            }
        }
    }


    private fun revelarBotonesAdyacentesRecursivo(tablero: Array<Array<Celda>>, fila: Int, columna: Int, posicionesARevelar: MutableList<Pair<Int, Int>>) {
        // Verifica si la posición está dentro de los límites del tablero
        if (fila in 0 until tablero.size && columna in 0 until tablero[0].size) {
            // Verifica si la posición ya fue revelada
            if (Pair(fila, columna) !in posicionesARevelar) {
                // Agrega la posición a la lista de posiciones a revelar
                posicionesARevelar.add(Pair(fila, columna))

                // Verifica si la posición tiene un valor de 0 (sin bomba)
                if (tablero[fila][columna].contenido == 0) {
                    // Llama recursivamente a las posiciones adyacentes
                    for (i in -1..1) {
                        for (j in -1..1) {
                            revelarBotonesAdyacentesRecursivo(tablero, fila + i, columna + j, posicionesARevelar)
                        }
                    }
                }

                // Establece el texto del botón cuando se revela
                cambiarTextoBoton(tablero,fila, columna)
            }
        }
    }


    private fun cambiarTextoBoton(tablero: Array<Array<Celda>>, fila: Int, columna: Int) {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val button = gridLayout.getChildAt(fila * gridLayout.columnCount + columna) as Button
        button.text = tablero[fila][columna].contenido.toString()
    }

    private fun cambiarFondoBombas(fila: Int, columna: Int, texto: String) {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val button = gridLayout.getChildAt(fila * gridLayout.columnCount + columna) as Button
        button.background = getDrawable(R.drawable.terrorista)
    }




    private fun mostrarMensaje(mensaje: String) {
        val builder = AlertDialog.Builder(this)

        if (mensaje.equals(getString(R.string.partidaGanada)) || mensaje.equals(getString(R.string.partidaPerdida))) {
            // Configurar el mensaje
            builder.setMessage(mensaje)

            // Configurar la imagen
            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.jisus)  // Reemplaza "tu_imagen" con el nombre de tu imagen en res/drawable
            builder.setView(imageView)

            // Configurar el botón de aceptar
            builder.setPositiveButton(R.string.aceptar) { dialog, which ->
                dialog.dismiss()
            }

            // Crear y mostrar el AlertDialog
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun mostrarTableroEnPantalla(tablero: Array<Array<Celda>>) {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.removeAllViews()  // Elimina vistas existentes antes de agregar nuevas

        val columnCount = tablero[0].size

        // Configura el GridLayout
        gridLayout.columnCount = columnCount
        gridLayout.rowCount = tablero.size

        for (i in tablero.indices) {
            for (j in tablero[0].indices) {
                val button = Button(this)
                //mostrar valor de la celda en el boton
                //button.text = tablero[i][j].contenido.toString()

                button.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    button.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                button.setPadding(0, 0, 0, 0)

                // Asigna un OnClickListener a cada botón
                button.setOnClickListener {
                    // Lógica al hacer clic en el botón
                    descubirCelda(i, j, button, tablero)
                }

                button.setOnLongClickListener {
                    ponerBandera(i, j, button, tablero)

                }

                gridLayout.addView(button)
            }
        }
    }

    private fun ponerBandera(fila: Int, columna: Int, button: Button, tablero: Array<Array<Celda>>): Boolean {
        val celda = tablero[fila][columna]

        var numeroMinas = when{
            dificultadSeleccionada.equals(getString(R.string.principiante)) -> MINAS_PRINCIPIANTE
            dificultadSeleccionada.equals(getString(R.string.amateur)) -> MINAS_AMATEUR
            dificultadSeleccionada.equals(getString(R.string.avanzado)) -> MINAS_AVANZADO
            else -> MINAS_PRINCIPIANTE
        }

        if (celda.descubierta) {
            return false
        }

        if (celda.contenido == -1) {
            banderasColocadas++
            button.background = getDrawable(R.drawable.terroristaencontrado)

        }else{
            descubrirMinas(button, tablero)
            mostrarMensaje(getString(R.string.partidaPerdida))

        }

        if (banderasColocadas == numeroMinas) {
            mostrarMensaje(getString(R.string.partidaGanada))
        }
        return true
    }



    //descubrir todas las minas del tablero
    private fun descubrirMinas(button: Button, tablero: Array<Array<Celda>>) {
        for (i in tablero.indices) {
            for (j in tablero[0].indices) {
                val celda = tablero[i][j]
                if (celda.contenido == -1) {
                    cambiarFondoBombas(i, j, celda.contenido.toString())
                }
            }
        }
    }


}







