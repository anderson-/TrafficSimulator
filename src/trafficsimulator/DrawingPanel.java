/** 
 * DrawingPanel.java
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import trafficsimulator.maphandler.MapHandler;

/**
 * Classe responsavel pela exibição dos gráficos e animações do simulador.
 */

public final class DrawingPanel extends JPanel {

	// buffers
	private BufferStrategy strategy;
	//private BufferedImage imgMap;
	// modos de exibição
	private ArrayList<MapHandler> avaliableModes;
	private MapHandler currentMode;
	private static boolean showMenuPopup = true;
	// etc..
	private boolean pause = false;

	public DrawingPanel(MapHandler defaultMapHandler) {
		//this.map = map;
		avaliableModes = new ArrayList<MapHandler>();

		setIgnoreRepaint(true);

		addHandler(defaultMapHandler);
		//tryChangeMode(defaultMapHandler);
		this.addHierarchyBoundsListener(new HierarchyBoundsListener() {
			@Override
			public void ancestorMoved(HierarchyEvent e) {
			}

			@Override
			public void ancestorResized(HierarchyEvent e) {
				System.out.println(e.getChangeFlags());
			}
		});
	}

	@Override
	public void paintComponent(Graphics g1) {
		//super.paintComponent(g1);
		if (!pause) {
			Graphics2D g = (Graphics2D) g1;
			int w, h;
			w = this.getWidth();
			h = this.getHeight();

			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, w, h);

			g.setClip(0, 0, w, h);

			// desenha a primeira camada
			currentMode.drawBg(g);

			// desenha a segunda camada
			currentMode.draw(g);

			// desenha informações e efeitos extras
			currentMode.drawExtra(g);

			g.dispose();

		}
	}

	public void loop() {
		while (true) {
			repaint();

			try {
				Thread.sleep(3);
			} catch (Exception e) {
			}
		}
	}

	public void pause(boolean state) {
		pause = state;
	}

	public boolean tryChangeMode(MapHandler sm) {

		if (sm != null && avaliableModes.contains(sm) && sm.allowModeSwap()) { //TODO: não é o MapEditor que não deixa sair é o Simulation que não deixa entrar!!
			for (MouseListener l : getMouseListeners()) {
				if (l instanceof MapHandler) {
					removeMouseListener(l);
				}
			}

			for (MouseMotionListener l : getMouseMotionListeners()) {
				if (l instanceof MapHandler) {
					removeMouseMotionListener(l);
				}
			}

			if (currentMode != null) {
				currentMode.modeExited();
			}

			currentMode = sm;
			addMouseListener(new PopupListener(currentMode.getPopupMenu()));
			addMouseListener(currentMode);
			addMouseMotionListener(currentMode);
			currentMode.modeEntered();
			return true;
		} else {
			return false;
		}
	}

	public void addHandler(MapHandler m) {
		if (!avaliableModes.contains(m)) {
			avaliableModes.add(m);
		}
	}

	public MapHandler getCurrentMode() {
		return currentMode;
	}

	public void addToolBars(JTabbedPane j) {
		for (MapHandler mode : avaliableModes) {
			mode.addContentPanelIn(j);
		}
	}

	public void setPopupMenusVisible(boolean val) {
		showMenuPopup = val;
	}

	public boolean isPopupMenusVisible() {
		return showMenuPopup;
	}

	private class PopupListener extends MouseAdapter {

		JPopupMenu popup;

		PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (showMenuPopup) {
				maybeShowPopup(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (showMenuPopup) {
				maybeShowPopup(e);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (showMenuPopup) {
				maybeShowPopup(e);
			}
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(),
						   e.getX(), e.getY());
			}
		}
	}
}
