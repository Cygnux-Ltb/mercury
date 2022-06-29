package io.mercury.library.cryption;

import javax.annotation.Nullable;

import org.bouncycastle.LICENSE;
import org.slf4j.Logger;

public class BouncyCastleLicense {

	/**
	 * 
	 */
	public static void showLicense() {
		showLicense(null);
	}

	/**
	 * 
	 * @param log
	 */
	public static void showLicense(@Nullable Logger log) {
		if (log != null)
			log.info(LICENSE.licenseText);
		else
			System.out.println(LICENSE.licenseText);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(LICENSE.licenseText);
	}

}
