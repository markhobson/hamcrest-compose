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

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.StringDescription.asString;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code ConjunctionMatcher}.
 */
public class ConjunctionMatcherTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test(expected = NullPointerException.class)
	public void constructorWithNullMatchersThrowsException()
	{
		new ConjunctionMatcher<>(null);
	}
	
	@Test
	public void andReturnsCompositeMatcher()
	{
		ConjunctionMatcher<Object> actual = new ConjunctionMatcher<>(asList(anything("x"))).and(anything("y"));
		
		assertThat(asString(actual), is("x and y"));
	}
	
	@Test
	public void andPreservesMatcher()
	{
		ConjunctionMatcher<Object> matcher = new ConjunctionMatcher<>(asList(anything("x")));
		
		matcher.and(anything());
		
		assertThat(asString(matcher), is("x"));
	}
	
	@Test(expected = NullPointerException.class)
	public void andWithNullMatcherThrowsException()
	{
		new ConjunctionMatcher<>(asList(anything())).and(null);
	}

	@Test
	public void describeToWhenMatcherDescribesMatcher()
	{
		StringDescription description = new StringDescription();
		
		new ConjunctionMatcher<>(asList(anything("x"))).describeTo(description);
		
		assertThat(description.toString(), is("x"));
	}
	
	@Test
	public void describeToWhenMatchersDescribesMatchers()
	{
		StringDescription description = new StringDescription();
		
		new ConjunctionMatcher<>(asList(anything("x"), anything("y"))).describeTo(description);
		
		assertThat(description.toString(), is("x and y"));
	}
	
	@Test
	public void matchesWhenMatchersMatchReturnsTrue()
	{
		assertThat(new ConjunctionMatcher<>(asList(startsWith("x"), endsWith("y"))).matches("xy"), is(true));
	}
	
	@Test
	public void matchesWhenFirstMatcherDoesNotMatchReturnsFalse()
	{
		assertThat(new ConjunctionMatcher<>(asList(startsWith("x"), endsWith("y"))).matches("zy"), is(false));
	}
	
	@Test
	public void matchesWhenSecondMatcherDoesNotMatchReturnsFalse()
	{
		assertThat(new ConjunctionMatcher<>(asList(startsWith("x"), endsWith("y"))).matches("xz"), is(false));
	}
	
	@Test
	public void matchesWhenMatchersDoNotMatchReturnsFalse()
	{
		assertThat(new ConjunctionMatcher<>(asList(startsWith("x"), endsWith("y"))).matches("z"), is(false));
	}
	
	@Test
	public void describeMismatchWhenFirstMatcherDoesNotMatchDescribesMismatch()
	{
		StringDescription description = new StringDescription();
		
		new ConjunctionMatcher<>(asList(nothing("x"), anything())).describeMismatch("y", description);
		
		assertThat(description.toString(), is("x was \"y\""));
	}
	
	@Test
	public void describeMismatchWhenSecondMatcherDoesNotMatchDescribesMismatch()
	{
		StringDescription description = new StringDescription();
		
		new ConjunctionMatcher<>(asList(anything(), nothing("x"))).describeMismatch("y", description);
		
		assertThat(description.toString(), is("x was \"y\""));
	}
	
	@Test
	public void describeMismatchWhenMatchersDoNotMatchDescribesMismatches()
	{
		StringDescription description = new StringDescription();
		
		new ConjunctionMatcher<>(asList(nothing("x"), nothing("y"))).describeMismatch("z", description);
		
		assertThat(description.toString(), is("x was \"z\" and y was \"z\""));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static <T> Matcher<T> nothing(String mismatch)
	{
		return new BaseMatcher<T>()
		{
			@Override
			public void describeTo(Description description)
			{
				description.appendText("nothing");
			}
			
			@Override
			public boolean matches(Object actual)
			{
				return false;
			}
			
			@Override
			public void describeMismatch(Object actual, Description description)
			{
				description.appendText(mismatch).appendText(" was ").appendValue(actual);
			}
		};
	}
}
