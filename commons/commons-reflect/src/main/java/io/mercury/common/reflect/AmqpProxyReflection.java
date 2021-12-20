package io.mercury.commons.reflect;

import static io.mercury.common.collections.ImmutableSets.newImmutableSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import io.mercury.common.collections.MutableMaps;

/**
 * 加载 Annotation 反射信息
 * 
 * @author yellow013
 *
 */

public final class AmqpProxyReflection {

	private static final AmqpProxyReflection INSTANCE = new AmqpProxyReflection();

	private AmqpProxyReflection() {
	}

	public static final AmqpProxyReflection getInstance() {
		return INSTANCE;
	}

	private final AtomicBoolean isInitialized = new AtomicBoolean(false);

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public final <A extends Annotation> void initialize(String scanPackage, Class<A>... annotations) {
		if (isInitialized.compareAndSet(false, true)) {
			MutableMap<Class<?>, ImmutableSet<Method>> tempMap = MutableMaps.newUnifiedMap();
			for (Class<?> annotation : annotations) {
				tempMap.put(annotation, scanPackage(scanPackage, (Class<A>) annotation));
			}
			this.annotationMethodMap = tempMap.toImmutable();
		} else {
			throw new IllegalStateException("AmqpProxyReflection is not init");
		}
	}

	private ImmutableMap<Class<?>, ImmutableSet<Method>> annotationMethodMap;

	/**
	 * 
	 * @param <A>
	 * @param scanPackage
	 * @param annotation
	 * @return
	 */
	public final <A extends Annotation> ImmutableSet<Method> scanPackage(String scanPackage, Class<A> annotation) {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(scanPackage)).setScanners(Scanners.SubTypes,
						Scanners.MethodsAnnotated, Scanners.MethodsParameter, Scanners.TypesAnnotated));
		ImmutableSet<Method> immutableSet = newImmutableSet(reflections.getMethodsAnnotatedWith(annotation));
		immutableSet.each(this::assertionProxyeedMethod);
		return immutableSet;
	}

	/**
	 * 验证方法返回值是否为void
	 * 
	 * @param method
	 */
	private void assertionProxyeedMethod(Method method) {
		if (method.getReturnType() != Void.TYPE)
			throw new IllegalArgumentException("method [" + method + "] return type is not void");
	}

	/**
	 * 
	 * @param proxyeedClass
	 * @return
	 */
	public <A extends Annotation> ImmutableSet<Method> getImplMethods(Class<A> proxyeedClass) {
		if (isInitialized.get())
			return annotationMethodMap.get(proxyeedClass);
		throw new IllegalStateException("AmqpProxyReflection is not init");
	}

}
