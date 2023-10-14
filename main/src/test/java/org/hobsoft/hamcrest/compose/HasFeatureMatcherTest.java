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

import java.util.function.Function;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.hobsoft.hamcrest.compose.TestMatchers.nothing;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code HasFeatureMatcher}.
 */
public class HasFeatureMatcherTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void describeToDescribesMatcher()
	{
		Matcher<String> matcher = hasFeature("x", "y", String::length, anything("z"));
		StringDescription description = new StringDescription();
		
		matcher.describeTo(description);
		
		assertThat(description.toString(), is("x z"));
	}
	
	@Test
	public void describeToWhenNoDescriptionUsesName()
	{
		Matcher<String> matcher = hasFeature("x", String::length, anything("y"));
		StringDescription description = new StringDescription();
		
		matcher.describeTo(description);
		
		assertThat(description.toString(), is("x y"));
	}
	
	@Test
	public void describeToWhenNoNameUsesFunction()
	{
		Matcher<String> matcher = hasFeature(stringToLength("x"), anything("y"));
		StringDescription description = new StringDescription();
		
		matcher.describeTo(description);
		
		assertThat(description.toString(), is("x y"));
	}
	
	@Test
	public void describeToWhenNoNameUsesMethodReference()
	{
		Matcher<String> matcher = hasFeature(String::length, anything("y"));
		StringDescription description = new StringDescription();

		matcher.describeTo(description);

		assertThat(description.toString(), is("length y"));
	}

	@Test
	public void matchesWhenFeatureMatcherMatchesReturnsTrue()
	{
		Matcher<String> matcher = hasFeature("x", "y", String::length, equalTo(1));
		
		assertThat(matcher.matches("z"), is(true));
	}
	
	@Test
	public void matchesWhenFeatureMatcherDoesNotMatchReturnsFalse()
	{
		Matcher<String> matcher = hasFeature("x", "y", String::length, equalTo(2));
		
		assertThat(matcher.matches("z"), is(false));
	}
	
	@Test
	public void describeMismatchDescribesMismatch()
	{
		Matcher<String> matcher = hasFeature("x", "y", String::length, nothing("z"));
		StringDescription description = new StringDescription();
		
		matcher.describeMismatch("a", description);
		
		assertThat(description.toString(), is("y z was <1>"));
	}
	
	@Test
	public void describeMismatchWhenNoNameUsesFunction()
	{
		Matcher<String> matcher = hasFeature(stringToLength("x"), nothing("y"));
		StringDescription description = new StringDescription();
		
		matcher.describeMismatch("a", description);
		
		assertThat(description.toString(), is("x y was <1>"));
	}

	@Test
	public void describeMismatchWhenNoNameUsesMethodReference()
	{
		Matcher<String> matcher = hasFeature(String::length, nothing("y"));
		StringDescription description = new StringDescription();

		matcher.describeMismatch("a", description);

		assertThat(description.toString(), is("length y was <1>"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Function<String, Integer> stringToLength(String name)
	{
		return new Function<String, Integer>()
		{
			@Override
			public Integer apply(String string)
			{
				return string.length();
			}

			@Override
			public String toString()
			{
				return name;
			}
		};
	}
}
