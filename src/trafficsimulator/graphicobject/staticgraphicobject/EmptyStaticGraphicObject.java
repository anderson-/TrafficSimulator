/** 
 * EmptyStaticGraphicObject.java
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

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Classe que define um objeto vazio no mapa.
 */

public class EmptyStaticGraphicObject extends StaticGraphicObject{

	private boolean isWarning;

	private int crossVal = 0;
	
	public EmptyStaticGraphicObject (){

	}
	
	public EmptyStaticGraphicObject (boolean w){
		isWarning = w;
	}
	
	public int getCrossVal (){
		return crossVal;
	}
	
	public void setCrossVal (int v){
		crossVal = v;
	}

	public void setSize (int width, int height){
		if (width == 0 || height == 0){
			System.out.println("ERR");
		}
		super.setSize(width,height);
	}

	@Override
	public void draw(Graphics2D g, int mode){
		
	}
	
	Color bg = Color.decode("#559A8C");

	@Override
	public void drawBg(Graphics2D g, int mode) {
		if (mode == 0){
			int width = (int) getWidth();
			int height = (int) getHeight();

			g.setColor(bg);
			g.fillRect(0,0,width, height);

			g.setColor(Color.white);
			g.drawString("?", width/2 -2, height/2 +5);
		} else {
			
		}
	}

//	@Override
//	public void drawBg(Graphics2D g, SimulationMode o) {
//		int width = (int) getWidth();
//		int height = (int) getHeight();
//		
//		g.setColor(Color.blue);
//		g.fillRect(0,0,width, height);
//
//	}
//
//	@Override
//	public void draw(Graphics2D g, SimulationMode o) {
//		int width = (int) getWidth();
//		int height = (int) getHeight();
//		
//		if (isWarning){
//			g.setColor(Color.red);
//			g.drawString("!", width/2 -2, height/2 +5);
//		} else {
//			g.setColor(Color.white);
//			g.drawString("?", width/2 -2, height/2 +5);
//		}
//	}

}

