import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MLP {

	private final double LEARNING_RATE = 0.7;
	private int N;
	private int[] Hs;
	private int M;
	private int L;
	
//	private Layer inputBuffer;
	private ArrayList<Layer> hiddenLayers;
	private Layer outputLayer;
	
	private double[][] teacherInput;
	private double[][] teacherOutput;
	
	public MLP(int inputDimension, int outputDimension, int... hiddenLayerDimensions ){
		
		this.N = inputDimension;
		this.M = outputDimension;
		this.Hs = hiddenLayerDimensions;
		this.L = Hs.length;
		init();
	}
	
	private void init(){
//		inputBuffer = new Layer(true,1,N);
		outputLayer =  new Layer(Hs[Hs.length-1],M);
		hiddenLayers = new ArrayList<Layer>(L); 
		

		Layer firstHiddenLayer = new Layer(N,Hs[0]);	
		hiddenLayers.add(firstHiddenLayer);
		
		for (int l = 1; l<L;l++ ){
			Layer hiddenLayer = new Layer(Hs[l-1],Hs[l]);
			hiddenLayers.add(hiddenLayer);
		}
	}
	
	public double[] calcOutput(double[] input) throws Exception{
		//double[] temp = inputBuffer.processInput(input);
		double[] temp = input;
		for (int l = 0; l<L; l++){
			temp = hiddenLayers.get(l).processInput(temp);
		}
		temp = outputLayer.processInput(temp);
		return temp;
	}
	
	private ArrayList<ArrayList<double[]>> calcOutputForTraining(double[] input) throws Exception{
		ArrayList<double[]>layerOuts = new ArrayList<double[]>();
		ArrayList<double[]>nets = new ArrayList<double[]>();
		double[] temp = input;
		double[] temp2 = input;
		layerOuts.add(temp);
		nets.add(temp2);
		for (int l = 0; l<L; l++){
			temp2 = hiddenLayers.get(l).getNets(temp);
			temp = hiddenLayers.get(l).processInput(temp);		
			layerOuts.add(temp);
			nets.add(temp2);
		}
		temp2 =outputLayer.getNets(temp);
		temp = outputLayer.processInput(temp);		
		layerOuts.add(temp);
		nets.add(temp2);
		ArrayList<ArrayList<double[]>> tuple = new ArrayList<ArrayList<double[]>>();
		tuple.add(nets);
		tuple.add(layerOuts);
		return tuple;
	}
	
	public void printWeights(){
//		System.out.println("===================================");
	//	System.out.println("Input Layer: ");
//		inputBuffer.printWeights();
		for (int i=0; i< L  ; i++){
			System.out.println("===================================");
			System.out.println("Hidden Layer "+i+":");
			hiddenLayers.get(i).printWeights();
		}
		System.out.println("===================================");
		System.out.println("Output Layer:");
		outputLayer.printWeights();
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
		ArrayList<double[]> layerOuts = tuple.get(0);
		ArrayList<double[]> layerNets = tuple.get(1);
		//weightChange in Output Layer
		double[] outs = layerOuts.get(layerOuts.size() -1);
		double[] nets = layerNets.get(layerNets.size() -1);
		double[] deltas = new double[M];
		
		ArrayList<double[]> layerDeltas = new ArrayList<double[]>();
		
		for (int m = 0; m < M; m++){
			// case: transfer function is hyperbolic tangent
			deltas[m] = (teacherOut[m]-outs[m])*(1-Math.pow(outputLayer.transferFunction(nets[m]),2));
		}
		layerDeltas.add(deltas);
		
		//last hiddenLayer
		nets = layerNets.get(layerNets.size() -2 );
		double[] deltasCur = new double[Hs[L-1]];
		for (int h = 0; h < Hs[L-1]; h++){
			double weightedDeltaSum = 0;
			for(int k = 0; k< M ;k++)
				weightedDeltaSum += getWeight(L-1,h, k)*deltas[k];
			// case: transfer function is hyperbolic tangent
			deltasCur[h] = weightedDeltaSum*(1-Math.pow(outputLayer.transferFunction(nets[h]),2));
		}
		deltas = deltasCur;
		layerDeltas.add(deltas);
		
		//hidden Layers
		for (int i = L-2; i>=0; i-- ){
			nets = layerNets.get(layerNets.size() -2 -(L-1-i));
			deltasCur = new double[Hs[i]];
			for (int h = 0; h < Hs[i]; h++){
				double weightedDeltaSum = 0;
				for(int k = 0; k<Hs[i+1];k++)
					weightedDeltaSum += getWeight(i,h, k)*deltas[k];
				// case: transfer function is hyperbolic tangent
				deltasCur[h] = weightedDeltaSum*(1-Math.pow(outputLayer.transferFunction(nets[h]),2));
			}
			deltas = deltasCur;
			layerDeltas.add(deltas);		
		}
		
		outputLayer.deltaRule(LEARNING_RATE, layerDeltas.get(0), layerOuts.get(layerOuts.size() -2));
		hiddenLayers.get(L-1).deltaRule(LEARNING_RATE, layerDeltas.get(1), layerOuts.get(layerOuts.size() -3));
		
		for (int i = L-2; i>=0; i-- ){
			hiddenLayers.get(i).deltaRule(LEARNING_RATE, layerDeltas.get(2+(L-2-i)), layerOuts.get(layerOuts.size() -3 -(L-1-i)));	
		}

	}
	
	private double getWeight(int l,int h, int k){
		Layer layer;
		if (l<L-1)
			layer = hiddenLayers.get(l+1);
		else
			layer = outputLayer;
		return layer.getWeight(k, h+1);
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
	
	
}
