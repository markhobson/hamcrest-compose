/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hobsoft.hamcrest.compose;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
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
	 * @see org.hamcrest.TypeSafeDiagnosingMatcher#TypeSafeDiagnosingMatcher().
	 */
	protected TypeAndNullSafeDiagnosingMatcher()
	{
		this.expectedType = TYPE_FINDER.findExpectedType(getClass());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// abstract methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Similar to {@link org.hamcrest.TypeSafeDiagnosingMatcher#matchesSafely(Object, Description)}, but propagates 
	 * null values too.
	 */
	protected abstract boolean matchesSafely(T item, Description mismatchDescription);
	
	// ----------------------------------------------------------------------------------------------------------------
	// Diagnosing methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Receives an item and check for match if it is null or an instance of the desired class.
	 *
	 * @param item 
	 *            Object to be verified.
	 * @param mismatchDescription
	 *            Description for the mismatches to be appended into.
	 * @return 
	 *            If the given item matches.
	 */
	@Override
	public boolean matches(Object item, Description mismatchDescription)
	{
		if (item != null && !expectedType.isInstance(item))
		{
			return false;
		}
		
		@SuppressWarnings("unchecked")
		T castItem = (T) item;
		
		return matchesSafely(castItem, mismatchDescription);
	}
}
