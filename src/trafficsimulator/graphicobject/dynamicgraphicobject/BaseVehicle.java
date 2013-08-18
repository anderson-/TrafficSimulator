/** 
 * BaseVehicle.java
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import trafficsimulator.DrawingPanel;
import trafficsimulator.graphicobject.staticgraphicobject.road.Intersection;
import trafficsimulator.graphicobject.staticgraphicobject.road.Street;
import trafficsimulator.path.Path;
import trafficsimulator.trafficcontrol.TrafficLight.State;
import trafficsimulator.util.PVector;

/**
 * Classe que define um carro simples.
 */

public class BaseVehicle extends DynamicGraphicObject{

	public final static ArrayList<BufferedImage> RESOURCES;
	
	static {
		RESOURCES = new ArrayList<BufferedImage>();
		
		try {
			RESOURCES.add(ImageIO.read(DrawingPanel.class.getResourceAsStream("resources/my_pixel_art/PoliceCar.gif")));
			RESOURCES.add(ImageIO.read(DrawingPanel.class.getResourceAsStream("resources/my_pixel_art/Car.gif")));
		} catch (Exception e){
		}
	}
	
	private Rectangle shape;
	
	private BufferedImage imgBase;
	
	private Shape tShape;
	
	private int maxDim;
	
	public BaseVehicle (){
		imgBase = RESOURCES.get((int)(Math.random()*RESOURCES.size()));
		shape = new Rectangle(0,0, imgBase.getWidth(), imgBase.getHeight());
		tShape = shape;
		maxDim = (imgBase.getWidth() > imgBase.getHeight())? imgBase.getWidth() : imgBase.getHeight();
	}
	
	@Override
	public int getX(){
		return location.x - maxDim/2;
	}
	
	@Override
	public int getY(){
		return location.y - maxDim/2;
	}
	
	@Override
	public void move (double dTime) {
		
		boolean stop = false;
		
		int pIndex = currentPath.getRoad().getVehicles().indexOf(this);
		
		DynamicGraphicObject nextCar = null;
		
		if (pIndex != 0){
			nextCar = currentPath.getRoad().getVehicles().get(pIndex-1);
		} else {
			if (pathIndex + 1 < pathList.size()){
				Path p = pathList.get(pathIndex+1);
				
				ArrayList<DynamicGraphicObject> carArray = p.getRoad().getVehicles();
				if (carArray.size() > 0){
					nextCar = carArray.get(carArray.size()-1);
				}
			}
//			if (pathIndex + 2 < pathList.size()){
//				Path p = pathList.get(pathIndex+2);
//				stop |= !p.getRoad().fits(this);
//			}
		}
		
		if (nextCar != null && /*temp*/ nextCar instanceof BaseVehicle){
			stop = (vLocation.dist(((BaseVehicle)nextCar).getVLocation()) < 33);
		}
		
		if (stop){
			velocity = 0;	
		} else {
			if (currentPath.getRoad() instanceof Street){
				State s = ((Street) currentPath.getRoad()).tl.getState();

				if (bezierT >= 0.7 && (s == State.RED || s == State.YELLOW)){
					velocity = 0;
				} else {
					velocity = 10;
				}
			} else {
				velocity = 10;
			}
		}
		
		double pixMove = (velocity*11*dTime); //quantos pixels andou
		
		while (pixMove > 0){
			
			// espaço restante entre o carro e o final da rua
			double gap = (currentPath.getPxLenght() - currentPath.getPxLenght()*bezierT);
			
			if (pixMove >= gap){
				
				if (pathIndex + 2 < pathList.size()){
					Path p = pathList.get(pathIndex+2);
					if (p.getRoad() instanceof Street){
						stop |= (((Street)p.getRoad()).tl.getState() == State.RED && ((Street)p.getRoad()).getVehicles().size() >= 3);
					}
				}
				
				if (!stop){
					currentPath.getRoad().remove(this);
					pathIndex = (short) ((pathIndex + 1 < pathList.size())? pathIndex + 1 : 0);
					currentPath = pathList.get(pathIndex);
					currentPath.getRoad().add(this);
					bezierT = 0;
					pixMove -= gap;
				} else {
					pixMove = 0;
					break;
				}
				
			} else {
				bezierT += pixMove/currentPath.getPxLenght();
				pixMove = 0;
			}
			
		}
		
		location = currentPath.getPoint(bezierT);
		vLocation.x = location.x;
		vLocation.y = location.y;
		
	}
	
	@Override
	public Shape getShape() {
		AffineTransform t = new AffineTransform();
		t.rotate(Math.toRadians(currentPath.getAngle(bezierT)),imgBase.getWidth()/2 , imgBase.getHeight()/2);
		tShape = t.createTransformedShape(shape);
		return tShape;
	}

	@Override
	public boolean contains(Point p) {
		p.translate((int)-getX(), (int)-getY()); //TODO: temp
		return tShape.contains(p);
	}

	@Override
	public boolean contains(int x, int y) {
		x -= getX(); //TODO: temp
		y -= getY();
		return tShape.contains(x, y);
	}

	@Override
	public boolean intersects(Rectangle r) {
		return tShape.intersects(r);
	}

	boolean temp = false;
	
	@Override
	public void draw(Graphics2D g, int mode) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void drawBg(Graphics2D g, int mode) {
		AffineTransform t = new AffineTransform();
		t.translate(((maxDim/2)-(imgBase.getWidth()/2)), ((maxDim/2)-(imgBase.getHeight()/2)));
		
		t.rotate(Math.toRadians(currentPath.getAngle(bezierT)),imgBase.getWidth()/2 , imgBase.getHeight()/2);
		
		//g.setColor((getK() == 1)? Color.blue : Color.red);
		
		tShape = t.createTransformedShape(shape);
		//g.draw(tShape);
		
		g.drawImage(imgBase, t, null);
		
		setK(0);
		
		//g.setColor(Color.orange);
		//g.drawString(currentPath.getRoad().getVehicles().indexOf(this) + ".", 0, 0);
		
	}

	@Override
	public double getWidth() {
		return tShape.getBounds().width;
	}

	@Override
	public double getHeight() {
		return tShape.getBounds().height;
	}

	@Override
	public Rectangle getDefaultBounds() {
		return shape;
	}


}