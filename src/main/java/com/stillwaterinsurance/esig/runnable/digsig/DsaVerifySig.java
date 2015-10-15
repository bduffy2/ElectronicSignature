package com.stillwaterinsurance.esig.runnable.digsig;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class DsaVerifySig {

	public static void main(String[] args) {

		String pubKeyFile = "suepk";
		String sigFile = "sig";
		String dataFile = "BlankPage.pdf";

		if (args.length > 2) {
			pubKeyFile = args[0];
			sigFile = args[1];
			dataFile = args[2];
		}

		try {

			// Read in public key bytes
			final FileInputStream keyfis = new FileInputStream(pubKeyFile);
			final byte[] encKey = new byte[keyfis.available()];
			keyfis.read(encKey);
			keyfis.close();

			// Generate public key
			final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
			final KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
			final PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

			// Input signature bytes
			final FileInputStream sigfis = new FileInputStream(sigFile);
			final byte[] sigToVerify = new byte[sigfis.available()];
			sigfis.read(sigToVerify);
			sigfis.close();

			// Initialize signature object for verification
			final Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
			sig.initVerify(pubKey);

			// Supply the Signature Object With the Data to be Verified
			final FileInputStream datafis = new FileInputStream(dataFile);
			final BufferedInputStream bufin = new BufferedInputStream(datafis);
			final byte[] buffer = new byte[1024];
			int len;
			while (bufin.available() != 0) {
				len = bufin.read(buffer);
				sig.update(buffer, 0, len);
			}
			bufin.close();

			// Verify the Signature
			final boolean verifies = sig.verify(sigToVerify);
			System.out.println("signature verifies: " + verifies);

		} catch (final Exception e) {
			System.err.println("Caught exception " + e.toString());
		}

	}

}
