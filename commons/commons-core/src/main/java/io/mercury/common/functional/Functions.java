package io.mercury.common.functional;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.impl.list.mutable.FastList;

public final class Functions {

	private Functions() {
	}

	/**
	 * 
	 * @param <R>
	 * @param fun          : Parameterless function to be executed
	 * @param afterSuccess : After the function succeeds...
	 * @param afterFailure : After the function fails...
	 * @param defSupplier  : default return value supplier...
	 * @return
	 */
	public final static <R> R fun(@Nonnull final Supplier<R> fun, @Nullable final Function<R, R> afterSuccess,
			@Nullable final Consumer<Exception> afterFailure, @Nonnull final Supplier<R> defSupplier) {
		try {
			R r = fun.get();
			if (afterSuccess != null)
				r = afterSuccess.apply(r);
			if (r == null)
				return defSupplier.get();
			return r;
		} catch (Exception e) {
			if (afterFailure != null)
				afterFailure.accept(e);
			return defSupplier.get();
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param fun
	 * @param afterSuccess
	 * @param afterFailure
	 * @return
	 */
	public final static <T> List<T> listFun(@Nonnull final Supplier<List<T>> fun,
			@Nullable final Function<List<T>, List<T>> afterSuccess, @Nullable final Consumer<Exception> afterFailure) {
		return fun(fun, afterSuccess, afterFailure, FastList::new);
	}

	/**
	 * 
	 * @param <R>
	 * @param fun          : Parameterless function to be executed
	 * @param afterSuccess : After the boolean function succeeds...
	 * @param afterFailure : After the boolean function fails...
	 * @return
	 */
	public final static <R> boolean booleanFun(@Nonnull final Supplier<R> fun,
			@Nonnull final BooleanFunction<R> afterSuccess, @Nullable final BooleanFunction<Exception> afterFailure) {
		try {
			return afterSuccess.booleanValueOf(fun.get());
		} catch (Exception e) {
			if (afterFailure != null)
				return afterFailure.booleanValueOf(e);
			return false;
		}
	}

}