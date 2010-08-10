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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

/**
 * Manages arguments and their help.
 * Arguments are read sequentially from given array. An option is matched from one argument by an option identifier,
 * its value is parsed from following arguments (if necessary) and other arguments are parsed. When all arguments are
 * parsed, default values of arguments and missing arguments are solved.
 * Note that char '+' can be used instead of option identifier to match the last argument.
 * @author Vít Šesták AKA v6ak
 */
public final class ArgumentsManager {

	private final Map<String, Parameter<?>> options = new HashMap<String, Parameter<?>>();
	
	private final Map<Parameter<?>, Set<String>> allParameters = new HashMap<Parameter<?>, Set<String>>();
	
	private final List<Parameter<?>> sequentialParameters = new LinkedList<Parameter<?>>();
	
	/**
	 * Help summary for the command line application, for example use of the application.
	 */
	@Getter @Setter
	private String helpSummary;
	
	/**
	 * Help foot can contain some details and examples of usage of the application.
	 */
	@Getter @Setter
	private String helpFoot;
	
	/**
	 * The application name means how to call this application.
	 * Examples:
	 * <ul>
	 * 	<li>foo
	 * 	<li>java -jar foo.jar
	 * </ul>
	 */
	@Getter @Setter
	private String appName;
	
	private <T> void addOptionByIdentifer(String optionIdentifer, Parser<T> parser, Parameter<T> parameter){
		// FIXME: duplicate identifiers
		if (optionIdentifer == null) {
			throw new NullPointerException("optionIdentifer must not be null");
		}
		if (parser == null) {
			throw new NullPointerException("parser must not be null");
		}
		options.put(optionIdentifer, parameter);
		Set<String> names = allParameters.get(parameter);
		if(names == null){
			names = new HashSet<String>();
			allParameters.put(parameter, names);
		}
		names.add(optionIdentifer);
	}
	
	/**
	 * Adds an option with long name and short name.
	 * Short name creates an option identifier "-" + shortName.
	 * Long name creates an option identifier "--" + longName.
	 * <strong>Warning: If shortName or longName is duplicate, the behavior is currently undefined!</strong>
	 * @param <T> type of parsed value
	 * @param shortName the short name
	 * @param longName the long name
	 * @param parser parser to parse the argument
	 * @return parameter that allows you to load parsed argument and set some details
	 * @throws NullPointerException if longName or parser is null
	 */
	public <T> Parameter<T> addOption(char shortName, String longName, Parser<T> parser){
		if (parser == null) {
			throw new NullPointerException("parser must not be null");
		}
		if (longName == null) {
			throw new NullPointerException("longName must not be null");
		}
		final Parameter<T> parameter = new Parameter<T>(parser);
		addOptionByIdentifer("-"+shortName, parser, parameter);
		//if( longName != null ){
		addOptionByIdentifer("--"+longName, parser, parameter);
		//}
		sequentialParameters.add(parameter);
		return parameter;
	}
	
	/**
	 * Adds an option with short name.
	 * Short name creates an option identifier "-" + shortName.
	 * <strong>Warning: If shortName is duplicate, the behavior is currently undefined!</strong>
	 * @param <T> type of parsed value
	 * @param shortName the short name
	 * @param parser parser to parse the argument
	 * @return parameter that allows you to load parsed argument and set some details
	 * @throws NullPointerException if parser is null
	 */
	public <T> Parameter<T> addOption(char shortName, Parser<T> parser){
		if (parser == null) {
			throw new NullPointerException("parser must not be null");
		}
		final Parameter<T> parameter = new Parameter<T>(parser);
		addOptionByIdentifer("-"+shortName, parser, parameter);
		sequentialParameters.add(parameter);
		return parameter;
	}
	
	/**
	 * Adds an option with long name and short name.
	 * Long name creates an option identifier "--" + longName.
	 * <strong>Warning: If longName is duplicate, the behavior is currently undefined!</strong>
	 * @param <T> type of parsed value
	 * @param longName the long name
	 * @param parser parser to parse the argument
	 * @return parameter that allows you to load parsed argument and set some details
	 * @throws NullPointerException if longName or parser is null
	 */
	public <T> Parameter<T> addOption(String longName, Parser<T> parser){
		if (parser == null) {
			throw new NullPointerException("parser must not be null");
		}
		if (longName == null) {
			throw new NullPointerException("longName must not be null");
		}
		final Parameter<T> parameter = new Parameter<T>(parser);
		addOptionByIdentifer("--"+longName, parser, parameter);
		sequentialParameters.add(parameter);
		return parameter;
	}
	
	/**
	 * Parses given command line arguments.
	 * @param argsArray command line arguments that can be passed to static void main(String[]). <strong>If an element of this array contains null, the behavior is undefined!</strong>
	 * @return parsed arguments
	 * @throws ArgumentsException if an parsing problem occurs
	 * @throws NullPointerException if argsArray is null
	 */
	public Values parse(String[] argsArray) throws ArgumentsException{
		if (argsArray == null) {
			throw new NullPointerException("argsArray must not be null");
		}
		final List<String> args = Arrays.asList(argsArray);
		final ListIterator<String> iterator = args.listIterator();
		final Map<Parameter<?>, Set<String>> missingParameters = new HashMap<Parameter<?>, Set<String>>(allParameters);
		final Values values = new Values();
		Parameter<?> lastParameter = null;
		// parse arguments
		while (iterator.hasNext()) {
			final int i = iterator.nextIndex();
			final String name = iterator.next();
			final Parameter<?> parameter;
			if(name.equals("+")){
				parameter = lastParameter;
			}else{
				parameter = options.get(name);
			}
			if(parameter == null){
				throw new UnexpectedArgumentException(i, name);
			}
			missingParameters.remove(parameter);
			values.parseArgument(parameter, name, iterator);
			lastParameter = parameter;
		}
		// use default values
		for (final Entry<Parameter<?>, Set<String>> entry: missingParameters.entrySet()) {
			final Parameter<?> parameter = entry.getKey();
			final Parser<?> parser = parameter.getParser();
			try {
				values.set(parameter, parser.getDefaultValue());
			} catch (DefaultValueMissingException e) {
				throw new MissingParameterException(entry.getValue());
			}
		}
		return values;
	}
	
	/**
	 * Writes help to given writer.
	 * @param writer writer to write help to
	 * @throws NullPointerException if writer is null
	 */
	public void writeHelp(PrintWriter writer){
		if (writer == null) {
			throw new NullPointerException("writer must not be null");
		}
		writer.println("Usage: "+ ((appName != null)?appName:"<application name>") + " arguments");
		if(helpSummary != null){
			writer.println(helpSummary);
		}
		writer.println();
		for (final Parameter<?> parameter : sequentialParameters) {
			final String mandatoryNote = parameter.getMandatoryNote();
			writer.format("  %-20s\t%s%s%n", allParameters.get(parameter),
					(mandatoryNote != null ? "("+mandatoryNote+") " :""),
					parameter.getDescription());
		}
		if(helpFoot != null){
			writer.println();
			writer.println(helpFoot);
		}
	}
	
	/**
	 * Writes help to given stream.
	 * @param stream stream to write help to
	 * @throws NullPointerException if stream is null
	 */
	public void writeHelp(PrintStream stream){
		if (stream == null) {
			throw new NullPointerException("stream must not be null");
		}
		final PrintWriter writer = new PrintWriter(stream);
		writeHelp(writer);
		writer.flush();
	}
	
}
