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
package org.hobsoft.hamcrest.compose.demo;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;

/**
 * Factory for {@code Person} matchers.
 * <p>
 * Demonstrates how to use the Hamcrest Compose {@code compose} and {@code hasFeature} matchers.
 * 
 * @see Person
 */
public final class PersonMatchers
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private PersonMatchers()
	{
		throw new AssertionError();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static Matcher<Person> personEqualTo(Person expected)
	{
		return compose(hasFeature("title", Person::getTitle, equalTo(expected.getTitle())))
			.and(hasFeature("first name", Person::getFirstName, equalTo(expected.getFirstName())))
			.and(hasFeature("last name", Person::getLastName, equalTo(expected.getLastName())));
	}
}
