package DecisionTrees;

import java.util.ArrayList;

public class AttributeDescriptor {
	public AttributeType type;
	public String name;
	public int position;
	
	// Used by Numeric
	public int decisionValues;
	// Used by Categorical
	public ArrayList values;
}
