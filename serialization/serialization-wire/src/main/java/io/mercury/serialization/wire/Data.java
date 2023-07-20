package io.mercury.serialization.wire;

import io.mercury.serialization.json.JsonWrapper;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireKey;
import net.openhft.chronicle.wire.WireOut;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjLongConsumer;

/**
 * The code for the class Data
 */
public final class Data implements Marshallable {

    private String message;
    private long number;
    private TimeUnit timeUnit;
    private double price;


    private static final WireKey MESSAGE_KEY = () -> "message";
    private static final BiConsumer<Data, String> MESSAGE_READER = (t, s) -> t.message = s;

    private static final WireKey NUMBER_KEY = () -> "number";
    private static final ObjLongConsumer<Data> NUMBER_READER = (t, l) -> t.number = l;

    private static final WireKey TIME_UNIT_KEY = () -> "timeUnit";
    private static final BiConsumer<Data, TimeUnit> TIME_UNIT_READER = (t, e) -> t.timeUnit = e;

    private static final WireKey PRICE_KEY = () -> "price";
    private static final ObjDoubleConsumer<Data> PRICE_READER = (t, d) -> t.price = d;

    public Data() {
    }

    public Data(String message, long number, TimeUnit timeUnit, double price) {
        this.message = message;
        this.number = number;
        this.timeUnit = timeUnit;
        this.price = price;
    }

    @Override
    public void readMarshallable(@Nonnull WireIn wire) throws IllegalStateException {
        wire
                .read(MESSAGE_KEY).text(this, MESSAGE_READER)
                .read(NUMBER_KEY).int64(this, NUMBER_READER)
                .read(TIME_UNIT_KEY).asEnum(TimeUnit.class, this, TIME_UNIT_READER)
                .read(PRICE_KEY).float64(this, PRICE_READER);
    }

    @Override
    public void writeMarshallable(@Nonnull WireOut wire) {
        wire.write(MESSAGE_KEY).text(this.message)
                .write(NUMBER_KEY).int64(this.number)
                .write(TIME_UNIT_KEY).asEnum(this.timeUnit)
                .write(PRICE_KEY).float64(this.price);
    }

    @Override
    public String toString() {
        return JsonWrapper.toJson(this);
    }

}
