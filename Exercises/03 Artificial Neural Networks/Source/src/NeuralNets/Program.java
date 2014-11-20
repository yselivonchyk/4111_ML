package NeuralNets;

import java.util.ArrayList;

public class Program {
	public static ArrayList<Example> learning = new ArrayList<>();

	
	public static void main(String[] args) {
		fillInExamples();
		
		double[] ns = new double[]{0.0001, 0.001, 0.01, 0.05, 0.1, 0.2, 0.4, 0.6, 0.8, 1};
		for(double step : ns){
			System.out.println();
			Network n = new Network(step);
			n.train(learning);
			n.test(learning);
		}
	}


	public static void fillInExamples() {
		//x1 ∧ (x2 ∨ x3)
		learning.add(new Example(new boolean[]{false, false, false}, false));
		learning.add(new Example(new boolean[]{false, false, true}, false));
		learning.add(new Example(new boolean[]{false, true, false}, false));
		learning.add(new Example(new boolean[]{false, true, true}, false));
		learning.add(new Example(new boolean[]{true, false, false}, false));
		learning.add(new Example(new boolean[]{true, true, false}, true));
		learning.add(new Example(new boolean[]{true, false, true}, true));
		learning.add(new Example(new boolean[]{true, true, true}, true));
	}

}
