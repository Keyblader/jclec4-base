package net.sf.jclec.syntaxtree.mut;

import net.sf.jclec.syntaxtree.SyntaxTree;
import net.sf.jclec.syntaxtree.NonTerminalNode;
import net.sf.jclec.syntaxtree.SyntaxTreeSchema;
import net.sf.jclec.syntaxtree.IMutateSyntaxTree;

import net.sf.jclec.util.random.IRandGen;

/**
 * NT mutator.
 * 
 * Se modifica aleatoriamente una rama de forma que el arbol
 * resultante presente la misma profundidad que el original.
 * 
 * @author Amelia Zafra
 */


public class NTMutator implements IMutateSyntaxTree 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -2706522794304044852L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */
	
	public NTMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// java.lang.Object methods

	public boolean equals(Object other)
	{
		if (other instanceof NTMutator) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	@Override
	public SyntaxTree mutateSyntaxTree(SyntaxTree parent, SyntaxTreeSchema schema, IRandGen randgen)	
	{	
		// Son genotype
		SyntaxTree son = new SyntaxTree(); 
		
		// Select a no terminal symbol 
		int p0_branchStart = selectSymbol(parent, schema, randgen);
		
		// If a no terminal symbol doesn't exist in tree, copy parents and return 
		if (p0_branchStart == -1) {
			for(int i=0; i<parent.size(); i++)	son.addNode(parent.getNode(i).copy());
			
			return son;
		}
		
		// Assign the selected symbol
		NonTerminalNode selectedSymbol = (NonTerminalNode) parent.getNode(p0_branchStart);
		
		// Set branch end 
		int p0_branchEnd = parent.subTree(p0_branchStart);
		
		// Determine the maximum size to fill
		int p0_swapBranch = 0;
		for(int i=p0_branchStart; i<p0_branchEnd; i++){
			if(parent.getNode(i).arity()!=0)
				p0_swapBranch ++;
		}
		
		// Create son (first fragment)
		for (int i=0; i<p0_branchStart; i++) 
			son.addNode(parent.getNode(i).copy());
		
		// Create son (second fragment) -- keep the same number of derivations that its father
		schema.fillSyntaxBranch(son, selectedSymbol.getSymbol(), p0_swapBranch, randgen);
			
		// Create son (third fragment) 
		for(int i=p0_branchEnd; i<parent.size(); i++) 
			son.addNode(parent.getNode(i).copy());
		
		// Return son
		return son;

	}	
		
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////

	/**
	 * Seleccionar un simbolo no terminal cualquiera del arbol.
	 * 
	 * @param tree Arbol donde selecionar un simbolo cualquiera
	 * 
	 * @return Localizacion del simbolo seleccionado
	 */
	
	private final int selectSymbol(SyntaxTree tree, SyntaxTreeSchema schema, IRandGen randgen)
	{	
		// Tree length
		int treeLength = tree.size();
		// Generate a tree position at random
		int startPos = randgen.choose(0, treeLength);
		
		int actPos = startPos;
		for(int i=0; i<treeLength; i++) {
			// Update actPos
			actPos = (startPos+i)%treeLength;
			// Check symbol is nontermianl
			if( !schema.isTerminal(tree.getNode(actPos).getSymbol()) )
				return actPos;
		}				
		return -1;
	}
}
