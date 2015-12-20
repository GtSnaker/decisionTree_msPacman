package decisiontree;

import java.util.ArrayList;

import dataRecording.DataTuple;

public class C45 extends AtributeSelector {
	
	@Override
	public String selectAttr(ArrayList<DataTuple> data, ArrayList<String> attrs) {
		String res = null;
		float infoD = entropy(data, "DirectionChosen");
//		System.out.println(infoD);
		
		ArrayList<Float> infoA = new ArrayList<Float>();
		ArrayList<Float> gainA = new ArrayList<Float>();
		ArrayList<Float> gainRatioA = new ArrayList<Float>();
		float splitInfoD = 0;

		//calcular infoA
		for (String attr : attrs) {
			float auxInfoA = infoA(data, attr);
			System.out.println(auxInfoA);
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
		
		//calcular gainratioA
		for (int i = 0; i < infoA.size(); i++) {
			gainRatioA.add(gainA.get(i) - splitInfoD);
		}
				
		//conseguir mas alto
		int max1 = 0;
		for (int i = 0; i < gainA.size(); i++) {
			if(gainRatioA.get(i) > max1) max1 = i;
		}
		
		res = attrs.get(max1);

		return res;
	}
	
}
