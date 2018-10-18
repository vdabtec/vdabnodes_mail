/*LICENSE*
 * Copyright (C) 2013 - 2018 MJA Technology LLC 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package vdab.extnodes.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.AnalysisObject;
import com.lcrc.af.datatypes.AFFile;
import com.lcrc.af.service.AnalysisServiceUsingHandler;
import com.lcrc.af.servicehandlers.ServiceHandler_A;
import com.lcrc.af.util.AnalysisDataUtility;
import com.lcrc.af.util.StringUtility;
import com.lcrc.af.util.TemplateUtility;

public class ServiceHandler_Mail extends ServiceHandler_A {

	private String c_User;
	private String c_Password;

	private String c_Host ;
	private Integer c_Port ;
	private String c_Recipient ;
	private String c_Subject;
	private String c_Body ;
	public ServiceHandler_Mail(AnalysisServiceUsingHandler  invokingNode, AnalysisEvent ev) {
		super(invokingNode, ev);
	}
	// There are three types of events it can receive
	// a)String - If the body is null the string will be placed in the body.
	// b)AFFile - If the event contains any FILE, it is added as an attachment.
	// c) ALL - All event data values can be used in Templates in the Body and Subject
	
	protected void processEventInThread(AnalysisEvent ev) {
		MailService an = (MailService) getNode();
		
		c_User = an.get_User();
		c_Password= an.get_Password();
		c_Host = an.get_SMTPHost() ;
		c_Port = an.get_SMTPPort() ;
		c_Recipient = an.get_Recipient() ;

		ArrayList<AFFile> attachmentList = new ArrayList<AFFile>();
		
		if (! ev.isTriggerEvent()){	

			HashMap<String,String> tvMap = AnalysisDataUtility.buildTagValueMap(ev.getAnalysisData());
			
			// Build body with tags if necessary
			c_Body = an.getTemplateAttribute( "Body", tvMap);
			if (c_Body == null)
				c_Body = ev.getStringData();
			
			//Find All File Attachments
			if (an.get_AddAttachment().booleanValue()){
				if (an.getAFFile() != null)
					attachmentList.add(an.getAFFile());
				ArrayList<AnalysisData> allFileAds = ev.getAnalysisData().getArrayOfSubDataOfClass(AFFile.class);
				for (AnalysisData ad : allFileAds){
					Object obj= ad.getData();
					if (obj instanceof AFFile)
						attachmentList.add((AFFile) obj);
				}
			}
			
			c_Subject = an.getTemplateAttribute( "Subject", tvMap);
							
		}
		if (c_Body != null){
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", c_Host);
			props.put("mail.smtp.port", c_Port.toString());
			if (an.get_DeliveryReceipt() != null && an.get_DeliveryReceipt().booleanValue()){
				String dsn = "SUCCESS,FAILURE,DELAY ORCPT=rfc822;" +c_User; 
				props.put("mail.smtp.dsn.notify", dsn);
			}
			Session session = Session.getInstance(props,
					new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(c_User, c_Password);
				}
			});

			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(c_User));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(c_Recipient));
				message.setSubject(c_Subject);
				if (attachmentList.size() <= 0){
					message.setText(c_Body);	
				}
				else{
					Multipart multipart = new MimeMultipart();	
					MimeBodyPart textPart = new MimeBodyPart();
					textPart.setText(c_Body);
					multipart.addBodyPart(textPart);
					addAttachments(multipart, attachmentList);
					message.setContent(multipart);					
				}
	
				Transport.send(message);
				an.serviceCompleted(ev);
			} 
			catch (Exception e) {
				setErrorOnNode("Send mail failed e>"+e);
				an.serviceFailed(ev, 1);
			}
		}
		else {
			setErrorOnNode("Email body was not properly defined");
			an.serviceFailed(ev, 1);
		}
	}

	private void addAttachments(Multipart multipart, ArrayList<AFFile> attachments) throws MessagingException{
	
		for (AFFile theFile: attachments){
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(theFile.getFilePath());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(theFile.getFilename());
			multipart.addBodyPart(messageBodyPart);
		}
	}
}
/** ATTACHMENTS 
Message message = new MimeMessage(session);
message.setFrom(new InternetAddress("from.mail.id@gmail.com"));
message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse("to.mail.id@gmail.com"));
message.setSubject("Testing Subject");
message.setText("PFA");



System.out.println("Sending");

Transport.send(message);

System.out.println("Done");

} catch (MessagingException e) {

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*; 
public class testMail {

    public static void main(String args[]) throws Exception {

        String host = "172.15.4.12";
        String from = "sample@example.com";
        String to = "austin@mymail.com"; 
       
        


        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
       properties.setProperty("mail.smtp.host", host);
       properties.put("mail.smtp.dsn.notify", dsn);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set the RFC 822 "From" header field using the
        // value of the InternetAddress.getLocalAddress method.
        message.setFrom(new InternetAddress(from));

        // Add the given addresses to the specified recipient type.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // Set the "Subject" header field.
        message.setSubject("hi..!");

        // Sets the given String as this part's content,
        // with a MIME type of "text/plain".
        message.setText("Delivered:");

        // Send message
        Transport.send(message);
        
         System.out.println("Message Send.....");
    }


*****/
