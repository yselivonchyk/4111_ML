package DecisionTrees;

import java.io.IOException;
import java.io.OutputStreamWriter;
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

	// reference to root of tree
	public DecisionTreeNode root;
	public DecisionTreeNode parent;
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

	// adds leftChild child
	public void addLeftChild(DecisionTreeNode leftChild) {
		this.leftChild = leftChild;
		this.leftChild.root = this;
		this.leftChild.root = this.root;
	}

	// adds rightChild child
	public void addRightChild(DecisionTreeNode rightChild) {
		this.rightChild = rightChild;
		this.rightChild.parent = this;
		this.rightChild.root = this.root;
	}

	public boolean testExample(Example example) throws Exception{
		if(this.isLeafNode)
			return this.classify;
		
		Object value = example.getAttributeValue(this.attribute.position);
		boolean leftBranch = false;
			switch(this.attribute.type){
			case Numeric:
				double val = (double)(int)value;
				leftBranch = (val < numericDecisionValue);
				break;
			case Boolean:
				leftBranch = (boolean)value;
				break;
			case Categorical:
				String categorySet = this.categoricalDecisoinValues.toString();
				leftBranch = categorySet.contains((String)value);
				break;
			case Target:
				throw new Exception("Unexpected usage of Target attribute within decision tree");
			}
			
		return (leftBranch ? this.leftChild : this.rightChild)
				.testExample(example);
	}

	// TODO: writing print to text file

	// Prints the decision table to the screen in a basic format.
	public void print(OutputStreamWriter out) throws Exception {
		out.write(String.format(
				"%d %s ",
				this.nodeId,
				this.parentTestResult != null 
				? (this.parentTestResult ? "yes" : "no")
						 : "null"));

		if (this.isLeafNode) {
			out.write(String.format("%b\n", this.classify));
		} else {
			switch (this.attribute.type) {
			case Boolean:
				out.write(String.format("%s", this.attribute.name));
				break;
			case Numeric:
				out.write(String.format("%s < %d", this.attribute.name,
						this.numericDecisionValue));
				break;
			case Categorical:
				out.write(String.format("%s in %s", this.attribute.name,
						this.categoricalDecisoinValues.toString()).replace('[', '{').replace(']', '}'));
				break;
			case Target:
				break;
			}

			out.write(String.format(" %d %d\n", this.leftChild.nodeId,
					this.rightChild.nodeId));

			this.leftChild.print(out);
			this.rightChild.print(out);
		}
	}
	
	public void printTree(OutputStreamWriter out) throws IOException {
        if (rightChild != null) {
            rightChild.printTree(out, true, "");
        }
        printNodeValue(out);
        if (leftChild != null) {
            leftChild.printTree(out, false, "");
        }
    }
	
    private void printNodeValue(OutputStreamWriter out) throws IOException {
        if (toString() == null) {
            out.write("<null>");
        } else {
            out.write(toString());
        }
        out.write('\n');
    }
    
    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
    private void printTree(OutputStreamWriter out, boolean isRight, String indent) throws IOException {
        if (rightChild != null) {
            rightChild.printTree(out, true, indent + (isRight ? "        " : " |      "));
        }
        out.write(indent);
        if (isRight) {
            out.write(" /");
        } else {
            out.write(" \\");
        }
        out.write("----- ");
        printNodeValue(out);
        if (leftChild != null) {
            leftChild.printTree(out, false, indent + (isRight ? " |      " : "        "));
        }
    }

    // Used only for print purpose
    public boolean isRightChild(){
    	if(this.parent == null)
    		return false;
    	else
    		return (this.parent.rightChild == this);
    }
    
    // used only for print purpose
    public String toString(){
    	if(this.isLeafNode)
    		return "[" + this.classify.toString() + "]";
    	switch (this.attribute.type) {
		case Boolean:
			return this.attribute.name;
		case Numeric:
			return this.attribute.name + " < " + this.numericDecisionValue;
		case Categorical:
			return this.attribute.name + " in " +  this.categoricalDecisoinValues.toString();
		default:
			return "You look pretty today";
		}
    }
}
