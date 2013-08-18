/** 
 * Intersection.java
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
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import trafficsimulator.graphicobject.dynamicgraphicobject.DynamicGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.StaticGraphicObject;
import trafficsimulator.path.Path;
import trafficsimulator.trafficcontrol.TrafficLight;
import trafficsimulator.trafficcontrol.TrafficLight.State;
import trafficsimulator.util.BadLuckException;

/**
 * Classe que define um cruzamento entre ruas.
 */

public class Intersection extends Road {
	
	private HashMap<Path,Road> inRoutes;
	private HashMap<Path,Road> outRoutes;
	public IntersectionTLS state;
	
	private ArrayList<Path> pathList;
	
	private boolean out = false;
	
	private ArrayList<ArrayList<StaticGraphicObject>> crossroad;
	
	public Intersection(){
		inRoutes = new HashMap<Path,Road>();
		outRoutes = new HashMap<Path,Road>();
		pathList = new ArrayList<Path>();
		crossroad = new ArrayList<ArrayList<StaticGraphicObject>>();
	}
	
	@Override
	public void setSize (int width, int height){
		
		// TODO: atualizar o tamanho e etc..
		super.setSize(width,height);
		//for(Path p: pathList){
			//p.setRect(width, height);
		//}
	}
	
	public void addInput(Road road, Path path) {
		inRoutes.put(path,road);
		pathList.add(path);
	}	

	public void addOutput(Road road, Path path) {
		out = true;
		outRoutes.put(path,road);
		pathList.add(path);
	}

	@Override
	public Path getRandPath (Road input){
		Path p = null;
		if (inRoutes.containsValue(input)){
			ArrayList<Path> pList = new ArrayList<Path>();
			for (Entry<Path,Road> e : inRoutes.entrySet()){
				if (e.getValue() == input){
					pList.add(e.getKey());
				}
			}
			p = pList.get((int)(Math.random()*pList.size()));
		} else {
			if (!pathList.isEmpty()){
				p = pathList.get((int)(Math.random()*pathList.size()));
			}
		}
		
		if (p == null){
			throw new BadLuckException("Nenhum caminho encontrado!");
		}
		
		//p.translateTo((int)getX(), (int)getY());
		
		return p;
	}

	@Override
	public Path [] getPathList() {
		Object [] tmp = pathList.toArray();
		Path [] ret = new Path [tmp.length];
		
		for (int i = 0; i < tmp.length; i++){
			ret[i] = (Path) tmp[i];
		}
		
		return ret;
	}
	
	@Override
	public Road getOutput (Path p){
		if (!outRoutes.containsKey(p)){
			throw new BadLuckException("não tem...");
		}
		return outRoutes.get(p);
	}
	
	@Override
	public Road getInput (Path p){
		if (!inRoutes.containsKey(p)){
			throw new BadLuckException("não tem...");
		}
		return outRoutes.get(p);
	}
	
	@Override
	public void drawBg (Graphics2D g, int mode){
		//updateRoutes(x);
		if (mode == 0){
			g.setColor(Color.WHITE);
		} else if (mode == 1){
			g.setColor(Color.DARK_GRAY);
		} else if (mode == 2){
			Color bgColor = Color.getHSBColor((float)((100.0f - trafficFlowPerTime*100.0f)/360.0), 1, 1);
			g.setColor(bgColor);
		} else if (mode == 3){
			g.setColor(Color.DARK_GRAY);
		}
		g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
		
		g.translate(-this.getX(), -this.getY());
		
		//g.setColor((K == 1)? Color.GREEN : Color.BLUE);
		for (Path a: pathList){
			if (a instanceof CubicCurve2D){
				g.setColor(Color.red);
				
				g.draw((CubicCurve2D) a);
			} else if (a instanceof Line2D){
				g.setColor(Color.CYAN);
				g.draw((Line2D) a);
			}
				//System.out.println(((CubicCurve2D)a).toString());
		}
		setK(0);
	}

	@Override
	public boolean hasOutput() {
		return out;
	}

	public IntersectionTLS getState() {
		return state;
	}

	@Override
	public void draw(Graphics2D g, int mode) {
		g.setColor(Color.orange);
	}
	
	public class IntersectionTLS { //traffic light state
		public ArrayList<TrafficLight> H;
		public ArrayList<TrafficLight> V;
		
		public IntersectionTLS(){
			H = new ArrayList<TrafficLight>();
			V = new ArrayList<TrafficLight>();
		}
		
		public void setH (State s){
			for (TrafficLight t : H){
				t.setState(s);
			}
		}
		
		public void setV (State s){
			for (TrafficLight t : V){
				t.setState(s);
			}
		}
		
		public State getH (){
			State s = null;
			
			for (TrafficLight t : H){
				if (!(s == null || s == t.getState())){
					return null;
				}
			}
			
			return s;
		}
		
		public State getV (){
			State s = null;
			
			for (TrafficLight t : V){
				if (!(s == null || s == t.getState())){
					return null;
				}
			}
			
			return s;
		}

		public void swap() {
			for (TrafficLight t : H){
				t.setState(t.getState().getOposite());
				System.out.println("ht"+ t);
				System.out.println("a");
			}
			
			for (TrafficLight t : V){
				t.setState(t.getState().getOposite());
				System.out.println("vt"+ t);
				System.out.println("b");
			}
		}
	}
	
	@Override
	public boolean fits (DynamicGraphicObject obj){
		return true; //TODO
	}

}
	
