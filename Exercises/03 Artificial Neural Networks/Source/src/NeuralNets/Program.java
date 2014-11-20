package NeuralNets;

import java.util.ArrayList;

public class Program {
	public static ArrayList<Example> learning = new ArrayList<>();

	
	public static void main(String[] args) {
		fillInExamples();
		
		Network n = new Network(1);
		
		
		System.out.println(n.test(learning));
	}


	public static void fillInExamples() {
		//x1 ∧ (x2 ∨ x3)
		learning.add(new Example(new boolean[]{true, true, true}, true));
		learning.add(new Example(new boolean[]{true, false, true}, true));
		learning.add(new Example(new boolean[]{true, true, false}, true));
		learning.add(new Example(new boolean[]{true, false, false}, false));
		learning.add(new Example(new boolean[]{false, true, true}, false));
		learning.add(new Example(new boolean[]{false, true, false}, false));
		learning.add(new Example(new boolean[]{false, false, true}, false));
		learning.add(new Example(new boolean[]{false, false, false}, false));
	}

}
