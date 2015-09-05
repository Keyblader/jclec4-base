package net.sf.jclec.util.range;

import net.sf.jclec.IConfigure;
import net.sf.jclec.util.random.IRandGen;


import org.apache.commons.lang.builder.ToStringBuilder;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationRuntimeException;

/**
 * Arithmetic interval.
 * 
 * @author Sebastian Ventura
 */

public class Interval implements IRange, IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */

	private static final long serialVersionUID = 4742142066614454991L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Left extremum */
	
	protected double left;

	/** Right extremum */
	
	protected double right;
	
	/** Interval closure */

	protected Closure closure;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public Interval()
	{
		super();
	}

	/**
	 * Constructor that set interval properties.
	 * 
	 * @param left    Left extremum
	 * @param right   Right extremum
	 * @param closure Interval closure
	 */
	
	public Interval(double left, double right, Closure closure)
	{
		super();
		setClosure(closure);
		setLeft(left);
		setRight(right);
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Setting and getting properties
	
	/**
	 * Access to interval closure
	 * 
	 * @return Interval closure
	 */
	
	public final Closure getClosure() 
	{
		return closure;
	}

	/**
	 * Set interval closure.
	 * 
	 * @param closure New interval closure value
	 */
	
	public final void setClosure(Closure closure) 
	{
		this.closure = closure;
	}

	/**
	 * Access to left extremum
	 * 
	 * @return Left extremum
	 */
	
	public final double getLeft() 
	{
		return left;
	}

	/**
	 * Set left extremum
	 * 
	 * @param left Left extremum
	 */
	
	public final void setLeft(double left) 
	{
		this.left = left;
	}

	/**
	 * Access to right extremum
	 * 
	 * @return Right extremum
	 */
	
	public final double getRight() 
	{
		return right;
	}

	/**
	 * Set right extremum
	 * 
	 * @param right Right extremum
	 */

	public final void setRight(double right) 
	{
		this.right = right;
	}
	
	// IRange interface
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean contains(double value)
	{
		switch(closure)
		{
			case OpenOpen:
			{
				return ((value > left) && (value < right));
			}
			case OpenClosed:
			{
				return ((value > left) && (value <= right));				
			}
			case ClosedOpen:
			{
				return ((value >= left) && (value < right));				
			}
			case ClosedClosed:
			{
				return ((value >= left) && (value <= right));				
			}
		}
		// This point is never reached
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public double efWidth() 
	{
		return (efRight()-efLeft());
	}

	/**
	 * {@inheritDoc}
	 */
	
	public double getRandom(IRandGen randgen)
	{
		return randgen.uniform(efLeft(), efRight());
	}
	
	/**
	 * {@inheritDoc}
	 */

	public double nearestOf(double value) 
	{
		if (contains(value)) {
			return value;
		} 
		else {
			// Effective extrema
			double lefv = efLeft(), refv = efRight();
			// left extremum distance
			double led = Math.abs(value - lefv);
			// right extremum distance
			double red = Math.abs(value - refv);
			// Distances comparison
			if (led < red) {
				return lefv;
			} 
			else {
				return refv;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */

	public double scale(double value) 
	{
		if ((value < 0.0) || (value > 1.0)) {
			throw new IllegalArgumentException(
					"\"value\" must be between 0.0 and 1.0");
		}
		// Effective extrema
		double lefv = efLeft();
		double refv = efRight();
		// Returns scaled value
		return lefv + value * (refv - lefv);
	}

	/**
	 * {@inheritDoc}
	 */

	public double unscale(double value) {
		if (!contains(value)) {
			throw new IllegalArgumentException(
					"\"value\" must belong to this interval");
		}
		// Effective extrema
		double lefv = efLeft();
		double refv = efRight();
		// Returns unscaled value
		return (value - lefv) / (refv - lefv);
	}
	
	// IConfigure interface
	
	/**
	 * Configuration parameters for an interval are:
	 * 
	 * <ul>
	 * <li>
	 * <code>[@left] (double)</code> </p>
	 * Left extremum 
	 * </li>
	 * <li>
	 * <code>[@right] (double)</code> </p>
	 * Right extremum 
	 * </li>
	 * <li>
	 * <code>[@closure] (String)</code> </p>
	 * Interval closure. Supported values are "closed-closed", "closed-open", 
	 * "open-closed" and "open-open". Default value is "closed-closed".   
	 * </li>
	 * </ul>
	 */
	
	public void configure(Configuration configuration)  
	{
		// Get left extremum
		double left = configuration.getDouble("[@left]");
		// Set left extremum
		setLeft(left);
		// Get right extremum
		double right = configuration.getDouble("[@right]");
		// Set right extremum
		setRight(right);
		// Get closure string
		String closureString = configuration.getString("[@closure]", "closed-closed");
		// Convert closureString
		Closure closure;
		if (closureString.equals("closed-closed")) {
			closure = Closure.ClosedClosed;
		}
		else if (closureString.equals("open-open")) {
			closure = Closure.OpenOpen;
		}
		else if (closureString.equals("closed-open")) {
			closure = Closure.ClosedOpen;
		}
		else if (closureString.equals("open-closed")) {
			closure = Closure.OpenClosed;
		}
		else {
			throw new ConfigurationRuntimeException("Illegal value for interval closure");
		}
		// Set closure
		setClosure(closure);
	}
	
	// java.lang.Oject methods
	
	public boolean equals(Object other)
	{
		if (other instanceof Interval){
			Interval cother = (Interval) other;
			return (closure == cother.closure && left == cother.left && right == cother.right);
		}
		else {
			return false;
		}
	}

	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this);
		tsb.append(closure);
		tsb.append("left",left);
		tsb.append("right",right);
		return tsb.toString();
	}
	
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Efective left extremum
	 */
	
	private final double efLeft()
	{
		switch(closure)
		{
			case OpenOpen:
			case OpenClosed:
				return (left + Double.MIN_VALUE);				
			case ClosedOpen:
			case ClosedClosed:
				return left;				
		}
		// This point is never reached
		return 0.0;
	}

	/**
	 * Efective right extremum
	 */
	
	private final double efRight()
	{
		switch(closure)
		{
			case OpenOpen:
			case ClosedOpen:
				return (right - Double.MIN_VALUE);				
			case OpenClosed:
			case ClosedClosed:
				return right;				
		}
		// This point is never reached
		return 0.0;
	}
}