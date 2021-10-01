package io.mercury.common.functional;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
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
	public final static <R> R exec(@Nonnull final Supplier<R> fun, @Nullable final Function<R, R> afterSuccess,
			@Nullable final Consumer<Exception> afterFailure, @Nonnull final Supplier<R> defSupplier) {
		try {
			R r = fun.get();
			if (afterSuccess != null)
				r = afterSuccess.apply(r);
			if (r == null && defSupplier != null)
				return defSupplier.get();
			return r;
		} catch (Exception e) {
			if (afterFailure != null)
				afterFailure.accept(e);
			return defSupplier != null ? defSupplier.get() : null;
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
	public final static <T> List<T> exec(@Nonnull final Supplier<List<T>> fun,
			@Nullable final Function<List<T>, List<T>> afterSuccess, @Nullable final Consumer<Exception> afterFailure) {
		return exec(fun, afterSuccess, afterFailure, FastList::new);
	}

	/**
	 * 
	 * @param <R>
	 * @param func         : Parameterless function to be executed
	 * @param afterSuccess : After the boolean function succeeds...
	 * @param afterFailure : After the boolean function fails...
	 * @return
	 */
	public final static <R> boolean exec(@Nonnull final Supplier<R> func,
			@Nonnull final BooleanFunction<R> afterSuccess, @Nullable final BooleanFunction<Exception> afterFailure) {
		try {
			return afterSuccess.booleanValueOf(func.get());
		} catch (Exception e) {
			if (afterFailure != null)
				return afterFailure.booleanValueOf(e);
			return false;
		}
	}

	/**
	 * 
	 * @param <R>
	 * @param func         : Parameterless function to be executed
	 * @param afterSuccess : After the int function succeeds...
	 * @param afterFailure : After the int function fails...
	 * @return
	 */
	public final static <R> int exec(@Nonnull final Supplier<R> func, @Nonnull final IntFunction<R> afterSuccess,
			@Nullable final IntFunction<Exception> afterFailure) {
		try {
			return afterSuccess.intValueOf(func.get());
		} catch (Exception e) {
			if (afterFailure != null)
				return afterFailure.intValueOf(e);
			return -1;
		}
	}

}
