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

import java.util.ListIterator;

/**
 * A simple abstract implementation of parser of argument that is not repeated.
 * @author Vít Šesták AKA v6ak
 *
 * @param <T> type of parsed value
 */
public abstract class AbstractUnrepeatableParser<T> extends AbstractParser<T>{

	/**
	 * @param defaultValue default value strategy for this parser
	 */
	public AbstractUnrepeatableParser(DefaultValue<? extends T> defaultValue) {
		super(defaultValue);
	}
	
	/**
	 * parses new value from {@link ListIterator}
	 * @param argumentsListIterator iterator to parse values from
	 * @param name argument value that matched option associated to this parser
	 * @return new value
	 * @throws ArgumentsException if arguments parsing problem occurs
	 * @throws NullPointerException is <strong>possibly</strong> thrown if argumentsListIterator, an element of argumentsListIterator or name is null
	 */
	protected abstract T parseFrom(java.util.ListIterator<String> argumentsListIterator, String name) throws ArgumentsException; 
	
	
	/**
	 * @see v6.args.parser.Parser#parseFrom(java.util.ListIterator, java.lang.String, java.lang.Object)
	 */
	@Override
	public final T parseFrom(java.util.ListIterator<String> stringListIterator, String name, T currentValue) throws ArgumentsException {
		if(currentValue != null){
			throw new DuplicateOptionException(name);
		}
		return parseFrom(stringListIterator, name);
	}
	
}
