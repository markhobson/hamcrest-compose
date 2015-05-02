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

import static org.hamcrest.Matchers.is;
import static org.hobsoft.hamcrest.compose.ConjunctionMatcher.compose;
import static org.hobsoft.hamcrest.compose.HasFeatureMatcher.hasFeature;

/**
 * 
 */
public final class PersonMatcher
{
	private PersonMatcher()
	{
		throw new AssertionError();
	}
	
	public static Matcher<Person> personEqualTo(Person expected)
	{
		return compose(hasFeature("title", Person::getTitle, is(expected.getTitle())))
			.and(hasFeature("firstName", Person::getFirstName, is(expected.getFirstName())))
			.and(hasFeature("lastName", Person::getLastName, is(expected.getLastName())));
	}
}
