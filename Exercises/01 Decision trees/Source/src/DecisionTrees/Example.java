package DecisionTrees;
import java.util.ArrayList;

// Class that describes single experiment i.e. contains attribute values and results (target value)
public class Example {
	public ArrayList<Object> attributeValues;

	// Constructor; populates the attributeValues ArrayList with Objects.
	public Example (String fileData, ArrayList<AttributeDescriptor> attributes)
		throws Exception {
		attributeValues = new ArrayList<Object>();
		String[] values;
		values = fileData.split(",");

		// Loop over attributes and convert boolean and numeric types.
		for(int j = 0; j < attributes.size(); j++) {
			AttributeDescriptor jDescriptor = attributes.get(j);
			String jValue = values[j];

			attributeValues.add(ConvertToType(jDescriptor, jValue));
		}
	}

	public Boolean GetTargetValue() {
		Object t = attributeValues.get(attributeValues.size() - 1);
		// TODO: make this safer.
		return (Boolean)t;
	}

	public Object GetAttributeValue(int index) {
		// TODO: make this safer.
		return attributeValues.get(index);
	}

	// Convert String type values to Objects based on an AttributeDescriptor.
	private Object ConvertToType(AttributeDescriptor attDesc, String value) throws Exception {
		switch(attDesc.type){
		case  Numeric:
			return Integer.parseInt(value);
		case Categorical:
			return value;
		case Boolean:
		case Target:
			return value.equals("yes");
		default:
			throw new Exception("Unknown attribute type: " + attDesc.type);
		}
	}
}
