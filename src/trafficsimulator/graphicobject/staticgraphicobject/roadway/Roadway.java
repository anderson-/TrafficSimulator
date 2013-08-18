///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package trafficsimulator.graphicobject.staticgraphicobject.roadway;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map.Entry;
//import java.util.Set;
//import trafficsimulator.graphicobject.staticgraphicobject.StaticGraphicObject;
//import trafficsimulator.path.Path;
//import trafficsimulator.util.BadLuckException;
//import trafficsimulator.util.Cardinal;
//
///**
// *
// * @author anderson
// */
//
//
//public abstract class Roadway extends StaticGraphicObject{
//
//	protected HashMap<Path,Roadway> inRoutes;
//	protected HashMap<Path,Roadway> outRoutes;
//	
//	protected ArrayList<Path> pathList;
//	
//	protected HashMap<Roadway,Cardinal> inTmpRoutes;
//	protected HashMap<Roadway,Cardinal> outTmpRoutes;
//	
//	protected boolean input = false;
//	protected boolean output = false;
//	
//	protected Roadway(){
//		inRoutes = new HashMap<Path,Roadway>();
//		outRoutes = new HashMap<Path,Roadway>();
//		pathList = new ArrayList<Path>();
//		inTmpRoutes = new HashMap<Roadway,Cardinal>();
//		outTmpRoutes = new HashMap<Roadway,Cardinal>();
//	}
//	
//	@Override
//	public void setSize (int width, int height){
//		super.setSize(width,height);
//		for(Path p: pathList){
//			p.setRect(width, height);
//		}
//	}
//	
//	public void unroute() {
//		inTmpRoutes.clear();
//		outTmpRoutes.clear();
//		//TODO: não usar clear
//	}
//	
//	public void addInput(Roadway roadway, Cardinal cardinal) {
//		inTmpRoutes.put(roadway, cardinal);
//		if (roadway != null){
//			input = true;
//		} else {
//			System.err.println("Warning: addInput(): recebendo null [" + this + "]");
//		}
//	}	
//
//	public void addOutput(Roadway roadway, Cardinal cardinal) {
//		outTmpRoutes.put(roadway, cardinal);
//		if (roadway != null){
//			output = true;
//		} else {
//			System.err.println("Warning: addOutput(): recebendo null [" + this + "]");
//		}
//	}
//
//	public abstract Path createPath(Roadway in, Cardinal inC, Roadway out, Cardinal outC);
//	
//	public void route(){
//		
//		if (this instanceof Street) return;
//		
//		if (inTmpRoutes.isEmpty() || outTmpRoutes.isEmpty()){
//			return;//throw new BadLuckException("impossivel rotear!");
//		}
//		
//		for (Entry<Roadway,Cardinal> a : inTmpRoutes.entrySet()){
//			for (Entry<Roadway,Cardinal> b : outTmpRoutes.entrySet()){
//				Path p = createPath(a.getKey(),a.getValue(),b.getKey(),b.getValue());
//				pathList.add(p);
//				inRoutes.put(p,a.getKey());
//				outRoutes.put(p, b.getKey());
//				System.err.println("alterando " + this.hashCode());
//			}
//		}
//	}
//	
//	public boolean hasOutput (){
//		return output;
//	}
//	
//	public boolean hasInput(){
//		return input;
//	}
//	
//	public Path getRandPath (Roadway input){
//		Path p = null;
//		if (inRoutes.containsValue(input)){
//			System.err.println("** contem valor");
//			ArrayList<Path> pList = new ArrayList<Path>();
//			for (Entry<Path,Roadway> e : inRoutes.entrySet()){
//				System.err.println("** add");
//				if (e.getValue() == input){
//					pList.add(e.getKey());
//				}
//			}
//			
//			p = pList.get((int)(Math.random()*pList.size()));
//		} else {
//			if (!pathList.isEmpty()){
//				p = pathList.get((int)(Math.random()*pathList.size()));
//			}
//		}
//		
//		if (p == null){
//			throw new BadLuckException("Nenhum caminho encontrado!");
//		}
//		
//		System.err.println("encontrado " + p.hashCode());
//		
//		p.translateTo((int)getX(), (int)getY());
//		
//		System.err.println("enviando " + p.hashCode()+ " transladado para ("+getX()+","+getY()+")");
//		
//		return p;
//	}
//
//	public ArrayList<Path> getPathList() {
//		return new ArrayList<Path>(pathList);
//	}
//	
//	public Roadway getOutput (Path p){
//		System.err.println("procurando "+ p.hashCode() + "em " + this.hashCode());
//		System.out.println("em " + this + " tam " + outRoutes.size() + " pedido " + p + " ret " + outRoutes.get(p));
//		
//		if (!outRoutes.containsKey(p)){
//			System.out.println("não tem...");
//		}
//		
//		
//		return outRoutes.get(p);
//	}
//	
//	public interface DefaultRoute {
//		
//		public Path getPath();
//		
//		public Roadway getInput();
//		
//		public Roadway getOutput();
//		
//	}
//	
//	protected class Route implements DefaultRoute{
//
//		private Path path;
//		private Roadway input;
//		private Roadway output;
//		
//		public Route (Roadway input, Path path, Roadway output){
//			this.input = input;
//			this.path = path;
//			this.output = output;
//		}
//		
//		@Override
//		public Path getPath() {
//			return path;
//		}
//
//		@Override
//		public Roadway getInput() {
//			return input;
//		}
//
//		@Override
//		public Roadway getOutput() {
//			return output;
//		}
//		
//	}
//	
//}
