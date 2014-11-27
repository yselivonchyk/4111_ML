import java.util.Random;



public class SimpleXOR {
	double rate;

	int N;								//N dimensional input
	int H;								//H hidden neurons
	int M;								//M dimensional output
	
	double[][] hiddenWeights;			//weights from input to hidden layer	
	double[][] outputWeights;			//weights from hidden to output layer
	double[] input;						//input buffer
	double[] hiddenOuts;				//buffer of outputs of hidden layer
	double[] hiddenNets;				//buffer of nets (weighted sums) of hidden layer
	double[] outputOuts;				//buffer of outputs of output layer
	double[] outputNets;				//buffer of nets (weighted sums) of output layer
	
	double[][]hiddenWeightChange;		//weight change arrays for batch learning 
	double[][]outputWeightChange;
	
	double[] outputDelta;				//buffers for delta values
	double[] hiddenDelta;
	
	int counter;						//counter for averaging in batch learning mode
	
	//Constructor
	public SimpleXOR(int n, int h, int m, double rate) {
		this.rate = rate;
		N = n;
		H = h;
		M = m;
		init();
	}
	
	//initializes everything necessary
	private void init() {
		hiddenWeights = new double[N+1][H];
		outputWeights = new double[H+1][M];
		hiddenOuts = new double[H];
		hiddenNets= new double[H];
		outputOuts = new double[M];
		outputNets= new double[M];
		
		hiddenWeightChange = new double[N+1][H];
		outputWeightChange= new double[H+1][M];
		
		hiddenDelta = new double[H];
		outputDelta  = new double[M];
		
		counter = 0;
		
		Random rand = new Random();
		for(int h =0; h<H; h++){
			for(int n =0; n<=N; n++){
				hiddenWeights[n][h]= -0.5+rand.nextDouble();
				hiddenWeightChange[n][h]= 0;
			}
		}
		
		for(int m =0; m<M; m++){
			for(int h =0; h<=H; h++){
				outputWeights[h][m] = -0.5+rand.nextDouble();
				outputWeightChange[h][m]= 0;
			}
		}
		
	}
	
	//process step of hidden layer using input buffer
	private void processHiddenLayer(){
		for(int h=0; h<H;h++){
			hiddenNets[h]= 0;
			hiddenOuts[h]= 0;
		}
		for(int h=0; h<H;h++){
			for(int n=0; n<N;n++){	
				hiddenNets[h]+= input[n]*hiddenWeights[n][h];
			}
			hiddenNets[h]+= hiddenWeights[N][h];
			hiddenOuts[h]= Math.tanh(hiddenNets[h]);
		}
	}
	
	//process step of output layer using hidden buffer
	private void processOutputLayer(){
		for(int m=0; m<M;m++){
			outputNets[m]= 0;
			outputOuts[m]= 0;
		}
		for(int m=0; m<M;m++){
			for(int h=0; h<H;h++){	
				outputNets[m]+= hiddenOuts[h]*outputWeights[h][m];
			}
			outputNets[m]+= outputWeights[H][m];
			outputOuts[m]= Math.tanh(outputNets[m]);
		}
	}
	
	//calculates output of MLP given an input
	public double[] processAll(double[] input){
		this.input =input;
		processHiddenLayer();
		processOutputLayer();
		return outputOuts;
	}
	
	//calculates delta values for output layer
	public void getDeltaOutput(double[] output){
		for (int m = 0; m < M; m++){
			double diff = (output[m]-outputOuts[m]);
			outputDelta[m] = diff*(1.0-Math.pow(Math.tanh(outputNets[m]),2));
		}
	}
	
	//calculates delta values for hidden layer
	//getDeltaOutput is a prerequisite!!
	public void getDeltaHidden(){
		for (int h = 0; h < H; h++){		
			//delta values are calculated from delta values of output layer 
			//and weights between hidden and output layer
			double weightedDeltaSum = 0;
			for(int m = 0; m< M ;m++)
				weightedDeltaSum += outputWeights[h][m]*outputDelta[m];
			
			hiddenDelta[h] = weightedDeltaSum*(1-Math.pow(Math.tanh(hiddenNets[h]),2));
		}
	}
	
	//cumulates weight changes in arrays
	public void cumulateWeights(){
		for(int h =0; h<H; h++){
			for(int n =0; n<N; n++){
				hiddenWeightChange[n][h]+= rate*hiddenDelta[h]*input[n];
			}
			hiddenWeightChange[N][h]+= rate*hiddenDelta[h];
		}
		
		for(int m =0; m<M; m++){
			for(int h =0; h<H; h++){
				outputWeightChange[h][m]+= rate*outputDelta[m]*hiddenOuts[h];
			}
			outputWeightChange[H][m]+= rate*outputDelta[m];
		}
		//counts how often cumulation is used
		counter++;
	}
	
	//backpropagation
	public void backpropagate(double output[]){
		getDeltaOutput(output);
		getDeltaHidden();
		cumulateWeights();
	}
	
	//applies weight change by means of change arrays
	public void updateWeights(){
		
		for(int h =0; h<H; h++){
			for(int n =0; n<=N; n++){
				hiddenWeights[n][h]+= hiddenWeightChange[n][h]/counter;
				hiddenWeightChange[n][h]= 0;
			}
		}
		
		for(int m =0; m<M; m++){
			for(int h =0; h<=H; h++){
				outputWeights[h][m]+= outputWeightChange[h][m]/counter;
				outputWeightChange[h][m]= 0;
			}
		}
	
		counter = 0;	
	}
	
	//trains MLP for XOR
	public void trainXOR(){
	
		//create training data
		double[] input1 = {0,0};
		double[] input2 = {0,1};
		double[] input3 = {1,0};
		double[] input4 = {1,1};
		
		double[] output1 = {-1};
		double[] output2 = {1};
		double[] output3 = {1};
		double[] output4 = {-1};
		
		int counter = 1;
		boolean notGoodEnough=true;
		while(notGoodEnough){

			processAll( input1);
			backpropagate(output1);
			
			//calculate averaged squared error
			double error1 = 0;
			for(int m =0; m<M;m++)
				error1+=Math.pow(output1[m]-outputOuts[m], 2);
			error1 /= 2;
			
			
			processAll(input2);
			backpropagate(output2);
			
			//calculate averaged squared error
			double error2 = 0;
			for(int m =0; m<M;m++)
				error2+=Math.pow(output2[m]-outputOuts[m], 2);
			error2 /= 2;

			processAll( input3);
			backpropagate(output3);
			
			//calculate averaged squared error
			double error3 = 0;
			for(int m =0; m<M;m++)
				error3+=Math.pow(output3[m]-outputOuts[m], 2);
			error3 /= 2;

			processAll( input4);
			backpropagate(output4);
			
			//calculate averaged squared error
			double error4 = 0;
			for(int m =0; m<M;m++)
				error4+=Math.pow(output4[m]-outputOuts[m], 2);
			error4 /= 2;
			
			//optional batch learning
			if(counter%1 == 0){
				updateWeights();
			}
			counter++;
			
			double error = (error1+error2+error3+error4)/4;
			//termination criterion
			notGoodEnough = (error > Math.pow(10, -3))&& (counter < Math.pow(10, 6));
		}
	}
	
	//prints information about current state of MLP
	public void print(){
		System.out.println("===================================================");
		System.out.println(N+"-"+H+"-"+M+"- Multi Layer Perceptron");
		System.out.println("---------------------------------------------------");
		System.out.println("Hidden Layer: ");
		for(int n =0; n<=N; n++){
			for(int h =0; h<H; h++){
				System.out.print("w_("+n+","+h+"): "+hiddenWeights[n][h] +"\t");
			}
			System.out.print("\n");
		}
		
		System.out.println();
		System.out.println("Output Layer: ");
		for(int h =0; h<=H; h++){
			for(int m =0; m<M; m++){
				System.out.print("w_("+h+","+m+"): "+outputWeights[h][m] +"\t");
			}
			System.out.print("\n");
		}
		System.out.println("===================================================");
	}

}
