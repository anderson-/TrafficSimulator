/** 
 * TSGUI.java
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

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import trafficsimulator.trafficcontrol.VehicleManager;
import trafficsimulator.maphandler.MapHandler;
import trafficsimulator.maphandler.mapeditor.MapEditorModeG;
import trafficsimulator.maphandler.simulation.SimulationMode;

// diagramas no javadoc: -docletpath "/home/anderson/uml/lib/ydoc.jar" -resourcepath "/home/anderson/uml/resources" -doclet ydoc.doclets.YStandard -umlautogen

/**
 * Classe responsavel pela GUI do programa.
 */
public class TSGUI {

	/**
	 *	Define a altura do painel de controle.
	 */
	public final int PANEL_HEIGHT = 160;
	
	// Componetes basicos da GUI
	private trafficsimulator.SplashScreen splashScreen;
	private JFrame mainWindow;
	private JPanel mainPanel;
	private GraphicsDevice graphicsDevice;
	private static JLabel statusLabel = new JLabel(" ... ");
	private static JTabbedPane control;
	
	// Componentes basicos do simulador
	private DrawingPanel drawingPanel;
	private Map map;
	private VehicleManager vm;
	private Clock clock;

	/**
	 * Metodo principal.
	 * <p>
	 * Lista de argumentos suportados:
	 * <dl>
	 * <dt>-f ou --fullscreen</dt>
	 * <dd>Executa o programa em tela cheia</dd>
	 * <dt>Milk</dt>
	 * <dd>White cold drink</dd>
	 * </dl>
	 * 
	 * @param args o vetor de strings contendo os argumentos para ajustes do
	 * programa
	 */
	
	public static void main(String[] args) {

		boolean fullscreen = false;
		boolean systemLook = false;

		for (String arg : args) {

			if (arg.equals("-f")) {
				fullscreen = true;
			} else if (arg.equals("-l")) {
				systemLook = true;
			}

		}

		TSGUI simulator = new TSGUI(fullscreen, systemLook);

		simulator.run();

	}
	
	/**
	 * 
	 * @param fullscreen
	 * @param systemLookAndFeel
	 */
	public TSGUI(boolean fullscreen, boolean systemLookAndFeel) {

		// exibir a tela de splash
		splashScreen = new trafficsimulator.SplashScreen("/trafficsimulator/resources/my_pixel_art/splash.png");
		splashScreen.splash();
		
		clock = new Clock();
		map = new Map();
		vm = new VehicleManager();
		

		MapHandler.setMainDataManagers(map, vm, clock);

		if (fullscreen) {
			GraphicsEnvironment env = GraphicsEnvironment.
					getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = env.getScreenDevices();
			graphicsDevice = devices[0];
			mainWindow = new JFrame(graphicsDevice.getDefaultConfiguration());
			mainPanel = (JPanel) mainWindow.getContentPane();
		} else {
			mainWindow = new JFrame();
			mainPanel = (JPanel) mainWindow.getContentPane();
			mainPanel.setPreferredSize(new Dimension(800, 600));
			
		}

		if (systemLookAndFeel) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
			}
		}

		SwingUtilities.updateComponentTreeUI(mainWindow);

		mainWindow.setTitle("Traffic Simulator");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel.setLayout(new GridBagLayout());

		//mainWindow.setResizable(false);

		makeInterface(mainPanel);

		mainWindow.pack();
		
		// iniciar centralizado
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle frame = mainWindow.getBounds();
		mainWindow.setLocation((screen.width - frame.width)/2, (screen.height - frame.height)/2);
		
		mainWindow.setVisible(true);

		if (fullscreen) {
			mainWindow.dispose();
			mainWindow.setUndecorated(graphicsDevice.isFullScreenSupported());
			graphicsDevice.setFullScreenWindow(mainWindow);
			mainWindow.validate();
		}

		// aceleração por hardware

//		if(System.getProperty("os.name").startsWith("Win")) {
//			System.setProperty("sun.java2d.d3d","true");
//			System.out.println("Usando Microsoft Direct3D");
//		} else {
//			System.setProperty("sun.java2d.opengl", "true"); // -Dsun.java2d.opengl=true
//			System.out.println("Usando OpenGL");
//		}
		
	}

	/**
	 * Constroi a interface do programa.
	 * 
	 * @param panel painel de conteudo do JFrame, retornado por JFrame.getContentPane()
	 */
	private void makeInterface(JPanel panel) {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("Exit");
		//fileMenu.add(new JMenuItem("Exit"));
		
		fileMenu.addMenuListener(new MenuListener (){

			@Override
			public void menuSelected(MenuEvent e) {
				System.exit(0);
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				
			}

			@Override
			public void menuCanceled(MenuEvent e) {
				
			}
			
		});
		
		menuBar.add(fileMenu);
		menuBar.add(Box.createGlue());
		menuBar.add(statusLabel);

		mainWindow.setJMenuBar(menuBar);

		drawingPanel = new DrawingPanel(new MapEditorModeG());

		control = new JTabbedPane();

		control.setTabPlacement(JTabbedPane.LEFT);
		control.setPreferredSize(new Dimension(300, PANEL_HEIGHT));
		control.addChangeListener(
				new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						JTabbedPane pane = (JTabbedPane) e.getSource();
						drawingPanel.pause(true);
						if (pane.getSelectedComponent() instanceof MapHandler) {
							if (!drawingPanel.tryChangeMode((MapHandler) pane.getSelectedComponent())) {
								pane.setSelectedIndex(-1);
							}
						}
						drawingPanel.pause(false);
					}
				});

		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(control)
				.addComponent(drawingPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
				.addComponent(drawingPanel, GroupLayout.DEFAULT_SIZE, PANEL_HEIGHT, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(control, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

		drawingPanel.addHandler(new SimulationMode());
		drawingPanel.addMouseMotionListener(map);
		drawingPanel.addMouseWheelListener(map);

		drawingPanel.addToolBars(control);

	}

	/**
	 * Inicia a execução do simulador.
	 * 
	 */
	private void run() {
		splashScreen.dispose();
		drawingPanel.loop();
	}

	/**
	 * Atualiza o conteudo da barra de status superior com um texto informativo.
	 *
	 * @param text um texto a ser exibido na barra de status
	 */
	public void updateStatus(String text) {
		statusLabel.setText("  " + text + "  ");
	}
}
