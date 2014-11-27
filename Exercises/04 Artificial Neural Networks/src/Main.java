
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MLP network = new MLP(2, 1, 3 );
		try {
			network.train("trainXOR.pat-1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		network.printWeights();
		
		double[] input1 = {0,0};
		double[] input2 = {0,1};
		double[] input3 = {1,0};
		double[] input4 = {1,1};
		
		try {
			System.out.println(network.calcOutput(input1)[0]);
			System.out.println(network.calcOutput(input2)[0]);
			System.out.println(network.calcOutput(input3)[0]);
			System.out.println(network.calcOutput(input4)[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
