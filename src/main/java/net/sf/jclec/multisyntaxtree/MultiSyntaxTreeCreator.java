package net.sf.jclec.multisyntaxtree;

import net.sf.jclec.ISpecies;

import net.sf.jclec.base.AbstractCreator;
import net.sf.jclec.syntaxtree.SyntaxTree;
import net.sf.jclec.syntaxtree.SyntaxTreeSchema;

/**
 * Expression tree individuals creator.
 * 
 * @author Sebastian Ventura
 */

public class MultiSyntaxTreeCreator extends AbstractCreator 
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
	
	protected transient MultiSyntaxTreeSpecies species;
	
	/** Individuals schema */
	
	protected transient SyntaxTreeSchema [] schema;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty (default) constructor.
	 */
	
	public MultiSyntaxTreeCreator() 
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
		return (other instanceof MultiSyntaxTreeCreator);
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
		if (spc instanceof MultiSyntaxTreeSpecies) {
			// Type conversion 
			this.species = (MultiSyntaxTreeSpecies) spc;
			// Sets genotype schema
			this.schema = ((MultiSyntaxTreeSpecies) spc).getGenotypeSchema();
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
		int numberOfSyntaxTrees = schema.length;
		// Allocate space for genotype
		SyntaxTree [] genotype = new SyntaxTree[numberOfSyntaxTrees];
		// Create all genotype trees
		for (int i=0; i<numberOfSyntaxTrees; i++) {
			// Select expression size
			int minSize = schema[i].getMinDerivSize();
			int maxSize = schema[i].getMaxDerivSize();
			int actSize = randgen.choose(minSize, maxSize);
			// Create Expression tree
			genotype[i] = schema[i].createSyntaxTree(actSize, randgen);
		}
		// Put new son in created buffer
		createdBuffer.add(species.createIndividual(genotype));
	}
}
