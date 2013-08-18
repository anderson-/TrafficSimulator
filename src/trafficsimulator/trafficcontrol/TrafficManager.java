/** 
 * TrafficManager.java
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

import java.util.ArrayList;
import trafficsimulator.Clock;
import trafficsimulator.Clock.ClockListener;
import trafficsimulator.Map.MapTLS;
import trafficsimulator.graphicobject.staticgraphicobject.road.Intersection.IntersectionTLS;

/**
 * Classe responsavel por controlar os semaforos e cruzamentos.
 */

public abstract class TrafficManager implements ClockListener{
	
	public static final TrafficManager DEFAULT = new Default();
	public static final TrafficManager SMART = new Smart();
	public static final TrafficManager MANUAL = new Manual();
	
	private MapTLS mtls;
	
	public synchronized void init (MapTLS mtls){
		this.mtls = mtls;
	}
	
	public synchronized MapTLS getMapTLS (){
		return mtls;
	}
	
	protected static class Default extends TrafficManager{
		
		public int GREEN_TIME = 4;
		public int YELLOW_TIME = 2;
		
		@Override
		public void clockIncrease(Clock c) {
			
			int state = ((int)(c.getElapsedMilis()/1000.0))%((GREEN_TIME+YELLOW_TIME)*2);
			
			for (ArrayList<IntersectionTLS> a : getMapTLS().getMatrix()){
				for (IntersectionTLS i : a){
					switch (state){
						case 1:
						case 2:
						case 3:
						case 4:
							i.setH(TrafficLight.State.GREEN);
							i.setV(TrafficLight.State.RED);
							break;
						case 5:
						case 6:
							i.setH(TrafficLight.State.YELLOW);
							i.setV(TrafficLight.State.RED);
							break;
						case 7:
						case 8:
						case 9:
						case 10:
							i.setH(TrafficLight.State.RED);
							i.setV(TrafficLight.State.GREEN);
							break;
						case 11:
						case 0:
							i.setH(TrafficLight.State.RED);
							i.setV(TrafficLight.State.YELLOW);
							break;
					}
				}
			}
			
		}
		
		@Override
		public String toString (){
			return "Default";
		}
		
	}
	
	protected static class Smart extends TrafficManager{

		@Override
		public void clockIncrease(Clock c) {
			System.out.println("Not supported yet.");
		}
		
		@Override
		public String toString (){
			return "Smart";
		}

	}
	
	
	protected static class Manual extends TrafficManager{

		@Override
		public void clockIncrease(Clock c) {
			// nada
		}
		
		@Override
		public String toString (){
			return "Manual";
		}

	}
	
}
