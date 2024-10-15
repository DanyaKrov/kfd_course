package service
import model.Figure

interface FigureService {
    fun addFigure(figure: Figure)
    fun getPerimeter(): Double
    fun getArea(): Double
}