package decisiontree;

import java.util.ArrayList;

import dataRecording.DataTuple;

public class ID3 extends AtributeSelector {

	@Override
	public String selectAttr(ArrayList<DataTuple> data, ArrayList<String> attrs) {
		String res = null;
		float infoD = entropy(data, "DirectionChosen");
//		System.out.println(infoD);
		
		ArrayList<Float> infoA = new ArrayList<Float>();
		ArrayList<Float> gainA = new ArrayList<Float>();

		//calcular infoA
		for (String attr : attrs) {
			float auxInfoA = infoA(data, attr);
			infoA.add(auxInfoA);
		}
		
		//calcular gainA
		for (int i = 0; i < infoA.size(); i++) {
			gainA.add(infoD - infoA.get(i));
		}
		
		//conseguir mas alto
		float max = 0;
		int posMax = 0;
		
		for (int i = 0; i < gainA.size(); i++) {
			if(gainA.get(i) > max) {
				max = gainA.get(i);
				posMax = i;
			}
		}
		
		res = attrs.get(posMax);
		return res;
	}

}
