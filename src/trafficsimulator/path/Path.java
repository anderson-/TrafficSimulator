/** 
 * Path.java
 * 
 * Copyright (C) 2012 
 *	 Anderson de Oliveira Antunes  <anderson.utf@gmail.com>
 *   Fernando Henrique Carvalho Ferreira  <vojnik_f.henrique@hotmail.com>
 *
 * This file is part of TrafficSimulator.
 *
 * TrafficSimulator is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * TrafficSimulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with TrafficSimulator. If not, see http://www.gnu.org/licenses/.
 */

package trafficsimulator.path;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import trafficsimulator.graphicobject.staticgraphicobject.road.Road;

/**
 * Interface para tipos de vias do simulador.
 * <p>
 * Implementações dessa interface podem definir caminhos para o
 * movimento de objetos no simulador. Por exemplo, {@link trafficsimulator.CubicPath}
 * é um tipo de via definida por uma curva de bézier.
 * 
 */

public interface Path extends Shape{

	/**
	 * @return tamanho da via em pixels
	 */

	public int getPxLenght();

	/**
	 * @param t valor de parametrização da curva, um número pertencente ao intervalo fechado [0,1]
	 * @return um {@link java.awt.Point2D} pertencente à curva
	 */
	
	public Point getPoint(double t);

	/**
	 * @param t valor de parametrização da curva, um número pertencente ao intervalo fechado [0,1]
	 * @return uma {@link java.awt.geom.Line2D} tangente à curva no ponto B(t)
	 */
	
	public Line2D getTangentLine(double t);

	public void translateTo (int x, int y);
	
	public Road getRoad();
	
	/**
	 * @param t valor de parametrização da curva, um <code>double</code> pertencente ao intervalo fechado [0,1]
	 * @return ângulo da reta tangente à curva no ponto B(t)
	 */
	
	public double getAngle(double t);
}
