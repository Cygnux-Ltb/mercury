package io.mercury.common.sys;

import java.lang.reflect.Field;
import java.net.URL;
import java.security.CodeSource;

public class NativeTime {
	public static final boolean LOADED;
	private static double ticks_per_nanosecond = 1.0;

	static {
		boolean loaded = false;
		try {
			String destDir = System.getProperty("java.io.tmpdir");
			String osname = System.getProperty("os.name");
			String arch = System.getProperty("os.arch");
			String pattern = osname + java.io.File.separator + arch;

			try {
				// TODO
				// unpack .so from jar to tmpdir/os/arch
				CodeSource src = NativeTime.class.getProtectionDomain().getCodeSource();
				if (src != null) {
					String jarFile = src.getLocation().getFile();
					java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);
					java.util.Enumeration<?> enumEntries = jar.entries();
					while (enumEntries.hasMoreElements()) {
						java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();

						if (!containsIgnoreCase(file.getName(), pattern))
							continue;

						java.io.File f = new java.io.File(destDir + java.io.File.separator + file.getName());

						if (!f.exists()) {
							java.io.File parent = f.getParentFile();
							if (parent != null) {
								parent.mkdirs();
								f = new java.io.File(destDir + java.io.File.separator + file.getName());
							}
						}

						if (file.isDirectory()) { // if its a directory, create it
							continue;
						}

						System.out.println("Unpacking " + file.getName() + " to " + f.toString());

						java.io.InputStream is = jar.getInputStream(file); // get the input stream
						java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
						while (is.available() > 0) { // write contents of 'is' to 'fos'
							fos.write(is.read());
						}
						fos.close();
						is.close();

						// update java.library.path to include tmpdir/os/arch
						// Note, java.library.path is cached by the JVM at startup, so force via
						// reflective access
						// This may be an issue with Java 10+
						// See
						// https://stackoverflow.com/questions/5419039/is-djava-library-path-equivalent-to-system-setpropertyjava-library-path
						String libpath = System.getProperty("java.library.path");
						libpath = libpath + java.io.File.pathSeparator + destDir + java.io.File.separator + pattern;

						try {
							System.setProperty("java.library.path", libpath);
							Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
							fieldSysPath.setAccessible(true);
							fieldSysPath.set(null, null);
						} catch (java.lang.IllegalAccessException e) {
							e.printStackTrace();
							// ignored
						} catch (java.lang.NoSuchFieldException e) {
							e.printStackTrace();
							// ignored
						}

						System.load(f.toString());
						loaded = true;
					}
					jar.close();
				}
			} catch (java.io.FileNotFoundException unused) {
				unused.printStackTrace();
			} catch (java.io.IOException unused) {
				unused.printStackTrace();
			}

			loaded = tryLoad("libnativetime.so");
			if (!loaded)
				loaded = tryLoad(pattern + java.io.File.separator + "libnativetime.so");
			if (!loaded)
				System.loadLibrary("nativetime");

			// initial calibration
			calibrate(10);

			// background thread for finer-grained calibration
			Thread t = new Thread(() -> {
				calibrate(1000);
			});
			t.setDaemon(true);
			t.start();

			loaded = true;
		} catch (UnsatisfiedLinkError ule) {
			ule.printStackTrace();
			loaded = false;
		}

		LOADED = loaded;
	}

	private static boolean tryLoad(String path) {
		try {
			URL url = NativeTime.class.getClassLoader().getResource(path);
			if (url != null) {
				System.load(url.getFile());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// ignored.
		}
		return false;
	}

	private static boolean containsIgnoreCase(String s1, String s2) {
		return s1.toLowerCase().contains(s2.toLowerCase());
	}

	/**
	 * Spin for given number of ms to calibrate ticks per nanosecond
	 * 
	 * @param ms = time in milliseconds to spin
	 */
	private static void calibrate(int ms) {
		long ticks1 = cpuid_rdtsc();
		long nanos1 = System.nanoTime();

		// spin for 10ms
		long end = nanos1 + ms * 1000000L;
		while (System.nanoTime() < end)
			;

		long ticks2 = cpuid_rdtsc();
		long nanos2 = System.nanoTime();

		ticks_per_nanosecond = ((double) (ticks2 - ticks1)) / (nanos2 - nanos1);
	}

	public static double ticksPerNanosecond() {
		return ticks_per_nanosecond;
	}

	public native static long rdtsc();

	public native static long cpuid_rdtsc();

	public native static long rdtscp();

	public native static long clocknanos();
	
	public static void main(String[] args) {
		
		System.out.println(NativeTime.clocknanos());
		
		
	}

}
