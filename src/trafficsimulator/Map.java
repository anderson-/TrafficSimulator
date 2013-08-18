/**
 * Map.java
 *
 * Copyright (C) 2012 Anderson de Oliveira Antunes <anderson.utf@gmail.com>
 * Fernando Henrique Carvalho Ferreira <vojnik_f.henrique@hotmail.com>
 *
 * This file is part of TrafficSimulator.
 *
 * TrafficSimulator is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrafficSimulator is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrafficSimulator. If not, see http://www.gnu.org/licenses/.
 */
package trafficsimulator;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import trafficsimulator.graphicobject.staticgraphicobject.CityBlock;
import trafficsimulator.graphicobject.staticgraphicobject.road.Intersection;
import trafficsimulator.graphicobject.staticgraphicobject.EmptyStaticGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.Reference;
import trafficsimulator.graphicobject.staticgraphicobject.StaticGraphicObject;
import trafficsimulator.graphicobject.staticgraphicobject.road.Intersection.IntersectionTLS;
import trafficsimulator.graphicobject.staticgraphicobject.road.Street;
import trafficsimulator.path.CubicPath;
import trafficsimulator.path.LinearPath;
import trafficsimulator.path.Path;
import trafficsimulator.util.BadLuckException;
import trafficsimulator.util.Compass;

/**
 * Classe que representa o mapa em que ocorre a simulação.
 */
public class Map implements MouseListener, MouseMotionListener, MouseWheelListener {

	/**
	 *
	 */
	public static final int DEFAULT_OBJECT_WIDTH = 80;
	/**
	 *
	 */
	public static final int DEFAULT_OBJECT_HEIGHT = 80;
	/**
	 *
	 */
	public static final int MAX_SIZE = Short.MAX_VALUE;
	private double zoom = 1.0;
	private int posX = 0, posY = 0;
	private boolean allowZoom = true;
	private boolean allowMove = true;
	private Point mouse = new Point(0, 0);
	private ArrayList<ArrayList<StaticGraphicObject>> matrix;
	private int nRows = 0, nCols = 0;
	private int[] widthArray;
	private int[] heightArray;
	private int widthSum = 0;
	private int heightSum = 0;

	/**
	 *
	 */
	public Map() {
		matrix = new ArrayList<ArrayList<StaticGraphicObject>>();
	}

	/**
	 * Adiciona uma nova linha preenchida com
	 * {@link trafficsimulator.graphicobject.staticgraphicobject.EmptyStaticGraphicObject}
	 * no final do mapa
	 */
	public synchronized void addRow() {
		matrix.add(new ArrayList<StaticGraphicObject>());
		for (int a = 0; a < nCols; a++) {
			matrix.get(nRows).add(new EmptyStaticGraphicObject());
		}
		nRows = matrix.size();
	}

	/**
	 * Adiciona um nova linha preenchida com
	 * {@link trafficsimulator.graphicobject.staticgraphicobject.EmptyStaticGraphicObject}
	 * após a posição i do mapa
	 *
	 * @param i posição
	 */
	public synchronized void addRow(int i) {
		if (i < 0) {
			throw new IllegalArgumentException("i < 0");
		}
		matrix.add(i, new ArrayList<StaticGraphicObject>());
		for (int a = 0; a < nCols; a++) {
			matrix.get(i).add(new EmptyStaticGraphicObject());
		}
		nRows = matrix.size();
	}

	/**
	 * Remove a ultima linha do mapa
	 */
	public synchronized void removeRow() {
		matrix.remove(nRows - 1);
		nRows = matrix.size();
	}

	/**
	 * Remove a linha
	 * <code>i</code> do mapa
	 * 
	 * @param i 
	 */
	public synchronized void removeRow(int i) {
		if (i >= 0 && i < nRows) {
			matrix.remove(i);
			nRows = matrix.size();
		} else {
			throw new IllegalArgumentException(" i != [0,nRows[");
		}
	}

	/**
	 *
	 * @param i
	 * @return
	 */
	public synchronized int addRows(int i) {
		while (i >= nRows) {
			matrix.add(new ArrayList<StaticGraphicObject>());
			for (int a = 0; a < nCols; a++) {
				matrix.get(nRows).add(new EmptyStaticGraphicObject());
			}
			nRows = matrix.size();
		}

		while (i < 0) {
			matrix.add(0, new ArrayList<StaticGraphicObject>());
			for (int a = 0; a < nCols; a++) {
				matrix.get(0).add(new EmptyStaticGraphicObject());
			}

			i++;
			nRows = matrix.size();
		}

		return i;
	}

	/**
	 *
	 * @return
	 */
	public synchronized int getNRows() {
		return nRows;
	}

	/**
	 *
	 */
	public synchronized void addCol() {
		for (int a = 0; a < nRows; a++) {
			matrix.get(a).add(new EmptyStaticGraphicObject());
		}
		nCols = matrix.get(0).size();
	}

	/**
	 *
	 * @param j
	 */
	public synchronized void addCol(int j) {
		if (j < 0) {
			throw new IllegalArgumentException("j < 0");
		}
		for (int a = 0; a < nRows; a++) {
			matrix.get(a).add(j, new EmptyStaticGraphicObject());
		}
		nCols = matrix.get(0).size();
	}

	/**
	 *
	 */
	public synchronized void removeCol() {
		for (int a = 0; a < nRows; a++) {
			matrix.get(a).remove(nCols - 1);
		}
		nCols = matrix.get(0).size();
	}

	/**
	 *
	 * @param j
	 */
	public synchronized void removeCol(int j) {
		if (j >= 0 && j < nCols) {
			for (int a = 0; a < nRows; a++) {
				matrix.get(a).remove(j);
			}
			nCols = matrix.get(0).size();
		} else {
			throw new IllegalArgumentException("j != [0,nCols[");
		}
	}

	/**
	 *
	 * @param j
	 * @return
	 */
	public synchronized int addCols(int j) {
		while (j >= nCols) {
			for (int a = 0; a < nRows; a++) {
				matrix.get(a).add(new EmptyStaticGraphicObject());
			}
			nCols = matrix.get(0).size();
		}

		while (j < 0) {
			for (int a = 0; a < nRows; a++) {
				matrix.get(a).add(0, new EmptyStaticGraphicObject());
			}
			j++;
			nCols = matrix.get(0).size();
		}
		
		return j;
	}

	/**
	 *
	 * @return
	 */
	public synchronized int getNCols() {
		return nCols;
	}

	/**
	 *
	 * @param i
	 * @param j
	 * @param obj
	 */
	public synchronized void set(int i, int j, StaticGraphicObject obj) {
		i = addRows(i);
		j = addCols(j);

		if (obj == null) {
			matrix.get(i).set(j, new EmptyStaticGraphicObject());
		}
		
		StaticGraphicObject oldObj = matrix.get(i).get(j);
		matrix.get(i).set(j, obj);
		obj.copyPropertiesOf(oldObj);
	}

	/**
	 *
	 * @param oldObj
	 * @param newObj
	 */
	public synchronized void replace(StaticGraphicObject oldObj, StaticGraphicObject newObj) {
		if (oldObj != null) {
			int oi = oldObj.getI(), oj = oldObj.getJ();
			if (!matrix.get(oi).get(oj).equals(oldObj)) {
				for (int i = 0; i < nRows; i++) {
					for (int j = 0; j < nCols; j++) {
						if (matrix.get(i).get(j).equals(oldObj)) {
							oi = i;
							oj = j;
							i = nRows;
							break;
						} else {
							oi = -1;
							oj = -1;
						}
					}
				}
			}

			if (oi >= 0 && oj >= 0) {
				if (newObj == null) {
					newObj = new EmptyStaticGraphicObject();
				}
				newObj.copyPropertiesOf(oldObj);
				matrix.get(oi).set(oj, newObj);
			}
		} else {
			throw new NullPointerException("oldObj == null");
		}
	}

	/**
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	public synchronized StaticGraphicObject get(int i, int j) {
		if (i >= 0 && j >= 0 && i < nRows && j < nCols) {
			return matrix.get(i).get(j);
		} else {
			return null;
		}
	}

	// ==============================================> Metodos de Matriz (Avançado)
	/**
	 *
	 * @param i
	 * @param j
	 * @param objs
	 */
	public synchronized void setMap(int i, int j, StaticGraphicObject... objs) {

		i = addRows(i);
		j = addCols(j);

		if (objs.length == i * j) {
			for (int a = 0; a < i; a++) {
				for (int b = 0; b < j; b++) {
					set(a, b, objs[a * (j - 1) + b]);
				}
			}
		} else {
			throw new IllegalArgumentException("i * j != objs.length");
		}
	}
	
	public String save (){
		StringBuilder ss = new StringBuilder();
		StaticGraphicObject obj;
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				obj = matrix.get(i).get(j);
				if (obj instanceof Street){
					ss.append(((Street)obj).getDirection().getIndex());
					ss.append(":").append((byte)i).append(":");
					ss.append((byte)j).append("#");
				}
			}
		}
		
		System.out.println("data:" + ss);
		
		return ss.toString();
	}
	
	public void load (String data){
		
		clear();
		
		for (String s : data.split("#")){
			String [] info = s.split(":");
			System.out.println(s + " " + info.length);
			if (info.length == 3){
				try {
					Compass dir = Compass.values()[Integer.parseInt(info[0])];
					int i = Integer.parseInt(info[1]);
					int j = Integer.parseInt(info[2]);
					
					set(i, j, new Street(dir));
				} catch (Exception ex) {
					System.out.println("erro");
				}
			} else {
				System.out.println("erro2");
			}
		}
		
	}

	/**
	 *
	 */
	public synchronized void clear() {
		for (int a = 0; a < nRows; a++) {
			matrix.get(a).clear();
		}
		matrix.clear();

		nCols = 0;
		nRows = 0;
	}

	/**
	 *
	 * @param model
	 */
	public synchronized void setModel(int model) {
		clear();

		if (model == 0) {
			set(0, 1, new Street(Compass.Up));
			set(0, 2, new Street(Compass.Up));
			set(0, 3, new Street(Compass.Up));
			set(0, 5, new Street(Compass.Up));
			set(0, 6, new Street(Compass.Down));

			set(1, 0, new Street(Compass.Right));
			set(2, 0, new Street(Compass.Right));
			set(1, 4, new Street(Compass.Right));
			set(1, 7, new Street(Compass.Right));

			set(3, 1, new Street(Compass.Up));
			set(3, 2, new Street(Compass.Up));
			set(3, 3, new Street(Compass.Up));
			set(3, 5, new Street(Compass.Up));
			set(3, 6, new Street(Compass.Down));

		} else {
			set(0, 1, new Street(Compass.Right));
			set(1, 0, new Street(Compass.Up));
			set(1, 2, new Street(Compass.Down));
			set(2, 1, new Street(Compass.Left));

			set(2, 3, new Street(Compass.Left));
			set(3, 4, new Street(Compass.Up));
			set(3, 2, new Street(Compass.Down));
			set(4, 3, new Street(Compass.Right));

			set(1, 4, new Street(Compass.Up));
			set(0, 5, new Street(Compass.Right));
			set(1, 6, new Street(Compass.Down));
			set(2, 5, new Street(Compass.Left));

			set(5, 2, new Street(Compass.Down));
			set(6, 1, new Street(Compass.Left));
			set(5, 0, new Street(Compass.Up));
			set(4, 1, new Street(Compass.Right));

			set(4, 5, new Street(Compass.Right));
			set(5, 6, new Street(Compass.Down));
			set(6, 5, new Street(Compass.Left));
			set(5, 4, new Street(Compass.Up));

			set(2, 7, new Street(Compass.Left));
			set(4, 7, new Street(Compass.Right));

		}
	}

	/**
	 *
	 * @return
	 */
	public synchronized int[] getWidthArray() {
		return (int[]) widthArray.clone();
	}

	/**
	 *
	 * @return
	 */
	public synchronized int[] getHeightArray() {
		return (int[]) heightArray.clone();
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public synchronized StaticGraphicObject get(Point p) {

		int ri = -1, rj = -1;
		int x = p.x, y = p.y;

		if (x >= 0 && y >= 0) {

			for (int i = 0; i < widthArray.length; i++) {
				if (x <= widthArray[i]) {
					ri = i;
					break;
				} else {
					x -= widthArray[i];
					ri = -1;
					if (x < 0) {
						break;
					}
				}
			}

			for (int j = 0; j < heightArray.length; j++) {
				if (y <= heightArray[j]) {
					rj = j;
					break;
				} else {
					y -= heightArray[j];
					rj = -1;
					if (y < 0) {
						break;
					}
				}
			}

		}

		if (ri != -1 && rj != -1) {
			return get(rj, ri);
		} else {
			return null;
		}

	}

	/**
	 *
	 * @return
	 */
	public synchronized int getWidth() {
		return widthSum;
	}

	/**
	 *
	 * @return
	 */
	public synchronized int getHeight() {
		return heightSum;
	}
	
	public synchronized Rectangle getBounds(){
		return new Rectangle(widthSum, heightSum);
	}

	/**
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	public synchronized java.util.Map<Compass, StaticGraphicObject> getNeighborhoodMap(int i, int j) {

		HashMap<Compass, StaticGraphicObject> m = new HashMap<Compass, StaticGraphicObject>();

		for (Compass c : Compass.values()) {
			m.put(c, ((i + c.getYInc()) >= 0 && (i + c.getYInc()) < nRows && (j + c.getXInc()) >= 0 && (j + c.getXInc()) < nCols) ? matrix.get(i + c.getYInc()).get(j + c.getXInc()) : null);
		}

		return m;
	}

	private boolean checkCrossroad(Point a, Point b, int i, int j) {

		if (j < a.x || a.x < 0) {
			a.x = j;
		}
		if (j > b.x || b.x < 0) {
			b.x = j;
		}
		if (i < a.y || a.y < 0) {
			a.y = i;
		}
		if (i > b.y || b.y < 0) {
			b.y = i;
		}

		boolean ret = false;

		for (Entry<Compass, StaticGraphicObject> e : getNeighborhoodMap(i, j).entrySet()) {
			if (e != null && e.getValue() instanceof EmptyStaticGraphicObject) {
				int v = (((EmptyStaticGraphicObject) e.getValue()).getCrossVal());
				if (v >= 1) {
					if (v >= 2) {
						ret = true;
					}

					((EmptyStaticGraphicObject) e.getValue()).setCrossVal(0);

					ret |= checkCrossroad(a, b, i + e.getKey().getYInc(), j + e.getKey().getXInc());
				}
			}
		}

		return ret;
	}

	/**
	 *
	 */
	public synchronized void route() {

		long t = System.currentTimeMillis();

		StaticGraphicObject obj;

		// define o numero de ruas vizinhas a um objeto vazio

		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				obj = matrix.get(i).get(j);

				if (obj instanceof EmptyStaticGraphicObject) {
					int v = 0;
					for (Entry<Compass, StaticGraphicObject> e : getNeighborhoodMap(i, j).entrySet()) {
						if (e.getValue() instanceof Street) {
							if (((Street) e.getValue()).getDirection().sameAxis(e.getKey())) {
								v++;
							}
						}
					}
					((EmptyStaticGraphicObject) obj).setCrossVal(v);
				}
			}
		}

		// define um vetor de cruzamentos definidos por dois pontos

		ArrayList<Point[]> crossroads = new ArrayList<Point[]>();

		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				obj = matrix.get(i).get(j);

				if (obj instanceof EmptyStaticGraphicObject) {
					int v = ((EmptyStaticGraphicObject) obj).getCrossVal();

					if (v >= 1) {
						((EmptyStaticGraphicObject) obj).setCrossVal(0); //desnecessario?

						Point a = new Point(-1, -1), b = new Point(0, 0);

						boolean valid = checkCrossroad(a, b, i, j);

						if (v >= 2) {
							valid = true;
						}

						if (!valid && (a.x != b.x && a.y != b.y)) {
							valid = true;
						}

						if (valid) {
							crossroads.add(new Point[]{a, b});
						}
					}
				}
			}
		}

		// para cada par de pontos cria um cruzamento

		int o = 10;
		int n = 10;

		ArrayList<Street> str;
		ArrayList<Compass> dir;
		ArrayList<Boolean> in;
		ArrayList<int[]> pt;

		Intersection newC;

		for (Point[] c : crossroads) {

			newC = new Intersection();

			set(c[0].y, c[0].x, newC);

			for (int i = c[0].y; i <= c[1].y; i++) {
				for (int j = c[0].x; j <= c[1].x; j++) {
					obj = matrix.get(i).get(j);

					if (obj instanceof EmptyStaticGraphicObject) {
						set(i, j, new Reference(newC));
						//TODO: add em crossroad
					}
				}
			}

			str = new ArrayList<Street>();
			dir = new ArrayList<Compass>();
			in = new ArrayList<Boolean>();
			pt = new ArrayList<int[]>();

			for (int i = c[0].y; i <= c[1].y; i++) {
				if (c[0].x - 1 >= 0 && c[0].x - 1 < nCols) {
					obj = matrix.get(i).get(c[0].x - 1);

					if (obj instanceof Street) {
						str.add((Street) obj);
						dir.add(Compass.Left);
						pt.add(new int[]{i, c[0].x - 1});

						if (Compass.Right == ((Street) obj).getDirection()) {
							in.add(true);
						} else if (Compass.Left == ((Street) obj).getDirection()) {
							in.add(false);
						} else {
							return;
							//throw new BadLuckException("??");
						}
					}
				}

				if (c[1].x + 1 >= 0 && c[1].x + 1 < nCols) {
					obj = matrix.get(i).get(c[1].x + 1);

					if (obj instanceof Street) {
						str.add((Street) obj);
						dir.add(Compass.Right);
						pt.add(new int[]{i, c[1].x + 1});

						if (Compass.Left == ((Street) obj).getDirection()) {
							in.add(true);
						} else if (Compass.Right == ((Street) obj).getDirection()) {
							in.add(false);
						} else {
							return;
							//throw new BadLuckException("??");
						}
					}
				}
			}

			for (int j = c[0].x; j <= c[1].x; j++) {
				if (c[0].y - 1 >= 0 && c[0].y - 1 < nRows) {
					obj = matrix.get(c[0].y - 1).get(j);

					if (obj instanceof Street) {
						str.add((Street) obj);
						dir.add(Compass.Up);
						pt.add(new int[]{c[0].y - 1, j});

						if (Compass.Down == ((Street) obj).getDirection()) {
							in.add(true);
						} else if (Compass.Up == ((Street) obj).getDirection()) {
							in.add(false);
						} else {
							return;
							//throw new BadLuckException("??");
						}
					}
				}

				if (c[1].y + 1 >= 0 && c[1].y + 1 < nRows) {
					obj = matrix.get(c[1].y + 1).get(j);

					if (obj instanceof Street) {
						str.add((Street) obj);
						dir.add(Compass.Down);
						pt.add(new int[]{c[1].y + 1, j});

						if (Compass.Up == ((Street) obj).getDirection()) {
							in.add(true);
						} else if (Compass.Down == ((Street) obj).getDirection()) {
							in.add(false);
						} else {
							return;
							//throw new BadLuckException("??");
						}
					}
				}
			}

			// retas
			Street A, B;
			for (int a = 0; a < str.size(); a++) {
				if (in.get(a)) {
					A = str.get(a);
					for (int b = 0; b < str.size(); b++) {
						B = str.get(b);
						if (A != B) {//??
							if (!in.get(b)) {
								double ax = A.getDirection().getRectMidX(A.getWidth());
								double ay = A.getDirection().getRectMidY(A.getHeight());

								ax += A.getX();
								ay += A.getY();

								double bx = B.getDirection().getOposite().getRectMidX(B.getWidth());
								double by = B.getDirection().getOposite().getRectMidY(B.getHeight());

								bx += B.getX();
								by += B.getY();

								Point2D.Double pa = new Point2D.Double(ax, ay);
								Point2D.Double pb = new Point2D.Double(bx, by);

								double delta = .3; //curvatura [0,1]

								double cax = -A.getDirection().getXInc() * Math.abs(ax - bx) * delta
											 + Math.abs(A.getDirection().getXInc()) * bx
											 + Math.abs(A.getDirection().getYInc()) * ax;

								double cay = -A.getDirection().getYInc() * Math.abs(ay - by) * delta
											 + Math.abs(A.getDirection().getYInc()) * by
											 + Math.abs(A.getDirection().getXInc()) * ay;

								double cbx = B.getDirection().getXInc() * Math.abs(ax - bx) * delta
											 + Math.abs(B.getDirection().getXInc()) * ax
											 + Math.abs(B.getDirection().getYInc()) * bx;

								double cby = B.getDirection().getYInc() * Math.abs(ay - by) * delta
											 + Math.abs(B.getDirection().getYInc()) * ay
											 + Math.abs(B.getDirection().getXInc()) * by;

								Point2D.Double pca = new Point2D.Double(cax, cay);
								Point2D.Double pcb = new Point2D.Double(cbx, cby);

								int dc = Math.abs(A.getDirection().getXInc() * (pt.get(a)[1] - pt.get(b)[1]) + A.getDirection().getYInc() * (pt.get(a)[0] - pt.get(b)[0]));
								int dn = Math.abs(B.getDirection().getXInc() * (pt.get(a)[1] - pt.get(b)[1]) + B.getDirection().getYInc() * (pt.get(a)[0] - pt.get(b)[0]));

								Path p = null;

								if (A.getDirection() == B.getDirection()) {
									//dn = Math.abs((pt.get(a)[1] - pt.get(b)[1]) + (pt.get(a)[0] - pt.get(b)[0]));
									dn = A.getDirection().getOpositeAxis()[1].getXInc() * (pt.get(a)[1] - pt.get(b)[1]) + A.getDirection().getOpositeAxis()[1].getYInc() * (pt.get(a)[0] - pt.get(b)[0]);
									if (ax == bx || ay == by) {
										p = new LinearPath(newC, A.getDirection(), pa, pb);

									} else {
//										System.out.println("ia: " + (pt.get(a)[0]-1) + " ja: " + (pt.get(a)[1]-1));
//										System.out.println("ib: " + (pt.get(b)[0]-1) + " jb: " + (pt.get(b)[1]-1));
//										System.out.println();
										//System.out.println("- dc  = " + dc + "| dn = " + dn);
										// "curva reta"
										if (dn == 1 && dc == 4) {
											//System.out.println("dc  = " + dc + "| dn = " + dn);
											p = new CubicPath(newC, pa, pca, pcb, pb);
										}
									}
								} else {
									//break;
									if (dn == 1) {
										p = new CubicPath(newC, pa, pca, pcb, pb);
									}
								}
								//p = new CubicPath(newC,pa,pca,pcb,pb);
								if (p != null) {
									newC.addInput(A, p);
									A.setOutput(newC);
									newC.addOutput(B, p);
									B.setInput(newC);

									if (newC.state == null) {
										newC.state = newC.new IntersectionTLS();
									}
									if (A.getDirection().sameAxis(Compass.Up)) {
										if (!newC.state.V.contains(A.tl)){
											newC.state.V.add(A.tl);
										}
										//System.out.println("tV: " + newC.state.V.size());
									} else {
										if (!newC.state.H.contains(A.tl)){
											newC.state.H.add(A.tl);
										}
										//System.out.println("tH: " + newC.state.H.size());
									}
								}



							}
						}
					}
				}
			}

		}
		System.out.println(this.getTrafficState().toString());

		t = System.currentTimeMillis() - t;

		System.out.println("routed in " + t + " ms");

	}
	
	/**
	 *
	 * @param t
	 */
	public synchronized void setAffineTransform(AffineTransform t) {
		if (t != null) {
			t.translate(posX, posY);
			t.scale(zoom, zoom);
		}
	}

	/**
	 *
	 * @param route
	 */
	public synchronized void update(boolean route) { // object change

		//O QUE FAZ:
		//remover cruzamentos e cityblocks
		//ver se precisa adicionar ou remover bordas
		//calcular o novo tamanho dos objetos
		//criar cruzamentos e definir a nova posição dos objetos
		//substituir objetos vazios por cityblocks
		//desenhar

		// FAZENDO:
		StaticGraphicObject obj;

		if (nCols == 0 && nRows == 0) {
			set(0, 0, new EmptyStaticGraphicObject());
			System.out.println("vazio");
		} else {

			//ver se precisa adicionar ou remover bordas
			System.out.println("bordas");

			short minX = MAX_SIZE;
			short maxX = -1;
			short minY = MAX_SIZE;
			short maxY = -1;
			short pOX = -1;
			short pOY = -1;

			for (short i = 0; i < nRows; i++) {
				for (short j = 0; j < nCols; j++) {
					obj = matrix.get(i).get(j);
					//System.out.println("em " + i + " " + j);
					if (!(obj instanceof EmptyStaticGraphicObject)) {
						if (!(obj instanceof Street)) {
							replace(obj, new EmptyStaticGraphicObject());
						} else {
							for (Path p : ((Street)obj).getPathList()){
								p.getRoad().clear();
							}
							
							minX = (j < minX) ? j : minX;
							minY = (i < minY) ? i : minY;

							maxX = (j > maxX) ? j : maxX;
							maxY = (i > maxY) ? i : maxY;
						}
					}
				}
			}


			minX = (short) ((minX == MAX_SIZE) ? 0 : minX - 1);
			minY = (short) ((minY == MAX_SIZE) ? 0 : minY - 1);
			maxX = (short) ((maxX == -1) ? 0 : maxX + 1);
			maxY = (short) ((maxY == -1) ? 0 : maxY + 1);

			addCols(maxX);
			addRows(maxY);

			while (nCols - 1 > maxX) {
				removeCol();
			}

			while (nRows - 1 > maxY) {
				removeRow();
			}

			addCols(minX);
			addRows(minY);

			while (minX > 0) {
				removeCol(0);
				minX--;
			}

			while (minY > 0) {
				removeRow(0);
				minY--;
			}

		}

		System.out.println("tamanho");

		//calcular o novo tamanho dos objetos e definir a nova posição dos objetos

		widthArray = new int[nCols];
		heightArray = new int[nRows];

		widthSum = 0;
		heightSum = 0;

		for (int i = 0; i < nRows; i++) {

			heightArray[i] = 0;

			for (int j = 0; j < nCols; j++) {
				obj = matrix.get(i).get(j);

				obj.setMatrixPosition(i, j);

				if (!(obj instanceof EmptyStaticGraphicObject)) {
					if (widthArray[j] < obj.getWidth()) {
						widthArray[j] = (int) obj.getWidth();
					}

					if (heightArray[i] < obj.getHeight()) {
						heightArray[i] = (int) obj.getHeight();
					}
				}
			}
		}

		for (int i = 0; i < nRows; i++) {
			heightArray[i] = (heightArray[i] > 0) ? heightArray[i] : DEFAULT_OBJECT_HEIGHT;
			heightSum += heightArray[i];
		}

		for (int j = 0; j < nCols; j++) {
			widthArray[j] = (widthArray[j] > 0) ? widthArray[j] : DEFAULT_OBJECT_WIDTH;
			widthSum += widthArray[j];
		}

		int x = 0, y;
		for (int i = 0; i < nRows; i++) {
			y = 0;
			for (int j = 0; j < nCols; j++) {
				obj = matrix.get(i).get(j);
				obj.setLocation(y, x);

				if (widthArray[j] > 0 && heightArray[i] > 0) { // && !(obj instanceof Road)
					obj.setSize(widthArray[j], heightArray[i]);
				}
				y += widthArray[j];
			}
			x += heightArray[i];
		}

		System.out.println("rotas");
		route();

		if (!route) {
			for (short i = 0; i < nRows; i++) {
				for (short j = 0; j < nCols; j++) {
					obj = matrix.get(i).get(j);
					if (!(obj instanceof EmptyStaticGraphicObject)) {
						if (!(obj instanceof Street)) {
							replace(obj, new EmptyStaticGraphicObject());
						}
					}
				}
			}
		}

		if (route) {
			//criar cruzamentos 

			System.out.println("finaliza");
			//substituir objetos vazios por cityblocks

			for (int i = 1; i < nRows - 1; i++) {
				for (int j = 1; j < nCols - 1; j++) {
					obj = matrix.get(i).get(j);
					if (obj instanceof EmptyStaticGraphicObject) {
						replace(obj, new CityBlock());
					}
				}
			}
		}

		System.out.println("fim");
	}

	/**
	 *
	 * @param g
	 * @param mode
	 */
	public synchronized void draw(Graphics2D g, int mode) {

		StaticGraphicObject obj;

		AffineTransform org = g.getTransform();
		AffineTransform t;

		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				obj = matrix.get(i).get(j);

				t = new AffineTransform(org);
				t.translate(obj.getX(), obj.getY());

				g.setTransform(t);
				obj.drawBg(g, mode);
			}
		}

		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				obj = matrix.get(i).get(j);

				t = new AffineTransform(org);
				t.translate(obj.getX(), obj.getY());

				g.setTransform(t);
				obj.draw(g, mode);
			}
		}

		g.setTransform(org);
	}

	/**
	 *
	 * @param z
	 */
	public synchronized void setZoom(double z) {
		if (z > 0) {
			zoom = z;
		}
	}

	/**
	 *
	 * @return
	 */
	public synchronized double getZoom() {
		return zoom;
	}

	/**
	 *
	 * @param val
	 */
	public synchronized void setZoomEnabled(boolean val) {
		allowZoom = val;
	}

	/**
	 *
	 * @return
	 */
	public synchronized boolean isZoomEnabled() {
		return allowZoom;
	}

	/**
	 *
	 * @return
	 */
	public synchronized Point getPosition() {
		return new Point(posX, posY);
	}

	/**
	 *
	 * @param p
	 */
	public synchronized void setPosition(Point p) {
		posX = (int) p.getX();
		posY = (int) p.getY();
	}

	/**
	 *
	 */
	public synchronized void centerMap() {
		//posX = getWidth()/2 - (int)imgMap.getWidth()/2;
		//posY = getHeight()/2 - (int)imgMap.getHeight()/2;
	}

	/**
	 *
	 * @param val
	 */
	public synchronized void setMovable(boolean val) {
		allowMove = val;
	}

	/**
	 *
	 * @return
	 */
	public synchronized boolean isMovable() {
		return allowMove;
	}

	/**
	 *
	 * @return
	 */
	public synchronized Point getMouse() {
		return new Point((int) ((mouse.getX() - posX) / zoom), (int) ((mouse.getY() - posY) / zoom));
	}

	@Override
	public synchronized String toString() {
		throw new BadLuckException("não implementado");
	}

	/**
	 *
	 */
	public synchronized void printString() {
		System.out.println(toString());
	}

	@Override
	public synchronized void mouseDragged(MouseEvent e) {
		if (allowMove) {
			posX -= (int) (mouse.getX() - e.getPoint().getX());
			posY -= (int) (mouse.getY() - e.getPoint().getY());
			mouse = e.getPoint();
			e.consume();
		}
	}

	@Override
	public synchronized void mouseMoved(MouseEvent e) {
		mouse = e.getPoint();
		e.consume();
	}

	@Override
	public synchronized void mouseClicked(MouseEvent e) {
		if (e.getButton() == 2) {
			zoom = 0.0;
		}
	}

	@Override
	public synchronized void mouseEntered(MouseEvent e) {
	}

	@Override
	public synchronized void mouseExited(MouseEvent e) {
	}

	@Override
	public synchronized void mousePressed(MouseEvent e) {
	}

	@Override
	public synchronized void mouseReleased(MouseEvent e) {
	}

	@Override
	public synchronized void mouseWheelMoved(MouseWheelEvent e) {
		if (allowZoom) {
			zoom = ((zoom + e.getWheelRotation() * 0.5) > 0.1) ? zoom + e.getWheelRotation() * 0.1 : zoom;
		}
		e.consume();

	}

	/**
	 *
	 * @return
	 */
	public synchronized MapTLS getTrafficState() {
		return new MapTLS(this);
	}

	/**
	 *
	 */
	public class MapTLS {

		private ArrayList<ArrayList<IntersectionTLS>> matrix;

		/**
		 *
		 * @param m
		 */
		public MapTLS(Map m) {

			matrix = new ArrayList<ArrayList<IntersectionTLS>>();

			StaticGraphicObject obj;

			for (int i = 0; i < m.nRows; i++) {
				for (int j = 0; j < m.nCols; j++) {
					obj = m.get(i, j);
					if (obj instanceof Intersection) {
						while (i >= matrix.size()) {
							matrix.add(new ArrayList<IntersectionTLS>());
						}
						matrix.get(i).add(((Intersection) obj).getState());
					}
				}
			}
		}

		/**
		 *
		 * @return
		 */
		public synchronized ArrayList<ArrayList<IntersectionTLS>> getMatrix() {
			return matrix;
		}

		@Override
		public synchronized String toString() {

			String r = "";

			for (ArrayList<IntersectionTLS> i : matrix) {
				for (IntersectionTLS obj : i) {
					r += "[.] ";
				}
				r += "\n";
			}

			return r;
		}
	}
}
