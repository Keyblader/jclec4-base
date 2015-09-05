package net.sf.jclec.algorithm.niching;

import java.util.List;

import net.sf.jclec.IIndividual;
import net.sf.jclec.fitness.IValueFitness;
import net.sf.jclec.fitness.SimpleValueFitness;

import org.apache.commons.configuration.Configuration;

/**
 * Fitness clearing algorithm.
 * 
 * NOTE: This algorithm assumes that the optimal points to search are maximum points. 
 * 
 * @author Sebastian Ventura
 * @author Amelia Zafra
 */

public class Clearing extends SpatialNiching 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -5042241420325727708L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////

	/** Number of individuals in each sub-population*/
	
	protected int kappa;
	
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Niche radius */
	
	private transient double deltaShare = 0.0;

	/** Sharing distance */
	
	private transient double [][] dValues;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */
	
	public Clearing() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Setting and getting properties
	
	public final int getKappa() 
	{
		return kappa;
	}

	public final void setKappa(int kappa) 
	{
		this.kappa = kappa;
	}

	// IConfigure interface
	
	/**
	 * {@inheritDoc}
	 * 
	 * Configuration parameters for this algorithm are: 
	 */
	
	@Override
	public void configure(Configuration configuration) 
	{
		// Call super.configure method
		super.configure(configuration);
		// Set kappa value
		int kappa = configuration.getInt("kappa", 2);
		setKappa(kappa);
	}	
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	/**
	 * 
	 */
	
	@Override
	protected void createNiches() 
	{
		// Get all bset individual in sorted form and put them in nset
		int bsetSize = bset.size();
		List<IIndividual> aux = bettersSelector.select(bset, bsetSize);
		for (IIndividual ind : aux)
			nset.add(ind.copy());

		////////////////////
		// Clearing process
		////////////////////
		
		// Calculate delta share
		calculateDeltaShare();
		// Calculate dValues
		calculateDValues();
		
		for (int i=0; i<bsetSize ; i++) {
			// Current individual
			IIndividual ind_i = nset.get(i);
			// If fitness has not been cleared
			if ( ((IValueFitness) ind_i.getFitness()).getValue() > 0 ) {
				// Number of individuals per niche
				int numberOfWinners = 1;
				for (int j=i+1; j<bsetSize; j++) {
					// Other individual
					IIndividual ind_j = nset.get(j);
					// If individual fitness has not been cleared and 
					// individual belongs to current niche
					if ( (((IValueFitness) ind_i.getFitness()).getValue() > 0) && (dValues[i][j] < deltaShare) ) {
						if (numberOfWinners < kappa) {
							numberOfWinners++;
						}
						else {
							SimpleValueFitness newFitness = new SimpleValueFitness(0.0);
							ind_j.setFitness(newFitness);
						}
					}
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 *  Calculate the niche radius (delta_share)
	 *  
	 *  The established niche radius consists of considering within a 
	 *  niche all individuals with a distance smaller than 20% of the 
	 *  maximum distance between all individuals.
     */
	
	private final void calculateDeltaShare() 
	{
		double maximumDistance = -1.;
		for(int i=0; i<bset.size(); i++)
		{
			for(int j=i+1; j<bset.size(); j++)
			{
				double d = distance.distance(bset.get(i), bset.get(j));
				if( maximumDistance < d) maximumDistance = d;
			}
		}
		// Set niche radius value
		deltaShare = 0.2*maximumDistance;
	} 	
	
	/**
	 * Calculate distances between individuals in bset
	 */
	
	private final void calculateDValues()
	{
		// Allocate space for sh_dij
		int dijSize = bset.size();
		dValues = new double [dijSize][];
		for (int i=0; i<dijSize; i++)
			dValues[i] = new double[dijSize];
		//Calculate sh_dij values
		for (int i=0; i<dijSize; i++) {
			// Diagonal value is zero
			dValues[i][i] = 0.0;
			// Values over and under diagonal
			for (int j=i+1; j<dijSize; j++) 
				dValues[i][j] = dValues[j][i] = distance.distance(bset.get(i), bset.get(j));
		}
	}
}
