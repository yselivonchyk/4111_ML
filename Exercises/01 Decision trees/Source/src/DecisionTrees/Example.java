package DecisionTrees;
import java.util.ArrayList;

// Class that describes single experiment i.e. contains attribute values and results (target value)
public class Example {
	public ArrayList<Object> attributeValues;

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

	private Object ConvertToType(AttributeDescriptor attDesc, String value) {
		Object result = 0; // Compiler wants the return value to be initialized.

		if(attDesc.type == AttributeType.Numeric){
			result = Integer.parseInt(value);
		}

		else if(attDesc.type == AttributeType.Boolean) {
			if(value.equals("yes")) {
				result = true;
			}
			else if(value.equals("no")) {
				result = false;
			}
		}

		else if(attDesc.type == AttributeType.Categorical) {
			result = value;
		}

		else if(attDesc.type == AttributeType.Target) {
			if(value.equals("yes")) {
				result = true;
			}
			else if(value.equals("no")) {
				result = false;
			}
		}
		else {
			; //TODO: This should not be reached, throw exeption.
		}
		return result;
	}
}
