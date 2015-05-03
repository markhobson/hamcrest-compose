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

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
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

	@Test(expected = NullPointerException.class)
	public void constructorWithNullFunctionThrowsException()
	{
		new HasFeatureMatcher<>("x", "y", null, anything());
	}
	
	@Test
	public void describeToDescribesMatcher()
	{
		StringDescription description = new StringDescription();
		Matcher<String> matcher = new HasFeatureMatcher<>("x", "y", Integer::parseInt, anything("z"));
		
		matcher.describeTo(description);
		
		assertThat(description.toString(), is("x z"));
	}
	
	@Test
	public void matchesWhenFeatureMatcherMatchesReturnsTrue()
	{
		Matcher<String> matcher = new HasFeatureMatcher<>("x", "y", Integer::parseInt, is(1));
		
		assertThat(matcher.matches("1"), is(true));
	}
	
	@Test
	public void matchesWhenFeatureMatcherDoesNotMatchReturnsFalse()
	{
		Matcher<String> matcher = new HasFeatureMatcher<>("x", "y", Integer::parseInt, is(2));
		
		assertThat(matcher.matches("1"), is(false));
	}
	
	@Test
	public void describeMismatchDescribesMismatch()
	{
		StringDescription description = new StringDescription();
		Matcher<String> matcher = new HasFeatureMatcher<>("x", "y", Integer::parseInt, nothing("z"));
		
		matcher.describeMismatch("1", description);
		
		assertThat(description.toString(), is("y z was <1>"));
	}
}
