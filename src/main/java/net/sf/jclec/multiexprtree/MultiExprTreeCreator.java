package net.sf.jclec.multiexprtree;

import net.sf.jclec.ISpecies;

import net.sf.jclec.base.AbstractCreator;

import net.sf.jclec.exprtree.ExprTree;
import net.sf.jclec.exprtree.ExprTreeSchema;

/**
 * Expression tree individuals creator.
 * 
 * @author Sebastian Ventura
 */

public class MultiExprTreeCreator extends AbstractCreator 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 4365866784680115536L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Individual species */
	
	protected transient MultiExprTreeSpecies species;
	
	/** Individuals schema */
	
	protected transient ExprTreeSchema [] schema;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty (default) constructor.
	 */
	
	public MultiExprTreeCreator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// java.lang.Object methods
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof MultiExprTreeCreator){
			return true;
		}
		else {
			return false;
		}
	}

	/////////////////////////////////////////////////////////////////
	// -------------------------- Overwriting AbstractCreator methods
	/////////////////////////////////////////////////////////////////

	// AbstractCreator methods

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void prepareCreation() 
	{
		ISpecies spc = context.getSpecies();
		if (spc instanceof MultiExprTreeSpecies) {
			// Type conversion 
			this.species = (MultiExprTreeSpecies) spc;
			// Sets genotype schema
			this.schema = ((MultiExprTreeSpecies) spc).getGenotypeSchema();
		}
		else {
			throw new IllegalStateException("Illegal species in context");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void createNext() 
	{
		// Number of expression trees
		int numberOfExpressionTrees = schema.length;
		// Allocate space for genotype
		ExprTree [] genotype = new ExprTree[numberOfExpressionTrees];
		// Create all genotype trees
		for (int i=0; i<numberOfExpressionTrees; i++) {
			// Select expression size
			int minSize = schema[i].getMinTreeSize();
			int maxSize = schema[i].getMaxTreeSize();
			int actSize = randgen.choose(minSize, maxSize);
			// Create Expression tree
			genotype[i] = schema[i].createExprTree(actSize, randgen);
		}
		// Put new son in created buffer
		createdBuffer.add(species.createIndividual(genotype));
	}
}
