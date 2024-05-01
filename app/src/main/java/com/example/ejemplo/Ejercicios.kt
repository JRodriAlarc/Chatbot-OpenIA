package com.example.ejemplo

//Calcular x Operación Básica
fun operaciones(operacion: Int, num01: Int, num02: Int):Int {
    when (operacion) {
        1 -> return num01 + num02
        2 -> return num01 - num02
        3 -> return num01 / num02
        4 -> return num01 * num02
        5 -> return num01 % num02
        else -> throw IllegalArgumentException ("Operación Inválida")
    }
}

//Calcular el Área de x figura
fun area(figura: String, base: Double, altura:Double = 0.0):Double{
    val op = base * altura
    when (figura) {
        "cuadrado" -> return base * base
        "rectangulo" -> return op
        "triangulo" -> return op / 2
        "romboide" -> return op
        else -> throw IllegalArgumentException ("Figura Inválida")
    }
}

//Determinar si un Numero es par o Impar
fun par(num: Int):String{
    when(num % 2) {
        0 -> return "$num es un número Par"
        else -> return "$num es un número Impar"
    }
}

//Calcular el Promedio de un Arreglo
fun promedio(vararg numbers:Int):Double{
    var sum:Int = 0
    for (number in numbers){
        sum += number
    }
    val longitud:Int = numbers.size
    return (sum.toDouble() / longitud)
}

fun main(){
    println(operaciones(4, 3, 3))
    println(area("cuadrado", 6.0))
    println(par(45))
    println(promedio(9, 9, 9, 10, 8))

    /*
    var num: Int = 0
    while (num <= 10){
        println(num)
        num++
    }
    */
}