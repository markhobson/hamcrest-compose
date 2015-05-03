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

/**
 * 
 * 
 * @param <T>
 *            the type of this matcher
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

	public ConjunctionMatcher(List<Matcher<T>> matchers)
	{
		this.matchers = unmodifiableList(new ArrayList<>(matchers));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public ConjunctionMatcher<T> and(Matcher<T> matcher)
	{
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
