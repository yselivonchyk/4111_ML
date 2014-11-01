package DecisionTrees;

public class DecisionTreeNode {
	public DecisionTreeNode leftChild;
	public DecisionTreeNode rightChild;
	// Base decision on attribute and particular value of it
	public AttributeDescriptor attribute;
	public Object value;

	// If the node does not branch into children, then it is a leaf node that
	// classifies to a target value.
	public Boolean isLeafNode;
	public Boolean classify;

	// Constructor; isLeafNode defaults to false.
	public DecisionTreeNode() {
		isLeafNode = false;
	}
}
