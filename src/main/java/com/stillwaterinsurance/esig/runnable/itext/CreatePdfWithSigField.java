package com.stillwaterinsurance.esig.runnable.itext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;


public class CreatePdfWithSigField {

	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		final CreatePdfWithSigField signatures = new CreatePdfWithSigField();
		signatures.createPdfA();
		signatures.createPdfB();
		System.out.println("Done");
	}
	
	public void createPdfA() throws IOException, DocumentException {
		final Document document = new Document();
		final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("testsigA.pdf"));
		document.open();
		document.add(new Paragraph("PDF with a signature field"));

		final PdfFormField field = PdfFormField.createSignature(writer);
		writer.addAnnotation(field);
		field.setWidget(new Rectangle(75, 730, 275, 750), PdfAnnotation.HIGHLIGHT_INVERT);
		field.setFieldName("sigField");
		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		field.setPage();
		field.setMKBorderColor(BaseColor.BLACK);
		field.setMKBackgroundColor(BaseColor.WHITE);

		document.close();
	}
	
	public void createPdfB() throws IOException, DocumentException {
		final Document document = new Document();
		final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("testsigB.pdf"));
		document.open();
		document.add(new Paragraph("another PDF with a signature field"));
		document.add(new Paragraph("pdf B"));

		final PdfFormField field = PdfFormField.createSignature(writer);
		writer.addAnnotation(field);
		field.setWidget(new Rectangle(75, 730, 275, 750), PdfAnnotation.HIGHLIGHT_INVERT);
		field.setFieldName("sigField");
		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		field.setPage();
		field.setMKBorderColor(BaseColor.BLACK);
		field.setMKBackgroundColor(BaseColor.WHITE);

		document.close();
	}
}
