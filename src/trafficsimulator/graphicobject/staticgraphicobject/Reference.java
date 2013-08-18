/** 
 * Reference.java
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

package trafficsimulator.graphicobject.staticgraphicobject;

import java.awt.Graphics2D;

/**
 * Classe que define uma  referencia para um objeto. Utilizada principalmente
 * para formar os cruzamentos entre ruas, que são compostos por varios objetos,
 * um deles obrigratoriamente é do tipo Intersection, os demais são referencias
 * para ele.
 */

public final class Reference extends StaticGraphicObject {

	private StaticGraphicObject ref;

	public Reference(StaticGraphicObject o) {
		ref = o;
	}

	public void setReference(StaticGraphicObject o) {
		ref = o;
	}

	public StaticGraphicObject getReference() {
		return ref;
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
	}

	@Override
	public void drawBg(Graphics2D g, int mode) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void draw(Graphics2D g, int mode) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

}
