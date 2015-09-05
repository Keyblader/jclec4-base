package net.sf.jclec.multisyntaxtree;

import net.sf.jclec.IConfigure;

import net.sf.jclec.exprtree.IPrimitive;

import net.sf.jclec.syntaxtree.SyntaxTree;
import net.sf.jclec.syntaxtree.TerminalNode;
import net.sf.jclec.syntaxtree.NonTerminalNode;
import net.sf.jclec.syntaxtree.SyntaxTreeSchema;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Species for SyntaxTreeIndividual.
 * 
 * @author Sebastian Ventura
 */

public class MultiSyntaxTreeIndividualSpecies extends MultiSyntaxTreeSpecies implements IConfigure 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -5659464424179862147L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	public MultiSyntaxTreeIndividualSpecies() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ------------------------------- Setting and getting properties
	/////////////////////////////////////////////////////////////////

	// Setting properties
	
	/**
	 * Set the number of syntax trees per individual 
	 */
	
	public void setNumberOfSyntaxTrees(int numberOfSyntaxTrees)
	{
		// Allocate space for genotype schema
		genotypeSchema = new SyntaxTreeSchema[numberOfSyntaxTrees];
		// Allocate space for each schema element
		for (int i=0; i<numberOfSyntaxTrees; i++) {
			genotypeSchema[i] = new SyntaxTreeSchema();
		}
	}
	
	/**
	 * Set the name of the root symbol for a syntax tree
	 * 
	 * @param syntaxTreeIndex Tree involved
	 * @param rootSymbolName Root symbol name
	 */
	
	public void setRootSymbol(int syntaxTreeIndex, String rootSymbolName)
	{
		genotypeSchema[syntaxTreeIndex].setRootSymbol(rootSymbolName);
	}
		
	/**
	 * Set terminal symbols for this grammar.
	 * 
	 * @param syntaxTreeIndex index of the schema
	 * @param terminals array of terminal nodes
	 */
		
	public void setTerminals(int syntaxTreeIndex, TerminalNode [] terminals)
	{
		genotypeSchema[syntaxTreeIndex].setTerminals(terminals);
	}
		
	/**
	 * Set non-terminal symbols for this grammar.
	 * 
	 * @param syntaxTreeIndex index of the schema
	 * @param nonTerminals array of nonterminal nodes
	 */
		
	public void setNonTerminals(int syntaxTreeIndex, NonTerminalNode [] nonTerminals)
	{
		genotypeSchema[syntaxTreeIndex].setNonTerminals(nonTerminals);
	}
		
	/**
	 * Set the maximum derivation size for this schema.
	 * 
	 * @param maxDerivSize Maximum of derivations 
	 */
		
	public void setMaxDerivSize(int syntaxTreeIndex, int maxDerivSize) 
	{
		genotypeSchema[syntaxTreeIndex].setMaxDerivSize(maxDerivSize);
	}

	/////////////////////////////////////////////////////////////////
	// ---------------------- Implementing ISyntaxTreeSpecies methods
	/////////////////////////////////////////////////////////////////

	/**
	 * Creates a MultiSyntaxTreeIndividual
	 * 
	 * @return new MultiSyntaxTreeIndividual individual
	 */
	
	public MultiSyntaxTreeIndividual createIndividual() 
	{
		return new MultiSyntaxTreeIndividual();
	}

	/**
	 * {@inheritDoc}
	 */
	
	public MultiSyntaxTreeIndividual createIndividual(SyntaxTree [] genotype) 
	{
		return new MultiSyntaxTreeIndividual(genotype);
	}

	/**
	 * Configuration method.
	 * 
	 * Configuration parameters for this species are...
	 * 
	 */
		
	@SuppressWarnings("unchecked")
	public void configure(Configuration settings) 
	{
		// Get number of syntax trees
		int numberOfSyntaxTrees = settings.getInt("[@number-of-trees]", 1);
		setNumberOfSyntaxTrees(numberOfSyntaxTrees);
		// For each prefix expression
		for (int i=0; i<numberOfSyntaxTrees; i++) {
			// Header
			String header = "syntax-tree("+i+")";

			// Get terminal symbols
			int numberOfTermSymbols = 
				settings.getList(header+".terminal-symbols.symbol.name").size();
			TerminalNode [] terminals = 
				new TerminalNode[numberOfTermSymbols]; 
			for (int j=0; j<numberOfTermSymbols; j++) {
				TerminalNode termSymbol = new TerminalNode();
				// Symbol name
				String symbolName = 
					settings.getString(header+".terminal-symbols.symbol("+j+")"+".name");
				termSymbol.setSymbol(symbolName);
				// Symbol code
				try {
					String termSymbolCodeClassname = 
						settings.getString(header+".terminal-symbols.symbol("+j+")"+".code");
					Class<? extends IPrimitive> termSymbolCodeClass = 
						(Class<? extends IPrimitive>) Class.forName(termSymbolCodeClassname);
					IPrimitive symbolCode = termSymbolCodeClass.newInstance();
					termSymbol.setCode(symbolCode);
				} 
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
				catch (InstantiationException e) {
					e.printStackTrace();
				} 
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				// Set array element
				terminals[j] = termSymbol;
			}
			// Set terminal symbols for i-th tree
			setTerminals(i, terminals);
			// Get non-terminal symbols
			int numberOfNonTermSymbols = 
				settings.getList(header+".non-terminal-symbols.symbol.name").size();
			NonTerminalNode [] nonTermSymbols = 
				new NonTerminalNode[numberOfNonTermSymbols];
			for (int j=0; j<numberOfNonTermSymbols; j++) {
				NonTerminalNode nonTermSymbol = new NonTerminalNode();
				// Symbol name
				String symbolName = 
					settings.getString(header+".non-terminal-symbols.symbol("+j+")"+".name");
				nonTermSymbol.setSymbol(symbolName);
				// Symbol production
				String [] symbolProduction = (String [])
					settings.getList(header+".non-terminal-symbols.symbol("+j+")"+".production-rule.element").toArray(new String[0]);				
				nonTermSymbol.setProduction(symbolProduction);
				// Set array element
				nonTermSymbols[j] = nonTermSymbol;
			}			
			setNonTerminals(i, nonTermSymbols);
			// Get root-symbol
			String rootSymbol = settings.getString(header+".root-symbol");
			setRootSymbol(i, rootSymbol);
			// Get max-tree-depth
			int maxDerivSize = settings.getInt(header+".max-deriv-size");
			setMaxDerivSize(i, maxDerivSize);
		}
	}
	/////////////////////////////////////////////////////////////////
	// ------------------------- Overwriting java.lang.Object methods
	/////////////////////////////////////////////////////////////////
	
	public boolean equals(Object other)
	{
		if (other instanceof MultiSyntaxTreeIndividualSpecies) {
			// Type conversion
			MultiSyntaxTreeIndividualSpecies cother = (MultiSyntaxTreeIndividualSpecies) other;
			// Performs comparison
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(genotypeSchema, cother.genotypeSchema);
			// Return comparison result
			return eb.isEquals();
		}
		else {
			return false;
		}
	}
}
