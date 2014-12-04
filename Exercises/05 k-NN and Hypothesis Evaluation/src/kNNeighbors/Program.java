package kNNeighbors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;


public class Program {

	public static void main(String[] args) throws Exception {

		String inputFileName = "./data_exercise_1.csv";

		ArrayList<AttributeDescriptor> attributes = new ArrayList<>();
		//ArrayList<Example> trainingExamples = new ArrayList<>();
		ArrayList<Example> Examples = new ArrayList<>();
		
		File input = new File(inputFileName);
		BufferedReader reader = new BufferedReader(new FileReader(input));
		
		AttributeDescriptor[] descriptors = buildAttributeDescriptors(reader.readLine());
		
		for(AttributeDescriptor attDesc : descriptors) attributes.add(attDesc);

		while(true){
			String line = reader.readLine();
			if(line != null){
				System.out.println(line);
				Example example = new Example(line, attributes);
				Examples.add(example);
			}
			else
				break;

		}

		// TODO: Exercise 5.2 (b) here.
		int maxK = 3;
		for (int k = 1; k<=maxK; k++){
			System.out.println("===================================");
			System.out.println("Iteration k = "+k);
			System.out.println("-----------------------------------");
			Random rand = new Random();
			for (int i = 1; i<=5; i++){
				ArrayList<Example> ExamplesCopy = Examples;
				int randomInstanceIndex = rand.nextInt(ExamplesCopy.size());
				System.out.println("Choose Instance No: "+randomInstanceIndex);
				
				Example randomInstance = ExamplesCopy.get(randomInstanceIndex);
				ExamplesCopy.remove(randomInstanceIndex);
				
				ArrayList<Example> neighbors = findNearestNeighbor(randomInstance, k,
						ExamplesCopy, descriptors);
				
				boolean winnerLabel = getWinnerLabel(neighbors);
				
				System.out.println("Target Label: "+ randomInstance.getTargetValue());
				System.out.println("kNN Label: "+ winnerLabel);

			}
		}



		// TODO: Exercise 5.3 (c) here.
		// Leave One Out Validation;
		System.out.println("###################################################");
		System.out.println("Leave One Out Validation");
		System.out.println("###################################################");
		for (int k = 1; k<=3; k++){
			System.out.println("===================================");
			System.out.println("kNN: k = "+k);
			System.out.println("-----------------------------------");	
			int counter = 0;
			for (int i = 0; i< Examples.size(); i++){
				Example curExample = Examples.get(0);
				Examples.remove(0);
				ArrayList<Example> neighbors = findNearestNeighbor(curExample, k,
						Examples, descriptors);
				
				boolean winnerLabel = getWinnerLabel(neighbors);
				if (winnerLabel == curExample.getTargetValue())
					counter++;
				
				Examples.add(curExample);
			}
			double error = (double)counter/(double)Examples.size();
			System.out.println("Error: "+error);
		}
	}
	
	private static boolean getWinnerLabel(ArrayList<Example> neighbors){
		HashMap<Boolean, Integer> countingMap = new HashMap<>();
		for (int j = 0; j<neighbors.size(); j++){
			boolean label = neighbors.get(j).getTargetValue();
			if (countingMap.containsKey(label)){
				countingMap.put(label, countingMap.get(label)+1);
			}else{
				countingMap.put(label, 1);
			}
		}

		return countingMap.get(true) > countingMap.get(false);

	}

	// TODO: Find k nearest neighbors. Untested!
	private static ArrayList<Example> findNearestNeighbor(Example ex, int k,
		ArrayList<Example> Examples, AttributeDescriptor[] descriptors) {


		double biggestNearestDistance = 1e10;
		double[] nearestDistances = new double[k];
		for(int i = 0; i < k; i++) {
			nearestDistances[i] = 1e10;
		}
		
		ArrayList<Example> nearestNeighbors = new ArrayList<>();
		for(int i = 0; i < k; i++) {
			nearestNeighbors.add(Examples.get(i));
		}

		// Loop over all Examples to find the k nearest neigbors.
		for(int i = k; i < Examples.size(); i++) {
			Example exTest = Examples.get(i);
			double d = distance(ex, exTest, descriptors);

			if(d < biggestNearestDistance) {

				// Find the farthest of the nearest neighbors.
				int jIdx = 0;
				double jDistMax = nearestDistances[0];
				double jDist;

				for(int j = 0; j < k; j++) {
					jDist = nearestDistances[j];
					if(jDist > jDistMax) {
						jDistMax = jDist;
						jIdx = j;
					}
				}

				// Replace the farthest of the nearest neighbors.
				nearestNeighbors.set(jIdx, exTest);
				nearestDistances[jIdx] = d;

				// Update biggestNearestDistance.
				jDistMax = nearestDistances[0];
								biggestNearestDistance = d;
				for(int j = 0; j < k; j++) {
					jDist = nearestDistances[j];
					if(jDist > jDistMax) {
						jDistMax = jDist;
					}
				}
				biggestNearestDistance = jDistMax;				
			}
		}	

		return nearestNeighbors;
	}


	// TODO: Distance function.
	private static double distance(Example ex1, Example ex2, 
		AttributeDescriptor[] descriptors) {

		for(AttributeDescriptor attribute : descriptors) {
			switch(attribute.type) {
			case Boolean:
				break;
			case Numeric:
				break;
			case Categorical:
					break;
			default:
					System.out.println("unexpected attribute type: " + attribute.type);
			}
		}

		return 42;
	}


	private static AttributeDescriptor[] buildAttributeDescriptors(
			String descriptionLine) throws Exception {
		// Split original string into parts dedicated to each attribute
		String[] attributes = descriptionLine.split(",");
		
		AttributeDescriptor[] result = new AttributeDescriptor[attributes.length];
		for(int i = 0; i < attributes.length; i++)
		{
			AttributeDescriptor descriptor = new AttributeDescriptor();
			
			// First part of attribute description contains Attribute name: "a:n" -> "a"
			descriptor.name = attributes[i].split(":")[0];
			// second part contains attribute type
			descriptor.type = convertLetterToAttributeType(attributes[i].split(":")[1]);
			descriptor.position = i;
			result[i] = descriptor;
		}
		return result;
	}


	private static AttributeType convertLetterToAttributeType(String letter) throws Exception {
		switch(letter){
		case "n":
			return AttributeType.Numeric;
		case "c":
			return AttributeType.Categorical;
		case "b":
			return AttributeType.Boolean;
		case "t":
			return AttributeType.Target;
		default:
				throw new Exception("Unknown attribute type: " + letter);
		}
	}

}
