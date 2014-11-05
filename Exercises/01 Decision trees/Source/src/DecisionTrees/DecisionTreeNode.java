package DecisionTrees;

import java.util.ArrayList;

public class DecisionTreeNode {

	// Used by Numeric
	public int numericDecisionValue;
	// Used by Categorical
	public ArrayList<String> categoricalDecisoinValues;
	
	// Static counter variable is shared by all class instances.
	static int idCounter = 0;
	// Each node has an unique ID.
	public int nodeId;
	public Boolean parentTestResult;

	public DecisionTreeNode leftChild;
	public DecisionTreeNode rightChild;
	// Base decision on attribute and particular value of it
	public AttributeDescriptor attribute;

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
		System.out.printf("%d\t%s\t", 
				this.nodeId, 
				this.parentTestResult != null ? this.parentTestResult.toString() : "null" );
		
		if(this.isLeafNode) {
			System.out.printf(">>%b<<\n", this.classify);
		}
		else {
			switch(this.attribute.type) {
			case Boolean:
				System.out.printf("boolen\t\t%s\t", this.attribute.name);
				break;
			case Numeric:
				System.out.printf("numeric\t\t%s < %d\t" ,this.attribute.name, this.numericDecisionValue);
				break;
			case Categorical:
				System.out.printf("categorical\t%s in %s" ,this.attribute.name, this.categoricalDecisoinValues.toString());
				break;
			case Target:
				break;
			}

			System.out.printf("\t%d\t%d\n", 
					this.leftChild.nodeId, 
					this.rightChild.nodeId);

			this.leftChild.print();
			this.rightChild.print();
		}	
	}
}
