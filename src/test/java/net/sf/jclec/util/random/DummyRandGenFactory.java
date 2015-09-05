package net.sf.jclec.util.random;

/**
 * DummyRandGen factory.
 * 
 * @author Sebastian Ventura 
 */

public class DummyRandGenFactory implements IRandGenFactory 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 7814808696064709330L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////

	/** Dummy random generators sequence */
	
	private double [] dummySequence;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	public DummyRandGenFactory() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ------------------------------- Setting and getting properties
	/////////////////////////////////////////////////////////////////
	
	public double[] getDummySequence() 
	{
		return dummySequence;
	}

	public void setDummySequence(double[] dummySequence) 
	{
		this.dummySequence = dummySequence;
	}	

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// IRandGenFactory interface
	
	public IRandGen createRandGen() 
	{
		return new DummyRandGen(dummySequence);
	}
}
