package kNNeighbors;

import java.util.ArrayList;
import java.util.HashMap;

public class AttributeDescriptor {
	public static double[] weights = new double[]{1, 1.1, 0.9, 1.1, 1, 1.0, 1.0, 1.0, 1};
	
	public AttributeType type;
	public String name;
	public int position;
	
	private double deviation = -1;
	private ArrayList<Integer> numericList = new ArrayList<Integer>();
	private HashMap<String, String> categoricalList = new HashMap<>();
	
	
	public void registerEntry(Object input){
		switch(type){
		case  Numeric:
			int value = (int)input;
			numericList.add(value);
		case Categorical:
			String stringValue = input.toString();
			if(!categoricalList.containsKey(stringValue))
				categoricalList.put(stringValue, stringValue);
		case Boolean:
		case Target:
		default:
			return;
		}
	}
	
	public double getWeight(){
		switch(type){
		case  Numeric:
			if(deviation > 0)
				return 1.0/deviation;
			// calculate mean
			int sum = 0;
			for(int i = 0; i < numericList.size(); i++){
				sum += numericList.get(i);
			}
			double mean = sum / (double) numericList.size();
			// calculate deviation
			sum = 0;
			for(int i = 0; i < numericList.size(); i++){
				sum += (mean - numericList.get(i))*(mean - numericList.get(i));
			}
			deviation = Math.sqrt(sum/(double) numericList.size());
			return 1.0/deviation;
		case Categorical:
			//System.out.println(categoricalList.size());
			return 2/(double)categoricalList.size();
		case Boolean:
		case Target:
		default:
			return weights[position];
		}
	}

}
