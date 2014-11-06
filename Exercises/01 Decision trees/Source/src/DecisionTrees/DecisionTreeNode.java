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

	//reference to root of tree
	public DecisionTreeNode root;
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
		root = this;
		nodeId = idCounter;
		idCounter++;
		isLeafNode = false;
	}
	
	//adds left child
	public void addLeftChild(DecisionTreeNode leftChild){
		this.leftChild = leftChild;
		this.leftChild.root = this.root;
	}
	
	//adds right child
	public void addRightChild(DecisionTreeNode rightChild){
		this.rightChild = rightChild;
		this.rightChild.root = this.root;
	}
	
	public boolean decide(Example example){
		//start from root
		DecisionTreeNode temp = this.root;
		
		boolean decision = true;
		boolean boolUsed = false; // to check if decision was actually used
		
		//Traverse Tree 
		while (!temp.isLeafNode){
			 boolUsed = false;
			int index = temp.attribute.position;
			switch(temp.attribute.type){
			case Numeric:
				double val = (double)((Integer)example.getAttributeValue(index));
				decision = (val < numericDecisionValue);
				boolUsed = true;
				break;
			case Boolean:
				decision = (boolean)example.getAttributeValue(index);
				boolUsed = true;
				break;
			case Categorical:
				String category = (String)example.getAttributeValue(index);
				String categorySet = temp.categoricalDecisoinValues.toString();
				decision = categorySet.contains(category);
				boolUsed = true;
				break;
			case Target:
				boolUsed = true;
				throw new IllegalArgumentException("Target Attribute in non Leaf node!!");
			}
			if (boolUsed)
				temp = decision ? temp.leftChild : temp.rightChild; 
			else
				throw new IllegalArgumentException("Trapped in Loop - check attributes");
			
		}
		return decision;
		
	}
	
	//TODO: writing print to text file 
	
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
