package io.mercury.transport.attr;

import io.mercury.common.collections.CollectionUtil;
import io.mercury.common.collections.MutableSets;
import io.mercury.serialization.json.JsonWrapper;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.set.MutableSet;

public final class Topics {

    private final MutableSet<String> saved = MutableSets.newUnifiedSet();

    private Topics(String[] topics) {
        CollectionUtil.addAll(this.saved, topics);
    }

    public static Topics empty() {
        return with();
    }

    public static Topics with(String... topics) {
        return new Topics(topics);
    }

    public MutableSet<String> getAll() {
        return saved;
    }

    public int getCount() {
        return saved.size();
    }

    public Topics add(String... topics) {
        CollectionUtil.addAll(this.saved, topics);
        return this;
    }

    public void each(Procedure<String> procedure) {
        saved.each(procedure);
    }

    private transient String toString;

    @Override
    public String toString() {
        if (toString == null)
            this.toString = JsonWrapper.toJson(this);
        return toString;
    }

}
