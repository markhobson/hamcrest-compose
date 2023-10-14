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

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static java.util.Objects.requireNonNull;

/**
 * Matcher that matches a feature of an object that is extracted by a function.
 * <p>
 * Use {@code ComposeMatchers.hasFeature} to obtain instances of this class. 
 * 
 * @param <T>
 *            the type of the object to be matched
 * @param <U>
 *            the type of the feature to be matched
 * @see ComposeMatchers#hasFeature(String, Function, Matcher)
 */
class HasFeatureMatcher<T, U> extends FeatureMatcher<T, U>
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Function<T, U> featureFunction;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	HasFeatureMatcher(String featureDescription, String featureName, Function<T, U> featureFunction,
		Matcher<? super U> featureMatcher)
	{
		super(featureMatcher, featureDescription, featureName);
		
		this.featureFunction = requireNonNull(featureFunction, "featureFunction");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// FeatureMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected final U featureValueOf(T actual)
	{
		return featureFunction.apply(actual);
	}
}
