package net.sf.jclec.exprtree;

import java.util.Iterator;

import net.sf.jclec.JCLEC;

/**
 * Expression tree.
 * 
 * @author Sebastian Ventura
 */

public class ExprTree implements JCLEC
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
		
	private static final long serialVersionUID = -6257654283422181797L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Prefix expression code
	/////////////////////////////////////////////////////////////////

	/** This expression code */
	
	private IPrimitive [] blocks = new IPrimitive[10];
	
	/** Actual block index */
	
	private int blockIndex = 0;
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------------- Constructor
	/////////////////////////////////////////////////////////////////

	/**
	 * Constructor
	 */
	
	public ExprTree() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Expression blocks
	
	/**
	 * Add a block to this expression.
	 * 
	 * @param block Block to add
	 */
	
	public void addBlock(IPrimitive block)
	{
		if (blockIndex == blocks.length) {
			IPrimitive [] aux = new IPrimitive[2*blockIndex];
			for (int i=0; i<blockIndex; i++) 
				aux[i] = blocks[i];
			blocks = aux;
		}
		blocks[blockIndex++] = block;
	}

	/**
	 * Access to block in this expression tree.
	 * 
	 * @param blockIndex Block index
	 * 
	 * @return Desired block
	 */
	
	public IPrimitive getBlock(int blockIndex)
	{
		return blocks[blockIndex];
	}
	
	/**
	 * Set a block in the expression tree.
	 * 
	 * @param block New block 
	 * @param blockIndex Index of desired block
	 */
	
	public void setBlock(IPrimitive block, int blockIndex)
	{
		blocks[blockIndex] = block;
	}
	
	// Expression size
	
	/**
	 * Expression tree size.
	 * 
	 * @return Number of blocks in this tree
	 */
	
	public int size()
	{
		return blockIndex;
	}
	
	// Copy method
	
	/**
	 * Copy method.
	 * 
	 * @return A copy of this expression tree
	 */
	
	public ExprTree copy()
	{
		// Copy code
		IPrimitive [] codeCopy = new IPrimitive [blocks.length];
		for (int i=0; i<blockIndex; i++) 
			codeCopy[i] = blocks[i].copy();
		// Copy expression
		ExprTree result = new ExprTree();
		// Set codeIndex
		result.blockIndex = blockIndex;
		// Set code
		result.blocks = codeCopy;
		// Return result
		return result;
	}
	
	// Subtrees location
	
	/**
	 * Return the index corresponding to the node inmediately after 
	 * the subtree which root is rootIndex. 
	 * 
	 * If rootIndex = 0, this method should return this.size()+1 
	 * 
	 * @param rootIndex Index of subtree root
	 * 
	 * @return Index of node inmediately after this subtree
	 */
	
	public int subTree(int rootIndex)
	{
		int resultIndex = rootIndex;
		// Number of remaining sons
		int aux = 0;
		do {	
			try {
				// Increase aux to the actual node arity
				aux += blocks[resultIndex].argumentTypes().length;
				// 	Increment resultIndex
				resultIndex++;
				}
			catch (NullPointerException e) {
				System.out.println("tree="+this.toString());				
				System.out.println("blockIndex="+rootIndex);							
			}
		}		
		while (aux-- != 0);
		return resultIndex;
	}
	
	// Execution method
	
	/**
	 * Return a postorder traversal iterator. 
	 * 
	 * @return An postorder traversal iterator 
	 */
	
	public Iterator<IPrimitive> executeIterator()
	{
		return new ExecuteIterator();
	}	
	
	// java.lang.Object methods
	
	/**
	 * {@inheritDoc}
	 */
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("(");
		for (int i=0; i<blockIndex; i++) {
			sb.append(blocks[i]);
			if (i != blockIndex-1) {
				sb.append(" ");
			}
			else {
				sb.append(")");
			}
		}
		return sb.toString();
	}
	
//	public String toString()
//	{
//		StringBuffer sb = new StringBuffer();
//		
//		printSubtree(blocks, 0, sb);
//		
//		return sb.toString();
//	}
//
//	private int printSubtree (IPrimitive [] blocks, int root, StringBuffer sb)
//	{
//		// Subtree root symbol
//		IPrimitive rootSymbol = blocks[root];
//		// Number of sons for root
//		int rootSons = rootSymbol.argumentTypes().length;
//		// If root is a terminal
//		if (rootSons == 0) {
//			sb.append(rootSymbol.toString());
//			return 0;
//		}
//		else {
//			// Return value
//			int jump=0;
//			// Add "("
//			sb.append("(");
//			sb.append(rootSymbol.toString());
//			for (int i=1; i<=rootSons; i++) {
//				jump = printSubtree(blocks, (root+i)+jump, sb);
//			}
//			sb.append(")");
//			return jump+rootSons;
//		}
//	}
	
//	private transient int pindex = 0;
//	
//	public String toString()
//	{
//		pindex = 0;
//		StringBuffer sb = new StringBuffer();
//		toStringRecurse(sb);
//		return sb.toString();
//	}
//	
//	private void toStringRecurse(StringBuffer sb)
//	{
//		// Subtree root symbol
//		IPrimitive rootSymbol = blocks[pindex++];
//		// Number of sons for root
//		int rootSons = rootSymbol.argumentTypes().length;
//		// If root is a terminal
//		if (rootSons == 0) {
//			sb.append(rootSymbol.toString());
//		}
//		else {
//			// Add "("
//			sb.append("(");
//			sb.append(rootSymbol.toString());
//			for (int i=0; i<rootSons; i++) {
//				toStringRecurse(sb);
//			}
//			sb.append(")");
//		}
//	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean equals(Object other)
	{
		if (other instanceof ExprTree) {
			ExprTree cother = (ExprTree) other;
			if (blockIndex == cother.blockIndex) {
				for (int i=0; i<blockIndex; i++) {
					if (! blocks[i].equals(cother.blocks[i])) 
						return false;
				}
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------- Internal classes
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Iterator that returns node trees post order. This iterator is
	 * used by  ExprTreeFunctions  objects to  execute the code that 
	 * they contain (ExprTree objects). 
	 */
	
	class ExecuteIterator implements Iterator<IPrimitive>
	{
		private int actualIndex;
		
		ExecuteIterator()
		{
			actualIndex=blockIndex;
		}
		
		public boolean hasNext() 
		{
			return actualIndex > 0;
		}

		public IPrimitive next() 
		{
			return blocks[--actualIndex];
		}

		public void remove() 
		{
			throw new UnsupportedOperationException();		
		}
	}
}
