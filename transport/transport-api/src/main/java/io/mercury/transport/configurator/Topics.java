package io.mercury.transport.configurator;

import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.set.MutableSet;

import io.mercury.common.collections.MutableSets;
import io.mercury.common.util.StringUtil;

public class Topics {

	private final MutableSet<String> topics = MutableSets.newUnifiedSet();

	private Topics(String[] topics) {
		MutableSets.addElements(this.topics, topics);
	}

	public static Topics empty() {
		return with();
	}

	public static Topics with(String... topics) {
		return new Topics(topics);
	}

	public MutableSet<String> get() {
		return topics;
	}

	public Topics add(String... topics) {
		MutableSets.addElements(this.topics, topics);
		return this;
	}

	public void each(Procedure<String> procedure) {
		topics.each(procedure);
	}

	private transient String cache;

	@Override
	public String toString() {
		if (cache == null)
			this.cache = StringUtil.toString(topics);
		return cache;
	}

}
