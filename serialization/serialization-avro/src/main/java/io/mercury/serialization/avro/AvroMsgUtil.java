package io.mercury.serialization.avro;

import java.util.Collection;

import javax.annotation.Nonnull;

import io.mercury.serialization.avro.msg.AvroTextMsg;

public class AvroMsgUtil {

	public static boolean hasEpoch(@Nonnull Collection<AvroTextMsg> msgs) {
		return false;
	}

	public static boolean hasSequence(@Nonnull Collection<AvroTextMsg> msgs) {
		return false;
	}

}
