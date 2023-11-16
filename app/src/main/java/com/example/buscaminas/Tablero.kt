package com.example.buscaminas

import kotlin.random.Random

class Tablero {

        var filas: Int = 0
        var columnas: Int = 0
        var minas: Int = 0
        var celdas: Array<Array<Int>> = arrayOf()

        constructor(filas: Int, columnas: Int, minas: Int) {
            this.filas = filas
            this.columnas = columnas
            this.minas = minas
            this.celdas = Array(filas) { Array(columnas) { 0 } }
        }


        fun colocarMinas(tablero: Array<Array<Int>>, cantidadMinas: Int) {
            val random = Random.Default

            var minasRestantes = cantidadMinas
            //se genera un numero aleatorio entre 0 y el numero de filas y otro entre 0 y el numero de columnas
            //para colocar las minas aleatoriamente en el mapa, y se realizara este bucle hasta que
            //se hayan colocado todas las minas pasadas como parametro
            while (minasRestantes > 0) {
                val fila = random.nextInt(tablero.size)
                val columna = random.nextInt(tablero[0].size)

                if (tablero[fila][columna] != -1) {
                    tablero[fila][columna] = -1
                    minasRestantes--
                }
            }
        }
        fun calcularNumeros(tablero: Array<Array<Int>>) {
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


}