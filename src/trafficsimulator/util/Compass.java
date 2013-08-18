/** 
 * Compass.java
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

package trafficsimulator.util;

import java.awt.geom.Point2D;

/**
 * Classe que define uma forma de apontar para determinados sentidos.
 */

public enum Compass {
	Up (0,-1), Down (0,1), Left (-1,0), Right (1,0);
	
	private final int xc;
	private final int yc;
	
	private Compass (int xc, int yc){
		this.xc = xc;
		this.yc = yc;
	}
	
	public static Compass getCompass (int xinc, int yinc){
		if (xinc == 0){
			if (yinc == 1){
				return Down;
			} else if (yinc == -1){
				return Up;
			}
		} else if (xinc == -1){
			if (yinc == 0){
				return Left;
			}
		} else if (xinc == 1){
			if (yinc == 0){
				return Right;
			}
		}
		
		throw new BadLuckException("argumento invalido! (" + xinc + "," + yinc + ")");
	}
	
	public boolean sameAxis (Compass c){
		return (c == this || c == this.getOposite());
	}
	
	public Compass [] getAxis (){
		return new Compass [] {this,this.getOposite()};
	}
	
	public Compass [] getOpositeAxis (){
		if (this == Up || this == Down){
			return new Compass [] {Left,Right};
		} else {
			return new Compass [] {Up,Down};
		}
	}
	
	public double getRectMidX (double width){
		switch(this){
			case Up:
				return width/2;
			case Down:
				return width/2;
			case Left:
				return 0;
			case Right:
				return width;
			default:
				throw new BadLuckException("wtf?");
		}
	}
	
	public double getRectMidY (double height){
		switch(this){
			case Up:
				return 0;
			case Down:
				return height;
			case Left:
				return height/2;
			case Right:
				return height/2;
			default:
				throw new BadLuckException("wtf?");
		}
	}
	
	public Point2D getRectMidPoint (double w, double h){
		switch(this){
			case Up:
				return new Point2D.Double(w/2,0);
			case Down:
				return new Point2D.Double(w/2,h);
			case Left:
				return new Point2D.Double(0,h/2);
			case Right:
				return new Point2D.Double(w,h/2);
			default:
				throw new BadLuckException("wtf?");
		}
	}
	
	public Compass getOposite(){
		switch(this){
			case Up:
				return Down;
			case Down:
				return Up;
			case Left:
				return Right;
			case Right:
				return Left;
			default:
				throw new BadLuckException("wtf?");
		}
	}
	
	public int getXInc (){
		return xc;
	}
	
	public int getYInc (){
		return yc;
	}
	
	public byte getIndex (){
		for (byte b = 0; b < values().length; b++){
			if (this == values()[b]){
				return b;
			}
		}
		return -1;
	}
	
}
	

