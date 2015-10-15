package com.stillwaterinsurance.esig.runnable.digsig;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

public class DsaGenerateSig {

	public static void main(String[] args) {

		String fileName = "BlankPage.pdf";

		if (args.length > 0) {
			fileName = args[0];
		}

		try {

			// Create and Initialize Key Pair generator
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			final SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);

			// Generate the Pair of Keys
			final KeyPair pair = keyGen.generateKeyPair();
			final PrivateKey priv = pair.getPrivate();
			final PublicKey pub = pair.getPublic();

			// Get a Signature object and initialize it
			final Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
			dsa.initSign(priv);

			// Supply the Signature object the data to be signed
			final FileInputStream fis = new FileInputStream(fileName);
			final BufferedInputStream bufin = new BufferedInputStream(fis);
			final byte[] buffer = new byte[1024];
			int len;
			while ((len = bufin.read(buffer)) >= 0) {
				dsa.update(buffer, 0, len);
			}
			bufin.close();

			// Generate the Signature
			final byte[] realSig = dsa.sign();

			// Save the Signature in a file
			final FileOutputStream sigfos = new FileOutputStream("sig");
			sigfos.write(realSig);
			sigfos.close();

			// Save the Public Key in a file
			final byte[] key = pub.getEncoded();
			final FileOutputStream keyfos = new FileOutputStream("suepk");
			keyfos.write(key);
			keyfos.close();

		} catch (final Exception e) {
			System.err.println("Caught exception " + e.toString());
		}

	}

}
