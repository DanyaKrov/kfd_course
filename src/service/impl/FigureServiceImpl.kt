package service.impl

import model.Circle
import model.Figure
import model.Square
import service.FigureService
import kotlin.math.pow

object FigureServiceImpl: FigureService {
    private var figurePack: MutableList<Figure> = mutableListOf()
    private var perimeter: Double = 0.0
    private var area: Double = 0.0

    override fun addFigure(figure: Figure) {
        figurePack.add(figure)
        addPerimeter(figure)
        addSquare(figure)
    }

    override fun getPerimeter(): Double {
        return perimeter
    }

    override fun getArea(): Double {
        return area
    }

    fun addPerimeter(figure: Figure) {
        perimeter += when (figure) {
            is Circle -> figure.property * 2 * Math.PI
            is Square -> figure.property * 4
        }
    }


    fun addSquare(figure: Figure) {
        area += when (figure) {
            is Circle -> figure.property.pow(2) * Math.PI
            is Square -> figure.property.pow(2)
        }
    }
}