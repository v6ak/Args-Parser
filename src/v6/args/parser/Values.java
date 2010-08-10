/**
 * Copyright (c) 2010 Vít Šesták
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the copyright holders nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package v6.args.parser;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Represents parsed commandline arguments.
 * @author Vít Šesták AKA v6ak
 * @see ArgumentsManager
 */
public final class Values {

	private final Map<Parameter<?>, Object> values = new HashMap<Parameter<?>, Object>();
	
	Values(){
	}
	
	/**
	 * @param <T> type of the parameter
	 * @param parameter parameter 
	 * @return value associated to the parameter
	 * @throws NullPointerException if parameter is null
	 */
	@SuppressWarnings(value="unchecked")
	public <T> T get(Parameter<T> parameter){
		if (parameter == null) {
			throw new NullPointerException("parameter must not be null");
		}
		return (T)values.get(parameter);
	}
	
	<T> void set(Parameter<T> parameter, Object value){
		if (parameter == null) {
			throw new NullPointerException("parameter must not be null");
		}
		values.put(parameter, value);
	}

	<T> void parseArgument(Parameter<T> parameter, String name, ListIterator<String> iterator) throws ArgumentsException {
		set(parameter, parameter.getParser().parseFrom(iterator, name, get(parameter)));
	}

	public boolean getBoolean(Parameter<Boolean> booleanParameter) {
		return get(booleanParameter).booleanValue();
	}
	
	public int getInt(Parameter<Integer> intParameter) {
		return get(intParameter).intValue();
	}
	
}
