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
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * Matcher that composes a list of other matchers using a logical AND.
 * <p>
 * This matcher differs from {@code CoreMatchers.allOf} and {@code CoreMatchers.both} in the following ways:
 * <ul>
 * <li>It does not short circuit. This means that all mismatches are reported, not just the first one.
 * <li>It does not describe itself using parenthesis. This produces more readable descriptions.
 * <li>It supports an optional description to help describe the composition
 * <li>It does not repeat the matcher's description when describing a mismatch
 * </ul>
 * <p>
 * Use {@code ComposeMatchers.compose} to obtain instances of this class. 
 * 
 * @param <T>
 *            the type of the object to be matched
 * @see ComposeMatchers#compose(Matcher)
 */
public final class ConjunctionMatcher<T> extends TypeSafeDiagnosingMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final String SEPARATOR = " and ";
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final String compositeDescription;

	/**
	 * How deep this {@link ConjunctionMatcher} is in the hierarchy
	 */
	private final int indentation;
	
	private final List<Matcher<T>> matchers;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	ConjunctionMatcher(String compositeDescription, Iterable<Matcher<T>> matchers)
	{
		requireNonNull(matchers, "matchers");
		
		this.compositeDescription = compositeDescription;
		this.matchers = unmodifiableList(toList(matchers));
		this.indentation = 0;
	}

	ConjunctionMatcher(ConjunctionMatcher<T> parent, ConjunctionMatcher<T> src) {
		requireNonNull(parent, "parent");
		requireNonNull(src, "src");

		this.compositeDescription = src.compositeDescription;
		this.indentation = parent.indentation + 1; // deeper though!
		this.matchers = src.matchers.stream().map(m ->
			{   // Gotta go after the nested ones now
				if (m instanceof ConjunctionMatcher)
				{
					return new ConjunctionMatcher<>(this, (ConjunctionMatcher<T>)m);
				}
				return m;
			})
			.collect(Collectors.toList()); // intentional reference copy
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

		// Add some checks to see if we are "and-ing" a ConjunctionMatcher,
		// if so, we can do some nicer nesting.
		final Matcher<T> toConcat;
		if (matcher instanceof ConjunctionMatcher)
		{
			toConcat = new ConjunctionMatcher<>(this, (ConjunctionMatcher<T>)matcher);
		}
		else
		{
			toConcat = matcher;
		}

		return new ConjunctionMatcher<>(compositeDescription, concat(matchers, toConcat));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	private void describeIndentation(Description description) {
		for (int i = 0; i < indentation; ++i)
		{
			description.appendText("\t");
		}
	}

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

			description.appendText(start);

			boolean first = true;
			for (Matcher<T> matcher: matchers) {

				if (!first) {
					describeSeparatorForMatcher(description, matcher);
				}

				first = false;
				description.appendDescriptionOf(matcher);
			}
		}
	}

	/**
	 * Used to append a separator but takes into account the indentation requirements for composite values.
	 * @param description Description appended to
	 * @param matcher matcher being described
	 */
	private void describeSeparatorForMatcher(final Description description, final Matcher<T> matcher) {
		description.appendText("\n");
		if (matcher instanceof ConjunctionMatcher) {
			((ConjunctionMatcher<T>) matcher).describeIndentation(description);
		}

		description.appendText(SEPARATOR);
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
					describeSeparatorForMatcher(mismatch, matcher);
				}
				else if (compositeDescription != null)
				{
					mismatch.appendText(compositeDescription)
							.appendText(" ");
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
