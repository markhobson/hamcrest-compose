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
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.is;

/**
 * 
 */
public class PersonMatcher extends TypeSafeMatcher<Person>
{
	private final Matcher<Person> titleMatcher;
	
	private final Matcher<Person> firstNameMatcher;
	
	private final Matcher<Person> lastNameMatcher;

	public PersonMatcher(Person expected)
	{
		titleMatcher = new FeatureMatcher<Person, String>(is(expected.getTitle()), "title", "title")
		{
			@Override
			protected String featureValueOf(Person actual)
			{
				return actual.getTitle();
			}
		};
		
		firstNameMatcher = new FeatureMatcher<Person, String>(is(expected.getFirstName()), "firstName", "firstName")
		{
			@Override
			protected String featureValueOf(Person actual)
			{
				return actual.getFirstName();
			}
		};
		
		lastNameMatcher = new FeatureMatcher<Person, String>(is(expected.getLastName()), "lastName", "lastName")
		{
			@Override
			protected String featureValueOf(Person actual)
			{
				return actual.getLastName();
			}
		};
	}

	public static Matcher<Person> personEqualTo(Person expected)
	{
		return new PersonMatcher(expected);
	}

	public void describeTo(Description description)
	{
		description.appendDescriptionOf(titleMatcher).appendText(" and ");
		description.appendDescriptionOf(firstNameMatcher).appendText(" and ");
		description.appendDescriptionOf(lastNameMatcher);
	}

	@Override
	protected boolean matchesSafely(Person actual)
	{
		return titleMatcher.matches(actual)
			&& firstNameMatcher.matches(actual)
			&& lastNameMatcher.matches(actual);
	}
	
	@Override
	protected void describeMismatchSafely(final Person actual, Description mismatch)
	{
		List<SelfDescribing> mismatches = new ArrayList<SelfDescribing>();
		
		if (!titleMatcher.matches(actual))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					titleMatcher.describeMismatch(actual, mismatch);
				}
			});
		}
		
		if (!firstNameMatcher.matches(actual))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					firstNameMatcher.describeMismatch(actual, mismatch);
				}
			});
		}
		
		if (!lastNameMatcher.matches(actual))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					lastNameMatcher.describeMismatch(actual, mismatch);
				}
			});
		}
		
		mismatch.appendList("", " and ", "", mismatches);
	}
}
