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
 * Parses one {@link String} value.
 * @author Vít Šesták AKA v6ak
 *
 */
public final class StringParser extends AbstractUnrepeatableParser<String>{
	
	/**
	 * Creates a new {@link StringParser} <strong>with</strong> default value.
	 * @param defaultValue the default value (Note that null is a legal default value.)
	 */
	public StringParser(String defaultValue) {
		super(DefaultValue.getDefaultValue(defaultValue));
	}
	
	/**
	 * Creates a new {@link StringParser} <strong>without</strong> default value.
	 */
	public StringParser() {
		super(DefaultValue.<String>getNoDefaultValue());
	}
	
	@Override
	protected String parseFrom(ListIterator<String> iterator, String name) throws ArgumentsException {
		if(!iterator.hasNext()){
			throw new UnexpectedEndException();
		}
		final String value = iterator.next();
		return value;
	}
	
}
