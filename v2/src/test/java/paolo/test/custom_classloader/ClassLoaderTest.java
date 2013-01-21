package paolo.test.custom_classloader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import paolo.test.custom_classloader.support.MyBean;

public class ClassLoaderTest {

	@Test
	public void testLocalClass() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {

		MyBean myBean = new MyBean();

		assertEquals("v2", myBean.toString());

		assertEquals("paolo.test.custom_classloader.support.MyBean", myBean
				.getClass().getName());

	}

	@Test
	public void testManuallyLoadedClass() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {

		DirectoryBasedParentLastURLClassLoader classLoader = new DirectoryBasedParentLastURLClassLoader(
				"/data/workspaces/class_loader/custom-classloader/v2/src/test/resources");
		Class<?> classManuallyLoaded = classLoader
				.loadClass("paolo.test.custom_classloader.support.MyBean");

		Object myBeanInstanceFromReflection = classManuallyLoaded.newInstance();

		Method methodToString = classManuallyLoaded.getMethod("toString");

		assertEquals("v1", methodToString.invoke(myBeanInstanceFromReflection));

	}

	@Test
	public void testDifferentClassloaders() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {

		MyBean myBean = new MyBean();

		DirectoryBasedParentLastURLClassLoader classLoader = new DirectoryBasedParentLastURLClassLoader(
				"/data/workspaces/class_loader/custom-classloader/v2/src/test/resources");
		Class<?> classManuallyLoaded = classLoader
				.loadClass("paolo.test.custom_classloader.support.MyBean");

		Object myBeanInstanceFromReflection = classManuallyLoaded.newInstance();

		assertNotEquals(myBean.getClass(),
				myBeanInstanceFromReflection.getClass());

	}

	@Test
	public void testCannotCast() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		DirectoryBasedParentLastURLClassLoader classLoader = new DirectoryBasedParentLastURLClassLoader(
				"/data/workspaces/class_loader/custom-classloader/v2/src/test/resources");
		Class<?> classManuallyLoaded = classLoader
				.loadClass("paolo.test.custom_classloader.support.MyBean");

		Object myBeanInstanceFromReflection = classManuallyLoaded.newInstance();

		try {
			MyBean myBean = (MyBean) myBeanInstanceFromReflection;
			fail("An exception was expected here, condition");
		} catch (ClassCastException e) {
			assertTrue("the expected exception has been raised", true);
		}
	}

	@Test
	public void testExtraMethod() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {

		DirectoryBasedParentLastURLClassLoader classLoader = new DirectoryBasedParentLastURLClassLoader(
				"/data/workspaces/class_loader/custom-classloader/v2/src/test/resources");
		Class<?> classManuallyLoaded = classLoader
				.loadClass("paolo.test.custom_classloader.support.MyBean");

		Object myBeanInstanceFromReflection = classManuallyLoaded.newInstance();

		Method methodToString = classManuallyLoaded.getMethod("extraMehtod");

		assertEquals("extra_value",
				methodToString.invoke(myBeanInstanceFromReflection));
	}

}
