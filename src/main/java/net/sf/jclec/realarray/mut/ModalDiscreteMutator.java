package net.sf.jclec.realarray.mut;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Apply a Modal discrete mutation.
 *  
 * @author Alberto Lamarca
 * @author Sebastian Ventura 
 */

public class ModalDiscreteMutator extends ModalMutator 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Generated by eclipse */
	
	private static final long serialVersionUID = -617036782103757330L;

	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public ModalDiscreteMutator()
	{
		super();
	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Setting properties
	
	/**
	 * Ensures that Bm > 1.0
	 * 
	 * {@inheritDoc}
	 */
	
	public void setBm(double bm) 
	{
		if (bm > 1 )
			this.Bm = bm;
		else
			this.Bm = 2.0;
	}

	// java.lang.Object methods

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof ModalDiscreteMutator) {
			ModalDiscreteMutator o = (ModalDiscreteMutator) other;
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(locusMutProb, o.locusMutProb);
			eb.append(Bm, o.Bm);
			eb.append(minimumRange, o.minimumRange);
			eb.append(mutationRange, o.mutationRange);
			return eb.isEquals();
		}
		else {
			return false;
		}
	}	

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	// AbstractMutator methods
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void doLocusMutation(double[] parentChromosome, double[] mutantChromosome, int locusIndex) 
	{
		double rang = 
			mutationRange * genotypeSchema[locusIndex].efWidth();
		int pi=(int) (Math.log(minimumRange)/Math.log(Bm));
		double gamma = 0.0;
		for (int k=0 ; k>=pi;k--) {
			gamma += ((randgen.raw() < 0.0625)?1:0) * Math.pow(Bm,k);
		}
		double newValue;
		if (randgen.coin()) {
			newValue = parentChromosome[locusIndex] + rang*gamma;
		}
		else {
			newValue = parentChromosome[locusIndex] - rang*gamma;
		}
		//	Check the locus interval
		mutantChromosome[locusIndex] = genotypeSchema[locusIndex].nearestOf(newValue);	
	}

	// UniformMutator methods
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected double defaultLocusMutProb() 
	{
		return 0.6;
	}

	// ModalMutator methods

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected double defaultBm() 
	{
		return 2.0;
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected double defaultMutationRange() 
	{
		return 0.1;
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected double defaultMinimumRange() 
	{
		return 1E-05;
	}
}
