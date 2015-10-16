package com.stillwaterinsurance.esig.runnable.itext;

/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;


public class SignatureField {

	/** The resulting PDF */
	public static String ORIGINAL = "src/main/webapp/resources/results/unsigned.pdf";
	/** The resulting PDF */
	public static String SIGNED1 = "src/main/webapp/resources/results/signed_1.pdf";
	/** The resulting PDF */
	public static String SIGNED2 = "src/main/webapp/resources/results/signed_2.pdf";

	/** One of the resources. */
	public static final String RESOURCE = "src/main/webapp/resources/img/logo.gif";

	/** A properties file that is PRIVATE */
	public static String PATH = "key.properties";
	/** Some properties used when signing. */
	public static Properties properties = new Properties();

	/**
	 * Creates a PDF document.
	 * 
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createPdf(String filename) throws IOException, DocumentException {
		final Document document = new Document();
		final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		document.add(new Paragraph("This is a PDF with a signature field"));
		
		final PdfFormField field = PdfFormField.createSignature(writer);
		field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_INVERT);
		field.setFieldName("mySig");
		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		field.setPage();
		field.setMKBorderColor(BaseColor.BLACK);
		field.setMKBackgroundColor(BaseColor.WHITE);
		final PdfAppearance tp = PdfAppearance.createAppearance(writer, 72, 48);
		tp.rectangle(0.5f, 0.5f, 71.5f, 47.5f);
		tp.stroke();
		field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
		writer.addAnnotation(field);
		
		document.close();
	}

	/**
	 * Manipulates a PDF file src with the file dest as result
	 * 
	 * @param src
	 *            the original PDF
	 * @param dest
	 *            the resulting PDF
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 * @throws KeyStoreException
	 * @throws Exception
	 */
	public void signPdf(String src, String dest, boolean certified, boolean graphic)
			throws GeneralSecurityException, IOException, DocumentException {
		
		// private key and certificate
		final String path = properties.getProperty("PRIVATE");
		final String keystore_password = properties.getProperty("PASSWORD");
		final String key_password = properties.getProperty("PASSWORD");
		final KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
		ks.load(new FileInputStream(path), keystore_password.toCharArray());
		final String alias = ks.aliases().nextElement();
		final PrivateKey pk = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
		final Certificate[] chain = ks.getCertificateChain(alias);
		
		// reader and stamper
		final PdfReader reader = new PdfReader(src);
		final PdfStamper stamper = PdfStamper.createSignature(reader, new FileOutputStream(dest), '\0');
		
		// appearance
		final PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setVisibleSignature("mySig");
		appearance.setReason("It's personal.");
		appearance.setLocation("Foobar");
		if (certified) {
			appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
		}
		if (graphic) {
			appearance.setSignatureGraphic(Image.getInstance(RESOURCE));
			appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
		}
		
		// signature
		final ExternalSignature es = new PrivateKeySignature(pk, "SHA-256", "BC");
		final ExternalDigest digest = new BouncyCastleDigest();
		
		MakeSignature.signDetached(appearance, digest, es, chain, null, null, null, 0, CryptoStandard.CMS);
	}

	/**
	 * Main method.
	 *
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		properties.load(new FileInputStream(PATH));
		final SignatureField signatures = new SignatureField();
		signatures.createPdf(ORIGINAL);
		signatures.signPdf(ORIGINAL, SIGNED1, false, false);
		signatures.signPdf(ORIGINAL, SIGNED2, true, true);
		System.out.println("Done");
	}
}
