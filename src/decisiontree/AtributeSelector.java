package decisiontree;

import java.util.ArrayList;
import dataRecording.DataTuple;

public abstract class AtributeSelector {
	
	public String selectAttr(ArrayList<DataTuple> data, ArrayList<String> attrs) {return null;}
	
	public float entropy(ArrayList<DataTuple> data, String attr) {
		float info = 0;
		ArrayList<String> values = null;
		if(attr == "DirectionChosen") {
			values = DecisionTree.DirectionChosen;
		} else {
			values = DecisionTree.attrs.get(attr);
		}
		
		for (String value : values) {
			float pi = calculatePi(data, attr, value);
			info += pi*log2(pi);
		}
		return (-info);
	}
	
	public float infoA(ArrayList<DataTuple> data, String attr){
		float info = 0;
		ArrayList<String> values = DecisionTree.attrs.get(attr);
		for(String value : values){
			ArrayList<DataTuple> dt = DecisionTree.separarSubconjunto(data, attr, value);
			info += (dt.size() / data.size()) * entropy(dt, "DirectionChosen");
		}
		return info;
	}
	
	public float calculatePi(ArrayList<DataTuple> data, String attr, String value) {
		float pi = 0;
		if(data.size() > 0) {
			for (DataTuple dataTuple : data) {
				if (dataTuple.getAttribute(attr).equals(value)) {
					pi++;
				}
			}
			pi /= data.size();
		}
		return pi;
	}

	float log2(float x) {
		if(x == 0) return 0;
		float res = (float)(Math.log(x) / Math.log(2));
		return res;
	}

}
