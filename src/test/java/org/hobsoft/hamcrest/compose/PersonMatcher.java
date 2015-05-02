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

/**
 * 
 */
public class PersonMatcher extends TypeSafeMatcher<Person>
{
	private final Person expected;

	public PersonMatcher(Person expected)
	{
		this.expected = expected;
	}

	public static Matcher<Person> personEqualTo(Person expected)
	{
		return new PersonMatcher(expected);
	}

	public void describeTo(Description description)
	{
		description.appendText("title is ").appendValue(expected.getTitle()).appendText(" and ");
		description.appendText("firstName is ").appendValue(expected.getFirstName()).appendText(" and ");
		description.appendText("lastName is ").appendValue(expected.getLastName());
	}

	@Override
	protected boolean matchesSafely(Person actual)
	{
		return expected.getTitle().equals(actual.getTitle())
			&& expected.getFirstName().equals(actual.getFirstName())
			&& expected.getLastName().equals(actual.getLastName());
	}
	
	@Override
	protected void describeMismatchSafely(final Person actual, Description mismatch)
	{
		List<SelfDescribing> mismatches = new ArrayList<SelfDescribing>();
		
		if (!expected.getTitle().equals(actual.getTitle()))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					mismatch.appendText("title was ").appendValue(actual.getTitle());
				}
			});
		}
		
		if (!expected.getFirstName().equals(actual.getFirstName()))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					mismatch.appendText("firstName was ").appendValue(actual.getFirstName());
				}
			});
		}
		
		if (!expected.getLastName().equals(actual.getLastName()))
		{
			mismatches.add(new SelfDescribing()
			{
				public void describeTo(Description mismatch)
				{
					mismatch.appendText("lastName was ").appendValue(actual.getLastName());
				}
			});
		}
		
		mismatch.appendList("", " and ", "", mismatches);
	}
}
