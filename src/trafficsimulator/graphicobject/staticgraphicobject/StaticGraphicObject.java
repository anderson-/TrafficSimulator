/** 
 * StaticGraphicObject.java
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

package trafficsimulator.graphicobject.staticgraphicobject;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import trafficsimulator.graphicobject.GraphicObject;

/**
 * Classe que define qualquer objeto estatico no mapa.
 */

public abstract class StaticGraphicObject extends GraphicObject{

	private Point matrixPosition;
	private Rectangle shape;
	
	public StaticGraphicObject() {
		matrixPosition = new Point();
		shape = new Rectangle();
	}
	
	public void copyPropertiesOf(StaticGraphicObject obj){
		matrixPosition.setLocation(obj.getI(),obj.getJ());
		shape.setSize((int)obj.getWidth(),(int)obj.getHeight());
		shape.setLocation((int)obj.getX(), (int)obj.getY());
	}

	@Override
	public Shape getShape(){
		return shape;
	}
	
	public void setMatrixPosition (int i, int j){
		matrixPosition.setLocation(i,j);
	}
	
	public Point getMatrixPosition (){
		return matrixPosition;
	}
	
	public int getI(){
		return matrixPosition.x;
	}
	
	public int getJ(){
		return matrixPosition.y;
	}

	@Override
	public int getX(){
		return shape.x;
	}

	@Override
	public int getY(){
		return shape.y;
	}

	@Override
	public double getWidth(){
		return shape.width;
	}

	@Override
	public double getHeight(){
		return shape.height;
	}

	public void setSize(int width, int height){
		shape.setSize(width,height);
	}

	@Override
	public Point getLocation(){
		return shape.getLocation();
	}

	@Override
	public void setLocation(int x, int y){
		shape.setLocation(x,y);
	}

	@Override
	public boolean contains(Point p){
		return shape.contains(p);
	}

	@Override
	public boolean contains(int x, int y){
		return shape.contains(x,y);
	}

	@Override
	public boolean intersects(Rectangle r){
		return shape.intersects(r);
	}
	
}
	
