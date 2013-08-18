/** 
 * Clock.java
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

package trafficsimulator;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Classe responsavel por gerenciar o tempo da simulação e eventos.
 */

public class Clock extends Thread{

	private int d;
	private int h;
	private int m;
	private int s;
	private int ms;
	private long lastMs;
	private long dt;
	private boolean paused = true;
	private double ratio;
	private long ems;
	private ArrayList<Alarm> alarms;
	private final ArrayList<ClockListener> listeners;
	private int sleep;

	public Clock(int d, int h, int m, int s, int ms, double ratio) {
		this.d = d;
		this.h = h;
		this.m = m;
		this.s = s;
		this.ms = ms;
		this.ratio = ratio;
		ems = 0;
		alarms = new ArrayList<Alarm>();
		listeners = new ArrayList<ClockListener>();
	}

	public Clock() {
		this(0, 0, 0, 0, 0, 1.0);
	}

	public void increase() {
		if (!paused) {
			dt = System.currentTimeMillis() - lastMs;
			lastMs = System.currentTimeMillis();
			ms += (long) (dt * ratio);
			ems += (long) (dt * ratio);
			
			synchronized (alarms){
				for (Alarm a : alarms){
					int t = (int) ((ems - a.ms) / a.value);

					a.counter += t;
					if (t > 0) {
						a.ms = ems;
						a.set(true);
					}

				}
			}
			
			if (ms >= 1000) {
				s += ms / 1000;
				ms = ms % 1000;
				if (s >= 60) {
					m += s / 60;
					s = s % 60;
					if (m >= 60) {
						h += m / 60;
						m = m % 60;
						if (h >= 24) {
							d += h / 24;
							h = h % 24;
						}
					}
				}
			}
		}
	}
	
	
	@Override
	public void run() {
		while (!this.isInterrupted()){
			if (!paused){
				synchronized (listeners){
					for (ClockListener cl : listeners){
						cl.clockIncrease(this);
					}
				}
			}
			try {
				Thread.sleep(sleep);
			} catch (Exception ex) {
				
			}
		}
	}

	public void reset() {
		paused = false;
		lastMs = System.currentTimeMillis();
		d = h = m = s = ms = 0;
		ems = 0;
	}

	public void pause(boolean p) {
		paused = p;
		lastMs = System.currentTimeMillis();
	}

	public double getDt() {
		return (paused) ? 0 : dt * ratio / 1000.0;
	}

	public int[] getDate() {
		return new int[]{s, m, h, d};
	}

	public int getDay() {
		return d;
	}

	public void setDay(int d) {
		this.d = d;
	}

	public int getHour() {
		return h;
	}

	public void setHour(int h) {
		this.h = h;
	}

	public int getMinute() {
		return m;
	}

	public void setMinute(int m) {
		this.m = m;
	}

	public int getSecond() {
		return s;
	}

	public void setSecond(int s) {
		this.s = s;
	}

	public int getMs() {
		return ms;
	}

	public void setMs(int ms) {
		this.ms = ms;
	}
	
	public long getElapsedMilis (){
		return ems;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
		if (paused) {
			dt = 0;
			lastMs = 0;
		} else {
			dt = 1;
			lastMs = System.currentTimeMillis();
		}
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	
	public void addListener (ClockListener c){
		synchronized (listeners){
			listeners.add(c);
		}
	}
	
	public synchronized void removeListener (ClockListener c){
		synchronized (listeners){
			listeners.remove(c);
		}
	}

	@Override
	public String toString() {
		String str = "Clock{" + "d=" + d + ", h=" + h + ", m=" + m + ", s=" + s + ", ms=" + ms + ", dt=" + dt + " frames: " + (int)(1000.0f/dt) + ", paused=" + paused + ", ratio=" + ratio + '}';

		synchronized (alarms){
			for (Alarm a : alarms){
				str += "\n >>" + a;
			}
		}

		return str;
	}

	public void setSleep(int i) {
		sleep = i;
	}
	
	public interface ClockListener {
		
		public void clockIncrease (Clock c);
		
	}

	public class Alarm {
		public long value = 0;
		public long ms = 0;
		private boolean on = false;
		private int counter = 0;
		private Method method;
		private Object methodSource;
		
		public Alarm (int ms){
			value = ms;
		}
		
		public Alarm (int ms, int s){
			this(ms + s*1000);
		}
		
		public Alarm (int ms, int s, int m){
			this(ms,s + m*60);
		}
		
		public Alarm (int ms, int s, int m, int h){
			this(ms,s,m + h*60);
		}
		
		public Alarm (int ms, int s, int m, int h, int d){
			this(ms,s,m,h + d*24);
		}
		
		private void set (boolean state){
			on = state || on;
			if (state && method != null){
				try {
					method.invoke(methodSource);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("fail");
				}
			}
		}
		
		public boolean get (){
			if (on){
				on = false;
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			return "Alarm{" + "value=" + value + ", ms=" + ms + ", on=" + on + ", counter=" + counter + ", method=" + method + ", methodSource=" + methodSource + '}';
		}
		
		public int getCout (){
			return counter;
		}
		
		public void setMethod (Method m, Object source){
			method = m;
			methodSource = source;
		}
		
		public ArrayList<Alarm> t (){
			return alarms;
		}
		
	}
	
	
}
	
	
	
//	
//	public static void main (String [] args){
//		
//		Clock clock = new Clock(3);
//		
//		int a = clock.addTimer(1000);
//		int b = clock.addTimer(500);
//		int c = clock.addTimer(250);
//		
//		clock.start();
//		while (true){
//			clock.increase();
//			
//			if (clock.getTimer(a)){
//				System.out.println("a: " + clock.getTimerCount(a));
//			}
//			
//			if (clock.getTimer(b)){
//				System.out.println("b: " + clock.getTimerCount(b));
//			}
//			
//			if (clock.getTimer(c)){
//				System.out.println("c: " + clock.getTimerCount(c));
//			}
//			
//			//System.out.println(clock.getMs());
//			
//			try {
//				Thread.sleep(5);
//			} catch (Exception e){
//				
//			}
//		}
//	}

	
