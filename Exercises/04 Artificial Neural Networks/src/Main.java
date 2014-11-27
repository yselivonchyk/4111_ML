
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MLP network = new MLP(2, 1, 3 );
		try {
			double[] input1 = {0,0};
			double[] input2 = {0,1};
			double[] input3 = {1,0};
			double[] input4 = {1,1};
			
			System.out.println(network.calcOutput(input1)[0]);
			System.out.println(network.calcOutput(input2)[0]);
			System.out.println(network.calcOutput(input3)[0]);
			System.out.println(network.calcOutput(input4)[0]);
			
			network.train("trainXOR.pat-2");
			System.out.println("==================");
			System.out.println(network.calcOutput(input1)[0]);
			System.out.println(network.calcOutput(input2)[0]);
			System.out.println(network.calcOutput(input3)[0]);
			System.out.println(network.calcOutput(input4)[0]);
			
			network.printWeights();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}


}
