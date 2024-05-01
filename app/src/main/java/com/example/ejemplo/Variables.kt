package com.example.ejemplo

fun main(){
    val numInt: Int = 50
    val numLong: Long = 123143124311431
    val numFlot: Float = 5.296F
    val numDouble: Double = 3.1416
    val numBoolean: Boolean = true
    val valChar: Char = 'B'
    val valString: String = "Bla Bla Bla"
    val valString2: String = "425"

    println(numInt)
    println(numLong)
    println(numFlot)
    println(numDouble)
    println(numBoolean)
    println(valChar)
    println(valString)
    println(valString2)

    var num1: Int = 20
    num1 = 33

    println("Tengo $num1 años")
    println("Tengo " + num1.toString() + " años")
    println("Tengo $num1 años, " + valString)
    println(valString2+8)
    println(valString2.toInt()+8)

}
