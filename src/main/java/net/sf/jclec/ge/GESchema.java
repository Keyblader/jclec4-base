package net.sf.jclec.ge;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sf.jclec.syntaxtree.NonTerminalNode;
import net.sf.jclec.syntaxtree.SyntaxTree;
import net.sf.jclec.syntaxtree.SyntaxTreeSchema;
import net.sf.jclec.util.intset.IIntegerSet;

/**
 * Schema for GEIndividual and its subclasses.
 * 
 * @author Rafael Barbudo Lunar
 */

public class GESchema extends SyntaxTreeSchema 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 3206660269350387834L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Individual array genotype */
	
	protected IIntegerSet [] individualArrayGenotype;
	
	/** Number of derivations realized */
	
	protected int nOfDer = 0;
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------------- Constructor
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public GESchema() {
		super();
	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the schema for the individual array genotype
	 * 
	 * @param individualArray Schema for the individual genotype
	 */
	
	public final void setIndividualArrayGenotype(IIntegerSet[] individualArray) 
	{
		this.individualArrayGenotype = individualArray;
	}
	
	/**
	 * Create the phenotype for a given genotype
	 * 
	 * @param genotype Genotype of an individual
	 * 
	 * @return new SyntaxTree
	 */
	
	public SyntaxTree createSyntaxTree(int [] genotype)
	{
		// Create resulting tree
		SyntaxTree result = new SyntaxTree();
		// Fill result branch
		fillSyntaxBranch(result, rootSymbol, genotype, 0);
		// Once the tree is created, the number of derivations is reset
		this.nOfDer = 0;
		// Return resulting tree 
		return result;
	}

	/**
	 * Fills a syntaxtree using the symbol
	 * 
	 * @param owner given syntaxtree
	 * @param symbol Symbol to add
	 * @param genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype
	 * 
	 * @return Actual position of the genotype
	 */
	
	public int fillSyntaxBranch(SyntaxTree owner, String symbol, int [] genotype, int posGenotype)
	{
		if (isTerminal(symbol)) {
			owner.addNode(getTerminal(symbol));
		}
		else {
			// Select a production rule
			NonTerminalNode selectedProduction = selectProduction(symbol, genotype, posGenotype, false);
			// Increment position of genotype going back if it's necessary
			posGenotype++;
			if(posGenotype==genotype.length-1)
				posGenotype = 0;
			if (selectedProduction != null) {
				// Add this node
				owner.addNode(selectedProduction);								
				// Expand production symbols
				for(int i=0; i<selectedProduction.getProduction().length; i++)
					posGenotype = fillSyntaxBranch(owner, selectedProduction.getProduction()[i], genotype, posGenotype);
			}
			else {
				posGenotype = fillSyntaxBranch(owner, symbol, genotype, posGenotype);
			}
		}
		return posGenotype;
	}
	
	/**
	 * Select a production rule for a symbol of the grammar.
	 * 
	 * @param symbol  Symbol to expand
	 * @param genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype
	 * @param onlyTerminal A flag which indicates if we must select ony terminal productions or not
	 * 
	 * @return A production rule for  the given symbol.
	 */	
	
	protected NonTerminalNode selectProduction(String symbol, int [] genotype, int posGenotype, boolean onlyTerminal)
	{	
		/******************************************************************
		// Get all productions of this symbol
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
		// Number of productions
		int nOfProdRules = prodRules.length;
		//Index of the production rule chosen
		int chosen = -1;
			
		// The max number of derivations has been reached
		if(nOfDer >= getMaxDerivSize()){
			// Get the min derivation size for the actual symbol
			int minDerivSize = getMinDerivSize(symbol);
			// The min derivation size of all the rule productions 
			int minDerivSizeOfProd;
			
			for(int i=0; i<nOfProdRules; i++){
				minDerivSizeOfProd = 0;
				for(String prodRule: prodRules[i].getProduction())
					if(getMinDerivSize(prodRule)>minDerivSizeOfProd)
						minDerivSizeOfProd = getMinDerivSize(prodRule);
				if(minDerivSizeOfProd < minDerivSize){
					chosen = i;
					break;
				}
			}
		}
		// The max number of derivations has not been reached yet
		else{
			// Choose production based on the genotype
			chosen = genotype[posGenotype] % nOfProdRules;
		}
		// Increment the number of derivations
		this.nOfDer++;
		// Return production chosen
		return prodRules[chosen];
		*/////////////////////////////////////////////////////////////////////
		
		
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);

		if(onlyTerminal == true)
		{	
			List <NonTerminalNode> rulesList = new ArrayList<NonTerminalNode>();
			boolean nonTerminalFound;

			// Get only the terminal productions
			for(int i=0; i<prodRules.length; i++)
			{
				nonTerminalFound = false;
				for(int j=0; j<prodRules[i].getProduction().length; j++)
				{
					if(isTerminal(prodRules[i].getProduction()[j]) == false)
					{
						nonTerminalFound = true;
						break;
					}
				}
				if(nonTerminalFound == false)
					rulesList.add(prodRules[i]);
			}
			
			prodRules = rulesList.toArray(new NonTerminalNode [rulesList.size()]);
			
			if(prodRules.length == 0)
				return null;
		}
		

		// Number of productions
		int nOfProdRules = prodRules.length;
		// Choose production based on the genotype
		int chosen = genotype[posGenotype] % nOfProdRules;
		// Return production chosen
		return prodRules[chosen];
		
	}
	
	/**
	 * Get the minimum derivation size for a given symbol
	 * 
	 * @param symbol for which we need to know the minimum derivation size
	 * 
	 * @return minimum derivation size
	 */
		
	public int getMinDerivSize(String symbol) 
	{
		for (int i=0; i<maxDerivSize; i++) {
			if (!cardinality(symbol, i).equals(BigInteger.ZERO)) {
				return i;
			}
		}
		// This point should never be reached
		return -1;
	}
	
	/**
	 * Get the point of derivation dissimilarity for two given genotypes.
	 * 
	 * @param p0genotype  Symbol to expand
	 * @param p2genotype Genotype of an individual
	 * 
	 * @return The point of dissimilarity of the genotypes.
	 */	
	
	public int getPointOfDerivationDissimilarity(int[] p0genotype, int[] p1genotype) 
	{
		int genotypeMinLength = Math.min(p0genotype.length, p1genotype.length);
		//The first element indicate if the solution is reached and the second the reading position
		int [] posGenotype = {0,0};
		searchDerivationDissimilarity(p0genotype, p1genotype, posGenotype, genotypeMinLength, rootSymbol);
		return posGenotype[1];
	}

	/**
	 * Search for a possible derivation dissimilarity in a given possition of the phenotype.
	 * 
	 * @param p0genotype  Symbol to expand
	 * @param p2genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype and flag to control if solution is reached
	 * @param endPosition Final of the genotype
	 * @param symbol Symbol to derivate and check for dissimilarity
	 * 
	 * @return The point of dissimilarity of the genotypes.
	 */	
	
	public void searchDerivationDissimilarity(int[] p0genotype, int[] p1genotype, int [] posGenotype, int endPosition, String symbol) 
	{
		// TODO Revisar exahustivamente que realmente funcione bien
		NonTerminalNode prod0 = new NonTerminalNode();
		NonTerminalNode prod1 = new NonTerminalNode();
		
		// End of genotype is reached without a solution
		if(posGenotype[1] == endPosition)
			posGenotype[0] = -1;
		// Solution or end position is not reached yet
		if(posGenotype[0] == 0){	
			if (isTerminal(symbol) == false) {
				// Select a production rule for each genotype
				prod0 = selectProduction(symbol, p0genotype, posGenotype[1], false);
				prod1 = selectProduction(symbol, p1genotype, posGenotype[1], false);
				posGenotype[1] = posGenotype[1] + 1;
				if(prod0.equals(prod1))
					for(int i=0; i<prod0.getProduction().length; i++)
						searchDerivationDissimilarity(p0genotype, p1genotype, posGenotype, endPosition, prod0.getProduction()[i]);
				// Dissimilarity is found (posGenotype[1] is the first point where the derivation change)
				else
					posGenotype[0] = 1;
			}
		}
	}

	/**
	 * Map the phenotype from a given genotype using a grow technique
	 * 
	 * @param phenotype Phenotype of an individual
	 * @param symbol Symbol to add
	 * @param genotype Genotype to be mapped
	 * @param posGenotype Reading position of the genotype
	 * @param depth Actual depth of the tree
	 */
	
	/*public int grow(SyntaxTree phenotype, String symbol, int [] genotype, int posGenotype, int depth)
	{
		if (isTerminal(symbol)) {
			phenotype.addNode(getTerminal(symbol));
		}
		else{
			if(depth < getMaxDerivSize()-1)
			{
				NonTerminalNode selectedProduction = selectProduction(symbol, genotype, posGenotype, false);
				// Increment position of genotype going back if it's necessary
				posGenotype++;
				if(posGenotype==genotype.length-1)
					posGenotype = 0;
				if (selectedProduction != null) {
					// Add this node
					phenotype.addNode(selectedProduction);
					for(int i=0; i<selectedProduction.getProduction().length; i++)
						posGenotype = grow(phenotype, selectedProduction.getProduction()[i], genotype, posGenotype, depth+1);
				}
				else
					posGenotype = grow(phenotype, symbol, genotype, posGenotype, depth);
			}
			else
			{
				NonTerminalNode selectedProduction = selectProduction(symbol, genotype, posGenotype, true);
				// Increment position of genotype going back if it's necessary
				posGenotype++;
				if(posGenotype==genotype.length-1)
					posGenotype = 0;
				if (selectedProduction != null){
					phenotype.addNode(selectedProduction);
					for(int i=0; i<selectedProduction.getProduction().length; i++){
						posGenotype = grow(phenotype, selectedProduction.getProduction()[i], genotype, posGenotype, depth+1);
					}
				}
				else
				{
					mappingValid = false;
					return posGenotype;
				}
			}
		}
		return posGenotype;
	}*/
	
	public int grow(GEIndividual ind, String symbol, int posGenotype, int depth)
	{
		if (isTerminal(symbol)) 
			ind.getPhenotype().addNode(getTerminal(symbol));
		else
		{	
			NonTerminalNode selectedProduction = new NonTerminalNode();
			if(depth < getMaxDerivSize()-1)
				selectedProduction = selectProduction(symbol, ind.getGenotype(), posGenotype, false);
			else
				selectedProduction = selectProduction(symbol, ind.getGenotype(), posGenotype, true);
			
			// Increment position of genotype going back if it's necessary
			posGenotype++;
			if(posGenotype==ind.getGenotype().length-1)
				posGenotype = 0;
			if (selectedProduction != null){
				ind.getPhenotype().addNode(selectedProduction);
				for(int i=0; i<selectedProduction.getProduction().length; i++)
					posGenotype = grow(ind, selectedProduction.getProduction()[i], posGenotype, depth+1);
			}
			else
			{
				ind.setFeasibility(false);
				return posGenotype;
			}
		}
		return posGenotype;
	}
}