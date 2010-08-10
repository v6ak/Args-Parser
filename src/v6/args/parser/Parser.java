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

public interface Parser<T> {

	/**
	 * parses new value from {@link ListIterator}
	 * @param argumentsListIterator iterator to parse values from
	 * @param name argument value that matched option associated to this parser
	 * @param currentValue current value returned by previous parsing or null if there is no previous parsing
	 * @return new value
	 * @throws ArgumentsException if arguments parsing problem occurs
	 * @throws NullPointerException is <strong>possibly</strong> thrown if argumentsListIterator, an element of argumentsListIterator, name or currentValue is null
	 */
	public T parseFrom(ListIterator<String> argumentsListIterator, String name, T currentValue) throws ArgumentsException;

	/**
	 * @return default value
	 * @throws DefaultValueMissingException if this parser has no default value
	 */
	public T getDefaultValue() throws DefaultValueMissingException;
	
	/**
	 * @return true if and only if this parser has no default value
	 */
	public boolean isMandatory();
	
}
