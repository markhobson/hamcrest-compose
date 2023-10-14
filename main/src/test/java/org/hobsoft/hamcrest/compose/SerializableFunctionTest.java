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

import java.io.ObjectStreamException;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code SerializableFunction}.
 */
public class SerializableFunctionTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// types
	// ----------------------------------------------------------------------------------------------------------------
	
	private static class StringToLengthFunction implements SerializableFunction<String, Integer>
	{
		private final String name;
		
		StringToLengthFunction(String name)
		{
			this.name = name;
		}
		
		@Override
		public Integer apply(String string)
		{
			return string.length();
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getNameWithMethodReferenceReturnsMethodName()
	{
		SerializableFunction<String, Integer> function = String::length;
		
		assertThat(function.getName(), is("length"));
	}

	@Test
	public void getNameWithNoReplacementReturnsToString()
	{
		SerializableFunction<String, Integer> function = new StringToLengthFunction("x");
		
		assertThat(function.getName(), is("x"));
	}

	@Test
	public void getNameWithNoSerializedLambdaReplacementReturnsToString()
	{
		SerializableFunction<String, Integer> function = new StringToLengthFunction("x")
		{
			private Object writeReplace() throws ObjectStreamException
			{
				return "y";
			}
		};
		
		assertThat(function.getName(), is("x"));
	}
}
