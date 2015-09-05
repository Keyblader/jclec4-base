package net.sf.jclec.selector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;



import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.ISystem;

import org.apache.commons.configuration.Configuration;

/**
 * Better individuals selector.
 * 
 * @author Sebastian Ventura
 */

public class BettersSelector extends DeterministicSelector 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -697152908546090334L;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Compare individuals */
	
	protected transient Comparator<IIndividual> individualsComparator =  
		new Comparator<IIndividual> () 
		{
			/**
			 * {@inheritDoc} 
			 */
		
			public int compare(IIndividual arg0, IIndividual arg1) {
				return fitnessComparator.compare(arg0.getFitness(), arg1.getFitness());
			}
		};
	
	/** Fitness comparator (taken from context) */
	
	protected transient Comparator<IFitness> fitnessComparator;
	
	/** Auxiliary list */
	
	protected transient ArrayList<IIndividual> auxList = new ArrayList<IIndividual> ();

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor.
	 */
	
	public BettersSelector() 
	{
		super();
	}

	public BettersSelector(ISystem context) 
	{
		super();
		contextualize(context);
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// IConfigure interface

	public final void configure(Configuration settings) 
	{
		// Do nothing
	}

	// java.lang.Object methods

	@Override
	public boolean equals(Object other)
	{
		return (other instanceof BettersSelector);
	}

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	// AbstractSelector methods

	/**
	 * Prepare selection consists on take fitnesses comparator (to
	 * compare individuals by fitness). Then, copy all source inds in
	 * a temporary list
	 * 
	 * {@inheritDoc} 
	 */
	
	@Override
	protected void prepareSelection() 
	{
		// Clear auxiliary list
		auxList.clear();
		// Set fitness comparator
		fitnessComparator = context.getEvaluator().getComparator();
		// Puts source individuals in auxlist
		for (IIndividual ind : actsrc) auxList.add(ind);
	}

	/**
	 * This method take best individual in temporary list, then remove
	 * and return it.  
	 * 
	 * {@inheritDoc}
	 */
	
	@Override
	protected IIndividual selectNext() 
	{
		// Security mechanism
		if (auxList.isEmpty()) prepareSelection();
		// Select actual best
		IIndividual best = Collections.max(auxList, individualsComparator);
		// Extract best from auxlist
		auxList.remove(best);
		// Return best individual
		return best;	
	}
}
