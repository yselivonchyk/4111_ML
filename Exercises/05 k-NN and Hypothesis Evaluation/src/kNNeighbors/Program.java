package kNNeighbors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


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



		// TODO: Exercise 5.3 (c) here.
		
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
