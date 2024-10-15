package service.impl

import enums.Operation
import exception.BadPropertyException
import exception.WrongFigureTypeException
import exception.WrongOperationTypeException
import exception.WrongSizeException
import figureService
import model.Circle
import model.Square
import service.ConsoleService
import java.util.*
import kotlin.jvm.Throws

object ConsoleServiceImpl: ConsoleService {
    private var active: Boolean = true
    override fun work() {
        while (active) {
            println("Введите тип операции, которую хотите исполнить:\n1) " +
                    "добавить фигуру\n2) получить площадь всех фигур\n3) " +
                    "получить периметр всех фигур\n4) завершить выполнение")
            try {
                chooseOperation()
            }
            catch (_: WrongOperationTypeException) {
                println("Данной операции не существует")
            }
        }
    }

    @Throws(WrongOperationTypeException::class)
    private fun chooseOperation() {
        val operation = readln()
        when (operation) {
            Operation.ADD.number -> addFigure()
            Operation.GET_AREA.number -> println(figureService.getArea())
            Operation.GET_PERIMETER.number -> println(figureService.getPerimeter())
            Operation.FINISH.number -> active = false
            else -> throw WrongOperationTypeException("Некорректный тип операции")
        }
    }

    private fun addFigure() {
        while (active) {
            println("Введите тип фигуры и через пробел необходимый размер(пример: Circle 5)")
            val request = readln().split(" ")
            try {
                if (request[1].toDouble() < 0)
                    throw  WrongSizeException("Введите корректный размер фигуры")
                when (request[0].lowercase()) {
                    "circle" -> figureService.addFigure(Circle(request[1].toDouble()))
                    "square" -> figureService.addFigure(Square(request[1].toDouble()))
                    else -> throw WrongFigureTypeException("Некорректный тип фигуры")
                }
                active = false
            } catch (_: WrongFigureTypeException) {
                println("Введите корректный тип фигуры")
            } catch (_: WrongSizeException) {
                println("Введите корректный размер фигуры")
            } catch (_: RuntimeException) {
                println("Введите запрос в корректном формате")
            }
        }
        active = true
    }
}