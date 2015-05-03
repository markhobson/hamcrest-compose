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

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * Matcher that composes a list of other matchers using a logical AND.
 * <p>
 * This matcher differs from {@code CoreMatchers.allOf} and {@code CoreMatchers.both} by not short-circuiting
 * evaluation. This allows it to describe all mismatches and not just the first one. It is also less verbose when
 * describing a mismatch by not repeating the matcher's description.
 * <p>
 * Use {@code ComposeMatchers.compose} to obtain instances of this class. 
 * 
 * @param <T>
 *            the type of the object to be matched
 * @see ComposeMatchers#compose(Matcher)
 */
public class ConjunctionMatcher<T> extends TypeSafeDiagnosingMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final String SEPARATOR = " and ";
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final List<Matcher<T>> matchers;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	ConjunctionMatcher(List<Matcher<T>> matchers)
	{
		requireNonNull(matchers, "matchers");
		
		if (matchers.isEmpty())
		{
			throw new IllegalArgumentException("matchers cannot be empty");
		}
		
		this.matchers = unmodifiableList(new ArrayList<>(matchers));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Returns a composite matcher that comprises of this matcher logically ANDed with the specified matcher.
	 * <p>
	 * Note that this method returns a new matcher and does not modify this instance.
	 * 
	 * @param matcher
	 *            the matcher to logically AND to this matcher
	 * @return the composed matcher
	 */
	public ConjunctionMatcher<T> and(Matcher<T> matcher)
	{
		requireNonNull(matcher, "matcher");
		
		return new ConjunctionMatcher<>(concat(matchers, matcher));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	public void describeTo(Description description)
	{
		description.appendList("", SEPARATOR, "", matchers);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeDiagnosingMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected boolean matchesSafely(T actual, Description mismatch)
	{
		boolean matches = true;
		
		for (Matcher<T> matcher : matchers)
		{
			if (!matcher.matches(actual))
			{
				if (!matches)
				{
					mismatch.appendText(SEPARATOR);
				}
				
				matches = false;
				
				matcher.describeMismatch(actual, mismatch);
			}
		}
		
		return matches;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static <T> List<T> concat(List<T> list, T element)
	{
		List<T> newList = new ArrayList<>(list);
		newList.add(element);
		return newList;
	}
}
