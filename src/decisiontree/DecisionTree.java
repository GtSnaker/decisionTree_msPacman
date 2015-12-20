package decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import graph.SimpleNode;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DecisionTree extends Controller<MOVE> {
	public static HashMap<String, ArrayList<String>>	attrs;
	public static ArrayList<String>						DirectionChosen;
	public SimpleNode									root;

	public DecisionTree() {
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

		// attrs.put("DirectionChosen", valuesMove);
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

		attrs.put("isBlinkyEdible", valuesBool);
		attrs.put("isInkyEdible", valuesBool);
		attrs.put("isPinkyEdible", valuesBool);
		attrs.put("isSueEdible", valuesBool);

		DirectionChosen = valuesMove;
	}

	public void build() {
		DataTuple[] tuples = DataSaverLoader.LoadPacManData();
		ArrayList<DataTuple> data = new ArrayList<DataTuple>(Arrays.asList(tuples));
		ArrayList<String> attrList = new ArrayList<String>(attrs.keySet());
		root = generarArbol(data, attrList);
	}

	//
	// El algoritmo se inicializa con D (el conjunto de datos de entrenamiento),
	// la lista de atributos y el método de
	// selección de atributos S (la función que calcula el mayor beneficio).
	//
	// El algoritmo se puede programar en una única función, llamada
	// Generar_Arbol (D, lista de atributos), que
	// recibe como parámetros los datos (D) y la lista de atributos. Los pasos
	// de dicha función son:
	public SimpleNode generarArbol(ArrayList<DataTuple> D, ArrayList<String> attrList) {
		// 1. Se crea el nodo N.
		SimpleNode N = null;
		// 2. Si las tuplas en D tienen todas la misma clase C, return N como un
		// nodo hoja etiquetado con la clase C.
		if (tuplasMismaClase(D)) {
			String nombreClase = (D.get(0).DirectionChosen).toString();
			N = new SimpleNode(nombreClase);
			return N;
		}
		// 3. En otro caso, si la lista de atributos está vacía, return N como
		// un nodo hoja etiquetado con la clase
		// mayoritaria en D.
		else if (attrList.isEmpty()) {
			String nombreClase = getClaseMayoritaria(D);
			N = new SimpleNode(nombreClase);
			return N;
		}
		// 4. En otro caso:
		else {
			// 1. Aplicar el método de selección de atributos sobre los datos y
			// la lista de atributos, para encontrar el
			// mejor atributo actual A:
			// S(D, lista de atributos) -> A.
			
			AtributeSelector attrSelector = new ID3();
//			AtributeSelector attrSelector = new C45();
			
			String attr = attrSelector.selectAttr(D, attrList);
//			String attr = attrList.get(0);
			// 2. Etiquetar a N como A y eliminar A de la lista de atributos.
			N = new SimpleNode(attr);
			attrList.remove(attr);
			// 3. Para cada valor aj del atributo A:
			ArrayList<String> valoresA = attrs.get(attr);

			for (String aj : valoresA) {
				@SuppressWarnings("unchecked")
				ArrayList<String> copiaAttrList = (ArrayList<String>) attrList.clone();
				// a) Separar todas las tuplas en D para las cuales el atributo
				// A toma el valor aj
				// , creando el subconjunto de datos Dj
				ArrayList<DataTuple> Dj = separarSubconjunto(D, attr, aj);
				// b) Si Dj está vacío, añadir a N un nodo hoja etiquetado con
				// la clase mayoritaria en D.
				if (Dj.isEmpty()) {
					String nombreClase = getClaseMayoritaria(D);
					N.addChild(aj, new SimpleNode(nombreClase));
				}
				// c) En otro caso, añadir a N el nodo hijo resultante de llamar
				// a Generar_Arbol (Dj
				// , lista de atributos)
				else {
					N.addChild(aj, generarArbol(Dj, copiaAttrList));
				}
			}
			// 4. Return N.
			return N;
		}
	}

	public boolean tuplasMismaClase(ArrayList<DataTuple> data) {
		MOVE aux = data.get(0).DirectionChosen;
		for (DataTuple dataTuple : data) {
			if (dataTuple.DirectionChosen != aux) {
				return false;
			}
		}
		return true;
	}

	public String getClaseMayoritaria(ArrayList<DataTuple> data) {
		int left = 0, right = 0, up = 0, down = 0, none = 0;
		String claseMayoritaria = null;

		for (DataTuple tuple : data) {
			String clase = (tuple.DirectionChosen).toString();
			switch (clase) {
				case ("LEFT"):
					left++;
					break;
				case ("RIGHT"):
					right++;
					break;
				case ("UP"):
					up++;
					break;
				case ("DOWN"):
					down++;
					break;
				case ("NONE"):
					none++;
					break;
			}
		}

		int max = Math.max(Math.max(left, right), Math.max(Math.max(up, down), none));
		if (max == left) claseMayoritaria = "LEFT";
		else if (max == right) claseMayoritaria = "RIGHT";
		else if (max == up) claseMayoritaria = "UP";
		else if (max == down) claseMayoritaria = "DOWN";
		else claseMayoritaria = "NONE";
		return claseMayoritaria;
	}

	static public ArrayList<DataTuple> separarSubconjunto(ArrayList<DataTuple> data, String attr, String aj) {		
		ArrayList<DataTuple> res = new ArrayList<DataTuple>();
		for (DataTuple dataTuple : data) {
			if(dataTuple.getAttribute(attr).equals(aj)){
				res.add(dataTuple);
			}
		}
		return res;
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO devolver MOVE en funcion de el arbol generado con build
		return null;
	}

}