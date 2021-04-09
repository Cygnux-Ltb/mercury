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
public class WireModelTest {

	private static final Logger logger = LoggerFactory.getLogger(WireModelTest.class);
	private WireModel model;// = new WireModel();

	private final Function<Bytes<?>, Wire> wireType;

	public WireModelTest(Function<Bytes<?>, Wire> wireType) {
		this.wireType = wireType;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> combinations() {
		return Arrays.asList(
				new Object[] {
						(Function<Bytes<?>, Wire>) bytes -> new BinaryWire(bytes, false, true, false, 128, "binary", false) },
				new Object[] { WireType.TEXT }, new Object[] { WireType.BINARY },
				new Object[] { WireType.FIELDLESS_BINARY }
//                new Object[]{WireType.JSON}
		);
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		model = new WireModel(231343454, 13, UUID.randomUUID().toString());
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of readMarshallable method, of class WireModel.
	 */
	@Test
	public void testMultipleReads() {
		logger.info("Type: {}", this.wireType);

		Bytes<?> bytes = Bytes.elasticByteBuffer();
		Wire wire = wireType.apply(bytes);

		wire.writeDocument(true, model);
		logger.info(Wires.fromSizePrefixedBlobs(bytes));

		WireModel results = new WireModel();
		wire.readDocument(results, null);

		WireUtils.compareWireModel(model, results);

	}

}
