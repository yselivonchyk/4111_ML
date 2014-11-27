import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MLP {

	private final double LEARNING_RATE = 0.51;
	private int N;
	private int H;
	private int M;

	private Layer hiddenLayer;
	private Layer outputLayer;
	
	private double[][] teacherInput;
	private double[][] teacherOutput;
	
	public MLP(int inputDimension, int hiddenLayerDimension, int outputDimension ){
		
		this.N = inputDimension;
		this.M = outputDimension;
		this.H = hiddenLayerDimension;
		init();
	}
	
	private void init(){
		outputLayer =  new Layer(H,M);
		hiddenLayer = new Layer(N,H); 

	}
	
	public double[] calcOutput(double[] input) throws Exception{
		double[] temp = input;
		temp = hiddenLayer.processInput(temp);
		temp = outputLayer.processInput(temp);
		return temp;
	}
	
	private ArrayList<ArrayList<double[]>> calcOutputForTraining(double[] input) throws Exception{
		//stores the outputs of each neuron in each layer
		ArrayList<double[]>layerOuts = new ArrayList<double[]>();
		
		//stores the weighted sums (nets) of each neuron in each layer
		ArrayList<double[]>nets = new ArrayList<double[]>();
		
		double[] temp = input;
		double[] temp2 = input;
		
		//input layer
		layerOuts.add(temp);
		nets.add(temp2);

		//hidden layer
		temp2 = hiddenLayer.getNets(temp);
		temp = hiddenLayer.processInput(temp);
		layerOuts.add(temp);
		nets.add(temp2);
		
		//output layer
		temp2 =outputLayer.getNets(temp);
		temp = outputLayer.processInput(temp);		
		layerOuts.add(temp);
		nets.add(temp2);
		
		//return these results in tuple of array lists
		ArrayList<ArrayList<double[]>> tuple = new ArrayList<ArrayList<double[]>>();
		tuple.add(nets);
		tuple.add(layerOuts);
		return tuple;
	}
	

	public void train(String trainingDataPath)throws Exception{
		readTrainigdata(trainingDataPath);
		if (teacherInput[0].length != N || teacherOutput[0].length != M)
			throw new Exception("Training Data: Dimension mismatch!");
		
		int P = teacherInput.length;
		for (int p=0; p<P;p++){
			//getting p-th teacher data
			double[] inputVec = new double[N];	
			double[] teacherOutVec = new double[M];
			
			for (int n = 0; n <N ; n++){
				inputVec[n] = teacherInput[p][n];
			}
			
			for (int m = 0; m <M ; m++){
				teacherOutVec[m] = teacherOutput[p][m];
			}
			
			ArrayList<ArrayList<double[]>> tuple = calcOutputForTraining(inputVec);
			backpropagate(teacherOutVec, tuple);
		
		}
			
	}
	
	private void backpropagate(double[] teacherOut, ArrayList<ArrayList<double[]>> tuple){
		
		//get nets and outputs of eaach layer
		ArrayList<double[]> layerOuts = tuple.get(0);
		ArrayList<double[]> layerNets = tuple.get(1);
		
		
		//calculate deltas in Output Layer
		double[] outs = layerOuts.get(2);
		double[] nets = layerNets.get(2);
		double[] deltas = new double[M];
		
		//iterate through output layer
		for (int m = 0; m < M; m++){
			// case: transfer function is hyperbolic tangent
			deltas[m] = (teacherOut[m]-outs[m])*(1.0-Math.pow(outputLayer.transferFunction(nets[m]),2));
		}
		

		//calculate deltas in hidden Layer
		nets = layerNets.get(1);
		double[] deltasCur = new double[H];
		//iterate through hidden layer
		for (int h = 0; h < H; h++){
			
			//delta values are calculated from delta values of output layer 
			//and weights between hidden and output layer
			double weightedDeltaSum = 0;
			for(int m = 0; m< M ;m++)
				weightedDeltaSum += outputLayer.getWeight(h+1,m)*deltas[m];
			
			// case: transfer function is hyperbolic tangent
			deltasCur[h] = weightedDeltaSum*(1-Math.pow(outputLayer.transferFunction(nets[h]),2));
		}

		
		outputLayer.deltaRule(LEARNING_RATE, deltas, layerOuts.get(1));
		hiddenLayer.deltaRule(LEARNING_RATE, deltasCur, layerOuts.get(0));
		
	}

	private void readTrainigdata(String path){
		try {
			//read file
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line;
			bufferedReader.readLine(); //first line
			line = bufferedReader.readLine(); //second line
			
			//getting info about num of patterns, input and output dimension
			String[] splitted = line.split("\\s+");
			String lineP = splitted[1];
			String lineN = splitted[2];
			String lineM = splitted[3];
			
			//number of patterns
			splitted = lineP.split("=");
			int P = Integer.parseInt(splitted[1]);
			
			//dimension of input vector
			splitted = lineN.split("=");
			int N = Integer.parseInt(splitted[1]);
			
			//dimension of output vector
			splitted = lineM.split("=");
			int M = Integer.parseInt(splitted[1]);
			
			//creating arrays to store data
			teacherInput = new double[P][N];
			teacherOutput = new double[P][M];
			
			//read file
			int p = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] tempSplit = line.split("\\s+");
				if (tempSplit.length == N+M+1){
					for (int n = 0; n<N; n++){
						teacherInput[p][n] = Double.parseDouble(tempSplit[n+1]);
					}
					for (int m = 0; m<M; m++){
						teacherOutput[p][m] = Double.parseDouble(tempSplit[N+m+1]);
					}
					p++;
				}	
			}
			fileReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printWeights(){
		System.out.println("===================================");
		System.out.println("Hidden Layer "+":");
		hiddenLayer.printWeights();
		
		System.out.println("===================================");
		System.out.println("Output Layer:");
		outputLayer.printWeights();
	}
	
	
}
