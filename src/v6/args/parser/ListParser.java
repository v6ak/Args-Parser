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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Parses a {@link List} of values.
 * Each value is parsed by given element parser and added to the list so base parser <strong>should</strong> be a {@link AbstractUnrepeatableParser}.
 * Option is marked as non mandatory because the default value is empty list.
 * @author Vít Šesták AKA v6ak
 *
 * @param <T> base type of the list
 */
public final class ListParser<T> extends AbstractParser<List<T>>{

	private final Parser<T> elementParser;
	
	/**
	 * @param elementParser parser of list elements
	 * @throws NullPointerException if elementParser is null
	 */
	public ListParser(Parser<T> elementParser) {
		super(DefaultValue.getDefaultValue(Collections.<T>emptyList()));
		if (elementParser == null) {
			throw new NullPointerException("elementParser must not be null");
		}
		this.elementParser = elementParser;
	}
	
	@Override
	public final List<T> parseFrom(ListIterator<String> stringListIterator, String name,
			List<T> currentValue) throws ArgumentsException {
		// TODO: optimize
		final List<T> value = (currentValue == null)
			? new ArrayList<T>()
			: new ArrayList<T>(currentValue);
		value.add(elementParser.parseFrom(stringListIterator, name, null));
		return Collections.unmodifiableList(value);
	}

	/**
	 * Creates a new {@link ListParser}.
	 * @param elementParser equals to the same parameter in {@link #ListParser(Parser)}
	 * @param <T> equals to the same parameter in {@link #ListParser(Parser)}
	 * @see #ListParser(Parser)
	 * @return the created {@link ListParser}
	 */
	public static <T> ListParser<T> create(Parser<T> elementParser) {
		return new ListParser<T>(elementParser);
	}
	
}
