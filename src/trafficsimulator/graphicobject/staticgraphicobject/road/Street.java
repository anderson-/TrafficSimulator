/** 
 * Street.java
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

package trafficsimulator.graphicobject.staticgraphicobject.road;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import trafficsimulator.Clock;
import trafficsimulator.graphicobject.dynamicgraphicobject.DynamicGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.StaticGraphicObject;
import trafficsimulator.maphandler.mapeditor.MapEditorModeG;
import trafficsimulator.path.LinearPath;
import trafficsimulator.path.Path;
import trafficsimulator.trafficcontrol.TrafficLight;
import trafficsimulator.trafficcontrol.TrafficLight.State;
import trafficsimulator.util.BadLuckException;
import trafficsimulator.util.Compass;

/**
 * Classe que define uma rua.
 */

public class Street extends Road{

	public static final int arrowDifSize = 10;
	
	private int trafficJam = 0;
	protected double trafficJamPerTime = 0;
	
	
	private final LinearPath path;
	private Compass direction;
	private Road in = null;
	private Road out = null;
	public TrafficLight tl;
	public boolean tjVerify = false;
	
	/**
	 * Construtor Padrão (descrição)
	 *
	 * @param  variavel1 descrição da variavel
	 * @param  variavel2 descrição da variavel
	 */
	public Street (Compass dir){
		tl = new TrafficLight();
		path = new LinearPath(this,dir);
		direction = dir;
		super.setSize(Math.abs(dir.getXInc())*90 + 30,Math.abs(dir.getYInc())*90 + 30);
		path.setRect(Math.abs(dir.getXInc())*90 + 30,Math.abs(dir.getYInc())*90 + 30);
	}
	
	@Override
	public void setSize (int width, int height){
//		super.setSize(width, height);
//		path.setRect(width, height);
	}
	
	@Override
	public void copyPropertiesOf(StaticGraphicObject obj){
		super.setLocation(obj.getI(),obj.getJ());
		super.setLocation((int)obj.getX(), (int)obj.getY());
	}
	
	public void setInput(Road in) {
		this.in = in;
	}

	public void setOutput(Road out) {
		this.out = out;
	}

	
	
	public Compass getDirection (){
		return direction;
	}
	
	@Override
	public Path getRandPath(Road input) {
		return path;
	}

	@Override
	public Road getOutput(Path p) {
		
		if (path == p){
			return out;
		}
		throw new BadLuckException("Path errado..");
	}
	
	@Override
	public Road getInput(Path p) {
		
		if (path == p){
			return in;
		}
		throw new BadLuckException("Path errado..");
	}

	@Override
	public void setLocation(int x, int y){
		super.setLocation(x, y);
		path.translateTo((int)getX(), (int)getY());
	}
	
	@Override
	public Path[] getPathList() {
		return new Path [] {path};
	}

	@Override
	public boolean hasOutput() {
		return (out != null);
	}

	public boolean hasInput() {
		return (in != null);
	}

	@Override
	public void drawBg(Graphics2D g, int mode) {
		if (mode == 0){
			g.setColor(Color.WHITE);
		} else if (mode == 1){
			g.setColor(Color.DARK_GRAY);
		} else if (mode == 2){
			Color bgColor = Color.getHSBColor((float)((100.0f - trafficFlowPerTime*100.0f)/360.0), 1, 1);
			g.setColor(bgColor);
		} else if (mode == 3){
			Color bgColor = Color.getHSBColor((float)((100.0f - trafficJamPerTime*100.0f)/360.0), 1, 1);
			g.setColor(bgColor);
		}
		g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
	}
	
	private Color arrowColor = Color.decode("#FFA609");
	private Color asd = new Color (1.0f,0,0,0.5f);
	
	@Override
	public void draw(Graphics2D g, int mode) {
		
		
		if (in != null && out != null){
			g.setColor(Color.LIGHT_GRAY);
		} else if (in != null){
			g.setColor(Color.BLUE);
		} else if (out != null){
			g.setColor(Color.red);
		} else {
			g.setColor(Color.BLACK);
		}
		//g.drawString("=)", (int)getWidth()/2, (int)getHeight()/2);
		if (mode == 0){
			g.setColor(arrowColor);
			MapEditorModeG.drawArrow(g, (int)path.x1+direction.getXInc()*arrowDifSize, (int)path.y1+direction.getYInc()*arrowDifSize, (int)path.x2-direction.getXInc()*arrowDifSize, (int)path.y2-direction.getYInc()*arrowDifSize);
		} else {
			
			if (tl.getState() == State.GREEN){
				if (!tjVerify){
					trafficJam += this.getNVehicles()*5;
					System.out.println("asd" + trafficJam);
					tjVerify = true;
				} 
			} else {
				tjVerify = false;
			}
			
			g.setColor(tl.getColor());
			g.draw(path);
		}
	}
	
	@Override
	public boolean fits (DynamicGraphicObject obj){
		if (isEmpty()){
			return true;
		}
		
		DynamicGraphicObject last = getVehicles().get(getNVehicles()-1);
		double gap = (path.getPxLenght()*last.getBezierT());
		return (gap >= obj.getDefaultBounds().width);
	}
	
	@Override
	public void clockIncrease (Clock c){
		super.clockIncrease(c);
		trafficJamPerTime = trafficJam/((c.getElapsedMilis()+1)/1000.0);
		trafficJamPerTime = (trafficJamPerTime > 1)? 1 : trafficJamPerTime;
	}
}
