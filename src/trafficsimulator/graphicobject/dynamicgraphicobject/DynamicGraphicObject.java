/** 
 * DynamicGraphicObject.java
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

package trafficsimulator.graphicobject.dynamicgraphicobject;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import trafficsimulator.graphicobject.GraphicObject;
import trafficsimulator.path.Path;
import trafficsimulator.util.PVector;

/**
 * Classe que define um objeto movel.
 */

public abstract class DynamicGraphicObject extends GraphicObject{
	
	protected ArrayList<Path> pathList;
	
	protected int pathIndex;
	
	protected double bezierT;
			
	protected Path currentPath;
	
	protected Point location;
	
	protected PVector vLocation;
	
	protected double velocity;
	
	protected double aceleration;
	
	public DynamicGraphicObject (){
		location = new Point();
		vLocation = new PVector();
		bezierT = 0.0;
		pathIndex = 0;
	}
	
	public void setVelocity(double v){
		velocity = v;
	}
	
	public double getVelocity(){
		return velocity;
	}
	
	public void setAceleration(double a){
		aceleration = a;
	}
	
	public double getAceleration(){
		return aceleration;
	}
	
	public boolean stop(){
		return false;
	}
	
	public boolean isStopped(){
		return (velocity == 0);
	}
	
	public void setPathList(ArrayList<Path> pl){
		pathList = pl;
		currentPath = pathList.get(pathIndex);
		currentPath.getRoad().add(this);
	}
	
	public abstract void move (double dTime);
	
	@Override
	public int getX(){
		return location.x;
	}
	
	@Override
	public int getY(){
		return location.y;
	}
	
	public PVector getVLocation (){
		return vLocation;
	}
	
	public double getBezierT (){
		return bezierT;
	}
	
	@Override
	public Point getLocation(){
		return location;
	}

	public void setLocation(Point p){
		location = p;
	}
	
	@Override
	public void setLocation(int x, int y) {
		location = new Point(x,y);
	}
	
	public abstract Rectangle getDefaultBounds ();
	
}
	
