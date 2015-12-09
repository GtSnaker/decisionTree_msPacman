package decisiontree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dataRecording.DataTuple;
import graph.SimpleNode;

public class DecisionTree {
	public HashMap<String, ArrayList<String>> attrs;
	
	public DecisionTree(){
		attrs = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> valuesMove = new ArrayList<String>();
		valuesMove.add("UP");
		valuesMove.add("DOWN");
		valuesMove.add("RIGHT");
		valuesMove.add("LEFT");
		valuesMove.add("NEUTRAL");
		
		ArrayList<String> valuesDoubles = new ArrayList<String>();
		valuesDoubles.add("VERY_LOW");
		valuesDoubles.add("LOW");
		valuesDoubles.add("MEDIUM");
		valuesDoubles.add("HIGH");
		valuesDoubles.add("VERY_HIGH");
		valuesDoubles.add("NONE");
		
		ArrayList<String> valuesBool = new ArrayList<String>();
		double one = 1.0;
		double zero = 0.0;
		String oneString = new Double(one).toString();
		String zeroString = new Double(zero).toString();
		valuesBool.add(oneString);
		valuesBool.add(zeroString);
		
		attrs.put("DirectionChosen", valuesMove);
		attrs.put("pacmanPosition", valuesDoubles);
		
		attrs.put("numOfPillsLeft", valuesDoubles);
		attrs.put("numOfPowerPillsLeft", valuesDoubles);
		
		attrs.put("blinkyDist", valuesDoubles);
		attrs.put("inkyDist", valuesDoubles);
		attrs.put("pinkyDist", valuesDoubles);
		attrs.put("sueDist", valuesDoubles);
		
		attrs.put("blinkyDir", valuesMove);
		attrs.put("inkyDir", valuesMove);
		attrs.put("pinkyDir", valuesMove);
		attrs.put("sueDir", valuesMove);
			
		attrs.put("blinkyEdible", valuesBool);
		attrs.put("inkyEdible", valuesBool);
		attrs.put("pinkyEdible", valuesBool);
		attrs.put("sueEdible", valuesBool);
		
	}
//	
//	El algoritmo se inicializa con D (el conjunto de datos de entrenamiento), la lista de atributos y el método de
//	selección de atributos S (la función que calcula el mayor beneficio).
//	
//	El algoritmo se puede programar en una única función, llamada Generar_Arbol (D, lista de atributos), que
//	recibe como parámetros los datos (D) y la lista de atributos. Los pasos de dicha función son:
	public SimpleNode generarArbol(ArrayList<ArrayList<String>> D, ArrayList<String> attrList){

//		1. Se crea el nodo N.
		SimpleNode N = null;
//		2. Si las tuplas en D tienen todas la misma clase C, return N como un nodo hoja etiquetado con la clase C.
		if(tuplasMismaClase(D)) 
		{
			String nombreClase = D.get(0).get(0);
			N = new SimpleNode(nombreClase);
			return N;
		}
//		3. En otro caso, si la lista de atributos está vacía, return N como un nodo hoja etiquetado con la clase
//		mayoritaria en D.
		else if(attrList.isEmpty()) {
			String nombreClase = getClaseMayoritaria(D);
			N = new SimpleNode(nombreClase);
			return N;
		}
//		4. En otro caso:
		else {
//			1. Aplicar el método de selección de atributos sobre los datos y la lista de atributos, para encontrar el
//			mejor atributo actual A:
//			S(D, lista de atributos) -> A.
			//TODO
			//C4.5
			String attr = attrList.get(0);
//			2. Etiquetar a N como A y eliminar A de la lista de atributos.
			N = new SimpleNode(attr);
			attrList.remove(attr);
			
//			3. Para cada valor aj del atributo A:
			ArrayList<String> valoresA = attrs.get(attr);
				for (String aj : valoresA) {
					ArrayList<String> copiaAttrList = (ArrayList<String>) attrList.clone();
//					a) Separar todas las tuplas en D para las cuales el atributo A toma el valor aj
//					, creando el
//					subconjunto de datos Dj
					ArrayList<ArrayList<String>> Dj = null;
//					b) Si Dj está vacío, añadir a N un nodo hoja etiquetado con la clase mayoritaria en D.
//					c) En otro caso, añadir a N el nodo hijo resultante de llamar a Generar_Arbol (Dj
//					, lista de atributos)
					if(false){	}
					else{
						N.addChild(attr, 
								generarArbol(Dj, copiaAttrList));
					}	
				}
//			4. Return N.
			return N;
		}
	}
	
	public boolean tuplasMismaClase(ArrayList<ArrayList<String>> data){
		boolean res = true;
			if(!data.isEmpty()) {
			String clase = data.get(0).get(0);
			for(int i = 1; i < data.size(); i++){
				if(clase != data.get(i).get(0)){
					res = false;
				}
			}
		}
		return res;
	}
	
	public String getClaseMayoritaria(ArrayList<ArrayList<String>> data){
		int left = 0, right = 0, up = 0, down = 0, none = 0;
		String claseMayoritaria = null;
		
		for(ArrayList<String> as: data){
			String clase = as.get(0);
			switch(clase){
				case ("LEFT"): left++; break;
				case ("RIGHT"): right++; break;
				case ("UP"): up++; break;
				case ("DOWN"): down++; break;
				case ("NONE"): none++; break;
			}
		}
		
		int max = Math.max(
				Math.max(left, right),
				Math.max(Math.max(up, down), none));
		
		if(max == left) claseMayoritaria = "LEFT";
		else if(max == right) claseMayoritaria = "RIGHT";
		else if(max == up) claseMayoritaria = "UP";
		else if(max == down) claseMayoritaria = "DOWN";
		else claseMayoritaria = "NONE";
		return claseMayoritaria;
	}
	
	public ArrayList<String> loadData() throws IOException {
		File file = new File("myData/trainingData.txt");
		if(!file.exists()){
			System.out.println("Error al cargar el archivo trainingData.txt");
			System.exit(0);
		}
		
		ArrayList<String> tuplas = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("myData/trainingData.txt"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		    	tuplas.add(line);
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		} finally {
		    br.close();
		}
		
		return tuplas;
	}
	
	public ArrayList<DataTuple> parseData(ArrayList<String> data){
		ArrayList<DataTuple> parsedData = new ArrayList<DataTuple>();

		for(String str: data) {
			DataTuple tupla = new DataTuple(str);
			parsedData.add(tupla);
		}
		return parsedData;
	}
	
	public ArrayList<ArrayList<String>> discretizeData(ArrayList<DataTuple> data){
		ArrayList<ArrayList<String>> discretizedData = new ArrayList<ArrayList<String>>();
		int i = 0;
		for (DataTuple datatuple : data) {
			ArrayList<String> newData = new ArrayList<String>();
			discretizedData.add(newData);
			
			String DirectionChosen = datatuple.DirectionChosen.toString();
			
			String pacmanPosition = datatuple.discretizePosition(
					datatuple.pacmanPosition).toString();
			String numOfPillsLeft = datatuple.discretizeNumberOfPills(
					datatuple.numOfPillsLeft).toString();
			String numOfPowerPillsLeft = datatuple.discretizeNumberOfPowerPills(
					datatuple.numOfPowerPillsLeft).toString();
			
			String blinkyEdible = new Double(
					datatuple.normalizeBoolean(datatuple.isBlinkyEdible)).toString();
			String inkyEdible = new Double(
					datatuple.normalizeBoolean(datatuple.isInkyEdible)).toString();
			String pinkyEdible = new Double(
					datatuple.normalizeBoolean(datatuple.isPinkyEdible)).toString();
			String sueEdible = new Double(
					datatuple.normalizeBoolean(datatuple.isSueEdible)).toString();
			
			String blinkyDist = datatuple.discretizeDistance(datatuple.blinkyDist).toString();
			String inkyDist = datatuple.discretizeDistance(datatuple.inkyDist).toString();
			String pinkyDist = datatuple.discretizeDistance(datatuple.pinkyDist).toString();
			String sueDist = datatuple.discretizeDistance(datatuple.sueDist).toString();
			
			String blinkyDir = datatuple.blinkyDir.toString();
			String inkyDir = datatuple.inkyDir.toString();
			String pinkyDir = datatuple.pinkyDir.toString();
			String sueDir = datatuple.sueDir.toString();
			
			discretizedData.get(i).add(DirectionChosen);
			discretizedData.get(i).add(pacmanPosition);
			discretizedData.get(i).add(numOfPillsLeft);
			discretizedData.get(i).add(numOfPowerPillsLeft);
			discretizedData.get(i).add(blinkyEdible);
			discretizedData.get(i).add(inkyEdible);
			discretizedData.get(i).add(pinkyEdible);
			discretizedData.get(i).add(sueEdible);
			discretizedData.get(i).add(blinkyDist);
			discretizedData.get(i).add(inkyDist);
			discretizedData.get(i).add(pinkyDist);
			discretizedData.get(i).add(sueDist);
			discretizedData.get(i).add(blinkyDir);
			discretizedData.get(i).add(inkyDir);
			discretizedData.get(i).add(pinkyDir);
			discretizedData.get(i).add(sueDir);
			
			i++;
		}
		
		return discretizedData;
	}
	
	public void printAttrs(){
		ArrayList<String> k = new ArrayList<String>();
		ArrayList<ArrayList<String>> v = new ArrayList<ArrayList<String>>();
		
		for(String aux: attrs.keySet()){
			k.add(aux);
		}
		
		for(ArrayList<String> aux: attrs.values()){
			ArrayList<String> aux2 = new ArrayList<String>();
			for(String str: aux){
				aux2.add(str);
			}
			v.add(aux2);
		}
		
		for (int i = 0; i < v.size(); i++) {
			System.out.println(k.get(i));
			for (int j = 0; j < v.get(i).size(); j++) {
				System.out.println(v.get(i).get(j));
			}
			System.out.println("\n");
		}
	}
	
}