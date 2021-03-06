package kNNeighbors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;




public class Program {

	public static void main(String[] args) throws Exception {

		String inputFileName = "./data_exercise_1.csv";

		ArrayList<AttributeDescriptor> attributes = new ArrayList<>();
		ArrayList<Example> Examples = new ArrayList<>();
		
		File input = new File(inputFileName);
		BufferedReader reader = new BufferedReader(new FileReader(input));
		
		AttributeDescriptor[] descriptors = buildAttributeDescriptors(reader.readLine());
		
		for(AttributeDescriptor attDesc : descriptors) attributes.add(attDesc);

		while(true){
			String line = reader.readLine();
			if(line != null){
				// System.out.println(line);
				Example example = new Example(line, attributes);
				Examples.add(example);
			}
			else
				break;

		}

		// Exercise 5.2 (b).
		int maxK = 3;
		for (int k = 1; k<=maxK; k++){
			System.out.println("===================================");
			System.out.println("kNN with k = "+k);
			System.out.println("-----------------------------------");
			Random rand = new Random();
			for (int i = 1; i<=5; i++){
				ArrayList<Example> ExamplesCopy = new ArrayList<Example>(Examples);
				int randomInstanceIndex = rand.nextInt(ExamplesCopy.size());
				System.out.println("Choose Instance No: "+randomInstanceIndex);
				
				Example randomInstance = ExamplesCopy.get(randomInstanceIndex);
				ExamplesCopy.remove(randomInstanceIndex);
				
				ArrayList<Example> neighbors = findNearestNeighbor(randomInstance, k,
						ExamplesCopy, descriptors);
				
				boolean winnerLabel = getWinnerLabel(neighbors);
				
				System.out.println("Target Label: "+ randomInstance.getTargetValue());
				System.out.println("kNN result: "+ winnerLabel);
				System.out.println();

			}
		}


		// Exercise 5.2 (c).
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
				Example curExample = Examples.get(i);
				Examples.remove(i);
				ArrayList<Example> neighbors = findNearestNeighbor(curExample, k,
						Examples, descriptors);
				
				boolean winnerLabel = getWinnerLabel(neighbors);
				if (winnerLabel != curExample.getTargetValue())
					counter++;
				
				Examples.add(curExample);
			}
			double error = (double)counter/(double)Examples.size();
			System.out.println("Error: "+error);
		}
	}
	
	private static boolean getWinnerLabel(ArrayList<Example> neighbors){
		HashMap<Boolean, Integer> countingMap = new HashMap<>();
		countingMap.put(true, 0);
		countingMap.put(false, 0);
		for (int j = 0; j<neighbors.size(); j++){
			boolean label = neighbors.get(j).getTargetValue();

			countingMap.put(label, countingMap.get(label)+1);
		}
		return countingMap.get(true) > countingMap.get(false);
	}

	// private static boolean getWinnerLabel(ArrayList<Example> neighbors){
	// int counter = 0;
	// for (int j = 0; j<neighbors.size(); j++){
	// 	boolean label = neighbors.get(j).getTargetValue();
	// 	if(label) counter += 1;
	// 	else counter -= 1;
	// 	}
	// while(counter == 0) counter += -1 + (int)(Math.random() * ((1 - -1) + 1));
	// return counter > 0;
	// }

	// Find k nearest neighbors.
	private static ArrayList<Example> findNearestNeighbor(Example ex, int k,
		ArrayList<Example> Examples, AttributeDescriptor[] descriptors) {

		double biggestNearestDistance = 1e10;

		ArrayList<Example> nearestNeighbors = new ArrayList<>();
		for(int i = 0; i < k; i++) {
			nearestNeighbors.add(Examples.get(i));
		}

		double[] nearestDistances = new double[k];
		for(int i = 0; i < k; i++) {
			nearestDistances[i] = distance(ex, Examples.get(i), descriptors);
		}

		// Loop over all Examples to find the k nearest neighbors.
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

				// Replace the farthest of the nearest neighbors
				nearestNeighbors.set(jIdx, exTest);
				nearestDistances[jIdx] = d;

				// Update biggestNearestDistance.
				jDistMax = nearestDistances[0];
				
				for(int j = 0; j < k; j++) {
					jDist = nearestDistances[j];
					if(jDist > jDistMax) {
						jDistMax = jDist;
					}
				}
				biggestNearestDistance = jDistMax;
				// System.out.print(biggestNearestDistance);
				// System.out.print("\t");
				// System.out.print(distance(ex, nearestNeighbors.get(jIdx), descriptors));
				// System.out.print("\t");
				// System.out.println(d);
			}
		}
		// for(int i = 0; i < k; i++) {
		// 	Example exTest = nearestNeighbors.get(i);
		// 	double d = distance(ex, exTest, descriptors);
		// 	System.out.print(d + "/" + nearestDistances[i] + "\t");
		// }
		// System.out.println();
		return nearestNeighbors;
	}


	// Distance function.
	private static double distance(Example ex1, Example ex2, 
		AttributeDescriptor[] descriptors) {

		double distance = 0;

		for(int k = 0; k < descriptors.length - 1; k++) {
			AttributeDescriptor attribute = descriptors[k];

			int attIdx = attribute.position;

			switch(attribute.type) {
			case Boolean:
				if(ex1.getAttributeValue(attIdx) != ex2.getAttributeValue(attIdx)) {
					distance += 1* attribute.getWeight();
				}
				break;

			case Numeric:
				int attr1 = (int)ex1.getAttributeValue(attIdx);
				int attr2 = (int)ex2.getAttributeValue(attIdx);
				double diff = (attr1-attr2) * attribute.getWeight();
				if (diff < 0)
					diff = -diff;
				distance += diff;
				break;

			case Categorical:
				if(!ex1.getAttributeValue(attIdx).equals(ex2.getAttributeValue(attIdx))) {
					distance += 1* attribute.getWeight();
				}
				break;
			default:
				System.out.println("unexpected attribute type: " + attribute.type);
			}
		}
		// distance = Math.random();
		return distance;
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
