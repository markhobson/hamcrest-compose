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

import static java.util.Collections.singletonList;

/**
 * 
 * 
 * @param <T>
 *            the type of this matcher
 */
public class ConjunctionMatcher<T> extends TypeSafeDiagnosingMatcher<T>
{
	private static final String SEPARATOR = " and ";
	
	private final List<Matcher<T>> matchers;
	
	public ConjunctionMatcher(List<Matcher<T>> matchers)
	{
		this.matchers = new ArrayList<>(matchers);
	}
	
	public static <T> ConjunctionMatcher<T> compose(Matcher<T> matcher)
	{
		return new ConjunctionMatcher<>(singletonList(matcher));
	}
	
	public ConjunctionMatcher<T> and(Matcher<T> matcher)
	{
		List<Matcher<T>> newMatchers = new ArrayList<>(matchers);
		newMatchers.add(matcher);
		
		return new ConjunctionMatcher<>(newMatchers);
	}
	
	@Override
	public void describeTo(Description description)
	{
		description.appendList("", SEPARATOR, "", matchers);
	}
	
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
}
