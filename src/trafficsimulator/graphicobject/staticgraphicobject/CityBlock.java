/** 
 * CityBlock.java
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Classe que define um quarteirão da cidade.
 */

public class CityBlock extends StaticGraphicObject{
	
	private final static int cellX = 39;
	private final static int cellY = 39;
	private final static int spaceX = 3;
	private final static int spaceY = 3;
	
	
	public void generate (int width, int height){
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.gray);
		g.fillRect(0,0,width, height);
		
		g.setColor(Color.LIGHT_GRAY);
		
		int w = width/cellX;// + (width%(int)cellX)/2;
		int h = height/cellY;// + (height%(int)cellY)/2;
		
		int dx = (width%(int)cellX)/2;
		int dy = (height%(int)cellY)/2;
		
		int matrix[][] = new int[w][h];
		
		int c = 1;
		
		for (int i = 0; i < w; i++){
			for (int j = 0; j < h; j++){
				matrix[i][j] = 0;
			}
		}
		
		for (int i = 0; i < w; i++){
			for (int j = 0; j < h; j++){
				
				g.fillRect(i*cellX+spaceX+dx,j*cellY+spaceY+dy,(cellX-2*spaceX),(cellY-2*spaceY));

				if (matrix[i][j] == 0 && ((int)(Math.random()*1000))%2 == 1){
					matrix[i][j] = c;
					
					for (int a = -1; a <= 1; a++){
						for (int b = -1; b <= 1; b++){
							if (Math.abs(a) != Math.abs(b) && i+a > 0 && i+a < w && j+b > 0 && j+b < h){
								if (matrix[i+a][j+b] == 0){
									
									int r = ((int)(Math.random()*100))%3;
									
									if (r == 1 || r == 2){
										matrix[i+a][j+b] = c;
										
										if (a < 0){
											g.fillRect((i+a)*cellX+spaceX+dx,j*cellY+spaceY+dy,(2*cellX-2*spaceX),(cellY-2*spaceY));
										} else if (a > 0){
											g.fillRect(i*cellX+spaceX+dx,j*cellY+spaceY+dy,(2*cellX-2*spaceX),(cellY-2*spaceY));
										}
										
										if (b < 0){
											g.fillRect(i*cellX+spaceX+dx,(j+b)*cellY+spaceY+dy,(cellX-2*spaceX),(2*cellY-2*spaceY));
										} else if (b > 0){
											g.fillRect(i*cellX+spaceX+dx,j*cellY+spaceY+dy,(cellX-2*spaceX),(2*cellY-2*spaceY));
										}
									
									}
								}
							}	
						}
					}

					c++;
					
				}
				
			}
		}
		
		g.dispose();
	}
	
	BufferedImage img;
	
	public void generate2 (int width, int height){
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0,width, height);
		
		g.setColor(Color.gray);
		
		int w = width/cellX;
		int h = height/cellY;
		
		int dx = (width%(int)cellX)/2;
		int dy = (height%(int)cellY)/2;
		
		for (int i = 0; i < w; i++){
			for (int j = 0; j < h; j++){
				
				g.fillRect(i*cellX+spaceX+dx,j*cellY+spaceY+dy,(cellX-2*spaceX),(cellY-2*spaceY));
				
				if (((int)(Math.random()*1000))%2 == 1){

					for (int a = -1; a <= 1; a++){
						for (int b = -1; b <= 1; b++){
							if (Math.abs(a) != Math.abs(b) && i+a > 0 && i+a < w && j+b > 0 && j+b < h){
								if (((int)(Math.random()*100))%2 == 1){
									
									if (a < 0){
										g.fillRect((i+a)*cellX+spaceX+dx,j*cellY+spaceY+dy,(2*cellX-2*spaceX),(cellY-2*spaceY));
									} else if (a > 0){
										g.fillRect(i*cellX+spaceX+dx,j*cellY+spaceY+dy,(2*cellX-2*spaceX),(cellY-2*spaceY));
									}
									
									if (b < 0){
										g.fillRect(i*cellX+spaceX+dx,(j+b)*cellY+spaceY+dy,(cellX-2*spaceX),(2*cellY-2*spaceY));
									} else if (b > 0){
										g.fillRect(i*cellX+spaceX+dx,j*cellY+spaceY+dy,(cellX-2*spaceX),(2*cellY-2*spaceY));
									}
								
								}
							}	
						}
					}

				}
				
			}
		}
		
		g.setColor(Color.red);
		g.drawString("=)", width/2 -4, height/2 +5);
		
		g.dispose();
	}

	@Override
	public void drawBg(Graphics2D g, int mode) {
		g.drawImage(img, null, null);
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void draw(Graphics2D g, int mode) {
		g.drawImage(img, null, null);
		//throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@Override
	public void setSize(int width, int height){
		super.setSize(width, height);
		generate(width,height);
	}
	
	@Override
	public void copyPropertiesOf(StaticGraphicObject obj){
		super.setLocation(obj.getI(),obj.getJ());
		setSize((int)obj.getWidth(),(int)obj.getHeight());
		super.setLocation((int)obj.getX(), (int)obj.getY());
	}

}
	
