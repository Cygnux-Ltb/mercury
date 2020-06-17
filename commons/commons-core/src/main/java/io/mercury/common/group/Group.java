package io.mercury.common.group;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;

public interface Group<K, V> {

	@Nonnull
	V acquireMember(@Nonnull K k);

	@Nonnull
	ImmutableList<V> memberList();

}
