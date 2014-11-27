public class Layer {
	private int inputDimension;
	private int size;
	private Neuron[] neurons;
		
	
	public Layer(int inputDimension, int size) {
		this.inputDimension = inputDimension;
		this.size = size;
		init();
	}
	
	private void init(){
		neurons = new Neuron[size];
		for(int i = 0; i<size;i++){
			neurons[i] = new Neuron(inputDimension,0);
		}
	}
	
	public double[] processInput(double[] input) throws Exception{
		double[] out = new double[size];
		
		for (int i=0; i<size;i++){
			out[i] = neurons[i].process(input);
		}
		return out;
	}
	
	public double[] getNets(double[] input) throws Exception{
		double[] out = new double[size];
		for (int i=0; i<size;i++){
			out[i] = neurons[i].weightedSum(input);
		}
		return out;
	}
	
	public double transferFunction(double x){
		return neurons[0].transferFunction(x);
	}

	public void deltaRule(double rate, double[] deltas, double[] outs){
		for (int i = 0; i< size; i++){
			neurons[i].deltaRule(rate, deltas[i], outs);
		}
	}
	
	//gets weight from (g-1)-th neuron from upper layer to h-th neuron in this layer 
	public double getWeight(int g, int h){
		return neurons[h].getWeight(g);
	}
	
	public void printWeights(){
		for (int i=0; i< size; i++){	
			System.out.println("Neuron "+i+":");
			neurons[i].printWeights();
			System.out.println();
		}
	}
}
