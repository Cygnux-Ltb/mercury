package io.mercury.library.cryption;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.bouncycastle.LICENSE;

public class BouncyCastleLicenseTest {

	/**
	 * 
	 */
	public static final void showLicense() {
		showLicense(null);
	}

	/**
	 * 
	 * @param log
	 */
	public static final void showLicense(@Nonnull Logger log) {
		if (log != null) {
			log.info(LICENSE.licenseText);
		} else {
			System.out.println(LICENSE.licenseText);
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(LICENSE.licenseText);
	}

}
