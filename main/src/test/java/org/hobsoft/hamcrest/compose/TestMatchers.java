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

/**
 * Factory for matchers used by tests.
 */
final class TestMatchers
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private TestMatchers()
	{
		throw new AssertionError();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static <T> Matcher<T> nothing(String mismatch)
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
	
	public static <T> Matcher<T> relax(Matcher<? extends T> delegate)
	{
		return new BaseMatcher<T>()
		{
			@Override
			public boolean matches(Object item)
			{
				return delegate.matches(item);
			}
			
			@Override
			public void describeTo(Description description)
			{
				delegate.describeTo(description);
			}
		};
	}
	
	public static Matcher<CharSequence> charSeq(Matcher<? extends CharSequence> delegate)
	{
		return relax(delegate);
	}
}
