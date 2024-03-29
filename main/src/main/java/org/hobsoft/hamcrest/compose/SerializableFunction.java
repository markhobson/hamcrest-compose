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

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Represents a serialized function that accepts one argument and produces a result. The Java compiler will generate
 * the serialized form from a method reference.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable
{
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------
	
	default String getName()
	{
		for (Class<?> klass = getClass(); klass != null; klass = klass.getSuperclass())
		{
			try
			{
				Method writeReplace = klass.getDeclaredMethod("writeReplace");
				writeReplace.setAccessible(true);
				
				Object replacement = writeReplace.invoke(this);
				if (!(replacement instanceof SerializedLambda))
				{
					// not a method reference
					break;
				}
				
				SerializedLambda lambda = (SerializedLambda) replacement;
				return lambda.getImplMethodName();
			}
			catch (NoSuchMethodException exception)
			{
				// continue to superclass
			}
			catch (IllegalAccessException | InvocationTargetException exception)
			{
				// cannot get replacement
				break;
			}
		}
		
		return toString();
	}
}
