/** 
 * LinearPath.java
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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import trafficsimulator.graphicobject.staticgraphicobject.road.Road;
import trafficsimulator.util.Compass;

/**
 * Via definida por uma linha.
 */
 
public class LinearPath extends Line2D.Double implements Path{
	
	private int pxLenght;
	private Road obj;
	private Compass dir;
	private int posX = 0;
	private int posY = 0;
	/**
	 * Construtor Padrão (descrição)
	 *
	 * @param  variavel1 descrição da variavel
	 * @param  variavel2 descrição da variavel
	 */
	
	public LinearPath (Road obj, Compass dir){
		this.obj = obj;
		this.dir = dir;
	}

	public LinearPath(Road obj, Compass dir, Point2D.Double pa, Point2D.Double pb) {
		this.obj = obj;
		this.dir = dir;
		this.setLine(pa, pb);
		pxLenght = (int) getP1().distance(getP2());
	}
	
	@Override
	public Road getRoad() {
		return obj;
	}

	@Override
	public int getPxLenght(){
		return pxLenght;
	}
	
	@Override
	public Point getPoint(double t){
		double x = (1-t)*getX1() + t*getX2() + posX;
		double y = (1-t)*getY1() + t*getY2() + posY;
		
		return new Point((int)x, (int)y);
	}

	@Override
	public Line2D getTangentLine(double t){
		return this;
	}

	@Override
	public double getAngle(double t){
		
		if (x1 == x2){
			if (y1 > y2){
				//down
				return 270;
			} else {
				//up
				return 90;
			}
		} else if (y1 == y2){
			if (x1 > x2){
				//left
				return 180;
			} else {
				//right
				return 0;
			}
		} else {
			throw new IllegalStateException("Angulo invalido!");
		}
		
	}
	
	@Override
	public void translateTo(int x, int y) {
		posX = x;
		posY = y;
	}

	public void setRect(int width, int height) {
		setLine(dir.getOposite().getRectMidPoint(width, height), dir.getRectMidPoint(width, height));
		pxLenght = (int) getP1().distance(getP2());
	}

}
	
