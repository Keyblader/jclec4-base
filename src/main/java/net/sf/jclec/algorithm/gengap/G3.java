package net.sf.jclec.algorithm.gengap;

import java.util.List;

import net.sf.jclec.IConfigure;
import net.sf.jclec.IIndividual;
import net.sf.jclec.IRecombinator;

import net.sf.jclec.selector.RandomSelector;
import net.sf.jclec.selector.BettersSelector;
import net.sf.jclec.selector.RouletteSelector;

import net.sf.jclec.algorithm.PopulationAlgorithm;
import net.sf.jclec.base.RepeatRecombinator;

import org.apache.commons.lang.builder.EqualsBuilder;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationRuntimeException;

/**
 * <strong><u>G</u></strong>eneralized <strong><u>G</u></strong>eneration <strong><u>G</u></strong>ap algorithm.
 * 
 * @author Maria Catala-Carbonero 
 * @author Sebastian Ventura 
 * @author Carlos Garcia-Martinez
 */

public class G3 extends PopulationAlgorithm 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -2649346083463795286L;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Number of parents to select */
	
	protected int mu;
	
	/** Number of sons to obtain after recombination */
	
	protected int lambda;	
	
	/** Individuals recombinator */

	protected RepeatRecombinator recombinator;
	
	/** Number of individuals to replace */
	
	protected int r;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////
	
	/** Selector used in selection plan */
	
	protected transient RandomSelector parentsSelector;
	
	/** Selector used in replacement plan (random selector without repetition) */
	
	protected transient RandomSelector replacementSelector; 

	/** Selector used in update plan */ 

	protected transient RouletteSelector updateSelector;
	
	/** Selector used in update plan */
	
	protected transient BettersSelector bettersSelector;	

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty (default) constructor
	 */
	
	public G3() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Getting and setting properties
		
	/**
	 * Access to parents recombinator
	 * 
	 * @return Actual parents recombinator
	 */
	
	public IRecombinator getRecombinator() 
	{
		return recombinator.getDecorated();
	}

	/**
	 * Sets the parents recombinator.
	 * 
	 * @param recombinator New parents recombinator
	 */
	
	public void setRecombinator(IRecombinator recombinator) 
	{
		if (this.recombinator == null) {
			this.recombinator = new RepeatRecombinator(this);
		}
		this.recombinator.setDecorated(recombinator);
	}

	/**
	 * Access to mu parameter
	 * 
	 * @return Actual value for mu parameter
	 */
	
	public final int getMu() 
	{
		return mu;
	}

	/**
	 * Sets the value of the mu parameter
	 * 
	 * @param mu New value for mu parameter
	 */
	
	public final void setMu(int mu) 
	{
		this.mu = mu;
	}

	/**
	 * Access to lambda parameter
	 * 
	 * @return Actual value for lambda parameter
	 */
	
	public final int getLambda() 
	{
		return lambda;
	}

	/**
	 * Sets the value of the lambda parameter.
	 * 
	 * @param lambda New value for lambda parameter
	 */
	
	public final void setLambda(int lambda) 
	{
		this.lambda = lambda;
	}
	
	/**
	 * Access to r (number of individuals to replace) parameter
	 * 
	 * @return Actual value for r parameter
	 */
		
	public final int getR() {
		return r;
	}

	/**
	 * Sets the value of the r parameter.
	 * 
	 * @param r New value for r parameter
	 */

	public final void setR(int r) {
		this.r = r;
	}

	// IConfigure interface
	
	/**
	 * Configuration method.
	 * 
	 * Configuration parameters for BaseAlgorithm class are:
	 * 
	 * <ul>
	 * <li>
	 * <code>species: ISpecies (complex)</code></p>
	 * Individual species
	 * </li><li>
	 * <code>evaluator: IEvaluator (complex)</code></p>
	 * Individuals evaluator
	 * </li><li>
	 * <code>population-size: int</code></p>
	 * Population size
	 * </li><li>
	 * <code>max-of-generations: int</code></p>
	 * Maximum number of generations
	 * </li>
	 * <li>
	 * <code>provider: IProvider (complex)</code></p>
	 * Individuals provider
	 * </li>
	 * <li>
	 * <code>mu: int</code>
	 * Number of parents to select  
	 * </li>
	 * <li>
	 * <code>lambda: int</code>
	 * Number of son to obtain by recombination
	 * </li>
	 * <li>
	 * <code>recombinator: IRecombinator (complex)</code>
	 * Recombination operator
	 * </li>
	 * </ul>
	 */
	
	@SuppressWarnings("unchecked")
	public void configure(Configuration configuration)
	{
		// Call super.configure() method
		super.configure(configuration);
		// Mu parameter
		int mu = configuration.getInt("mu");
		setMu(mu);
		// Lambda parameter
		int lambda = configuration.getInt("lambda");
		setLambda(lambda);
		// Recombinator 
		try {
			// Recombinator classname
			String recombinatorClassname = 
				configuration.getString("recombinator[@type]");
			// Recombinator class
			Class<? extends IRecombinator> recombinatorClass = 
				(Class<? extends IRecombinator>) Class.forName(recombinatorClassname);
			// Recombinator instance
			IRecombinator recombinator = recombinatorClass.newInstance();
			// Configure recombinator if necessary
			if (recombinator instanceof IConfigure) {
				// Extract recombinator configuration
				Configuration recombinatorConfiguration = configuration.subset("recombinator");
				// Configure species
				((IConfigure) recombinator).configure(recombinatorConfiguration);
			}
			// Set species
			setRecombinator(recombinator);
		} 
		catch (ClassNotFoundException e) {
			throw new ConfigurationRuntimeException("Illegal recombinator classname");
		} 
		catch (InstantiationException e) {
			throw new ConfigurationRuntimeException("Problems creating an instance of recombinator", e);
		} 
		catch (IllegalAccessException e) {
			throw new ConfigurationRuntimeException("Problems creating an instance of recombinator", e);
		}
		// r parameter
		int r = configuration.getInt("r");
		setR(r);
	}	
	
	// java.lang.Object methods

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof G3) {
			G3 cother = (G3) other;
			EqualsBuilder eb = new EqualsBuilder();
			// Call super method
			eb.appendSuper(super.equals(other));
			// Mu
			eb.append(mu, cother.mu);
			// Lambda
			eb.append(lambda, cother.lambda);
			// Recombinator
			eb.append(recombinator, cother.recombinator);
			// R
			eb.append(r, cother.r);
			// Return test result
			return eb.isEquals();
		}
		else {
			return false;
		}
	}

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	public void doInit()
	{
		// Call super method
		super.doInit();
		// Initialize selectors
		parentsSelector = new RandomSelector(this);
		replacementSelector = new RandomSelector(this); 
		updateSelector  = new RouletteSelector(this);
		bettersSelector = new BettersSelector(this);	
		// Set number of recombinator repetitions
		setNumberOfRecombinatorRepetitions();
	}
	
	@Override
	protected void doSelection() 
	{	
		// Best individual in b
		IIndividual bestInB = bettersSelector.select(bset, 1).get(0);
		// Remove bestInb from b
		bset.remove(bestInB);
		// Select parents
		pset = parentsSelector.select(bset, mu-1);
		pset.add(bestInB);
		// Restore bset
		bset.add(bestInB);
	}

	@Override
	protected void doGeneration() 
	{
		// Recombine parents
		cset = recombinator.recombine(pset);
		// Evaluate all new individuals
		evaluator.evaluate(cset);
	}

	@Override
	protected void doReplacement() 
	{
		// Individuals in rset
		rset = replacementSelector.select(bset, r, false);
		// Remove rset individuals from bset
		bset.removeAll(rset);
	}

	@Override
	protected void doUpdate() 
	{
		// Join cset and rset individuals
		cset.addAll(rset);
		// rprimeset contains r best individuals
		List<IIndividual> rprimeset = bettersSelector.select(cset,r);
		// Put rprimeset individuals in bset 
		bset.addAll(rprimeset); 
	}
	
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Set the number of repetitions necessary to obtain lambda sons 
	 * from mu parents with a ppl/spl recombinator (with ppl parents 
	 * per litter and spl sons per litter)
	 */
	
	private final void setNumberOfRecombinatorRepetitions()
	{
		// Number of parents per litter
		int ppl = recombinator.getDecorated().getPpl();
		// Number of sons per litter
		int spl = recombinator.getDecorated().getSpl();
		// Number of repetitions to generate lambda sons
		int nor = (lambda*ppl)/(mu*spl);
		// Set nor
		recombinator.setNumberOfRepetitions(nor);
	}
}
