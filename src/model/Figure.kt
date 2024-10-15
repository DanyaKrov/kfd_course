package model

import exception.BadPropertyException
import kotlin.jvm.Throws

sealed class Figure @Throws(BadPropertyException::class) constructor(property: Double) {
    init {
        println("${this.javaClass.name}(property=${property})")
    }
}