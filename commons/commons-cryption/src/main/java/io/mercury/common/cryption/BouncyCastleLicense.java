package io.mercury.common.cryption;

import javax.annotation.Nonnull;

import org.bouncycastle.LICENSE;
import org.slf4j.Logger;

public class BouncyCastleLicense {

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

	public static void main(String[] args) {
		System.out.println(LICENSE.licenseText);
	}

}
