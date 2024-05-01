package com.example.ejemplo

fun main() {
    for (i in 1..5){
        println("Número: ${i*2}")
    }

    val miLista: Array<String> = arrayOf("hola", "como", "estas")

    for (palabra in miLista){
        println(palabra)
    }

    for (i in 0 .. miLista.size-1){
        println(miLista[i])
    }

    var count = 1
    while (count <= 10) {
        println("Contando:  ${count*count}")
        count++
    }

}