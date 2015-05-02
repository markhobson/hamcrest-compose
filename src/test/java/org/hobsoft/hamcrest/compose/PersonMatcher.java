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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.is;

/**
 * 
 */
public class PersonMatcher extends TypeSafeMatcher<Person>
{
	private final Matcher<String> titleMatcher;
	
	private final Matcher<String> firstNameMatcher;
	
	private final Matcher<String> lastNameMatcher;

	public PersonMatcher(Person expected)
	{
		titleMatcher = is(expected.getTitle());
		firstNameMatcher = is(expected.getFirstName());
		lastNameMatcher = is(expected.getLastName());
	}

	public static Matcher<Person> personEqualTo(Person expected)
	{
		return new PersonMatcher(expected);
	}

	public void describeTo(Description description)
	{
		description.appendText("title ").appendDescriptionOf(titleMatcher).appendText(" and ");
		description.appendText("firstName ").appendDescriptionOf(firstNameMatcher).appendText(" and ");
		description.appendText("lastName ").appendDescriptionOf(lastNameMatcher);
	}

	@Override
	protected boolean matchesSafely(Person actual)
	{
		return titleMatcher.matches(actual.getTitle())
			&& firstNameMatcher.matches(actual.getFirstName())
			&& lastNameMatcher.matches(actual.getLastName());
	}
	
	@Override
	protected void describeMismatchSafely(final Person actual, Description mismatch)
	{
		List<SelfDescribing> mismatches = new ArrayList<SelfDescribing>();
		
		if (!titleMatcher.matches(actual.getTitle()))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					mismatch.appendText("title ");
					titleMatcher.describeMismatch(actual.getTitle(), mismatch);
				}
			});
		}
		
		if (!firstNameMatcher.matches(actual.getFirstName()))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					mismatch.appendText("firstName ");
					firstNameMatcher.describeMismatch(actual.getFirstName(), mismatch);
				}
			});
		}
		
		if (!lastNameMatcher.matches(actual.getLastName()))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					mismatch.appendText("lastName ");
					lastNameMatcher.describeMismatch(actual.getLastName(), mismatch);
				}
			});
		}
		
		mismatch.appendList("", " and ", "", mismatches);
	}
}
