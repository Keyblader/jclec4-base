package net.sf.jclec.realarray.rec;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Apply a discrete crossover for two individuals
 *
 * @author Alberto Lamarca 
 * @author Sebastian Ventura 
 */

public class DiscreteCrossover extends UniformCrossover2x2
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 7559852733891035492L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public DiscreteCrossover() 
	{
		super();
	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// java.lang.Object methods

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof DiscreteCrossover) {
			DiscreteCrossover o = (DiscreteCrossover) other;
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(locusRecProb, o.locusRecProb);
			return eb.isEquals();
		}
		else {
			return false;
		}
	}	
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	// UniformCrossover2x2 methods
	
	@Override
	protected void recombineLocus(double[] p0_genome, double[] p1_genome, double[] s0_genome, double[] s1_genome, int locusIndex) 
	{
	    s0_genome[locusIndex] = p1_genome[locusIndex];
	    s1_genome[locusIndex] = p0_genome[locusIndex];
	}

	// UniformCrossover methods

	/** 
	 * {@inheritDoc}
	 */
	
	@Override
	protected double defaultLocusRecProb() 
	{
		return 0.6;
	}
}
