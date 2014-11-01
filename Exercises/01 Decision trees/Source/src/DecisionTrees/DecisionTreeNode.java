package DecisionTrees;

public class DecisionTreeNode {

	// Static counter variable is shared by all class instances.
	static int idCounter = 0;
	// Each node has an unique ID.
	public int nodeId;
	public Boolean parentTestResult;

	public DecisionTreeNode leftChild;
	public DecisionTreeNode rightChild;
	// Base decision on attribute and particular value of it
	public AttributeDescriptor attribute;
	public Object value;	// This is already implemented in AttributeDescriptor!

	// If the node does not branch into children, then it is a leaf node that
	// classifies to a target value.
	public Boolean isLeafNode;
	public Boolean classify;

	// Constructor; isLeafNode defaults to false.
	public DecisionTreeNode() {
		nodeId = idCounter;
		idCounter++;
		isLeafNode = false;
	}
}
