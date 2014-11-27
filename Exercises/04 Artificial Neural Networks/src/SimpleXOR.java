import java.util.Random;



public class SimpleXOR {
	final double LEARNING_RATE = 0.5;

	int N;
	int H;
	int M;
	
	double[][] hiddenWeights;
	double[][] outputWeights;
	double[] input;
	double[] hiddenOuts;
	double[] hiddenNets;
	double[] outputOuts;
	double[] outputNets;
	
	double[][]hiddenWeightChange;
	double[][]outputWeightChange;
	
	double[] outputDelta;
	double[] hiddenDelta;
	
	int counter;
	
	public void print(){
		System.out.println("===================================================");
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
	
	public SimpleXOR(int n, int h, int m) {
		N = n;
		H = h;
		M = m;
		init();
	}
	
	public void init() {
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
	
	public void processHiddenLayer(){
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
	
	public void processOutputLayer(){
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
	
	public double[] processAll(double[] input){
		this.input =input;
		processHiddenLayer();
		processOutputLayer();
		return outputOuts;
	}
	
	public void getDeltaOutput(double[] output){
		for (int m = 0; m < M; m++){
			double diff = (output[m]-outputOuts[m]);
			outputDelta[m] = diff*(1.0-Math.pow(Math.tanh(outputNets[m]),2));
		}
	}
	
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
	
	public void backpropagate(double output[]){
		getDeltaOutput(output);
		getDeltaHidden();
		cumulateWeights();
	}
	
	public void cumulateWeights(){
		for(int h =0; h<H; h++){
			for(int n =0; n<N; n++){
				hiddenWeightChange[n][h]+= LEARNING_RATE*hiddenDelta[h]*input[n];
			}
			hiddenWeightChange[N][h]+= LEARNING_RATE*hiddenDelta[h];
		}
		
		for(int m =0; m<M; m++){
			for(int h =0; h<H; h++){
				outputWeightChange[h][m]+= LEARNING_RATE*outputDelta[m]*hiddenOuts[h];
			}
			outputWeightChange[H][m]+= LEARNING_RATE*outputDelta[m];
		}
		counter++;
		
	}
	
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
	
	public void trainXOR(){
		
		double[] input1 = {0,0};
		double[] input2 = {0,1};
		double[] input3 = {1,0};
		double[] input4 = {1,1};
		
		double[] output1 = {-1};
		double[] output2 = {1};
		double[] output3 = {1};
		double[] output4 = {-1};
		
		boolean notGoodEnough=true;
		int counter = 1;
		while(notGoodEnough){

			processAll( input1);
			backpropagate(output1);
			
			
			processAll(input2);
			backpropagate(output2);

			processAll( input3);
			backpropagate(output3);

			processAll( input4);
			backpropagate(output4);
			
			//optional batch learning
			if(counter%1 == 0){
				updateWeights();
			}
			counter++;
			
			double error = 0;
			for(int m =0; m<M;m++)
				error+=Math.pow(output4[m]-outputOuts[m], 2);
			error /= M;
			notGoodEnough = (error > Math.pow(10, -4))&& (counter < Math.pow(10, 10));
		}
	}

}
