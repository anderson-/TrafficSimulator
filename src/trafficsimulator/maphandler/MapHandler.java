/** 
 * MapHandler.java
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

package trafficsimulator.maphandler;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import trafficsimulator.DrawingPanel;
import trafficsimulator.Map;

/**
 * Classe abstrata para modos de exibição e controle de {@link trafficsimulator.DrawingPanel}.
 * <p>
 * Implementações dessa Classe abstrata podem definir, por exemplo, como
 * {@link trafficsimulator.CityBlock} ou {@link trafficsimulator.Car}
 * são desenhados em {@link trafficsimulator.Map}. Também representam uma
 * aba no painel de controle do simulador.
 */

public abstract class MapHandler extends JPanel implements MouseListener, MouseMotionListener/* implements ChangeListener*/{

	private static HashMap<Class<?>,Object> mainDataManagers = null;
	
	public static void setMainDataManagers(Object ... mdms) {
		if (mainDataManagers == null){
			mainDataManagers = new HashMap<Class<?>,Object>();
			for (Object mdm : mdms){
				mainDataManagers.put(mdm.getClass(), mdm);
			}
		} else {
			throw new IllegalStateException("Main Data Managers already setted.");
		}
	}
	
	protected static Object getMDM (Class<?> c){
		return mainDataManagers.get(c);
	}
	
	public abstract void addContentPanelIn(JTabbedPane tabbedPane);
	
	public abstract JPopupMenu getPopupMenu();

	public abstract void draw(Graphics2D g);
	
	public abstract void drawBg(Graphics2D g);
	
	public abstract void drawExtra(Graphics2D g);
	
	public abstract void modeEntered();
	
	public abstract void modeExited();
	
	public boolean allowModeSwap() { return true; }
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}

}
