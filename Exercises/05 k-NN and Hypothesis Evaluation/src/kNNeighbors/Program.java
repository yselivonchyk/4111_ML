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

		Example ex1 = Examples.get(0);
		Example ex2 = Examples.get(1);
		double d = distance(ex1, ex2, descriptors);
		System.out.println(d);
		
		int maxK = 3;
		for (int k = 1; k<=maxK; k++){
			Random rand = new Random();
			for (int i = 1; i<=5; i++){
				ArrayList<Example> ExamplesCopy = Examples;
				int randomInstanceIndex = rand.nextInt(ExamplesCopy.size());
				
				Example randomInstance = ExamplesCopy.get(randomInstanceIndex);
				ExamplesCopy.remove(randomInstanceIndex);
				
				ArrayList<Example> neighbors = findNearestNeighbor(randomInstance, k,
						ExamplesCopy, descriptors);
				
				boolean winnerLabel = getWinnerLabel(neighbors);
				
				
			}
		}



		// TODO: Exercise 5.3 (c) here.
		
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

	// TODO: Find k nearest neighbors.
	private static ArrayList<Example> findNearestNeighbor(Example ex, int k,
		ArrayList<Example> Examples, AttributeDescriptor[] descriptors) {

		double biggestNearestDistance = 1e10;
		double[] nearestDistances = new double[k];
		for(int i = 0; i < k; i++) {
			nearestDistances[i] = 1e10;
		}

		int N = Examples.size();
		for(int i = 0; i < N; i++){
			Example exTest = Examples.get(i);
			double d = distance(ex, exTest, descriptors);

			if(d < biggestNearestDistance) {
				;
			}
		}

		ArrayList<Example> nearestNeighbors = new ArrayList<>();
		return nearestNeighbors;
	}


	// TODO: Distance function.
	private static double distance(Example ex1, Example ex2, 
		AttributeDescriptor[] descriptors) {

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
