import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import v6.args.parser.ArgumentsException;
import v6.args.parser.BooleanParser;
import v6.args.parser.MapParser;
import v6.args.parser.Parameter;
import v6.args.parser.ArgumentsManager;
import v6.args.parser.ListParser;
import v6.args.parser.Parser;
import v6.args.parser.StringParser;
import v6.args.parser.Values;
import static org.junit.Assert.*;


public class ParserTest {

	private ArgumentsManager argumentsManager;
	
	private Parameter<List<String>> repeatable;
	
	private Parameter<String> ommitable;
	
	private Parameter<String> mandatory;
	
	private Parameter<String> defaulted;
	
	private Parameter<String> anotherDefaulted;
	
	private Parameter<Boolean> bool;
	
	private Parameter<Map<String, String>> map;

	private Map<String, String> threeValuesMap = new HashMap<String, String>();
	
	{
		threeValuesMap.put("k1", "v1");
		threeValuesMap.put("k3", "v3");
		threeValuesMap.put("k2", "v2");
	}
	
	@Before
	public void runBefore(){
		argumentsManager = new ArgumentsManager();
		repeatable = argumentsManager.addOption('r', "repeatable", new ListParser<String>(new StringParser()));
		ommitable = argumentsManager.addOption("ommitable", new StringParser(null));
		mandatory = argumentsManager.addOption('m', "mandatory", new StringParser());
		final Parser<String> sp = new StringParser("dv");
		defaulted = argumentsManager.addOption('d', sp);
		anotherDefaulted = argumentsManager.addOption("ad", sp);
		bool = argumentsManager.addOption('f', "flag", new BooleanParser());
		map = argumentsManager.addOption('a', "map", new MapParser<String, String>(new StringParser(), new StringParser()));
		/*ap2 = new ArgumentsParser();
		sourceParameter = ap2.addOption('c', "class", new StringParser());
		outParameter = ap2.addOption('o', "out", new StringParser());*/
		//final Values values = parser.parse(args);
	}
	
	/*@Test()
	public void testSimpleParser(){
		final Values values = ap2.parse(new String[]{});
	}*/
	
	@Test(expected=ArgumentsException.class)
	public void testThatEmptyArgumentListFails() throws Exception {
		argumentsManager.parse(new String[]{});
	}
	
	@Test
	public void testMinimalArgumentList() throws Exception {
		final Values values = argumentsManager.parse(new String[]{"-m", "axc"});
		assertEquals(Collections.emptyList(), values.get(repeatable));
		assertEquals(null, values.get(ommitable));
		assertEquals("axc", values.get(mandatory));
		assertEquals("dv", values.get(anotherDefaulted));
		assertEquals("dv", values.get(defaulted));
		assertEquals(Boolean.FALSE, values.get(bool));
		assertEquals(Collections.emptyMap(), values.get(map));
	}
	
	@Test(expected=ArgumentsException.class)
	public void testMissingStringValue() throws Exception {
		argumentsManager.parse(new String[]{"-m"});
	}
	
	@Test(expected=ArgumentsException.class)
	public void testUnexpectedEndValue() throws Exception {
		argumentsManager.parse(new String[]{"-m", "axc", "UNEXPECTED"});
	}
	
	@Test(expected=ArgumentsException.class)
	public void testUnexpectedValueAtStart() throws Exception {
		argumentsManager.parse(new String[]{"UNEXPECTED", "-m", "axc"});
	}
	
	@Test(expected=ArgumentsException.class)
	public void testExtraMinus() throws Exception {
		argumentsManager.parse(new String[]{"--m", "axc"});
	}
	
	@Test(expected=ArgumentsException.class)
	public void testMissingMinus() throws Exception {
		argumentsManager.parse(new String[]{"-mandatory", "axc"});
	}
	
	@Test
	public void testMinimalArgumentListLongVariant() throws Exception {
		final Values values = argumentsManager.parse(new String[]{"--mandatory", "axc"});
		assertEquals(Collections.emptyList(), values.get(repeatable));
		assertEquals(null, values.get(ommitable));
		assertEquals("axc", values.get(mandatory));
		assertEquals("dv", values.get(defaulted));
		assertEquals("dv", values.get(anotherDefaulted));
		assertEquals(Boolean.FALSE, values.get(bool));
		assertEquals(Collections.emptyMap(), values.get(map));
	}
	
	@Test(expected=ArgumentsException.class)
	public void testDupliciteMapKey() throws ArgumentsException{
		argumentsManager.parse(new String[]{
				"-r", "a", "-m", "x", "--repeatable", "b", "--map", "k2", "v2", "--ommitable", "gaga", "-a", "k1", "v1", "-r", "c", "-f", "-d", "dddvvv", "-a", "k3", "v3", "--map", "k1", "v1", "-r", "a"});
	}
	
	@Test
	public void testMaximalArgumentList() throws Exception {
		final Values values = argumentsManager.parse(new String[]{
				"-r", "a", "--ad", "foobargoo", "-m", "x", "--repeatable", "b", "--map", "k2", "v2", "--ommitable", "gaga", "-a", "k1", "v1", "-r", "c", "-f", "-d", "dddvvv", "-a", "k3", "v3", "-r", "a"});
		assertEquals(Arrays.asList(new String[]{"a", "b", "c", "a"}), values.get(repeatable));
		assertEquals("gaga", values.get(ommitable));
		assertEquals("x", values.get(mandatory));
		assertEquals("dddvvv", values.get(defaulted));
		assertEquals("foobargoo", values.get(anotherDefaulted));
		assertEquals(Boolean.TRUE, values.get(bool));
		assertEquals(threeValuesMap , values.get(map));
	}
	
	@Test(expected=ArgumentsException.class)
	public void testDoubleMandatoryArgument() throws Exception {
		argumentsManager.parse(new String[]{
				"-r", "a", "-m", "x", "--repeatable", "-m", "cvcvc", "b", "--ommitable", "gaga", "-r", "c", "-f", "-d", "dddvvv", "-r", "a"});
	}
	
	@Test(expected=ArgumentsException.class)
	public void testDoubleDefaultedArgument() throws Exception {
		argumentsManager.parse(new String[]{
				"-r", "a", "-m", "x", "--repeatable", "-d", "cvcvc", "b", "--ommitable", "gaga", "-r", "c", "-f", "-d", "dddvvv", "-r", "a"});
	}
	
	@Test(expected=ArgumentsException.class)
	public void testDoubleBoolArgument() throws Exception {
		argumentsManager.parse(new String[]{
				"-r", "a", "-m", "x", "--repeatable", "--flag", "b", "--ommitable", "gaga", "-r", "c", "-f", "-d", "dddvvv", "-r", "a"});
	}
	
	@Test(expected=ArgumentsException.class)
	public void testUnexpectedValueInside() throws Exception {
		argumentsManager.parse(new String[]{
				"-r", "a", "-m", "x", "--repeatable", "cvcvc", "b", "--ommitable", "gaga", "-r", "c", "-f", "-d", "dddvvv", "-r", "a"});
	}
	
	@Test
	public void testMaximalArgumentListWithoutDefaultedAndBool() throws Exception {
		final Values values = argumentsManager.parse(new String[]{
				"-r", "a", "-m", "x", "--repeatable", "b", "--ommitable", "gaga", "-r", "c", "-r", "a"});
		assertEquals(Arrays.asList(new String[]{"a", "b", "c", "a"}), values.get(repeatable));
		assertEquals("gaga", values.get(ommitable));
		assertEquals("x", values.get(mandatory));
		assertEquals("dv", values.get(defaulted));
		assertEquals("dv", values.get(anotherDefaulted));
		assertEquals(Boolean.FALSE, values.get(bool));
		assertEquals(Collections.emptyMap(), values.get(map));
	}
	
	@Test
	public void testPlusChainedValues() throws Exception {
		final Values values = argumentsManager.parse(new String[]{
				"-r", "a", "+", "x", "+", "--repeatable", "+", "b", "-a", "k2", "v2", "+", "k1", "v1", "--ommitable", "gaga", "--repeatable", "c", "-r",  "a", "--flag", "-m", "mand", "--map", "k3", "v3"});
		assertEquals(Arrays.asList(new String[]{"a", "x", "--repeatable", "b", "c", "a"}), values.get(repeatable));
		assertEquals("gaga", values.get(ommitable));
		assertEquals("mand", values.get(mandatory));
		assertEquals("dv", values.get(defaulted));
		assertEquals("dv", values.get(anotherDefaulted));
		assertEquals(Boolean.TRUE, values.get(bool));
		assertEquals(threeValuesMap, values.get(map));
	}
	
	@Test(expected=ArgumentsException.class)
	public void testPlusAtStartFailsValues() throws Exception {
		argumentsManager.parse(new String[]{
				"+", "-r", "a", "+", "x", "+", "--repeatable", "+", "b", "--ommitable", "gaga", "--repeatable", "c", "-r",  "a", "--flag", "-m", "mand"});
	}
	
}
