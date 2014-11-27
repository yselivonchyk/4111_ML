public class Layer {
	private boolean isInputLayer;
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
		if(isInputLayer){
			for (int i=0; i<size;i++){
				out[i] = input[i];
			}
			return out;
		}
		
		for (int i=0; i<size;i++){
			out[i] = neurons[i].process(input);
		}
		return out;
	}
	
	public double[] getNets(double[] input) throws Exception{
		double[] out = new double[size];
		if(isInputLayer){
			for (int i=0; i<size;i++){
				out[i] = input[i];
			}
			return out;
		}
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
	
	public double getWeight(int h, int g){
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
