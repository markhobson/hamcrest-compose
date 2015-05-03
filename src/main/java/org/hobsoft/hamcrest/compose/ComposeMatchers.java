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

import static java.util.Collections.singletonList;

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

	public static <T> ConjunctionMatcher<T> compose(Matcher<T> matcher)
	{
		return new ConjunctionMatcher<>(singletonList(matcher));
	}
}
