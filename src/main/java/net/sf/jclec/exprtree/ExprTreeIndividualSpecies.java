package net.sf.jclec.exprtree;

import net.sf.jclec.IConfigure;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationRuntimeException;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * ExpreTreeIndividual species.
 * 
 * @author Sebastian Ventura
 */

public class ExprTreeIndividualSpecies extends ExprTreeSpecies implements IConfigure 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -5304951511814536309L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */
	
	public ExprTreeIndividualSpecies() 
	{
		super();
		// Initialize genotype schema
		genotypeSchema = new ExprTreeSchema();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Setting and getting properties
	
	public void setMinTreeSize(int minimumTreeSize)
	{
		genotypeSchema.setMinTreeSize(minimumTreeSize);
	}

	public void setMaxTreeSize(int maximumTreeSize)
	{
		genotypeSchema.setMaxTreeSize(maximumTreeSize);
	}
	
	/**
	 * Set the root type for an expression tree.
	 * 
	 * @param rootType      Root type for this tree 
	 */
	
	public void setRootType(Class<?> rootType)
	{
		genotypeSchema.setRootType(rootType);
	}
	
	/**
	 * Set the terminals set for an expression tree.
	 * 
	 * @param terminals     Terminals set for this tree
	 */
	
	public void setTerminals(IPrimitive [] terminals)
	{
		genotypeSchema.setTerminals(terminals);
	}

	/**
	 * Set the functions set for an expression tree.
	 * 
	 * @param functions     Functions set for this tree
	 */
	
	public void setFunctions(IPrimitive [] functions)
	{
		genotypeSchema.setFunctions(functions);	
	}
	
	// IExprTreeSpecies interface
	
	/**
	 * Creates an ExprTreeIndividual
	 * 
	 * @return new ExprTreeIndividual individual
	 */
	
	public ExprTreeIndividual createIndividual() 
	{
		return new ExprTreeIndividual();
	}

	/**
	 * {@inheritDoc}
	 */
	
	public ExprTreeIndividual createIndividual(ExprTree genotype) 
	{
		return new ExprTreeIndividual(genotype);
	}

	// IConfigure interface
	
	/**
	 * Configuration method.
	 * 
	 * Configuration parameters for PrefExprIndividualSpecies are:
	 * 
	 * <ul>
	 * </ul>
	 */
	
	@SuppressWarnings("unchecked")
	public void configure(Configuration settings) 
	{
		// Header
		String header = "expression-tree";
		// Get minimum-tree-size
		int minTreeSize = settings.getInt(header+".min-tree-size");
		setMinTreeSize(minTreeSize);
		// Get minimum-tree-size
		int maxTreeSize = settings.getInt(header+".max-tree-size");
		setMaxTreeSize(maxTreeSize);			
		// Get root type
		String rootTypeName = settings.getString(header+".root-type");
		try {
			Class<?> rootType = Class.forName(rootTypeName);
			setRootType(rootType);
		} 
		catch (ClassNotFoundException e) {
			throw new ConfigurationRuntimeException("");
		}
		// Get terminals set
		int numberOfTerminals = settings.getList(header+".terminals.terminal[@class]").size();
		IPrimitive [] terminals = new IPrimitive[numberOfTerminals];
		for (int j=0; j<numberOfTerminals; j++) {
			try {
				String terminalClassname = 
					settings.getString(header+".terminals.terminal("+j+")[@class]");
				Class<IPrimitive> terminalClass = 
					(Class<IPrimitive>) Class.forName(terminalClassname);
				terminals[j] = terminalClass.newInstance();
			} 
			catch (ClassNotFoundException e) {
				throw new ConfigurationRuntimeException();
			} 
			catch (InstantiationException e) {
				throw new ConfigurationRuntimeException();
			} 
			catch (IllegalAccessException e) {
				throw new ConfigurationRuntimeException();
			}
		}	
		setTerminals(terminals);
		// Get functions set
		int numberOfFunctions = settings.getList(header+".functions.function[@class]").size();
		IPrimitive [] functions = new IPrimitive [numberOfFunctions];
		for (int j=0; j<numberOfFunctions; j++) {
			try {
				String functionClassname = 
					settings.getString(header+".functions.function("+j+")[@class]");
				Class<IPrimitive> functionClass = 
					(Class<IPrimitive>) Class.forName(functionClassname);
				functions[j] = functionClass.newInstance();
			} 
			catch (ClassNotFoundException e) {
				throw new ConfigurationRuntimeException();
			} 
			catch (InstantiationException e) {
				throw new ConfigurationRuntimeException();
			} 
			catch (IllegalAccessException e) {
				throw new ConfigurationRuntimeException();
			}
		}
		setFunctions(functions);
	}

	
	// java.lang.Object methods
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean equals(Object other)
	{
		if (other instanceof ExprTreeIndividualSpecies) {
			ExprTreeIndividualSpecies cother = (ExprTreeIndividualSpecies) other;
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(genotypeSchema, cother.genotypeSchema);
			return eb.isEquals();
		}
		else {
			return false;
		}
	}
}
