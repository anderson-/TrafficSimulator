/** 
 * MapEditorMode.java
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

package trafficsimulator.maphandler.mapeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import trafficsimulator.Map;
import trafficsimulator.graphicobject.staticgraphicobject.EmptyStaticGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.StaticGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.road.Street;
import trafficsimulator.maphandler.*;
import trafficsimulator.util.Compass;

/**
 * Classe responsavel pelo editor de mapa.
 */

public class MapEditorModeG extends MapHandler {

	private final boolean ROUTE = false;
	private final ArrayList<StaticGraphicObject> selected;
	Map map;
	
	public MapEditorModeG(){
		selected = new ArrayList<StaticGraphicObject>();
		map = (Map) getMDM(Map.class);
		initComponents();
	}
	
	@Override
	public void addContentPanelIn (JTabbedPane tabbedPane){
		tabbedPane.addTab("", new ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/applications-development.png")), this,"Estatisticas");
	}
	
	@Override
	public JPopupMenu getPopupMenu(){
	
		JMenuItem menuItem;

        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("A popup menu item");
        //menuItem.addActionListener(this);                        <====
        popup.add(menuItem);
        menuItem = new JMenuItem("estatisticas");
        //menuItem.addActionListener(this);                        <====
        popup.add(menuItem);
		
		menuItem = new JMenuItem("Adicionar nova Linha");
        menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				addRow = !addRow;
			}
		  });
		popup.add(menuItem);
		
        menuItem = new JMenuItem("Adicionar nova Coluna");
        menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				addCol = !addCol;
			}
		  });
		popup.add(menuItem);
		
        return popup;
	}
	
	Point mouse = new Point();

	@Override
	public void modeEntered(){
		map.update(ROUTE);
		map.setPosition(new Point(100,100));
	}
	
	boolean addRow = false;
	boolean addCol = false;
	boolean selectRow = false;
	boolean selectCol = false;
	
	int selectedRow = -1;
	int selectedCol = -1;
	
	@Override
	public void draw (Graphics2D g){
		
		g.setTransform(new AffineTransform());
		g.translate(map.getPosition().x, map.getPosition().y);
		
		Point2D mousePos = map.getMouse();
		
		if (map.getBounds().contains(mousePos)){
		
			if (selectRow){
				
				int [] h = map.getHeightArray();
				int [] h2 = map.getHeightArray();
				for (int a = h.length-1; a >= 0; a--){
					for (int b = 0; b < a; b++){
						h[a] += h[b];
					}
				}

				for (int a = 0; a < h.length; a++){
					if (h[a] > mousePos.getY()-10 && h[a] > mousePos.getY()+10){
						g.drawRect(0, ((a-1 < 0)? 0 : h[a-1]), map.getWidth(), h2[a]);
						selectedRow = a;
						System.out.println("a"+a);
						break;
					} else {
						selectedRow = -1;
					}
				}
			}

			if (selectCol){
				
				int [] w = map.getWidthArray();
				int [] w2 = map.getWidthArray();
				for (int a = w.length-1; a >= 0; a--){
					for (int b = 0; b < a; b++){
						w[a] += w[b];
					}
				}

				for (int a = 0; a < w.length; a++){
					if (w[a] > mousePos.getX()-10 && w[a] > mousePos.getX()+10){
						g.drawRect(((a-1 < 0)? 0 : w[a-1]), 0, w2[a], map.getHeight());
						selectedCol = a;
						break;
					} else {
						selectedCol = -1;
					}
				}

			}

			if (addRow || addCol){

				g.setStroke(new BasicStroke(2));
				g.setColor(Color.green);

				int [] w = map.getWidthArray();
				for (int a = w.length-1; a >= 0; a--){
					for (int b = 0; b < a; b++){
						w[a] += w[b];
					}
				}
				int [] h = map.getHeightArray();
				for (int a = h.length-1; a >= 0; a--){
					for (int b = 0; b < a; b++){
						h[a] += h[b];
					}
				}

				if (addRow){
					for (int a = 0; a < h.length-1; a++){
						if (h[a] > mousePos.getY()-10 && h[a] < mousePos.getY()+10){
							g.drawLine(5, h[a], w[w.length-1]-5, h[a]);
							selectedRow = a;
							break;
						} else {
							selectedRow = -1;
						}
					}
				}

				if (addCol){
					for (int a = 0; a < w.length-1; a++){
						if (w[a] > mousePos.getX()-10 && w[a] < mousePos.getX()+10){
							g.drawLine(w[a], 5, w[a], h[h.length-1]-5);
							selectedCol = a;
							break;
						} else {
							selectedCol = -1;
						}
					}
				}
			}
		}
	}

	public static final int ARR_SIZE = 6;

	public static void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
		g.setStroke(new BasicStroke(2));
					
		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx*dx + dy*dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);
		
		// Draw horizontal arrow starting in (0, 0)
		g.drawLine(0, 0, len-ARR_SIZE-1, 0);
		g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
					  new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
		at.setToIdentity();
		g.transform(at);
	}
	
	boolean q = false;
	Compass c = null;
	
	@Override
	public void drawExtra(Graphics2D g) {
		
		StaticGraphicObject o = map.get(map.getMouse());
		
		if (o != null && false){
			Point teste = new Point();
			teste.setLocation(map.getPosition().x + (o.getX()+o.getWidth()/2)*map.getZoom(),map.getPosition().y + (o.getY()+o.getHeight()/2)*map.getZoom());
			
			g.setColor(Color.red);
			g.fillOval(teste.x-5, teste.y-5, 10, 10);
		}
		
		synchronized (selected){
			if (selected.size() == 1){
				g.setColor(Color.magenta);
				StaticGraphicObject s = selected.get(0);
				
				
				
				g.draw(s.getShape());
				
				Rectangle rec = s.getShape().getBounds();
				Point2D mousePos = map.getMouse();
				
				Point2D side = null;
				Point2D mid = new Point2D.Double(rec.getX() + (rec.getWidth()/2), rec.getY() + (rec.getHeight()/2));
				Point2D [] pts = new Point2D [4];

				pts[0] = new Point2D.Double(rec.getX() + rec.getWidth()/2, rec.getY());
				pts[1] = new Point2D.Double(rec.getX() + rec.getWidth()/2, rec.getY() + rec.getHeight());
				pts[2] = new Point2D.Double(rec.getX(), rec.getY() + rec.getHeight()/2);
				pts[3] = new Point2D.Double(rec.getX() + rec.getWidth(), rec.getY() + rec.getHeight()/2);

				for (Point2D pt : pts){
					//g.fillRect((int)(pt.getX()-5),(int) (pt.getY()-5), 10, 10);

					if (side == null || mousePos.distance(pt) < mousePos.distance(side)){
						side = pt;
					} else if (mousePos.distance(pt) == mousePos.distance(side)) {
						//return null;
					}
				}

				double x = (side.getX() - mid.getX());
				double y = (side.getY() - mid.getY());

				x /= (x == 0)? 1: Math.abs(x);
				y /= (y == 0)? 1: Math.abs(y);
				
				g.drawRect((int)(mid.getX()-5),(int) (mid.getY()-5), 10, 10);
					
				if (rec.contains(mousePos)){
					g.setColor(Color.blue);
					//drawArrow(g,(int)(mid.getX() + x*5), (int)(mid.getY() + y*5), (int)(side.getX() - x*5), (int)(side.getY() - y*5));
					drawArrow(g,(int)(mid.getX() + x*8), (int)(mid.getY() + y*8), (int)(side.getX() - x*2), (int)(side.getY() - y*2));
					q = true;
					c = Compass.getCompass((int) x, (int) y);
				} else {
					q = false;
					c = null;
				}
				
//				getCardinal((int) x, (int) y);
				
				
				
				
//				
//				Rectangle shape = s.getShape().getBounds();
//				
//				Point2D h = Cardinal.getAprox(shape, map.getMouse(), g);
//				g.setColor(Color.green);
//				g.fillRect((int)(h.getX()-5),(int) (h.getY()-5), 10, 10);
//				
				//posição do centro
//				
//				int x = (int) (map.getPosition().x + (s.getX() + s.getWidth()/2)*map.getZoom());
//				int y = (int) (map.getPosition().y + (s.getY() + s.getHeight()/2)*map.getZoom());
//				
//				int a = x;
//				int b = y;
//				
//				g.drawRect(x-5, y-5, 10, 10);
//				
//				int amp = 15;
//				
//				x = (int)(mouse.x - x);
//				y = (int)(mouse.y - y);
//				float dist = (float) Math.sqrt(x*x + y*y);
//				
//				float f = (Math.abs(x) < Math.abs(y))? x : y;
//				f = Math.abs(f);
//				f = (f < amp)? (amp-f)/amp : 1;
//				
//				Color color = new Color(1,0.6f,0,f);
//				
//				if (x > amp || x < -amp){
//					x = x/Math.abs(x);
//				} else {
//					x = 0;
//				}
//				
//				if (y > amp || y < -amp){
//					y = y/Math.abs(y);
//				} else {
//					y = 0;
//				}
//				
//				if ((x != 0) ^ /*XOR*/ (y != 0) && dist < s.getWidth()/2 && dist < s.getHeight()/2){
//					g.setColor(color);
//					//g.fillOval(a+x*40-5, b+y*40-5, 10, 10);
//					
//					q = true;
//					
//					//System.out.println(Cardinal.getCardinal(y, x).name());
//					c = Cardinal.getCardinal(x, y);
//					//drawArrow(g,a, b, (int)(a+x*s.getWidth()/2*map.getZoom()), (int)(b+y*s.getHeight()/2*map.getZoom()));
//					//g.drawLine(a, b, (a+x*30), (b+y*30));
//				} else {
//					q = false;
//					c = null;
//				}
//
			} else {
				q = false;
				for (StaticGraphicObject s : selected){
					g.setColor(Color.yellow);
					g.draw(s.getShape());
					g.drawRect((int)((s.getX() + s.getWidth()/2)*map.getZoom())-5, (int)((s.getY() + s.getHeight()/2)*map.getZoom())-5, 10, 10);
				}
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (selectedRow != -1){
			map.addRow(selectedRow+1);
			map.update(ROUTE);
			selectedRow = -1;
			addRow = false;
		}
		
		if (selectedCol != -1){
			map.addCol(selectedCol+1);
			map.update(ROUTE);
			selectedCol = -1;
			addCol = false;
		}
		
		mouse = e.getPoint();
		StaticGraphicObject o = map.get(map.getMouse());
		synchronized (selected){
			if (o == null){
				selected.clear();
			} else if (e.isShiftDown()){
				if (!selected.isEmpty()){
					StaticGraphicObject obj1 = selected.get(0);
					int i1 = obj1.getI();
					int j1 = obj1.getJ();
					int i2 = o.getI();
					int j2 = o.getJ();
					
					if (i2 < i1){
						int t = i1;
						i1 = i2;
						i2 = t;
					}
					
					if (j2 < j1){
						int t = j1;
						j1 = j2;
						j2 = t;
					}
					
					selected.clear();
					
					for (int i = i1; i <= i2; i++){
						for (int j = j1; j <= j2; j++){
							selected.add(map.get(i, j));
						}
					}
				}
			} else if (e.isControlDown()){
				if (e.isAltDown()){
					map.replace(o, new EmptyStaticGraphicObject());
					map.update(ROUTE);
					if (e.getButton() == MouseEvent.BUTTON2){
						
					}
				} else {
					if (o != null){
						if (selected.contains(o)){
							selected.remove(o);
						} else {
							selected.add(o);
						}
					}
				}
			} else {
				if (!selected.isEmpty()){
					if (selected.contains(o)){
						if (q){
							System.out.println("o " + o );
							map.replace(o, new Street(c));
							map.update(ROUTE);
						}
						selected.clear();
					} else {
						selected.clear();
						selected.add(o);
					}
				} else {
					selected.add(o);
				}
				
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mouse = e.getPoint();
	}
	
	public static String deserializeString(File file)
		throws IOException {
			int len;
			char[] chr = new char[4096];
			final StringBuffer buffer = new StringBuffer();
			final FileReader reader = new FileReader(file);
			try {
				while ((len = reader.read(chr)) > 0) {
					buffer.append(chr, 0, len);
				}
			} finally {
				reader.close();
			}
			return buffer.toString();
		}


	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup2 = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser2 = new javax.swing.JFileChooser();
        jpnlMap = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnLoad = new javax.swing.JButton();
        jbtnSave = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jtbtnSelectRows = new javax.swing.JToggleButton();
        jtbtnSelectColums = new javax.swing.JToggleButton();
        jbtnDelete = new javax.swing.JButton();
        jbtnDH = new javax.swing.JButton();
        jbtnDV = new javax.swing.JButton();
        jbtnClear = new javax.swing.JButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();

        jpnlMap.setBorder(javax.swing.BorderFactory.createTitledBorder("Map"));

        jbtnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/document-new.png"))); // NOI18N
        jbtnNew.setText("New");
        jbtnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNewActionPerformed(evt);
            }
        });

        jbtnLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/document-open.png"))); // NOI18N
        jbtnLoad.setText("Load");
        jbtnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLoadActionPerformed(evt);
            }
        });

        jbtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/media-floppy.png"))); // NOI18N
        jbtnSave.setText("Save");
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnlMapLayout = new javax.swing.GroupLayout(jpnlMap);
        jpnlMap.setLayout(jpnlMapLayout);
        jpnlMapLayout.setHorizontalGroup(
            jpnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMapLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnlMapLayout.setVerticalGroup(
            jpnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlMapLayout.createSequentialGroup()
                .addComponent(jbtnNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnLoad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnSave))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Selection"));

        jtbtnSelectRows.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/rows.png"))); // NOI18N
        jtbtnSelectRows.setText("Rows");
        jtbtnSelectRows.setEnabled(false);
        jtbtnSelectRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbtnSelectRowsActionPerformed(evt);
            }
        });

        jtbtnSelectColums.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/cols.png"))); // NOI18N
        jtbtnSelectColums.setText("Colums");
        jtbtnSelectColums.setEnabled(false);
        jtbtnSelectColums.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbtnSelectColumsActionPerformed(evt);
            }
        });

        jbtnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/emblem-unreadable.png"))); // NOI18N
        jbtnDelete.setText("Delete");
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });

        jbtnDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/go-bottom.png"))); // NOI18N
        jbtnDH.setText("Duplicate Horizontally");
        jbtnDH.setEnabled(false);
        jbtnDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDHActionPerformed(evt);
            }
        });

        jbtnDV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/go-last.png"))); // NOI18N
        jbtnDV.setText("Duplicate Verically");
        jbtnDV.setEnabled(false);
        jbtnDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDVActionPerformed(evt);
            }
        });

        jbtnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trafficsimulator/resources/extern_icons/tango/edit-clear.png"))); // NOI18N
        jbtnClear.setText("Clear");
        jbtnClear.setEnabled(false);
        jbtnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnClearActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setText("Union");
        jRadioButton3.setEnabled(false);

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("Intersection");
        jRadioButton4.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbtnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnDelete)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jtbtnSelectRows)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnDH))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jtbtnSelectColums)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnDV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton3)
                            .addComponent(jRadioButton4))
                        .addContainerGap(29, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtbtnSelectRows)
                    .addComponent(jbtnDH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtbtnSelectColums)
                    .addComponent(jbtnDV))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnClear)
                    .addComponent(jbtnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton4)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Object Properties"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(604, 604, 604)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(111, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpnlMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jpnlMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpnlMap.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
        map.clear();
		map.update(ROUTE);
    }//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLoadActionPerformed
        
		int returnVal = jFileChooser1.showOpenDialog(jPanel1);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File file = jFileChooser1.getSelectedFile();
				String data = deserializeString(file);
				map.load(data);
			} catch (IOException ex) {
				
			}
			
        }
		
		map.update(ROUTE);
    }//GEN-LAST:event_jbtnLoadActionPerformed

    private void jtbtnSelectRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbtnSelectRowsActionPerformed
        if (jtbtnSelectRows.isSelected()){
			jtbtnSelectColums.setSelected(false);
			selectCol = false;
			selectRow = true;
		} else {
			selectRow = false;
		}
    }//GEN-LAST:event_jtbtnSelectRowsActionPerformed

    private void jtbtnSelectColumsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbtnSelectColumsActionPerformed
        if (jtbtnSelectColums.isSelected()){
			jtbtnSelectRows.setSelected(false);
			selectRow = false;
			selectCol = true;
		} else {
			selectCol = false;
		}
    }//GEN-LAST:event_jtbtnSelectColumsActionPerformed

    private void jbtnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnClearActionPerformed
        synchronized (selected){
			selected.clear();
			jbtnClear.setEnabled(false);
		}
    }//GEN-LAST:event_jbtnClearActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed
        synchronized (selected){
			for (StaticGraphicObject obj : selected){
				map.replace(obj, new EmptyStaticGraphicObject());
			}
			selected.clear();
		}
		jbtnDelete.setEnabled(false);
    }//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnDHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnDHActionPerformed

    private void jbtnDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnDVActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
        int returnVal = jFileChooser2.showSaveDialog(jPanel1);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File file = jFileChooser2.getSelectedFile();
				
				FileWriter writer = new FileWriter(file);
				writer.write(map.save());
				writer.flush();
				
			} catch (IOException ex) {
				
			}
			
        }
    }//GEN-LAST:event_jbtnSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JButton jbtnClear;
    private javax.swing.JButton jbtnDH;
    private javax.swing.JButton jbtnDV;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnLoad;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JPanel jpnlMap;
    private javax.swing.JToggleButton jtbtnSelectColums;
    private javax.swing.JToggleButton jtbtnSelectRows;
    // End of variables declaration//GEN-END:variables

	@Override
	public void drawBg(Graphics2D g) {
		g.setColor(Color.red);
		g.translate(map.getPosition().x,map.getPosition().y);
		map.draw(g, 0);
	}

	@Override
	public void modeExited() {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	
}
