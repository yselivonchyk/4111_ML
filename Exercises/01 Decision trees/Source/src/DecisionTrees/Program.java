package DecisionTrees;

import java.io.*;
import java.util.ArrayList;

public class Program {
	private static ArrayList<AttributeDescriptor> attributes = new ArrayList<>();
	private static ArrayList<Example> examples = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		
		File input = new File("./data_exercise_1.csv");
		BufferedReader reader = new BufferedReader(new FileReader(input));
		
		AttributeDescriptor[] descriptors = BuildAttributeDescriptors(reader.readLine());
		
		while(true){
			String line = reader.readLine();
			if(line != null){
				examples.add(new Example(line));
				System.out.println(line);
			}
			else
				break;
		}
	}

	private static AttributeDescriptor[] BuildAttributeDescriptors(
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
			descriptor.type = ConvertLetterToAttributeType(attributes[i].split(":")[1]);
			descriptor.position = i;
			result[i] = descriptor;
			System.out.println(attributes[i]);
		}
		
		return result;
	}

	private static AttributeType ConvertLetterToAttributeType(String letter) throws Exception {
		switch(letter){
		case "n":
			return AttributeType.Numeric;
		case "c":
			return AttributeType.Catagorical;
		case "b":
			return AttributeType.Boolean;
		case "t":
			return AttributeType.Target;
		default:
				throw new Exception("Unknown attribute type: " + letter);
		}
	}
}
