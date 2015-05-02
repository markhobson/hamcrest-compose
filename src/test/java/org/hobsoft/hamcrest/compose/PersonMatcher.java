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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
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
		// TODO: implement
	}

	@Override
	protected boolean matchesSafely(Person actual)
	{
		return expected.getTitle().equals(actual.getTitle())
			&& expected.getFirstName().equals(actual.getFirstName())
			&& expected.getLastName().equals(actual.getLastName());
	}
}
