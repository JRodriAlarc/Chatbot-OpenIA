package com.example.ejemplo

//Definir antes de todo lo Demas
var Global:Int = 82

fun suma(num1:Int, num2:Int):Int{
    var resultado:Int = num1 + num2
    return resultado
}

fun saludo(usuario:String = "Invitado"): String{
    return "Bienvenido " + usuario
}
        //Permite Pasar varios Parametros
fun sumArreglo(vararg numbers:Int):Int{
    var sum = 0
    for (number in numbers){
        sum += number
    }
    return sum
}

fun main(){
    println(suma(15, 248))
    val valSaludo: String =  saludo("Cinthya")
    println(valSaludo)
    val result = sumArreglo(1, 2, 3, 4, 5, 100)
    println(result)
}