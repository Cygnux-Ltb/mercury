package io.mercury.common.functional;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.impl.list.mutable.FastList;

public final class Functions {

    private Functions() {
    }

    /**
     * @param <T>
     * @param supplier
     * @param successFunc
     * @param failureFunc
     * @return
     */
    public static <T> List<T> exec(@Nonnull final Supplier<List<T>> supplier,
                                   @Nullable final Function<List<T>, List<T>> successFunc,
                                   @Nullable final ThrowableHandler<? super Exception> failureFunc) {
        return exec(supplier, successFunc, failureFunc, FastList::new);
    }

    /**
     * @param <R>
     * @param supplier    : Parameterless function to be executed
     * @param successFunc : After the function succeeds...
     * @param failureFunc : After the function fails...
     * @param defSupplier : default return value supplier...
     * @return
     */
    public static <R> R exec(@Nonnull final Supplier<R> supplier, @Nullable final Function<R, R> successFunc,
                             @Nullable final ThrowableHandler<? super Exception> failureFunc, @Nullable final Supplier<R> defSupplier) {
        try {
            R r = supplier.get();
            if (successFunc != null)
                r = successFunc.apply(r);
            if (r == null && defSupplier != null)
                return defSupplier.get();
            return r;
        } catch (Exception e) {
            if (failureFunc != null)
                failureFunc.handle(e);
            return defSupplier != null ? defSupplier.get() : null;
        }
    }


    public static boolean ifSuccessful(@Nonnull final Runnable task) {
        try {
            task.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param <R>
     * @param supplier    : Parameterless function to be executed
     * @param successFunc : After the boolean function succeeds...
     * @param failureFunc : After the boolean function fails...
     * @return
     */
    public static <R> boolean execBool(@Nonnull final Supplier<R> supplier,
                                       @Nonnull final BooleanFunction<R> successFunc,
                                       @Nullable final BooleanFunction<Exception> failureFunc) {
        try {
            return successFunc.booleanValueOf(supplier.get());
        } catch (Exception e) {
            if (failureFunc != null)
                return failureFunc.booleanValueOf(e);
            return false;
        }
    }

    /**
     * @param <R>
     * @param supplier     : Parameterless function to be executed
     * @param successFunc  : After the int function succeeds...
     * @param afterFailure : After the int function fails...
     * @return
     */
    public static <R> int execInt(@Nonnull final Supplier<R> supplier,
                                  @Nonnull final ToIntFunction<R> successFunc,
                                  @Nullable final ToIntFunction<Exception> afterFailure) {
        try {
            return successFunc.applyAsInt(supplier.get());
        } catch (Exception e) {
            if (afterFailure != null)
                return afterFailure.applyAsInt(e);
            return -1;
        }
    }

    /**
     * @param <R>
     * @param supplier     : Parameterless function to be executed
     * @param successFunc  : After the int function succeeds...
     * @param afterFailure : After the int function fails...
     * @return
     */
    public static <R> long execLong(@Nonnull final Supplier<R> supplier,
                                    @Nonnull final ToLongFunction<R> successFunc,
                                    @Nullable final ToLongFunction<Exception> afterFailure) {
        try {
            return successFunc.applyAsLong(supplier.get());
        } catch (Exception e) {
            if (afterFailure != null)
                return afterFailure.applyAsLong(e);
            return -1;
        }
    }

    /**
     * @param <R>
     * @param isHas
     * @param supplier
     * @param val
     * @return
     */
    public static <R> R getOrDefault(@Nonnull BooleanSupplier isHas,
                                     @Nonnull Supplier<R> supplier,
                                     @Nonnull R val) {
        if (isHas.getAsBoolean())
            return supplier.get();
        return val;
    }

    /**
     * @param <R>
     * @param isHas
     * @param supplier
     * @param exception
     * @return
     * @throws E
     */
    public static <R, E extends Exception> R getOrThrows(@Nonnull final BooleanSupplier isHas,
                                                         @Nonnull final Supplier<R> supplier,
                                                         @Nonnull final E exception)
            throws E {
        if (isHas.getAsBoolean())
            return supplier.get();
        else
            throw exception;
    }

}
