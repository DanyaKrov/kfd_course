import kotlin.jvm.Throws
import kotlin.math.PI
import kotlin.math.pow

var consoleService: ConsoleServiceImpl = ConsoleServiceImpl
var figureService: FigureServiceImpl = FigureServiceImpl


fun main() {
    consoleService.work()
}


interface ConsoleService {
    fun work()
}

interface FigureService {
    fun addFigure(figure: Figure)
    fun getPerimeter(): Double
    fun getArea(): Double
}

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
            catch (_: WrongOperationTypeException) {}
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
            else -> throw WrongOperationTypeException()
        }
    }

    private fun addFigure() {
        while (active) {
            try {
                println("Введите тип фигуры и через пробел необходимый размер(пример: Circle 5)")
                val request = readln().split(" ")
                when (request[0]) {
                    "Circle" -> figureService.addFigure(Circle(request[1].toDouble()))
                    "Square" -> figureService.addFigure(Square(request[1].toDouble()))
                }
                active = false
            }
            catch (_: BadPropertyException) {}
        }
        active = true
    }
}


enum class Operation(val number: String) {
    ADD("1"),
    GET_AREA("2"),
    GET_PERIMETER("3"),
    FINISH("4")
}


object FigureServiceImpl: FigureService {
    private var figurePack: MutableList<Figure> = mutableListOf()

    override fun addFigure(figure: Figure) {
        figurePack.add(figure)
    }

    override fun getPerimeter(): Double {
        return figurePack.sumOf { it.getPerimeter() }
    }

    override fun getArea(): Double {
        return figurePack.sumOf { it.getArea() }
    }
}


abstract class Figure @Throws(BadPropertyException::class) constructor(property: Double) {
    init {
        if (property < 0)
            throw BadPropertyException()
        println("${this.javaClass.name}(property=${property})")
    }

    abstract var property: Double
    abstract fun getArea(): Double
    abstract fun getPerimeter(): Double
}


private class Circle(override var property: Double): Figure(property) {
    override fun getArea(): Double {
        return property.pow(2) * PI
    }

    override fun getPerimeter(): Double {
        return 2 * PI * property
    }
}


private class Square(override var property: Double): Figure(property) {
    override fun getArea(): Double {
        return property.pow(2)
    }

    override fun getPerimeter(): Double {
        return property * 4
    }
}


class BadPropertyException: Exception() {
    init {
        println("Некорректно задан размер фигуры")
    }
}


class WrongOperationTypeException: Exception() {
    init {
        println("Данной операции не существует")
    }
}