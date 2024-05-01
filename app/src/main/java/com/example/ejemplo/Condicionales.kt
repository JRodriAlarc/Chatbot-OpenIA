package com.example.ejemplo

fun par02(num: Int){
    when (num%2){
        0 -> print("par")
        else -> print("impar")
    }
}



fun main(){
    fun par(num:Int):String {
        var resultado = "Es impar"
        if (num%2==0){
            resultado = "Es par"
        }
        return resultado
    }
    println(par(6582))

    // Estructura if-else para determinar si un número es positivo o negativo
    val number = -5
    if (number >= 0) {
        println("El número es positivo")
    } else {
        println("El número es negativo")
    }

    val data: Any = 3.14
    when(data) {
        is Int -> println("$data Es un numero entero")
        is Double -> println("$data Es un numero de Punto Flotante")
        is String -> println("$data Es una cadena de texto")
        else -> println("$data Es Desconocido")
    }

}