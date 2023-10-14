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

import org.hamcrest.Matcher;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeatureValue;
import static org.hobsoft.hamcrest.compose.TestMatchers.charSeq;
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
	public void composeWithMatcherReturnsMatcher()
	{
		Matcher<String> actual = compose("x", startsWith("y")).and(endsWith("z"));
		
		assertThat(actual.matches("yz"), is(true));
	}
	
	@Test
	public void composeWithSupertypeMatcherReturnsMatcher()
	{
		Matcher<String> actual = ComposeMatchers.<String>compose("x", charSeq(startsWith("y")))
			.and(charSeq(endsWith("z")));
		
		assertThat(actual.matches("yz"), is(true));
	}
	
	@Test
	public void composeWithMatcherWithoutDescriptionReturnsMatcher()
	{
		Matcher<String> actual = compose(startsWith("x")).and(endsWith("y"));
		
		assertThat(actual.matches("xy"), is(true));
	}
	
	@Test(expected = NullPointerException.class)
	public void composeWithNullMatcherThrowsException()
	{
		compose("x", (Matcher<Object>) null);
	}
	
	@Test
	public void composeWithMatcherArrayReturnsMatcher()
	{
		Matcher<String> actual = compose("x", startsWith("y"), endsWith("z"));
		
		assertThat(actual.matches("yz"), is(true));
	}
	
	@Test
	public void composeWithSupertypeMatcherArrayReturnsMatcher()
	{
		Matcher<String> actual = compose("x", charSeq(startsWith("y")), charSeq(endsWith("z")));
		
		assertThat(actual.matches("yz"), is(true));
	}
	
	@Test
	public void composeWithMatcherArrayWithoutDescriptionReturnsMatcher()
	{
		Matcher<String> actual = compose(startsWith("x"), endsWith("y"));
		
		assertThat(actual.matches("xy"), is(true));
	}
	
	@Test(expected = NullPointerException.class)
	public void composeWithNullMatcherArrayThrowsException()
	{
		compose("x", (Matcher<Object>[]) null);
	}
	
	@Test(expected = NullPointerException.class)
	public void composeWithMatcherArrayContainingNullThrowsException()
	{
		compose("x", startsWith("y"), null, endsWith("z"));
	}
	
	@Test
	public void composeWithNoMatcherReturnsMatcher()
	{
		Matcher<?> matcher = compose("x");
		
		assertThat(matcher.matches("y"), is(true));
	}
	
	@Test
	public void composeWithMatcherListReturnsMatcher()
	{
		Matcher<String> actual = compose("x", asList(startsWith("y"), endsWith("z")));
		
		assertThat(actual.matches("yz"), is(true));
	}
	
	@Test
	public void composeWithSupertypeMatcherListReturnsMatcher()
	{
		Matcher<String> actual = compose("x", asList(charSeq(startsWith("y")), charSeq(endsWith("z"))));
		
		assertThat(actual.matches("yz"), is(true));
	}
	
	@Test
	public void composeWithMatcherListWithoutDescriptionReturnsMatcher()
	{
		Matcher<String> actual = compose(asList(startsWith("y"), endsWith("z")));
		
		assertThat(actual.matches("yz"), is(true));
	}
	
	@Test
	public void composeWithEmptyMatcherListReturnsMatcher()
	{
		Matcher<String> actual = compose("x", emptyList());
		
		assertThat(actual.matches("y"), is(true));
	}
	
	@Test(expected = NullPointerException.class)
	public void composeWithNullMatcherListThrowsException()
	{
		compose("x", (List<Matcher<? super Object>>) null);
	}
	
	@Test(expected = NullPointerException.class)
	public void composeWithMatcherListContainingNullThrowsException()
	{
		compose("x", asList(startsWith("y"), null, endsWith("z")));
	}
	
	@Test
	public void composeWithMatchersClonesList()
	{
		List<Matcher<? super String>> matchers = new ArrayList<>();
		matchers.add(startsWith("y"));
		
		Matcher<String> actual = compose("x", matchers);
		matchers.add(endsWith("z"));
		
		assertThat(actual.matches("y"), is(true));
	}
	
	@Test
	public void hasFeatureReturnsMatcher()
	{
		Matcher<String> actual = hasFeature("x", "y", String::length, equalTo(1));
		
		assertThat(actual.matches("z"), is(true));
	}
	
	@Test
	public void hasFeatureWithoutDescriptionReturnsMatcher()
	{
		Matcher<String> actual = hasFeature("x", String::length, equalTo(1));
		
		assertThat(actual.matches("y"), is(true));
	}

	@Test
	public void hasFeatureWithoutNameReturnsMatcher()
	{
		Matcher<String> actual = hasFeature(String::length, equalTo(1));

		assertThat(actual.matches("x"), is(true));
	}
	
	@Test(expected = NullPointerException.class)
	public void hasFeatureWithNullFunctionThrowsException()
	{
		hasFeature("x", "y", null, anything());
	}
	
	@Test
	public void hasFeatureValueReturnsMatcher()
	{
		Matcher<String> actual = hasFeatureValue("x", "y", String::length, 1);
		
		assertThat(actual.matches("z"), is(true));
	}
	
	@Test
	public void hasFeatureValueWithoutDescriptionReturnsMatcher()
	{
		Matcher<String> actual = hasFeatureValue("x", String::length, 1);
		
		assertThat(actual.matches("y"), is(true));
	}
	
	@Test
	public void hasFeatureValueWithoutNameReturnsMatcher()
	{
		Matcher<String> actual = hasFeatureValue(String::length, 1);
		
		assertThat(actual.matches("x"), is(true));
	}
	
	@Test(expected = NullPointerException.class)
	public void hasFeatureValueWithNullFunctionThrowsException()
	{
		hasFeatureValue("x", "y", null, anything());
	}
}
