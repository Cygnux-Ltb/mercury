package io.mercury.commons.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import io.mercury.common.collections.ImmutableSets;

/**
 * 加载 AmqpProxy 反射信息
 * 
 * @author tars
 *
 */
public final class AmqpProxyReflection {

	private static final AmqpProxyReflection instance = new AmqpProxyReflection();

	private AmqpProxyReflection() {
	}

	public static final AmqpProxyReflection instance() {
		return instance;
	}

	private AtomicBoolean isInit = new AtomicBoolean(false);

	private ImmutableMap<Class<Annotation>, ImmutableSet<Method>> implTypes;

	public <A extends Annotation> ImmutableSet<Method> scanPackage(String scanPackage, Class<A> annotationType) {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(scanPackage)).setScanners(new SubTypesScanner(),
						new MethodAnnotationsScanner(), new MethodParameterScanner(), new TypeAnnotationsScanner()));

		ImmutableSet<Method> immutableSet = ImmutableSets
				.newImmutableSet(reflections.getMethodsAnnotatedWith(annotationType));
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

	public ImmutableSet<Method> getImplMethods(Class<?> proxyeedClass) {
		if (isInit.get())
			return implTypes.get(proxyeedClass);
		throw new IllegalStateException("AmqpProxyReflection is not init");
	}

}
