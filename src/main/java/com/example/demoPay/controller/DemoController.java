package com.example.demoPay.controller;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demoPay.entry.SMSHandler;
@Controller
@RequestMapping("/demo")
public class DemoController {
	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	private SMSHandler smsHandler;

	@Autowired
	DemoController(SMSHandler smsHandler) {
		this.smsHandler = smsHandler;
	}
	
   @ResponseBody
   @RequestMapping(method = GET, produces = "text/plain")
   public String demo(@RequestParam(value="sms", defaultValue="xyz") String smsText, @RequestParam(value="id", defaultValue="ROCK") String senderDeviceId) {
	   logger.trace("Message received from User \"" + senderDeviceId + "\" : "+ smsText); // FIXME for demo purpose. consider removing this.
	   String response = smsHandler.handleSmsRequest(smsText, senderDeviceId);
	   logger.trace("Response: " + response); // FIXME for demo purpose. consider removing this.
       return response;
   }
}