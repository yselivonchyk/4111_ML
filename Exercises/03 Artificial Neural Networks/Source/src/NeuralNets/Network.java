package NeuralNets;

import java.util.ArrayList;
import java.util.Arrays;

// Simple neural network consisting single neuron
public class Network {
	// 3 input weights and 1 bias
	public double[] weights = new double[4];
	public double n = 0;
	public int iterationsCounter = -1;
	
	// n - gradient step
	public Network(double n){
		this.n = n;
		
		for(int i = 0; i < weights.length; i++){
			weights[i] = 0.5;//Math.random() - 0.5;
		}
	}
	
	//Train network
	public void train(ArrayList<Example> examples){
		boolean everythingIsOK = false;
		while(true){
			everythingIsOK = true;
			iterationsCounter++;
			
			for(Example ex : examples){
				// If example result does not correspond to network expectation
				// then change weights associated with Xi if Xi is involved in evaluation
				if(!testExample(ex, weights)){
					everythingIsOK = false;
					double delta = ex.result ? 1.0 : -1.0;
					
					for(int i = 0; i < ex.values.length; i++)
						weights[i] += n*delta*(ex.values[i] ? 1 : 0);
					// Aaaand update the bias
					weights[3] += n*delta;
				}
			}
			// termination condition
			if(everythingIsOK || iterationsCounter > 1000000) return;
		}
	}
	
	// Calculate portion or correct neuron executions
	public double test(ArrayList<Example> examples){
		double testres = test(examples, this.weights);
		System.out.println(String.format("\ntest: %d%%  iterations: %d\tn: %5.4f", (int)testres*100/1, iterationsCounter, n));
		for(int i = 0; i < weights.length; i++) System.out.print(String.format("%4.3f, ", weights[i]));
		return testres;
	}
	
	// Calculate portion or correct neuron executions
	private double test(ArrayList<Example> examples, double[] weights){
		int positiveResults = 0;
		for(Example ex : examples){
			if(testExample(ex, weights))
				positiveResults++;
		}
		return positiveResults / (double) examples.size();
	}
	
	//define whether network return correct result for single neuron
	public boolean testExample(Example ex, double[] weights){
		double result = 0;
		for(int i = 0; i < ex.values.length; i++){
			result += ex.values[i] ? weights[i] : 0;
		}
		return (result + weights[3] > 0) == ex.result;
	}
	
	//get neuron output weight
	public double getOutputWeight(Example ex, double[] weights){
		double result = 0;
		for(int i = 0; i < ex.values.length; i++){
			result += ex.values[i] ? weights[i] : 0;
		}
		return result;
	}
}
