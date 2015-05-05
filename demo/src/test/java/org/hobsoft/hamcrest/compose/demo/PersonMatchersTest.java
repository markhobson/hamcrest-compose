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

import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.hamcrest.compose.demo.PersonMatchers.personEqualTo;
import static org.junit.Assert.assertThat;

/**
 * 
 */
public class PersonMatchersTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void describeToDescribesExpected()
	{
		Person person = new Person("x", "y", "z");
		StringDescription description = new StringDescription();
		
		personEqualTo(person).describeTo(description);
		
		assertThat(description.toString(), is("title is \"x\" and firstName is \"y\" and lastName is \"z\""));
	}
	
	@Test
	public void matchesWhenEqualReturnsTrue()
	{
		Person person1 = new Person("x", "y", "z");
		Person person2 = new Person("x", "y", "z");
		
		assertThat(personEqualTo(person1).matches(person2), is(true));
	}
	
	@Test
	public void matchesWhenTitleUnequalReturnsFalse()
	{
		Person person1 = new Person("x1", "y", "z");
		Person person2 = new Person("x2", "y", "z");
		
		assertThat(personEqualTo(person1).matches(person2), is(false));
	}
	
	@Test
	public void matchesWhenFirstNameUnequalReturnsFalse()
	{
		Person person1 = new Person("x", "y1", "z");
		Person person2 = new Person("x", "y2", "z");
		
		assertThat(personEqualTo(person1).matches(person2), is(false));
	}
	
	@Test
	public void matchesWhenLastNameUnequalReturnsFalse()
	{
		Person person1 = new Person("x", "y", "z1");
		Person person2 = new Person("x", "y", "z2");
		
		assertThat(personEqualTo(person1).matches(person2), is(false));
	}
	
	@Test
	public void describeMismatchWhenTitleUnequalDescribesMismatch()
	{
		Person person1 = new Person("x1", "y", "z");
		Person person2 = new Person("x2", "y", "z");
		StringDescription description = new StringDescription();
		
		personEqualTo(person1).describeMismatch(person2, description);
		
		assertThat(description.toString(), is("title was \"x2\""));
	}
	
	@Test
	public void describeMismatchWhenFirstNameUnequalDescribesMismatch()
	{
		Person person1 = new Person("x", "y1", "z");
		Person person2 = new Person("x", "y2", "z");
		StringDescription description = new StringDescription();
		
		personEqualTo(person1).describeMismatch(person2, description);
		
		assertThat(description.toString(), is("firstName was \"y2\""));
	}
	
	@Test
	public void describeMismatchWhenLastNameUnequalDescribesMismatch()
	{
		Person person1 = new Person("x", "y", "z1");
		Person person2 = new Person("x", "y", "z2");
		StringDescription description = new StringDescription();
		
		personEqualTo(person1).describeMismatch(person2, description);
		
		assertThat(description.toString(), is("lastName was \"z2\""));
	}
	
	@Test
	public void describeMismatchWhenAllPropertiesUnequalDescribesMismatch()
	{
		Person person1 = new Person("x1", "y1", "z1");
		Person person2 = new Person("x2", "y2", "z2");
		StringDescription description = new StringDescription();
		
		personEqualTo(person1).describeMismatch(person2, description);
		
		assertThat(description.toString(), is("title was \"x2\" and firstName was \"y2\" and lastName was \"z2\""));
	}
}
