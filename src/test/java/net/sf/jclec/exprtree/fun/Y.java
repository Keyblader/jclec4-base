package net.sf.jclec.exprtree.fun;

import net.sf.jclec.exprtree.fun.Argument;

public class Y extends Argument<Double> 
{
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 7802393498828399525L;

	public Y() 
	{
		super(Double.class, 1);
	}
	
	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof Y;
	}	
	
	public String toString()
	{
		return "Y";
	}
}
