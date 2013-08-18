/** 
 * SimulationMode.java
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

package trafficsimulator.maphandler.simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import trafficsimulator.maphandler.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import trafficsimulator.Map;
import trafficsimulator.graphicobject.dynamicgraphicobject.DynamicGraphicObject;
import trafficsimulator.trafficcontrol.VehicleManager;
import trafficsimulator.Clock;
import trafficsimulator.graphicobject.staticgraphicobject.StaticGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.road.Intersection;
import trafficsimulator.graphicobject.staticgraphicobject.road.Road;
import trafficsimulator.trafficcontrol.TrafficManager;

/**
 * Classe responsavel pela simulação do ambiente.
 */

public class SimulationMode extends MapHandler {
	
	private JPanel simulatorPanel;
	Map map;
	final VehicleManager vm;
	Clock clock;
	Font font;
	private int drawingMode = 1;
	private TrafficManager tm = TrafficManager.DEFAULT;
	
	public SimulationMode (){
		
		map = (Map)getMDM(Map.class);
		vm =  (VehicleManager)getMDM(VehicleManager.class);
		clock = (Clock)getMDM(Clock.class);
		
		
		vm.setClock(clock);
		vm.setControl(trafficDensityPanel);
		//simulationPanel.setLayout(new GridBagLayout());		
		//simulationPanel.setPreferredSize(new Dimension(100,100));
		
		//add(new TrafficDensityPanel());
		
		InputStream is = getClass().getResourceAsStream("/trafficsimulator/resources/acme5.ttf");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
		} catch (Exception ex) {
			font = Font.getFont(Font.SANS_SERIF);
		}
		
		
		initComponents();
		clock.addListener(trafficDensityPanel);
		trafficDensityPanel.setClock(clock);
		trafficDensityPanel.reset();
		vm.setControl(trafficDensityPanel);
	}
	
	Point mouse = new Point();
	
	@Override
	public void draw (Graphics2D g) {
		clock.increase();
		vm.update();
		
		AffineTransform t = g.getTransform();
		
		if (p != null){
			t.translate(g.getClipBounds().width/2 -p.getX(),g.getClipBounds().height/2 -p.getY());
			//g.setRenderingHints((new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)));
		
			Color c = new Color(1.0f,0,0,0.5f);
		
			g.setColor(c);
			g.fillOval((int)p.getX()-5, (int)p.getY()-5, 10, 10);
		} else {
			t.translate(map.getPosition().x, map.getPosition().y);
		}
		
		
		
		
		
		g.setTransform(t);
		map.draw(g, drawingMode);
		
		vm.draw(g, drawingMode);
		
		
		
		
//		AffineTransform t = new AffineTransform();
//		for (DynamicGraphicObject d: map.getVehicles()){ //TODO: java.util.ConcurrentModificationException sincronizar... x(
//			//mouse.translate((int)-d.getX(),(int)-d.getY());
//			
//			((BaseVehicle)d).setK((d.contains(map.getMouse()))? 1 : 0);
//			d.move(dt);
//			d.draw();
//			t.setToIdentity();
//			t.translate(d.getX(), d.getY());
//			g.drawImage(d.getImage(),t, null);
//			
//		}
	
	}
	
	@Override
	public void drawExtra(Graphics2D g){
		//g.setFont(font);
		//g.drawString("Seed: " + Math.random(), 10, 20);
		g.setTransform(new AffineTransform());
		
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//					
//		g.setColor(new Color (1.0f,0,0,0.5f));
//		g.fillRoundRect(200, 200, 200, 300,30,30);
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
//		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
//					
		
		g.setColor(Color.black);
		
		g.drawString("Clock: " + clock, 10, 20);
		//g.drawString("fps: " + (new Double(1000/dt)).shortValue() , 10, 60);
		
		
		ArrayList<DynamicGraphicObject> v = vm.getArray();
		
		for (int a = 0; a < v.size(); a++){
			g.drawString("["+ a + ", "+v.size()+"] velocity: " + (float)v.get(a).getVelocity()*3.6f + " km/h" , 10, 40+a*20);	
//			if (v[a].getVelocity() > 100){
//				v[a].setAceleration(-10);
//			} else if (v[a].getVelocity() < 10){
//				v[a].setAceleration(10);
//			}
		}
		
		synchronized (vm){
			for (DynamicGraphicObject o : vm.getArray()){
				if (o.getLocation().distance(map.getMouse()) < 60){
				//if (o.contains(map.getMouse())){
					o.setK(1);
					break;
				} else {
					o.setK(0);
				}
			}
		}
		
	}
	
	@Override
	public void modeEntered() {
		vm.clear();
		map.update(true);
		vm.setRoads(map);
		clock.reset();
		
		if (!clock.isAlive()){
			clock.start();
			clock.setSleep(200);
		}
		
		tm.init(map.getTrafficState());
		
		clock.addListener(tm);
		clock.pause(true);
		
		if (!vm.isAlive()){
			vm.start();
		}
		
		StaticGraphicObject obj;
		
		for (int i = 0; i < map.getNRows(); i++){
			for (int j = 0; j < map.getNCols(); j++){
				obj = map.get(i,j);
				if (obj instanceof Road){
					clock.addListener((Road)obj);
				}
			}
		}
	}

	@Override
	public void modeExited() {
		clock.pause(true);
		vm.clear();
	}
	
	@Override
	public void addContentPanelIn (JTabbedPane tabbedPane){
		tabbedPane.addTab("", new ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/input-gaming.png")), this,"Simulação");
	}
	
	@Override
	public JPopupMenu getPopupMenu(){
	
		JMenuItem menuItem;

        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("A popup menu item");
		
        popup.add(menuItem);
		
		menuItem = new JMenuItem("Track");
        menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
			  //System.out.println("Selected: " + actionEvent.getActionCommand());
			  synchronized (vm){
					for (DynamicGraphicObject o : vm.getArray()){
						if (o.getLocation().distance(map.getMouse()) < 30){
						//if (o.contains(map.getMouse())){
							p = o;
							break;
						} else {
							p = null;
						}
					}
				}
			}
		  });
		
        popup.add(menuItem);
        menuItem = new JMenuItem("Simulator ");
        //menuItem.addActionListener(this);                        <====
        popup.add(menuItem);
		
		popup.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//				System.out.println("GAINED FOCUS");
				clock.pause(true);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//				System.out.println("LOST FOCUS");
				clock.pause(false);
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		});

        return popup;
	}
	
	DynamicGraphicObject p = null;
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mouse = e.getPoint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		mouse = e.getPoint();
		
		if (tm == TrafficManager.MANUAL){
			StaticGraphicObject o = map.get(map.getMouse());
			if (o != null && o instanceof Intersection){
				((Intersection)o).getState().swap();
				System.out.println("op");
			}
		}
		
//		synchronized (vm){
//			for (DynamicGraphicObject o : vm.getArray()){
//				if (o.contains(map.getMouse())){
//					p = o;
//					break;
//				} else {
//					p = null;
//				}
//			}
//		}
	}

	
	
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbtnPlay = new javax.swing.JButton();
        jbtnStop = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jslSimulationV = new javax.swing.JSlider();
        jPanel4 = new javax.swing.JPanel();
        trafficDensityPanel = new trafficsimulator.maphandler.simulation.util.TrafficDensityPanel();
        jLabel1 = new javax.swing.JLabel();
        jcombTrafficControl = new javax.swing.JComboBox();
        jcombDrawMode = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        jTabbedPane1.setBorder(null);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Time"));

        jbtnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/media-playback-start.png"))); // NOI18N
        jbtnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPlayActionPerformed(evt);
            }
        });

        jbtnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/media-playback-stop.png"))); // NOI18N
        jbtnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnStopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnPlay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnStop)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnPlay)
                    .addComponent(jbtnStop))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Simulation Velocity "));

        jslSimulationV.setMaximum(30);
        jslSimulationV.setPaintLabels(true);
        jslSimulationV.setSnapToTicks(true);
        jslSimulationV.setToolTipText("Velocidade");
        jslSimulationV.setValue(1);
        jslSimulationV.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jslSimulationVStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jslSimulationV, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jslSimulationV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Traffic Density"));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(trafficDensityPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(trafficDensityPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jLabel1.setLabelFor(jcombTrafficControl);
        jLabel1.setText("Traffic Light Control:");
        jLabel1.setToolTipText("");

        jcombTrafficControl.setModel(new DefaultComboBoxModel(new Object[] {TrafficManager.DEFAULT, TrafficManager.SMART, TrafficManager.MANUAL}));
        jcombTrafficControl.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcombTrafficControlItemStateChanged(evt);
            }
        });

        jcombDrawMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Traffic Flow", "Traffic Jam" }));
        jcombDrawMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcombDrawModeActionPerformed(evt);
            }
        });

        jLabel2.setText("Draw:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(5, 5, 5)
                                .addComponent(jcombTrafficControl, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcombDrawMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcombTrafficControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcombDrawMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/preferences-system.png")), jPanel2); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 674, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 155, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/utilities-system-monitor.png")), jPanel3); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jslSimulationVStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jslSimulationVStateChanged
        clock.setRatio(((javax.swing.JSlider)evt.getSource()).getValue());
    }//GEN-LAST:event_jslSimulationVStateChanged

    private void jbtnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPlayActionPerformed
        clock.setPaused(!clock.isPaused());
		if (clock.isPaused()){
			((JButton)evt.getSource()).setIcon(new ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/media-playback-start.png")));
		} else {
			((JButton)evt.getSource()).setIcon(new ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/media-playback-pause.png")));
		}
    }//GEN-LAST:event_jbtnPlayActionPerformed

    private void jbtnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnStopActionPerformed
        clock.setPaused(true);
		vm.clear();
		map.update(true);
		clock.reset();
    }//GEN-LAST:event_jbtnStopActionPerformed

    private void jcombTrafficControlItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcombTrafficControlItemStateChanged
        //System.out.println("mudando para " + ((JComboBox)evt.getSource()).getSelectedItem() + " == " + evt.getItem());
		clock.removeListener(tm);
		tm = ((TrafficManager)evt.getItem());
		tm.init(map.getTrafficState());
		clock.addListener(tm);
    }//GEN-LAST:event_jcombTrafficControlItemStateChanged

    private void jcombDrawModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcombDrawModeActionPerformed
        drawingMode = jcombDrawMode.getSelectedIndex()+1;
    }//GEN-LAST:event_jcombDrawModeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbtnPlay;
    private javax.swing.JButton jbtnStop;
    private javax.swing.JComboBox jcombDrawMode;
    private javax.swing.JComboBox jcombTrafficControl;
    private javax.swing.JSlider jslSimulationV;
    private trafficsimulator.maphandler.simulation.util.TrafficDensityPanel trafficDensityPanel;
    // End of variables declaration//GEN-END:variables

	@Override
	public void drawBg(Graphics2D g) {
		
	}

	
}
