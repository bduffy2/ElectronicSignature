package com.stillwaterinsurance.esig.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.DocumentException;
import com.stillwaterinsurance.esig.service.SignatureService;

@Controller
@RequestMapping("/index")
public class IndexController {

	@Autowired
	private SignatureService signatureService;
	
	@RequestMapping(value={"", "/"}, method=RequestMethod.GET)
	public String index(final ModelAndView mav, final HttpSession session) {
		return "/index";
	}
	
	@RequestMapping(value={"/signPdf"}, method=RequestMethod.POST)
	public ResponseEntity<String> signPdf(final HttpSession session, final HttpServletRequest request, 
			@RequestParam(value="document") final String document,
			@RequestParam(value="field") final String field,
			@RequestParam(value="signature") final String signature,
			@RequestParam(value="email") final String email) {
		
		String result = "";
		HttpStatus statusCode = HttpStatus.OK;
		
		try {
			signatureService.signPdf(document, field, signature, email);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(result, statusCode);
	}
	
}
