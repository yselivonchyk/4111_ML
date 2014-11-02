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
	
	// Prints the decision table to the screen in a basic format.
	public void print(){
		System.out.print(this.nodeId);
		System.out.print("\t");
		System.out.print(this.parentTestResult);
		System.out.print("\t");

		if(this.isLeafNode) {
			System.out.println(this.classify);
		}
		else {
			if(this.attribute.type == AttributeType.Boolean) {
				System.out.print("b");
			}
			else {
				System.out.print("x");
			}

			System.out.print("\t");
			System.out.print(this.leftChild.nodeId);
			System.out.print("\t");
			System.out.print(this.rightChild.nodeId);
			System.out.println();

			this.leftChild.print();
			this.rightChild.print();
		}	
	}
}
