package DecisionTrees;

import java.io.*;
import java.util.*;

public class Program {
	public static void main(String[] args) throws Exception {
		buildTreeAndCheckCorrectness("./data_exercise_1.csv", true);
		//buildTreeAndCheckCorrectness("./bool_1.csv", false);
		//buildTreeAndCheckCorrectness("./numeric_1.csv", false);
		//buildTreeAndCheckCorrectness("./categorical_1.csv", false);
	}	// End of main

	private static void buildTreeAndCheckCorrectness(String inputFileName, boolean checkCorrectness)
			throws FileNotFoundException, Exception, IOException {
		ArrayList<AttributeDescriptor> attributes = new ArrayList<>();
		ArrayList<Example> trainingExamples = new ArrayList<>();
		ArrayList<Example> testExamples = new ArrayList<>();
		
		File input = new File(inputFileName);
		BufferedReader reader = new BufferedReader(new FileReader(input));
		
		AttributeDescriptor[] descriptors = buildAttributeDescriptors(reader.readLine());
		
		for(AttributeDescriptor attDesc : descriptors) attributes.add(attDesc);
		
		//instantiate random number generator
		Random randGen = new Random();
		int randomInt;
		while(true){
			String line = reader.readLine();
			if(line != null){
				//System.out.println(line);
				Example example = new Example(line, attributes);
				//generate random integer 0 <=randomInt <= 8
			    randomInt = randGen.nextInt(9);
				
			    if (randomInt < 6 || !checkCorrectness)  
			    	trainingExamples.add(example);
			    else 
			    	testExamples.add(example);
			}
			else
				break;
		}

		// Builds the decision tree by recursively calling itself.
		DecisionTreeNode decisionTree = branchDecisionTree(attributes, trainingExamples);	
		printResults(decisionTree);
		
		System.out.println("Training data correctness result:");
		checkCorrectness(decisionTree, trainingExamples);
		
		if(checkCorrectness){
			System.out.println("Test data correctness:");
			checkCorrectness(decisionTree, testExamples);
		}
	}

	private static void printResults(DecisionTreeNode decisionTree)
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {
		
		String outputFile = "output.txt";
		
		OutputStreamWriter out = new OutputStreamWriter(
		          new FileOutputStream(outputFile), "utf-8");
		decisionTree.printTree(out);
		out.close();
		
		File input = new File(outputFile);
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line = null;
		while((line = reader.readLine()) != null)
			System.out.println(line);
		reader.close();
	}

	private static void checkCorrectness(DecisionTreeNode decisionTree, ArrayList<Example> testExamples) throws Exception {
		int numTests = testExamples.size();
		int numPosTests = evaluate(decisionTree, testExamples);
		double percentage = ((int)(numPosTests/(float)numTests * 10000 +0.5))/100.0;
		System.out.println("== Evalutation ===");
		System.out.println(numPosTests+" out of "+ numTests+" tests were correct, i.e. "+percentage+"%");
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

	// Returns the logarithm with respect to base 2.
	// Note: log() returns the natural logarithm.
	private static double log2(double arg) {
		return Math.log(arg)/Math.log(2.0d);
	}

	/*
	Returns the information gain for a binary test with binary target value.
	Function arguments (n_ij naming scheme):
	n11: Number of "yes" target values given a true test result.
	n12: Number of "yes" target values given a false test result.
	n21: Number of "no" target values given a true test result.
	n22: Number of "no" target values given a false test result.
	*/
	private static double informationGain(int n11, int n12, int n21, int n22) {

		int nTot = n11 + n12 + n21 + n22;

		// if (nTot == 0) throw new Exception("Function call to " +
		// 	"InformationGain() with all zero arguments");

		// Calculate target value probabilities (p_i's).
		int nTarget1 = n11 + n12;
		double pTarget1 = (double)nTarget1/nTot;
		double pTarget2 = 1 - pTarget1;

		// Calculate attribute value probabilities (p_j's).
		int nAttribute1 = n11 + n21;
		double pAttribute1 = (double)nAttribute1/nTot;
		double pAttribute2 = 1 - pAttribute1;

		// Calculate target value probabilities conditioned on attribute value
		// (p_ij's).
		double p11 = (double)n11 / nAttribute1;
		double p21 = (double)n21 / nAttribute1;
		double p12 = (double)n12 / (nTot - nAttribute1);
		double p22 = (double)n22 / (nTot - nAttribute1);

		double entropy = 0;
		// Handle zero probabilities separately in entropy calculations.
		if(pTarget1 != 0) entropy += pTarget1 * log2(1/pTarget1);
		if(pTarget2 != 0) entropy += pTarget2 * log2(1/pTarget2);

		double conditionalEntropy = 0;
		// Handle zero probabilities separately.
		if(nAttribute1 != 0) {
			if(p11 !=0) conditionalEntropy += pAttribute1 * p11 * log2(1/p11);
			if(p21 !=0) conditionalEntropy += pAttribute1 * p21 * log2(1/p21);
		}
		if(nTot - nAttribute1 != 0) {
			if(p12 !=0) conditionalEntropy += pAttribute2 * p12 * log2(1/p12);
			if(p22 !=0) conditionalEntropy += pAttribute2 * p22 * log2(1/p22);
		}

		return entropy - conditionalEntropy;
	}
	


	private static DecisionTreeNode branchDecisionTree(
		ArrayList<AttributeDescriptor> attributes,
		ArrayList<Example> examples) {

		DecisionTreeNode decisionTreeNode = new DecisionTreeNode();

		double informationGain = 0;
		double maxInformationGain = 0;

		// Index of the information gain maximizer attribute w.r.t 
		// ArrayList<AttributeDescriptor> attributes.
		int maxAttributeIndex = 0; 

		Boolean targetValue;
		Boolean testValue;
		double	numericTestValue=0;
		ArrayList<String> categoricalDecisionValues = null;

		// Counter variables to estimate probabilities.
		int nTrueTargetTrueTest;
		int nTrueTargetFalseTest;
		int nFalseTargetTrueTest;
		int nFalseTargetFalseTest;	

		// Loop over all attributes except the target and find the one that
		// yields the biggest information gain.
		for(int k = 0; k < attributes.size() - 1; k++) {

			AttributeDescriptor kAttribute = attributes.get(k);

			nTrueTargetTrueTest = 0;
			nTrueTargetFalseTest = 0;
			nFalseTargetTrueTest = 0;
			nFalseTargetFalseTest = 0;		

			// The test used depends on the attribute type.
			switch(kAttribute.type){
			case Boolean:
				for(Example example : examples) {

					targetValue = example.getTargetValue();
					testValue = (Boolean)example.getAttributeValue(
						kAttribute.position);

					// Increment the counter-variables.
					if(targetValue == true && testValue == true){
						nTrueTargetTrueTest++;
					}
					else if(targetValue == true && testValue == false){
						nTrueTargetFalseTest++;
					}
					else if(targetValue == false && testValue == true){
						nFalseTargetTrueTest++;
					}
					if(targetValue == false && testValue == false){
						nFalseTargetFalseTest++;
					}
				}
				informationGain = 
					informationGain(nTrueTargetTrueTest, nTrueTargetFalseTest,
					nFalseTargetTrueTest, nFalseTargetFalseTest);

				if(informationGain > maxInformationGain) {
					maxInformationGain = informationGain;
					maxAttributeIndex = k;
				}
				break;
			case Numeric:
				// Bubble sort:
				int j;
				boolean flag=true;
			
				Example example1;
				Example example2;
				while (flag) {
					flag=false;
					for(j=0;j<examples.size()-1;j++) {
						example1 = examples.get(j);
						example2 = examples.get(j+1);
						if ((int)example1.getAttributeValue(kAttribute.position) 
								> (int)example2.getAttributeValue(kAttribute.position)) {
							examples.set(j,example2);
							examples.set(j+1,example1);
							flag=true;
						}
					}
						
				}
				// end of sort
				
				// we increase the values of nTrueTargetFalseTest, nFalseTargetFalseTest,
				// because at the 
				// start all the test values are false
				for(Example example : examples) {
					if (example.getTargetValue() == true) {
						nTrueTargetFalseTest++;
					}
					else {
						nFalseTargetFalseTest++;
					}
				}
				
				Example tempExample;
				
				j=0;
				for(Example example : examples) {
					if (j>0){
						tempExample = examples.get(j-1);
						// Increment the countervariables.
						if(tempExample.getTargetValue() == true){
							nTrueTargetTrueTest++;
							nTrueTargetFalseTest--;
						}
						else {
							nFalseTargetTrueTest++;
							nFalseTargetFalseTest--;
						}
						
						if(example.getTargetValue()
								!=tempExample.getTargetValue()){
							informationGain = 
								informationGain(nTrueTargetTrueTest, nTrueTargetFalseTest,
								nFalseTargetTrueTest, nFalseTargetFalseTest);
							if(informationGain > maxInformationGain) {
								maxInformationGain = informationGain;
								maxAttributeIndex = k;
								numericTestValue = 
									((int)example.getAttributeValue(kAttribute.position)
										+(int)tempExample.getAttributeValue(kAttribute.position))/2;
							}
						}
					}
					j++;
				}
				break;
			case Categorical:
				HashMap<String,Vector<Integer>> valuesStatistics = new HashMap<>();
				int totalPositive = 0;
				int totalNegative = 0;
				
				// >> Count positive/negative tests for each attribute value
				for(Example example : examples) {
					String attrValue = (String)example.getAttributeValue(kAttribute.position);
					// initialize statistics record if it didn;t appear yet
					if(!valuesStatistics.containsKey(attrValue)){
						Vector<Integer> initial = new Vector<Integer>(2);
						initial.add(0);
						initial.add(0);
						valuesStatistics.put(attrValue, initial);
					}
					// increment total counter and value specific counter
					Vector<Integer> record = valuesStatistics.get(attrValue);
					if (example.getTargetValue()) {
						record.set(0, record.get(0) + 1); 
						totalPositive++;
					}
					else {
						record.set(1, record.get(1) + 1); 
						totalNegative++;
					}
				}
				
				// Get total number of possible combinations
				Set<String> values = valuesStatistics.keySet();
				int nValues = values.size();
				Object[] valuesArray = values.toArray();
				// total number of combinations. Exclude overflow value and value that considers every element.
				int combinations = (int)Math.pow(2, nValues) - 1 - 1;
				
				// >> iterate through each possible combination
				while(combinations > 0){
					int currentPositive = 0;
					int currentNegative = 0;
					ArrayList<String> currentDecision = new ArrayList<>();
					
					// >> Build combination of values based on combination number
					int combinationsCopy = combinations;
					for(int i = 0; i < nValues && combinationsCopy > 0; i++){
						boolean takeValueAtI = (combinationsCopy % 2) == 1;
						if(takeValueAtI){
							currentDecision.add((String)valuesArray[i]);
							Vector<Integer> statisticsToAdd = valuesStatistics.get(valuesArray[i]);
							currentPositive += statisticsToAdd.get(0);
							currentNegative += statisticsToAdd.get(1);
						}
						combinationsCopy = combinationsCopy / 2;
					}
					
					// >> Calculate gain and update maximum gain if needed
					double gain = informationGain(
							currentPositive, 
							currentNegative, 
							totalPositive - currentPositive, 
							totalNegative - currentNegative);
					//System.out.printf("combination %5d \t gain:  %10.9f \t%s\n", combinations,  gain, currentDecision.toString());
					
					if(gain > maxInformationGain) {
						maxInformationGain = gain;
						maxAttributeIndex = k;
						categoricalDecisionValues = currentDecision;
						
					}
					
					combinations--;
				}		
				break;
			default:
					System.out.println("unexpected attribute type: " + kAttribute.type);
			}
		}

		if(maxInformationGain > 0) {
			System.out.printf("New maximum information gain (%s, %s)", 
					attributes.get(maxAttributeIndex).type.toString(),
					attributes.get(maxAttributeIndex).name.toString());
			System.out.println("\n" + maxInformationGain);
			System.out.println();
			
			
			decisionTreeNode.attribute = attributes.get(maxAttributeIndex);

			attributes.remove(maxAttributeIndex);

			// Split the examples to left and right branch. Left side examples
			// give a true test result. Right side examples give a false test
			// result.
			ArrayList<Example> lExamples = new ArrayList<>();
			ArrayList<Example> rExamples = new ArrayList<>();

			// Perform the split in case of a boolean maximizing attribute.
			switch(decisionTreeNode.attribute.type) {
			case Boolean:
				for(Example example : examples) {
					testValue = (Boolean)example.getAttributeValue(
						decisionTreeNode.attribute.position);
					if(testValue == true) lExamples.add(example);
					else rExamples.add(example);
				}
				break;
			case Numeric:
				int tempTestValue;
				decisionTreeNode.numericDecisionValue = (int) numericTestValue;
				for(Example example : examples) {
					tempTestValue = (int)example.getAttributeValue(
							decisionTreeNode.attribute.position);
					if(tempTestValue < numericTestValue) lExamples.add(example);
					else rExamples.add(example);
						
				}
				break;
			case Categorical:
				decisionTreeNode.categoricalDecisoinValues = categoricalDecisionValues;
				for(Example example : examples) {
					String testCatValue = (String)example.getAttributeValue(
							decisionTreeNode.attribute.position);
					
					(categoricalDecisionValues.contains(testCatValue) 
						? lExamples 
						: rExamples)
					.add(example);
				}
				break;
			case Target:
				System.out.println("Target Attribute can not be the best gain decision!!!");
			}


			// TODO: Perform the split for non-boolean maximizing attributes.

			decisionTreeNode.addLeftChild( branchDecisionTree(attributes,
				lExamples));
			decisionTreeNode.addRightChild ( branchDecisionTree(attributes,
				rExamples));
			decisionTreeNode.leftChild.parentTestResult = true;
			decisionTreeNode.rightChild.parentTestResult = false;
		}

		else if(maxInformationGain == 0) {
			// This happens if either attributes only holds the 
			// target attribute, the node already classifies perfectly or the
			// entropy can not be decreases by any attribute.
			
			// Count target values and make this node a leaf node.
			decisionTreeNode.isLeafNode = true;
			int nTrueTargets = 0;
			int nFalseTargets = 0;
			for(Example example : examples) {
				if(example.getTargetValue() == true) nTrueTargets++;
				else nFalseTargets++;
			}
			if(nTrueTargets >= nFalseTargets) decisionTreeNode.classify = true;
			else decisionTreeNode.classify = false;
		}

		return decisionTreeNode;
	}
	
	
	
	private static int evaluate(DecisionTreeNode decisionTree, ArrayList<Example> testExamples) throws Exception{

		int numPosTests = 0;
		
		for (Example test: testExamples){
			boolean targetResult = (boolean) test.getTargetValue();
			boolean myResult = decisionTree.testExample(test);
			numPosTests = myResult == targetResult ? numPosTests +1 : numPosTests;
		}
		
		return numPosTests;
		
	}
}
