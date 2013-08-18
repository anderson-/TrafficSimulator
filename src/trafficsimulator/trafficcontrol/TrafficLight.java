/** 
 * TrafficLight.java
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

import java.awt.Color;
import trafficsimulator.util.BadLuckException;

/**
 * Classe que define um semaforo.
 */

public class TrafficLight extends Sign {
	
	public static enum State {
		RED(0,0,Color.RED),
		YELLOW(1,0,Color.YELLOW),
		ATENTION(1,0.5f,Color.YELLOW),
		GREEN(2,0,Color.GREEN);
		
		private final int index;
		private final float blink;
		private final Color color;
		
		private State (int index, float blink, Color color){
			this.index = index;
			this.blink = blink;
			this.color = color;
			if (index >= N_STATES){
				throw new BadLuckException("pisição invalida!");
			}
		}
		
		public State getOposite(){
			
			if (this == GREEN){
				return RED;
			} else {
				return GREEN;
			}
			
		}
	}
	
	public static final int N_STATES = 3;
	
	private State state = State.GREEN;
	/**
	 * Construtor Padrão (descrição)
	 *
	 * @param  variavel1 descrição da variavel
	 * @param  variavel2 descrição da variavel
	 */
	
	public TrafficLight (){
		super(0);
//		if (((int)(Math.random()*10))%2 == 0){
//			this.state = State.GREEN;
//		} else {
//			this.state = State.RED;
//		}
//		
	}
	
	public TrafficLight (State s){
		this();
		state = s;
	}
	
	public State getState (){
		return state;
	}
	
	public void setState (State s){
		state = s;
	}
	
	public Color getColor (){
		return state.color;
	}
}
	
