package io.mercury.common.reflect;

import org.eclipse.collections.api.set.ImmutableSet;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public final class AnnotationScanner {

    public static <A extends Annotation> ImmutableSet<Method> scanPackage(@Nonnull Class<A> annotation,
                                                                          @Nonnull String packages) {


        return null;
    }


}
