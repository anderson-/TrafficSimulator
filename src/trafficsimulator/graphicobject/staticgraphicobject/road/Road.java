/** 
 * Road.java
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

import java.util.ArrayList;
import trafficsimulator.Clock;
import trafficsimulator.Clock.ClockListener;
import trafficsimulator.graphicobject.dynamicgraphicobject.DynamicGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.StaticGraphicObject;
import trafficsimulator.path.Path;

/**
 * Classe que define uma via para trafego de veiculos.
 */

public abstract class Road extends StaticGraphicObject implements ClockListener{
	
	private ArrayList<DynamicGraphicObject> vehicles;
	private int trafficFlow = 0;
	protected double trafficFlowPerTime = 0;
	
	public Road (){
		vehicles =  new ArrayList<DynamicGraphicObject>();
	}
	
	public abstract Path getRandPath (Road input);
	public abstract Road getOutput (Path p);
	public abstract Road getInput (Path p);
	public abstract Path [] getPathList ();
	public abstract boolean hasOutput();
	
	public synchronized void add (DynamicGraphicObject v){
		vehicles.add(v);
		trafficFlow+=2;
	}
	
	public synchronized void remove (DynamicGraphicObject v){
		vehicles.remove(v);
	}
	
	public synchronized ArrayList<DynamicGraphicObject> getVehicles (){
		return vehicles;
	}
	
	public synchronized int getNVehicles (){
		return vehicles.size();
	}
	
	public synchronized boolean isEmpty (){
		return vehicles.isEmpty();
	}
	
	public abstract boolean fits (DynamicGraphicObject obj);

	public synchronized void clear() {
		vehicles.clear();
		trafficFlow = 0;
	}
	
	public synchronized int getTraffic (){
		return trafficFlow;
	}
	
	@Override
	public void clockIncrease(Clock c) {
		trafficFlowPerTime = trafficFlow/((c.getElapsedMilis()+1)/1000.0);
		trafficFlowPerTime = (trafficFlowPerTime > 1)? 1 : trafficFlowPerTime;
	}
	
}
