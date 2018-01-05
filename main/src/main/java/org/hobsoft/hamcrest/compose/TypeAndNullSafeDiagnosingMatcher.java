package org.hobsoft.hamcrest.compose;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.internal.ReflectiveTypeFinder;

/**
 * A copy of {@link org.hamcrest.TypeSafeDiagnosingMatcher}, uses the same mechanisms used by them, but propagates null 
 * values, what can be desired in some cases.
 *
 * @param <T> 
 *           type of object to be matched.
 */
public abstract class TypeAndNullSafeDiagnosingMatcher<T> extends DiagnosingMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------
	
	private static final ReflectiveTypeFinder TYPE_FINDER = 
			new ReflectiveTypeFinder("matchesSafely", 2, 0);
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------
	
	private final Class<?> expectedType;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * @see TypeSafeDiagnosingMatcher#TypeSafeDiagnosingMatcher().
	 */
	protected TypeAndNullSafeDiagnosingMatcher()
	{
		this.expectedType = TYPE_FINDER.findExpectedType(getClass());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// abstract methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Similar to {@link TypeSafeDiagnosingMatcher#matchesSafely(Object, Description)}, but propagates null values too.
	 */
	protected abstract boolean matchesSafely(T item, Description mismatchDescription);
	
	// ----------------------------------------------------------------------------------------------------------------
	// Diagnosing methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Receives an item and check for match if it is null or an instance of the desired class.
	 *
	 * @param item Object to be verified.
	 * @return If the given item matches.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean matches(Object item, Description mismatchDescription) 
	{
		return (item == null || expectedType.isInstance(item)) 
			&& matchesSafely((T) item, mismatchDescription);
	}
}
