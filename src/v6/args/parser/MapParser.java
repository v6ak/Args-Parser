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

import java.util.Collections;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Parses {@link Map} values. Parsing of the values is delegated to keyParser and valueParser.
 * If map option is matched, key is parsed by keyParser and then value is parsed by valueParser.
 * Option is marked as non mandatory because the default value is empty {@link Map}.
 * @author Vít Šesták AKA v6ak
 *
 * @param <K> key type of the map
 * @param <V> value type of the map
 */
public final class MapParser<K, V> extends AbstractParser<Map<K, V>> {

	private final Parser<K> keyParser;
	
	private final Parser<V> valueParser;
	
	// TODO: add MapParser(Parser(Map.Entry))
	
	/**
	 * @param keyParser parser of the key
	 * @param valueParser parser of the value
	 * @throws NullPointerException if keyParser or valueParser is null
	 */
	public MapParser(Parser<K> keyParser, Parser<V> valueParser) {
		super(DefaultValue.getDefaultValue(Collections.<K, V>emptyMap()));
		if (keyParser == null) {
			throw new NullPointerException("keyParser must not be null");
		}
		this.keyParser = keyParser;
		if (valueParser == null) {
			throw new NullPointerException("valueParser must not be null");
		}
		this.valueParser = valueParser;
	}
	
	@Override
	public final Map<K, V> parseFrom(ListIterator<String> stringListIterator, String name, Map<K, V> currentValue) throws ArgumentsException {
		// TODO: optimize
		final Map<K, V> map = (currentValue == null)
			? new HashMap<K, V>()
			: new HashMap<K, V>(currentValue);
		final K key = keyParser.parseFrom(stringListIterator, name, null);
		final V value = valueParser.parseFrom(stringListIterator, name, null);
		if(map.containsKey(key)){
			throw new DuplicateKeyException(name, key);
		}
		map.put(key, value);
		return map;
	}
	
}
