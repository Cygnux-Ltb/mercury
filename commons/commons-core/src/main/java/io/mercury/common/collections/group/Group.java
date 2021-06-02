package io.mercury.common.collections.group;

import java.io.Serializable;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
public interface Group<K, V> extends Serializable {

	@Nonnull
	V acquireMember(@Nonnull K k);

	@Nonnull
	ImmutableList<V> getMemberList();

}
