/** 
 * TrafficDensityPanel.java
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

package trafficsimulator.maphandler.simulation.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import trafficsimulator.Clock;
import trafficsimulator.Clock.ClockListener;

/**
 * Classe que define o painel que controla a densidade (carros/hora) da simulação.
 */

public class TrafficDensityPanel extends JPanel implements ClockListener{

	public static final int NSEGMENTS = 24*6;
	
	private TrafficDensityIntPanel trafficDensityIntPanel;
	private TrafficDensityProgressBar progressBar;
	
	private int timePos = 0;
	private int hours = 0;
	private int minutes = 0;
	private Clock clock;
	
	public TrafficDensityPanel(){
		trafficDensityIntPanel = new TrafficDensityIntPanel();
		trafficDensityIntPanel.init();
		progressBar = new TrafficDensityProgressBar(0,1440);
		progressBar.setStringPainted(true);
		progressBar.setString("00:00:00");
		progressBar.setValue(0);
		progressBar.init();
		GridBagConstraints cons = new GridBagConstraints(); 
		setLayout(new GridBagLayout());
		
		/*
		cons.gridy = 0;  
		cons.gridx = 0;
		cons.gridheight = 1;
		cons.gridwidth = 1;
		cons.weightx = 0.75;
		cons.weighty = 0.75;
		cons.insets = new Insets(4,4,4,4); 
        */       
        
              

		cons.gridx = 1;
		
		cons.gridy = 0;

        add(new JLabel("-max"), cons);  

		cons.gridy = 1;
        
        cons.anchor = GridBagConstraints.SOUTH;
        
        add(new JLabel("-min"), cons);  

		cons.gridy = 2;
		
		//cons.insets = new Insets(4,4,4,4);
		cons.fill = GridBagConstraints.BOTH;
		
		JButton reset = new JButton("X");
		
		reset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				trafficDensityIntPanel.reset();
			}
		});
		
		
		add(reset, cons);
		
		//cons.insets = new Insets(0,0,0,0);
		
		cons.gridx = 0;
		
		add(progressBar, cons);
        
        cons.gridy = 0;
        
        cons.gridheight = 2;
		cons.gridwidth = 1;
		
		cons.fill = GridBagConstraints.BOTH;
        
        cons.weightx = 1;
		cons.weighty = 1; 
		
		add(trafficDensityIntPanel, cons);
	}
	
	public void setClock (Clock c){
		clock = c;
	}

	@Override
	public void clockIncrease(Clock c) {
		int h = c.getHour();
		int m = c.getMinute();
		int s = c.getSecond();
		
		progressBar.setString(((h < 10)? "0" : "") + h + ":" + ((m < 10)? "0" : "") + m + ":" + ((s < 10)? "0" : "") + s);
		progressBar.setValue(c.getHour()*60+c.getMinute());
		
		timePos = (int)(((h*60.0+m)/(24*60))*NSEGMENTS);
		hours = h;
		minutes = m;
		
		trafficDensityIntPanel.repaint();
	}

	public double getValue() {
		return trafficDensityIntPanel.getValue(timePos);
	}

	public void reset (){
		trafficDensityIntPanel.reset();
	}
	
	private class TrafficDensityProgressBar extends JProgressBar implements MouseListener, MouseMotionListener{
		
		public TrafficDensityProgressBar (int min, int max){
			super(min,max);
		}
		
		public void init (){
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		
		private void setVal(MouseEvent e) {
			int min = (int)(e.getPoint().getX()/((double)this.getWidth()/1440));
			
			if (min >= 0 && min < 1440){
				int hor = min/60;
				min -= hor*60;
				
				clock.setHour(hor);
				clock.setMinute(min);
				
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			setVal(e);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			setVal(e);
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		
	}		
	private class TrafficDensityIntPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{

		private Rectangle [] rect;
		private double v [];
		public TrafficDensityIntPanel(){
			setPreferredSize(new Dimension(100,100));
			rect = new Rectangle[NSEGMENTS];
			for (int i = 0; i < NSEGMENTS; i++){
				rect[i] = new Rectangle();
			}
			v = new double [NSEGMENTS];
			reset();
		}
		
		
		public final void reset(){
			double theta = Math.random();
			double y = Math.random();
			for (int i = 0; i < NSEGMENTS; i++){
				v[i] = (1 - y) + y*(0.5 - Math.cos((i/(double)NSEGMENTS)*theta*10*Math.PI)/2);
			}
			repaint();
		}
		
		public void init(){
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		
		@Override
		public void paintComponent(Graphics g){
			
			int width = this.getWidth()/NSEGMENTS;
			int height = this.getHeight();
			
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setColor(g2.getBackground());
			
			g2.fillRect(0,0,this.getWidth(),this.getHeight());
			
			for (int i = 0; i < NSEGMENTS; i++){
				rect[i].setBounds(i*width+(this.getWidth()%NSEGMENTS)/2,height-(int)(v[i]*getHeight()),width,(int)(v[i]*getHeight()));
				Color color;
				if (timePos == i){
					color = new Color(50,174,255);
				} else {
					//100 -> 0
					
					//Color color = new Color((int)((double)h[i]/height*254.0), 140, 70); //x,138,70 
					color = Color.getHSBColor((100.0f-((float)v[i]*getHeight()/height*100.0f))/360.0f, 1, 1);
					//color = new Color(255,161,50);
				}
				g2.setPaint(color);
				g2.fill(rect[i]);
			}
			g2.setColor(Color.BLACK);
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawString(String.format("%.2f", v[timePos]), this.getWidth()/2,this.getHeight()/2);
			
		}
		
		public double getValue (int i){
			if (i >= 0 && i < NSEGMENTS){
				return v[i];
			}
			throw new IllegalArgumentException();
		}
		
		private void setVal(MouseEvent e){
			int i = (int)((e.getPoint().getX() - (this.getWidth()%NSEGMENTS)/2)/(this.getWidth()/NSEGMENTS));
			
			if (i >= 0 && i < NSEGMENTS && e.getPoint().getY() >= 0 && e.getPoint().getY() <= this.getHeight()){
				v[i] = (this.getHeight() - e.getPoint().getY())/this.getHeight();
//				h[i] = (int) (this.getHeight() - e.getPoint().getY());
//				h[i] = (h[i] >= this.getHeight())? this.getHeight() : h[i];
//				h[i] = (h[i] < 0)? 0 : h[i];
				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			setVal(e);
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			setVal(e);
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {

		}

	}

}
