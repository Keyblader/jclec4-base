package net.sf.jclec.algorithm.niching;

import net.sf.jclec.IIndividual;

import net.sf.jclec.fitness.SimpleValueFitness;

import org.apache.commons.configuration.Configuration;

/**
 * Fitness sharing algorithm.
 * 
 * NOTE: This algorithm assumes that the optimal points to search are maximum points. 
 * 
 * @author Sebastian Ventura
 * @author Amelia Zafra
 */

public class Sharing extends SpatialNiching 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -3565568784140690740L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties 
	/////////////////////////////////////////////////////////////////
		
	/** Alfa value */
	
	protected double alpha;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Niche radius */
	
	private transient double deltaShare = 0.0;
	
	/** Sharing distance */
	
	private transient double [][] sh_dij;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */
	
	public Sharing() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Setting and getting properties

	public final double getAlpha() 
	{
		return alpha;
	}

	public final void setAlpha(double alpha) 
	{
		this.alpha = alpha;
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
		// Call super.configure() method
		super.configure(configuration);
		// Set alpha value
		double alpha = configuration.getDouble("alpha");
		setAlpha(alpha);
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	@Override
	protected void createNiches() 
	{
		// Calculate niche radius
		calculateDeltaShare();
		// Calculate sh_dij values
		calculateSh_dij();
		// Create nset individuals
		int i=0;
		for (IIndividual ind : bset) {
			// Copy current individual
			IIndividual indcopy = ind.copy();
			// Calculate new fitness values
			// 1. Current fitness
			SimpleValueFitness currentFitness = 
				(SimpleValueFitness) ind.getFitness();
			// 2. Current fitness value
			double currentFitnessValue = currentFitness.getValue();
			// New fitness value
			double newFitnessValue = currentFitnessValue/sum(sh_dij[i]);
			// 3. New fitness
			SimpleValueFitness newFitness = 
				new SimpleValueFitness(newFitnessValue);
			// Assign new fitness to individual
			indcopy.setFitness(newFitness);
			// Add individual copy to nset
			nset.add(indcopy);
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
	
	private final void calculateSh_dij()
	{
		// Allocate space for sh_dij
		int sh_dijSize = bset.size();
		sh_dij = new double [sh_dijSize][];
		for (int i=0; i<sh_dijSize; i++)
			sh_dij[i] = new double[sh_dijSize];
		//Calculate sh_dij values
		for (int i=0; i<sh_dijSize; i++) {
			// Diagonal value is zero
			sh_dij[i][i] = 0.0;
			// Values over and under diagonal
			for (int j=i+1; j<sh_dijSize; j++) {
				double dij = distance.distance(bset.get(i), bset.get(j));
				sh_dij[i][j] = sh_dij[j][i] = 
					(dij < deltaShare) ? Math.pow(dij/deltaShare, alpha) : 0.0;
			}
		}
	}
	
	/** 
	 * Sum all the elements of array and returns result
	 * 
	 * @param array Array whose elements be added
	 * 
	 * @return Sum of <code>array</code> elements
	 */
	
	private final double sum(double [] array)
	{
		double result = 0.0;
		double arraylength = array.length;
		for (int i=0; i<arraylength; i++) result += array[i];
		return result;
	}
}
