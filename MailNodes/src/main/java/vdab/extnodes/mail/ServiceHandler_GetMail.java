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
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.datatypes.AFFile;
import com.lcrc.af.service.AnalysisServiceUsingHandler;
import com.lcrc.af.servicehandlers.ServiceHandler_A;
import com.lcrc.af.util.AnalysisDataUtility;
import com.lcrc.af.util.StringUtility;
import com.lcrc.af.util.TemplateUtility;

public class ServiceHandler_GetMail extends ServiceHandler_A {

	private String c_User;
	private String c_Password;

	private String c_Host ;
	private Integer c_Port ;
	private String c_Recipient ;
	private String c_Subject;
	private String c_Body ;
	public ServiceHandler_GetMail(AnalysisServiceUsingHandler  invokingNode, AnalysisEvent ev) {
		super(invokingNode, ev);
	}
	// There are three types of events it can receive
	// a)String - If the body is null the string will be placed in the body.
	// b)AFFile - If the event contains any FILE, it is added as an attachment.
	// c) ALL - All event data values can be used in Templates in the Body and Subject
	
	protected void processEventInThread(AnalysisEvent ev) {
		

		GetMailService an = (GetMailService) getNode();
		
		c_User = an.get_User();
		c_Password= an.get_Password();
		c_Host = an.get_POP3Server() ;
		c_Port = an.get_POP3Port() ;
		getEmails(c_Host, c_Port, c_User, c_Password);
		/**
		c_Recipient = an.get_Recipient() ;
		c_Subject = an.get_Subject();
		c_Body = an.get_Body();
		ArrayList<AFFile> attachmentList = new ArrayList<AFFile>();
		
		if (! ev.isTriggerEvent()){	

			HashMap<String,String> tvMap = AnalysisDataUtility.buildTagValueMap(ev.getAnalysisData());
			
			// Build body with tags if necessary
			if (c_Body != null){
				if (StringUtility.hasTags(c_Body))
					c_Body = StringUtility.buildFromTemplate(c_Body, tvMap);		
			}		
			else { // If Body not specified send the entire event
				c_Body = ev.getStringData();
			}
			
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
			
		
			if (c_Subject != null && StringUtility.hasTags(c_Subject))
				c_Subject = StringUtility.buildFromTemplate(c_Subject, tvMap);
							
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
			catch (MessagingException e) {
				setErrorOnNode("Send mail failed e>"+e);
				an.serviceFailed(ev, 1);
			}
		}
		else {
			setErrorOnNode("Email body was not properly defined");
			an.serviceFailed(ev, 1);
		}
		*/
	}
	   private void getEmails(String host, Integer port, String user, String password) {
			      try {

			      //create properties field
			    	  Properties properties = new Properties();

			    	  properties.put("mail.pop3.host", host);
			    	  properties.put("mail.pop3.port", port);
			    	  properties.put("mail.pop3.starttls.enable", "true");
			    	  Session emailSession = Session.getDefaultInstance(properties);

			    	  //create the POP3 store object and connect with the pop server
			    	  Store store = emailSession.getStore("pop3s");

			    	  store.connect(host, user, password);

			    	  //create the folder object and open it
			    	  Folder emailFolder = store.getFolder("INBOX");
			    	  emailFolder.open(Folder.READ_ONLY);

			    	  // retrieve the messages from the folder in an array and print it
			    	  Message[] messages = emailFolder.getMessages();
			    	  System.out.println("messages.length---" + messages.length);

			    	  for (int i = 0, n = messages.length; i < n; i++) {
			    		  Message message = messages[i];
			    		  System.out.println("---------------------------------");
			    		  System.out.println("Email Number " + (i + 1));
			    		  System.out.println("Subject: " + message.getSubject());
			    		  System.out.println("From: " + message.getFrom()[0]);
			    		  System.out.println("Text: " + message.getContent().toString());

			    	  }

			    	  //close the store and folder objects
			    	  emailFolder.close(false);
			    	  store.close();

			      } catch (NoSuchProviderException e) {
			    	  e.printStackTrace();
			      } catch (MessagingException e) {
			    	  e.printStackTrace();
			      } catch (Exception e) {
			    	  e.printStackTrace();
			      }
	   }


}
