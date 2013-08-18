/** 
 * VehicleManager.java
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

package trafficsimulator.trafficcontrol;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import trafficsimulator.Map;
import trafficsimulator.graphicobject.staticgraphicobject.StaticGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.road.Road;
import trafficsimulator.graphicobject.staticgraphicobject.road.Street;
import trafficsimulator.path.Path;
import trafficsimulator.Clock;
import trafficsimulator.graphicobject.dynamicgraphicobject.BaseVehicle;
import trafficsimulator.graphicobject.dynamicgraphicobject.DynamicGraphicObject;
import trafficsimulator.maphandler.simulation.util.TrafficDensityPanel;
import trafficsimulator.util.BadLuckException;

/**
 * Classe responsavel por controlar o movimento e desenho dos objetos moveis.
 */

public class VehicleManager extends Thread{
	
	public static final int MAX_PLIST_SIZE = 10000;
	public static final int MAX_VEHICLES = 100;
	
	private ArrayList<DynamicGraphicObject> vehicles;
	private ArrayList<Road> sources;
	private ArrayList<Road> drains;
	private Clock clock = null;
	private TrafficDensityPanel trafficDP;
	
	public VehicleManager(){
		vehicles = new ArrayList<DynamicGraphicObject>();
		sources = new ArrayList<Road>();
		drains = new ArrayList<Road>();
	}

	public Clock getClock() {
		return clock;
	}
	
	
	@Override
	public void run (){
		while (true){
				
			if (!clock.isPaused()){
				int nv = (int) (trafficDP.getValue()*MAX_VEHICLES);
				
				synchronized (vehicles){
					while (vehicles.size() <= nv && !clock.isPaused()){
						int sleep = (addCar() == null)? 1500 : 500; 
						nv = (int) (trafficDP.getValue()*MAX_VEHICLES);
				
						try {
							Thread.sleep(sleep);
						} catch (Exception ex) {

						}
					}
				}
			}
			
			
			try {
				Thread.sleep(2000);
			} catch (Exception ex) {
				
			}
		}
	}

	public void setClock(Clock clock) {
		this.clock = clock;
		
		//Alarm a = clock.new();
		
		
//		try {
//			
//			Alarm a = clock.new Alarm(0, 3);
//			a.setMethod(this.getClass().getMethod("addCar"), this);
//			clock.addAlarm(a);
//			
//		} catch (Exception e) {
//			
//		}
	}
	
	public void addSource (Road r){
		sources.add(r);
	}
	
	public void addDrain (Road r){
		drains.add(r);
	}
	
	public ArrayList<Path> createPathList (Road r){
		
		ArrayList<Path> pList = new ArrayList<Path>();
		
		Road it = r, prev = null;
		
		Path nxt;
		
		do {
			
			nxt = it.getRandPath(prev);
			
			pList.add(nxt);
			
			if (pList.size() > MAX_PLIST_SIZE){
				throw new BadLuckException("Não tem fim??");
			}
			
			prev = it;
			
			it = it.getOutput(nxt);
			
		} while (it != null);
		
		return pList;
	}
	
	public DynamicGraphicObject addCar (){
		
		BaseVehicle v = new BaseVehicle();
		//v.setVelocity(10);
		
		if (!sources.isEmpty()){
			Road r = sources.get(((int)(Math.random()*1000))%sources.size());
			int c = 0;
			while (!r.fits(v)){
				if (c > 10){
					return null;
				} else {
					c++;
				}
				r = sources.get(((int)(Math.random()*1000))%sources.size());
			}

			v.setPathList(createPathList(r));
			//System.out.println("adicionado");
			vehicles.add(v);
			return v;
		}
		return null;
	}
	
	public ArrayList<DynamicGraphicObject> getArray (){
		return vehicles;
	}	
	
	public void update (){
		double dt = clock.getDt();
		double dt2 = 0.1;
		while (dt > 0){
			
			dt -= dt2;
			
			if (dt <= 0){
				dt2 += dt;
			}
			
			for (DynamicGraphicObject d: new ArrayList<DynamicGraphicObject>(vehicles)){ //TODO: java.util.ConcurrentModificationException sincronizar... x(
				if (clock != null){
					d.move(dt2);
				}
			}
		}
	}
	
	public void draw (Graphics2D g, int mode){
		
		AffineTransform org = g.getTransform();
		AffineTransform t; 
		
		
		for (DynamicGraphicObject d: new ArrayList<DynamicGraphicObject>(vehicles)){ //TODO: java.util.ConcurrentModificationException sincronizar... x(

			t = new AffineTransform(org);
			t.translate(d.getX(), d.getY());
			g.setTransform(t);
			d.drawBg(g, mode);
			d.draw(g, mode);
			//g.drawImage(d.getImage(),t, null);
		}
			
		
	}

	public void setRoads(Map map) {
		
		StaticGraphicObject obj;
		
		for (int i = 0; i < map.getNRows(); i++){
			for (int j = 0; j < map.getNCols(); j++){
				obj = map.get(i, j);
				if (obj instanceof Street){
					if (((Street)obj).hasInput() ^ ((Street)obj).hasOutput()){
						if (!((Street)obj).hasInput()){
							addSource(((Street)obj));
						} else {
							addDrain(((Street)obj));
						}
					}
				}
			}
		}
	}

	public void clear() {
		synchronized (vehicles){
			vehicles.clear();
		}
	}
	
	public void setControl(TrafficDensityPanel trafficDP) {
		this.trafficDP = trafficDP;
	}
}
