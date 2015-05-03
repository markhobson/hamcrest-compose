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

import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.hamcrest.compose.TestMatchers.nothing;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code TestMatchers}.
 */
public class TestMatchersTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void nothingThenDescribeToDescribesMatcher()
	{
		StringDescription description = new StringDescription();
		
		nothing("x").describeTo(description);
		
		assertThat(description.toString(), is("nothing"));
	}
	
	@Test
	public void nothingThenMatchesReturnsFalse()
	{
		assertThat(nothing("x").matches("y"), is(false));
	}
	
	@Test
	public void nothingThenDescribeMismatchDescribesMismatch()
	{
		StringDescription description = new StringDescription();
		
		nothing("x").describeMismatch("y", description);
		
		assertThat(description.toString(), is("x was \"y\""));
	}
}
