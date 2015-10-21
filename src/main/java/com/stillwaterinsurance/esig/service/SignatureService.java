package com.stillwaterinsurance.esig.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;

@Service
public class SignatureService {

	@Value("${pfx.file}")
	private String pfxFile;

	@Value("${pfx.password}")
	private String pfxPassword;

	@Value("${esig.home}")
	private String eSigHome;

	@Value("${esig.results}")
	private String eSigResults;
	
	@Autowired
	JavaMailSender mailSender;

	/**
	 * Sign the given PDF
	 * 
	 * @param document
	 *            the name of the unsigned doc
	 * @param field
	 *            the field name where the doc is to be signed
	 * @param signature
	 *            the signature to put on the doc
	 */
	public void signPdf(final String document, final String field, final String signature, final String email)
			throws GeneralSecurityException, IOException, DocumentException {

		Security.addProvider(new BouncyCastleProvider());

		// private key and certificate
		final KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
		ks.load(new FileInputStream(pfxFile), pfxPassword.toCharArray());
		final String alias = ks.aliases().nextElement();
		final PrivateKey pk = (PrivateKey) ks.getKey(alias, pfxPassword.toCharArray());
		final Certificate[] chain = ks.getCertificateChain(alias);

		// reader and stamper
		final PdfReader reader = new PdfReader(eSigHome + document);
		final DateTime now = new DateTime();
		String[] srcSplit = document.split("\\.");
		String resultPdfName = srcSplit[0] + "_" + now.getMillis() + "_" + signature + "." + srcSplit[1];
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		final PdfStamper stamper = PdfStamper.createSignature(reader, new FileOutputStream(eSigResults + resultPdfName), '\0');
		final PdfStamper stamper = PdfStamper.createSignature(reader, outputStream, '\0');

		// appearance
		final PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setVisibleSignature(field);
		// appearance.setReason("Because.");
		// appearance.setLocation("Foobar");
		// appearance.setContact("bld test");

		BufferedImage image = new BufferedImage(400, 40, BufferedImage.TYPE_BYTE_INDEXED);
		Graphics2D graphics = image.createGraphics();
		// set the background to white
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 400, 40);
		// set gradient font of text to be converted to image
		GradientPaint gradientPaint = new GradientPaint(10, 5, Color.BLACK, 20, 10, Color.BLACK);
		graphics.setPaint(gradientPaint);
		/*
		 * Brush Script MT, Freestyle Script, Harlow Solid Italic, Lucida Handwriting, 
		 * Mistral, Palace Script MT, Pristina, Rage Italic, Script MT Bold, 
		 * Segoe Script, Vivaldi, Vladimir Script
		 */
		Font font = new Font("Lucida Handwriting", Font.PLAIN, 28);
		graphics.setFont(font);
		graphics.drawString(signature, 0, 30);

		appearance.setSignatureGraphic(com.itextpdf.text.Image.getInstance(image, null));
		appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

		// release resources used by graphics context
		graphics.dispose();

		// TODO - what is this?
		appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);

		// signature
		final ExternalSignature es = new PrivateKeySignature(pk, "SHA-256", "BC");
		final ExternalDigest digest = new BouncyCastleDigest();

		MakeSignature.signDetached(appearance, digest, es, chain, null, null, null, 0, CryptoStandard.CMS);
		
		
		
		//Send email with signed PDF attached
		final MimeMessage message = mailSender.createMimeMessage();
		try {
			// construct the text body part
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText("Signed PDF attached.\n\n"
					+ "This came from Electronic Signature Proof of Concept.");
			
			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(outputStream.toByteArray(), "application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName(resultPdfName);

			// construct the mime multi part
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);
			
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(email);
			helper.setSubject("Signed PDF");
			message.setContent(mimeMultipart);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
