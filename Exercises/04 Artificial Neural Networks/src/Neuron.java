import java.util.Random;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;


public class Neuron {
	private int inputDimension;
	private double[] weights;
	private int transferFunction;
	
	public Neuron(int inputDimension){
		this.inputDimension = inputDimension;
		this.transferFunction = 0; // default transferFunction
		init();
	}
	
	
	public Neuron(int inputDimension,int transferFunction){
		this.inputDimension = inputDimension;
		this.transferFunction = transferFunction; // some other transfer function
		init();
	}
	

	
	private void init(){
		weights = new double[inputDimension+1];
		Random rand = new Random();
		for(int i=0; i< inputDimension+1;i++){
			//random weight in range [-0.5,0.5]
			weights[i]= -0.5+rand.nextDouble();
			//weights[i]= 1.0;
		}
	}
	
	public double weightedSum(double[] incomingVector)throws Exception{
		if (incomingVector.length != inputDimension)
			throw new Exception("Dimension mismatch!"
							+incomingVector.length+":"+inputDimension);
		
		double weightedSum = weights[0];
		for (int i = 0; i< inputDimension; i++){
			weightedSum += weights[i+1]*incomingVector[i];
		}
		return weightedSum;
	}
	
	public double transferFunction(double x){
		switch(transferFunction){
			case 0: // default transfer function, in this case hyperbolic tangent
				return Math.tanh(x);
			case 1: // identity
				return x;
				
			default:
				return Math.tanh(x); 
		}
	}
	
	public double process(double[] incomingVector)throws Exception{
		double weightedSum = weightedSum(incomingVector);
		return transferFunction(weightedSum);
	}
	
	public void deltaRule(double rate,double delta, double out[]){
		weights[0]+=rate*delta;
		for (int i = 0; i< inputDimension; i++){
			weights[i+1]+=rate*delta* out[i];
		}
	}
	
	public double getWeight(int k){
		return weights[k];
	}
	
	public void printWeights(){
		for (int i=0; i< inputDimension+1; i++){
			System.out.print("w_"+i+": " +weights[i]);
			System.out.print("\t\t");
		}
		System.out.println();
	}

}
