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

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Represents a parameter.
 * @author Vít Šesták AKA v6ak
 *
 * @param <T> type of parsed value
 */
public final class Parameter<T> {

	/**
	 * parameter description that can be used in help
	 */
	@Getter
	private String description;
	
	/**
	 * note about necessity to use this parameter
	 * It is defaulted by {@link Parser#isMandatory()}.
	 */
	@Getter
	private String mandatoryNote; // TODO: think about its name
	
	@Getter(AccessLevel.PACKAGE)
	private final Parser<T> parser;
	
	Parameter(Parser<T> parser) {
		if (parser == null) {
			throw new NullPointerException("parser must not be null");
		}
		this.parser = parser;
		mandatoryNote = parser.isMandatory()
			? "mandatory"
			: null;
	}
	
	/**
	 * Sets description.
	 * @param description parameter description that can be used in help
	 * @return this
	 */
	public Parameter<T> setDescription(String description) {
		this.description = description;
		return this;
	}
	
	/**
	 * sets note about necessity
	 * <strong>It is defaulted by {@link Parser#isMandatory()}.</strong>
	 * @param mandatory note about necessity to use this parameter
	 * @return this
	 */
	public Parameter<T> setMandatoryNote(String mandatory) {
		this.mandatoryNote = mandatory;
		return this;
	}
	
}
