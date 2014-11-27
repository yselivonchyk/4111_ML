
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//test data
		double[] input1 = {0,0};
		double[] input2 = {0,1};
		double[] input3 = {1,0};
		double[] input4 = {1,1};
		
		for (int i = 1; i < 11; i++){
			System.out.println("Network No "+i+":");
			
			long startTime = System.currentTimeMillis();
			
			//Create MLP
			SimpleXOR network = new SimpleXOR(2, 2, 1, 0.1*i );
			
			/*
			//initial network properties
			network.print();
			
			//initial output
			System.out.println("Initial output:");
			System.out.println("(0,0): "+network.processAll(input1)[0]);
			System.out.println("(0,1): "+network.processAll(input2)[0]);
			System.out.println("(1,0): "+network.processAll(input3)[0]);
			System.out.println("(1,1): "+network.processAll(input4)[0]);
			*/
			
			//train MLP
			network.trainXOR();
			
			/*
			//network properties after training
			System.out.println();
			System.out.println("Network after training:");
			*/
			network.print();
			
			//output after training
			System.out.println("Output after training:");
			System.out.println("(0,0): "+network.processAll(input1)[0]);
			System.out.println("(0,1): "+network.processAll(input2)[0]);
			System.out.println("(1,0): "+network.processAll(input3)[0]);
			System.out.println("(1,1): "+network.processAll(input4)[0]);
			
			System.out.println("####################################################");
			long stopTime = System.currentTimeMillis();
		    long elapsedTime = stopTime - startTime;
		    System.out.println("Elapsed Time: "+elapsedTime+"ms");
		    System.out.println("Learning rate: "+i*0.1);
		    System.out.println("####################################################");
		    System.out.println();
		    System.out.println();
		}
	}
}
