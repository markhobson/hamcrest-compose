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

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

import static org.hamcrest.CoreMatchers.equalTo;

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
	 * Returns a matcher that logically ANDs the specified matchers with any number of further matchers.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", compose(startsWith("h")).and(containsString("a")).and(endsWith("m")));
	 * </pre>
	 * See {@code ConjunctionMatcher} as to how this matcher differs from {@code allOf} and {@code both}.
	 * 
	 * @param matchers
	 *            the initial matchers to compose
	 * @param <T>
	 *            the type of the object to be matched
	 * @return a matcher that can compose itself with further matchers
	 * @see ConjunctionMatcher
	 */
	@SafeVarargs
	public static <T> ConjunctionMatcher<T> compose(Matcher<? super T>... matchers)
	{
		return compose(null, matchers);
	}
	
	/**
	 * Returns a matcher that logically ANDs the specified matchers with any number of further matchers.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", compose("a word with", startsWith("h")).and(containsString("a")).and(endsWith("m")));
	 * </pre>
	 * See {@code ConjunctionMatcher} as to how this matcher differs from {@code allOf} and {@code both}.
	 * 
	 * @param compositeDescription
	 *            a description of this composite used by {@code describeTo}
	 * @param matchers
	 *            the initial matchers to compose
	 * @param <T>
	 *            the type of the object to be matched
	 * @return a matcher that can compose itself with further matchers
	 * @see ConjunctionMatcher
	 */
	@SafeVarargs
	public static <T> ConjunctionMatcher<T> compose(String compositeDescription, Matcher<? super T>... matchers)
	{
		requireNonNull(matchers, "matchers");
		
		return compose(compositeDescription, asList(matchers));
	}

	/**
	 * Returns a matcher that logically ANDs the specified matchers with any number of further matchers.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", compose(asList(startsWith("h"), containsString("a"))).and(endsWith("m")));
	 * </pre>
	 * See {@code ConjunctionMatcher} as to how this matcher differs from {@code allOf} and {@code both}.
	 * 
	 * @param matchers
	 *            the initial matchers to compose
	 * @param <T>
	 *            the type of the object to be matched
	 * @return a matcher that can compose itself with further matchers
	 * @see ConjunctionMatcher
	 */
	public static <T> ConjunctionMatcher<T> compose(Iterable<Matcher<? super T>> matchers)
	{
		return compose(null, matchers);
	}
	
	/**
	 * Returns a matcher that logically ANDs the specified matchers with any number of further matchers.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", compose("a word with", asList(startsWith("h"), containsString("a"))).and(endsWith("m")));
	 * </pre>
	 * See {@code ConjunctionMatcher} as to how this matcher differs from {@code allOf} and {@code both}.
	 * 
	 * @param compositeDescription
	 *            a description of this composite used by {@code describeTo}
	 * @param matchers
	 *            the initial matchers to compose
	 * @param <T>
	 *            the type of the object to be matched
	 * @return a matcher that can compose itself with further matchers
	 * @see ConjunctionMatcher
	 */
	public static <T> ConjunctionMatcher<T> compose(String compositeDescription, Iterable<Matcher<? super T>> matchers)
	{
		requireNonNull(matchers, "matchers");
		
		return new ConjunctionMatcher<>(compositeDescription, matchers);
	}

	/**
	 * Returns a matcher that matches the specified feature of an object.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", hasFeature(String::length, equalTo(3)));
	 * </pre>
	 * 
	 * @param featureFunction
	 *            a function to extract the feature from the object. The string representation of this function is used
	 *            as the feature name for {@code describeTo} and {@code describeMismatch}.
	 * @param featureMatcher
	 *            the matcher to apply to the specified feature
	 * @param <T>
	 *            the type of the object to be matched
	 * @param <U>
	 *            the type of the feature to be matched
	 * @return the feature matcher
	 */
	public static <T, U> Matcher<T> hasFeature(Function<T, U> featureFunction, Matcher<? super U> featureMatcher)
	{
		return hasFeature(featureFunction.toString(), featureFunction, featureMatcher);
	}

	/**
	 * Returns a matcher that matches the specified feature of an object.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", hasFeature("string length", String::length, equalTo(3)));
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
	 * assertThat("ham", hasFeature("a string with length", "string length", String::length, equalTo(3)));
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
	
	/**
	 * Returns a matcher that matches the specified feature value of an object.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", hasFeatureValue(String::length, 3));
	 * </pre>
	 * <p>
	 * This is equivalent to {@code hasFeature(featureFunction, equalTo(featureValue))}.
	 * 
	 * @param featureFunction
	 *            a function to extract the feature from the object. The string representation of this function is used
	 *            as the feature name for {@code describeTo} and {@code describeMismatch}.
	 * @param featureValue
	 *            the feature value to match
	 * @param <T>
	 *            the type of the object to be matched
	 * @param <U>
	 *            the type of the feature to be matched
	 * @return the feature matcher
	 */
	public static <T, U> Matcher<T> hasFeatureValue(Function<T, U> featureFunction, U featureValue)
	{
		return hasFeature(featureFunction, equalTo(featureValue));
	}
	
	/**
	 * Returns a matcher that matches the specified feature value of an object.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", hasFeatureValue("string length", String::length, 3));
	 * </pre>
	 * <p>
	 * This is equivalent to {@code hasFeature(featureName, featureFunction, equalTo(featureValue))}.
	 * 
	 * @param featureName
	 *            the name of this feature used by {@code describeTo} and {@code describeMismatch}
	 * @param featureFunction
	 *            a function to extract the feature from the object
	 * @param featureValue
	 *            the feature value to match
	 * @param <T>
	 *            the type of the object to be matched
	 * @param <U>
	 *            the type of the feature to be matched
	 * @return the feature matcher
	 */
	public static <T, U> Matcher<T> hasFeatureValue(String featureName, Function<T, U> featureFunction, U featureValue)
	{
		return hasFeature(featureName, featureFunction, equalTo(featureValue));
	}
	
	/**
	 * Returns a matcher that matches the specified feature value of an object.
	 * <p>
	 * For example:
	 * <pre>
	 * assertThat("ham", hasFeatureValue("a string with length", "string length", String::length, 3));
	 * </pre>
	 * <p>
	 * This is equivalent to {@code hasFeature(featureDescription, featureName, featureFunction,
	 * equalTo(featureValue))}.
	 * 
	 * @param featureDescription
	 *            a description of this feature used by {@code describeTo}
	 * @param featureName
	 *            the name of this feature used by {@code describeMismatch}
	 * @param featureFunction
	 *            a function to extract the feature from the object
	 * @param featureValue
	 *            the feature value to match
	 * @param <T>
	 *            the type of the object to be matched
	 * @param <U>
	 *            the type of the feature to be matched
	 * @return the feature matcher
	 */
	public static <T, U> Matcher<T> hasFeatureValue(String featureDescription, String featureName,
		Function<T, U> featureFunction, U featureValue)
	{
		return hasFeature(featureDescription, featureName, featureFunction, equalTo(featureValue));
	}
}
