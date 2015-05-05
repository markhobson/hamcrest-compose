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

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

/**
 * Factory for Hamcrest Compose matchers.
 */
public final class ComposeMatchers
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private ComposeMatchers()
	{
		throw new AssertionError();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Returns a matcher that logically ANDs the specified matcher with any number of further matchers.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("xyz", compose(startsWith("x")).and(containsString("y")).and(endsWith("z")));
	 * </pre>
	 * See {@code ConjunctionMatcher} as to how this matcher differs from {@code allOf} and {@code both}.
	 * 
	 * @param matcher
	 *            the first matcher to compose
	 * @return a matcher that can compose itself with further matchers
	 * @see ConjunctionMatcher
	 */
	public static <T> ConjunctionMatcher<T> compose(Matcher<T> matcher)
	{
		requireNonNull(matcher, "matcher");
		
		return new ConjunctionMatcher<>(singletonList(matcher));
	}
	
	/**
	 * Returns a matcher that matches the specified feature of an object.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("xyz", hasFeature("string length", String::length, is(3)));
	 * </pre>
	 * 
	 * @param featureName
	 *            the name of this feature used by {@code describeTo} and {@code describeMismatch}
	 * @param featureFunction
	 *            a function to extract the feature from the object
	 * @param featureMatcher
	 *            the matcher to apply to the specified feature
	 * @param <T>
	 *            the type of the object to be matched
	 * @param <U>
	 *            the type of the feature to be matched
	 * @return the feature matcher
	 */
	public static <T, U> Matcher<T> hasFeature(String featureName, Function<T, U> featureFunction,
		Matcher<? super U> featureMatcher)
	{
		return hasFeature(featureName, featureName, featureFunction, featureMatcher);
	}
	
	/**
	 * Returns a matcher that matches the specified feature of an object.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("xyz", hasFeature("a string with length", "string length", String::length, is(3)));
	 * </pre>
	 * 
	 * @param featureDescription
	 *            a description of this feature used by {@code describeTo}
	 * @param featureName
	 *            the name of this feature used by {@code describeMismatch}
	 * @param featureFunction
	 *            a function to extract the feature from the object
	 * @param featureMatcher
	 *            the matcher to apply to the specified feature
	 * @param <T>
	 *            the type of the object to be matched
	 * @param <U>
	 *            the type of the feature to be matched
	 * @return the feature matcher
	 */
	public static <T, U> Matcher<T> hasFeature(String featureDescription, String featureName,
		Function<T, U> featureFunction, Matcher<? super U> featureMatcher)
	{
		return new HasFeatureMatcher<>(featureDescription, featureName, featureFunction, featureMatcher);
	}
}
