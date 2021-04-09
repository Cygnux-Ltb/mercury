/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mercury.serialization.wire;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.Wire;
import net.openhft.chronicle.wire.WireType;
import net.openhft.chronicle.wire.Wires;

/**
 *
 * @author gadei
 */
@RunWith(value = Parameterized.class)
public class WirePropertyTest {

	private static final Logger logger = LoggerFactory.getLogger(WirePropertyTest.class);

	private WireProperty property;// = new WireModel();

	private final Function<Bytes<?>, Wire> wireType;

	public WirePropertyTest(Function<Bytes<?>, Wire> wireType) {
		this.wireType = wireType;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> combinations() {
		return Arrays.asList(new Object[] {
				(Function<Bytes<?>, Wire>) bytes -> new BinaryWire(bytes, false, true, false, 128, "binary", false) },
				new Object[] { WireType.TEXT }, new Object[] { WireType.BINARY },
				new Object[] { WireType.FIELDLESS_BINARY });
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		property = new WireProperty("reference", "@", "name", "value", 12312312, 123, UUID.randomUUID().toString());
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of readMarshallable method, of class WireProperty.
	 */
	@Test
	public void testReadMarshallable() {
	}

	/**
	 * Test of writeMarshallable method, of class WireProperty.
	 */
	@Test
	public void testWriteMarshallable() {
	}

	@Test
	public void testMultipleReads() {
		logger.info("Type: {}", this.wireType);

		Bytes<?> bytes = Bytes.elasticByteBuffer();
		Wire wire = wireType.apply(bytes);

		wire.writeDocument(true, property);
		logger.info(Wires.fromSizePrefixedBlobs(bytes));

		WireProperty results = new WireProperty();
		wire.readDocument(results, null);

		WireUtils.compareWireProperty(property, results);

	}

}
