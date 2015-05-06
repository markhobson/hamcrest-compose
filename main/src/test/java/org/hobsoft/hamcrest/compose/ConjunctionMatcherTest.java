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

import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.StringDescription.asString;
import static org.hobsoft.hamcrest.compose.ConjunctionMatcher.compose;
import static org.hobsoft.hamcrest.compose.TestMatchers.nothing;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code ConjunctionMatcher}.
 */
public class ConjunctionMatcherTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void composeWithMatcherReturnsCompositeMatcher()
	{
		ConjunctionMatcher<Object> actual = compose(anything("x"));
		
		assertThat(asString(actual), is("x"));
	}
	
	@Test(expected = NullPointerException.class)
	public void composeWithNullMatcherThrowsException()
	{
		compose(null);
	}
	
	@Test
	public void andReturnsCompositeMatcher()
	{
		ConjunctionMatcher<Object> actual = compose(anything("x")).and(anything("y"));
		
		assertThat(asString(actual), is("x and y"));
	}
	
	@Test
	public void andPreservesMatcher()
	{
		ConjunctionMatcher<Object> matcher = compose(anything("x"));
		
		matcher.and(anything());
		
		assertThat(asString(matcher), is("x"));
	}
	
	@Test(expected = NullPointerException.class)
	public void andWithNullMatcherThrowsException()
	{
		compose(anything()).and(null);
	}

	@Test
	public void describeToWhenMatcherDescribesMatcher()
	{
		StringDescription description = new StringDescription();
		
		compose(anything("x")).describeTo(description);
		
		assertThat(description.toString(), is("x"));
	}
	
	@Test
	public void describeToWhenMatchersDescribesMatchers()
	{
		StringDescription description = new StringDescription();
		
		compose(anything("x")).and(anything("y")).describeTo(description);
		
		assertThat(description.toString(), is("x and y"));
	}
	
	@Test
	public void matchesWhenMatchersMatchReturnsTrue()
	{
		assertThat(compose(startsWith("x")).and(endsWith("y")).matches("xy"), is(true));
	}
	
	@Test
	public void matchesWhenFirstMatcherDoesNotMatchReturnsFalse()
	{
		assertThat(compose(startsWith("x")).and(endsWith("y")).matches("zy"), is(false));
	}
	
	@Test
	public void matchesWhenSecondMatcherDoesNotMatchReturnsFalse()
	{
		assertThat(compose(startsWith("x")).and(endsWith("y")).matches("xz"), is(false));
	}
	
	@Test
	public void matchesWhenMatchersDoNotMatchReturnsFalse()
	{
		assertThat(compose(startsWith("x")).and(endsWith("y")).matches("z"), is(false));
	}
	
	@Test
	public void describeMismatchWhenFirstMatcherDoesNotMatchDescribesMismatch()
	{
		StringDescription description = new StringDescription();
		
		compose(nothing("x")).and(anything()).describeMismatch("y", description);
		
		assertThat(description.toString(), is("x was \"y\""));
	}
	
	@Test
	public void describeMismatchWhenSecondMatcherDoesNotMatchDescribesMismatch()
	{
		StringDescription description = new StringDescription();
		
		compose(anything()).and(nothing("x")).describeMismatch("y", description);
		
		assertThat(description.toString(), is("x was \"y\""));
	}
	
	@Test
	public void describeMismatchWhenMatchersDoNotMatchDescribesMismatches()
	{
		StringDescription description = new StringDescription();
		
		compose(nothing("x")).and(nothing("y")).describeMismatch("z", description);
		
		assertThat(description.toString(), is("x was \"z\" and y was \"z\""));
	}
}
