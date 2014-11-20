package NeuralNets;

public class Example {
	public boolean[] values = new boolean[3]; 
	public boolean result;
	
	public Example(boolean[] values, boolean result){
		this.values = values;
		this.result = result;
	}
	
	public double test(double[] weights){
		double result = 0;
		for(int i = 0; i < values.length; i++){
			result += values[i] ? weights[i] : 0;
		}
		return result;
	}
}
