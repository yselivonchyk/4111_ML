package DecisionTrees;

// Class that describes single experiment i.e. contains attribute values and results (target value)
public class Example {
	public String[] values;
	
	public Example (String fileData){
		values = fileData.split(",");
	}
}
