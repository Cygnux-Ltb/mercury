package io.mercury.serialization.fury;

import junit.framework.TestCase;
import org.apache.fury.Fury;
import org.apache.fury.config.Language;

/**
 * Unit test for simple App.
 */
public class Test0 extends TestCase {

    /**
     * Rigourous Test :-)
     */
    public void testCancelOrder() {
        TestObject object = new TestObject();
        object.setAccountId(1000)
                .setOrdSysId(1000001L)
                .setInstrumentCode("ag2412")
                .setRemark("TEST OBJECT REMARK");
        // Note that Fury instances should be reused between
        // multiple serializations of different objects.
        Fury fury = Fury.builder().withLanguage(Language.JAVA)
                .requireClassRegistration(true)
                .build();
        // Registering types can reduce class name serialization overhead, but not mandatory.
        // If class registration enabled, all custom types must be registered.
        fury.register(TestObject.class);
        byte[] bytes = fury.serializeJavaObject(object);

        System.out.println(bytes.length);
        System.out.println(new String(bytes));

        TestObject object1 = fury.deserializeJavaObject(bytes, TestObject.class);


        System.out.println(object);
        System.out.println(object == object1);
        System.out.println(object1);

    }

}
