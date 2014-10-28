package DecisionTrees;

import java.io.*;
import java.util.ArrayList;

public class Program {

	private static ArrayList attributes = new ArrayList();
	
	public static void main(String[] args) throws IOException {
		
		File input = new File("./data_exercise_1.csv");
		BufferedReader reader = new BufferedReader(new FileReader(input));
		
		AttributeDescriptor[] descriptors = BuildAttributeDescriptors(reader.readLine());
		
		while(true){
			String line = reader.readLine();
			if(line != null)
				System.out.println(line);
			else
				break;
		}
	}

	private static AttributeDescriptor[] BuildAttributeDescriptors(
			String descriptionLine) {
		// Split original string into parts dedicated to each attribute
		String[] attributes = descriptionLine.split(",");
		
		AttributeDescriptor[] result = new AttributeDescriptor[attributes.length];
		for(int i = 0; i < attributes.length; i++)
		{
			AttributeDescriptor descriptor = new AttributeDescriptor();
			
			// First part of attribute description contains Attribute name: "a:n" -> "a"
			descriptor.name = attributes[i].split(":")[0];
			descriptor.type = ConvertLetterToAttributeType(attributes[i].split(":")[1]);
			result[i] = descriptor;
			System.out.println(attributes[i]);
		}
		
		return null;
	}

	private static AttributeType ConvertLetterToAttributeType(String letter) {
		switch(letter){
		case "n":
			return AttributeType.Numeric;
		case "c":
			return AttributeType.Catagorical;
		case "t":
			return AttributeType.Target;
			//default:
				//throw new 
		}
	}
}
