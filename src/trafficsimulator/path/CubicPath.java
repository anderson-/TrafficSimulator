/** 
 * CubicPath.java
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
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import trafficsimulator.graphicobject.staticgraphicobject.road.Road;

/**
 * Via definida por uma curva de bézier.
 */

public class CubicPath extends CubicCurve2D.Double implements Path{

	private int pxLenght;
	private Road obj;
	private int posX = 0;
	private int posY = 0;
	
	/**
	 * @param x1 argumento <code>x</code> do ponto que define o inicio da curva.
	 * @param y1 argumento <code>y</code> do ponto que define o inicio da curva.
	 * @param ctrlx1 argumento <code>x</code> do ponto primeiro ponto de controle
	 * @param ctrly1 argumento <code>y</code> do ponto primeiro ponto de controle
	 * @param ctrlx2 argumento <code>x</code> do ponto segundo ponto de controle
	 * @param ctrly2 argumento <code>y</code> do ponto segundo ponto de controle
	 * @param x2 argumento <code>x</code> do ponto que define o final da curva.
	 * @param y2 argumento <code>y</code> do ponto que define o final da curva.
	 */
	
	public CubicPath (Road obj, Point2D a, Point2D ca, Point2D cb, Point2D b) {
		pxLenght = 0;
		this.obj = obj;
		
		this.setCurve(a, ca, cb, b);
		calcLenght(100);
	}
	
	@Override
	public Road getRoad() {
		return obj;
	}

	/**
	 * Calcula uma aproximação do comprimento da curva, somando a distancia
	 * entre <code>p</code> pontos pertencentes à curva.
	 * 
	 * @param p numero de pontos pertencentes à curva
	 */

	public final void calcLenght (int p){

		if (p <= 0) {
			return;
		}
		
		Point2D list[] = new Point2D[p+1];
		
		double incr = 1.0/p;
		double len = 0;
		
		for (int i = 0; i <= p; i++){
			list[i] = getPoint(i*incr);
		}
		
		for (int i = 0; i < p; i++){
			len += list[i].distance(list[i+1]);
		}

		pxLenght = (int) len;
		
	}

	@Override
	public int getPxLenght(){
		return pxLenght;  // TODO : usar setLenght não confundir tamanho em pixels e metros!
	}

	@Override	
	public Point getPoint(double t){

		double x=(1-t)*(1-t)*(1-t) * x1 + 3*t*(1-t)*(1-t)* ctrlx1 + 3*t*t*(1-t)*ctrlx2 + t*t*t*x2 + posX;
		double y=(1-t)*(1-t)*(1-t) * y1 + 3*t*(1-t)*(1-t)* ctrly1 + 3*t*t*(1-t)*ctrly2 + t*t*t*y2 + posY;
		
		return new Point((int)x, (int)y);
	}

	@Override
	public Line2D getTangentLine(double t){
			
		double ipx0=x1+(ctrlx1-x1)*t, ipy0=y1+(ctrly1-y1)*t;
		double ipx1=ctrlx1+(ctrlx2-ctrlx1)*t, ipy1=ctrly1+(ctrly2-ctrly1)*t;
		double ipx2=ctrlx2+(x2-ctrlx2)*t, ipy2=ctrly2+(y2-ctrly2)*t;
		
		double tx0=ipx0+(ipx1-ipx0)*t, ty0=ipy0+(ipy1-ipy0)*t;
		double tx1=ipx1+(ipx2-ipx1)*t, ty1=ipy1+(ipy2-ipy1)*t;

		return new Line2D.Double(tx0, ty0, tx1, ty1);
	}

	@Override
	public double getAngle(double t){

		Line2D tangent = getTangentLine(t);
		
		double lx1 = tangent.getX1();
		double lx2 = tangent.getX2();
		double ly1 = tangent.getY1();
		double ly2 = tangent.getY2();
		
		if (lx2 - lx1 == 0.0){
			if (ly2 > ly1){
				return 90.0;
			} else {
				return 270.0;
				}
		} else {
			double alpha = Math.toDegrees(Math.atan(((ly2-ly1)/(lx2-lx1))));
			if (lx2 > lx1){
				return alpha;
			} else {
				return alpha + 180.0;
			}
		}	  
	}

	@Override
	public void translateTo(int x, int y) {
		posX = x;
		posY = y;
	}
	
	public int getPosX (){
		return posX;
	}
	
	public int getPosY (){
		return posY;
	}
	
}
	
