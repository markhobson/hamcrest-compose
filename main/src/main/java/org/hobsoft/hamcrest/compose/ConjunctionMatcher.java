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
 * This matcher differs from {@code CoreMatchers.allOf} and {@code CoreMatchers.both} in the following ways:
 * <ul>
 * <li>It does not short circuit. This means that all mismatches are reported, not just the first one.
 * <li>It does not describe itself using parenthesis. This produces more readable descriptions.
 * <li>It describes each matcher on a separate line
 * <li>It supports an optional description to help describe the composition
 * <li>It does not repeat the matcher's description when describing a mismatch
 * </ul>
 * <p>
 * Use {@code ComposeMatchers.compose} to obtain instances of this class. 
 * 
 * @param <T>
 *            the type of the object to be matched
 * @see ComposeMatchers#compose(Matcher...)
 */
public final class ConjunctionMatcher<T> extends TypeSafeDiagnosingMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	//                                         Expected: <description>
	private static final String SEPARATOR = "\n          and ";
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final String compositeDescription;
	
	private final List<Matcher<? super T>> matchers;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	ConjunctionMatcher(String compositeDescription, Iterable<Matcher<? super T>> matchers)
	{
		requireNonNull(matchers, "matchers");
		matchers.forEach(matcher -> requireNonNull(matcher, "matcher"));
		
		this.compositeDescription = compositeDescription;
		this.matchers = unmodifiableList(toList(matchers));
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
	public ConjunctionMatcher<T> and(Matcher<? super T> matcher)
	{
		requireNonNull(matcher, "matcher");
		
		return new ConjunctionMatcher<>(compositeDescription, concat(matchers, matcher));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	public void describeTo(Description description)
	{
		if (matchers.isEmpty())
		{
			description.appendText(compositeDescription != null ? compositeDescription : "anything");
		}
		else
		{
			String start = (compositeDescription != null) ? compositeDescription + " " : "";
		
			description.appendList(start, SEPARATOR, "", matchers);
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeDiagnosingMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected boolean matchesSafely(T actual, Description mismatch)
	{
		boolean matches = true;
		
		for (Matcher<? super T> matcher : matchers)
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

	private static <E> List<E> toList(Iterable<E> iterable)
	{
		List<E> list = new ArrayList<>();
		iterable.forEach(list::add);
		return list;
	}

	private static <E> List<E> concat(List<E> list, E element)
	{
		List<E> newList = new ArrayList<>(list);
		newList.add(element);
		return newList;
	}
}
