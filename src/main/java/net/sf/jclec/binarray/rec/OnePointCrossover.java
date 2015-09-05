package net.sf.jclec.binarray.rec;

import net.sf.jclec.binarray.BinArrayIndividual;
import net.sf.jclec.binarray.BinArrayRecombinator;

/**
 * One point crossover operator for BinArrayIndividual and its subclasses.
 * 
 * @author Sebastian Ventura
 */

public class OnePointCrossover extends BinArrayRecombinator
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 3689351022304639024L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */

	public OnePointCrossover() 
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
		if (other instanceof OnePointCrossover) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	// AbstractRecombinator methods

	/**
	 * {@inheritDoc}
	 */

	@Override	
	protected void recombineNext() 
	{
		// Genotype length
		int gl = species.getGenotypeLength();
		// Parents conversion
		BinArrayIndividual p0 = 
			(BinArrayIndividual) parentsBuffer.get(parentsCounter);
		BinArrayIndividual p1 = 
			(BinArrayIndividual) parentsBuffer.get(parentsCounter+1);
		// Parents genotypes
		byte [] p0_genome = p0.getGenotype();
		byte [] p1_genome = p1.getGenotype();
		// Creating sons genotypes
		byte [] s0_genome = new byte[gl];
		byte [] s1_genome = new byte[gl];
		// Sets a crossover point
		int cp = randgen.choose(1, gl-1);
		// First son' genotype
		System.arraycopy(p0_genome,  0, s0_genome,  0, cp);
		System.arraycopy(p1_genome, cp, s0_genome, cp, gl-cp);
		// Second son' genotype
		System.arraycopy(p1_genome,  0, s1_genome,  0, cp);
		System.arraycopy(p0_genome, cp, s1_genome, cp, gl-cp);
		// Put sons in s
		sonsBuffer.add(species.createIndividual(s0_genome));
		sonsBuffer.add(species.createIndividual(s1_genome));
	}
	
	/*
	 * El mtodo realiza las siguientes acciones:
	 * 
	 * 1) Crea los genotipos de los hijos
	 * 2) Elige el punto de cruce al azar, generando un valor aleatorio
	 *    en el rango [1,genome.length-1]
	 * 3) Construye los nuevos individuos. Los fragmentos que se utilizan
	 *    en la construccin de los nuevos individuos son:
	 *    a) Desde 0 hasta mp-1 (ambos inclusive)
	 *    b) Desde mp hasta genome.length-1 (ambos inclusive)
	 */
}
