package NeuralNets;

import java.util.ArrayList;

// Simple neural network consisting single neuron
public class Network {
	// 3 input weights and 1 bias
	public double[] weights = new double[4];
	public double n = 0;
	
	// n - gradient step
	public Network(double n){
		this.n = n;
		
		for(int i = 0; i < weights.length; i++){
			weights[i] = 1;
		}
	}
	
	//Train network
	public void train(ArrayList<Example> examples){
		double[] deltaW = new double[4];
		
		double originalError = 1.0 - test(examples);
	}
	
	// Calculate portion or correct neuron executions
	public double test(ArrayList<Example> examples){
		return test(examples, this.weights);
	}
	
	// Calculate portion or correct neuron executions
	private double test(ArrayList<Example> examples, double[] weights){
		int positiveResults = 0;
		for(Example ex : examples){
			if((ex.getTestValue(this.weights) >= weights[3]) == ex.result)
				positiveResults++;
		}
		System.out.println(positiveResults + " " + examples.size());
		return positiveResults / (double) examples.size();
	}
}
