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

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.StringDescription.asString;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code ComposeMatchers}.
 */
public class ComposeMatchersTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void composeReturnsConjunctionMatcher()
	{
		assertThat(compose(startsWith("x")).and(endsWith("y")).matches("xy"), is(true));
	}
	
	@Test(expected = NullPointerException.class)
	public void composeWithNullMatcherThrowsException()
	{
		compose(null);
	}
	
	@Test
	public void hasFeatureReturnsHasFeatureMatcher()
	{
		assertThat(hasFeature("x", "y", String::length, is(1)).matches("z"), is(true));
	}
	
	@Test
	public void hasFeatureWithoutDescriptionDefaultsDescription()
	{
		Matcher<String> matcher = hasFeature("x", String::length, is(1));
		
		assertThat(asString(matcher), is("x is <1>"));
	}

	@Test
	public void hasFeatureWithoutNameDefaultsDescription()
	{
		Matcher<String> matcher = hasFeature(stringToLength("x"), is(1));

		assertThat(asString(matcher), is("x is <1>"));
	}

	@Test
	public void hasFeatureWithoutNameDefaultsName()
	{
		Matcher<String> matcher = hasFeature(stringToLength("x"), is(1));

		StringDescription description = new StringDescription();
		matcher.describeMismatch("yy", description);
		assertThat(description.toString(), is("x was <2>"));
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
