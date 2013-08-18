/** 
 * GraphicObject.java
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

package trafficsimulator.graphicobject;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * Classe que define qualquer tipo de objeto grafico no simulador.
 */

public abstract class GraphicObject {

	protected static double ppm; //pixels per meter
	private int K = 0;

	public static void setPPM(float ppm) {
		GraphicObject.ppm = ppm;
	}

	public static double pixelsToMeters(double p) {
		return ppm / p;
	}

	public static double metersToPrixels(double m) {
		return ppm * m;
	}

	@Deprecated
	public final void setK(int K) {
		this.K = K;
	}

	@Deprecated
	public final int getK() {
		return K;
	}

	public String isValid() {
		return null;
	}
	
	public abstract void drawBg(Graphics2D g, int mode);

	public abstract void draw(Graphics2D g, int mode);

	public abstract Shape getShape();

	public abstract int getX();

	public abstract int getY();

	public abstract double getWidth();

	public abstract double getHeight();

	public abstract Point getLocation();

	public abstract void setLocation(int x, int y);

	public abstract boolean contains(Point p);

	public abstract boolean contains(int x, int y);

	public abstract boolean intersects(Rectangle r);
}
