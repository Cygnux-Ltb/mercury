package io.mercury.transport.attr;

import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.set.MutableSet;

import io.mercury.common.collections.CollectionUtil;
import io.mercury.common.collections.MutableSets;
import io.mercury.common.util.StringSupport;

public class Topics {

	private final MutableSet<String> topics = MutableSets.newUnifiedSet();

	private Topics(String[] topics) {
		CollectionUtil.addAll(this.topics, topics);
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
		CollectionUtil.addAll(this.topics, topics);
		return this;
	}

	public void each(Procedure<String> procedure) {
		topics.each(procedure);
	}

	private transient String cache;

	@Override
	public String toString() {
		if (cache == null)
			this.cache = StringSupport.toString(topics);
		return cache;
	}

}
