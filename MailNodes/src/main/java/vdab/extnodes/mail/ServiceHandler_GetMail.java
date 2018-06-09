package vdab.extnodes.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.lcrc.af.AnalysisCompoundData;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.service.AnalysisServiceUsingHandler;
import com.lcrc.af.servicehandlers.ServiceHandler_A;

public class ServiceHandler_GetMail extends ServiceHandler_A {

	private String c_User;
	private String c_Password;
	private String c_Host ;
	private Integer c_Port ;
	private Integer c_ServerType;
	private Boolean c_MarkRead;
	private Boolean c_RemoveFromInbox;
	private Store c_Store;
	private Folder c_EmailFolder;
	private Session c_EmailSession; 

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
		c_Host = an.get_Server() ;
		c_Port = an.get_Port() ;
		c_ServerType = an.get_ServerType();
		c_MarkRead = an.get_MarkRead();
		c_RemoveFromInbox = an.get_RemoveFromInbox();

		try {
			openServer();
			Message[] messages = getEmails();
			
			if (messages.length > 0) {
				AnalysisEvent[] retEvs = processMessages(messages);
				an.serviceResponse(ev, retEvs);

				if (c_MarkRead && c_ServerType == MailServerType.IMAP) 
					markAsRead(messages);
				if (c_RemoveFromInbox)
					removeFromInbox(messages);
			}
		}
		catch(Exception e) {
			an.serviceFailed(ev, 5);
		}
		closeServer();

	}
	private AnalysisEvent[] processMessages(Message[] messages) throws Exception {
		ArrayList<AnalysisEvent> l = new ArrayList<AnalysisEvent>();
		for (Message msg: messages) {
			AnalysisCompoundData acd = new AnalysisCompoundData("Mail");
			String subject = null;
			String receivedDate = null;
			String body = null;
			String sentDate = null;
			String sender = null;
			Address[] senderAddresses = null;


			try {
				subject = msg.getSubject();
				receivedDate = msg.getReceivedDate() != null ? msg.getReceivedDate().toString() : null;
				body = getTextFromMessage(msg);
				sentDate =  msg.getSentDate() != null ? msg.getSentDate().toString() : null;
				senderAddresses = msg.getFrom();
				sender =  senderAddresses[0] != null ? senderAddresses[0].toString() : null;

				acd.addAnalysisData("Subject", subject);
				acd.addAnalysisData("Sender", sender);
				acd.addAnalysisData("Sent", sentDate);
				acd.addAnalysisData("Received", receivedDate);

				acd.addAnalysisData("Body", body);
				l.add(new AnalysisEvent(getNode(),acd));
			} catch(Exception e) {
				setErrorOnNode("Unable to process messages e>"+e);
				throw e;
			}

		}
		return l.toArray(new AnalysisEvent[l.size()]);

	}
	private String getTextFromMessage(Message message) throws IOException, MessagingException {
		
		String result = "";
		
		if (message.isMimeType("text/plain")) {
			result = message.getContent().toString();
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			result = getTextFromMimeMultipart(mimeMultipart);
		}
		return result;
	}
	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {

		int count = mimeMultipart.getCount();
		
		if (count == 0)
			throw new MessagingException("Multipart with no body parts not supported.");
		
		boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
		
		if (multipartAlt)
			return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			result.append(getTextFromBodyPart(bodyPart));
		}
		return result.toString();
	}
	private String getTextFromBodyPart(BodyPart bodyPart) throws IOException, MessagingException {

		String result = "";
		
		if (bodyPart.isMimeType("text/plain")) {
			result = (String) bodyPart.getContent();
		} else if (bodyPart.isMimeType("text/html")) {
			String html = (String) bodyPart.getContent();
			result = html;
		} else if (bodyPart.getContent() instanceof MimeMultipart){
			result = getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
		}
		return result;
	}
	private void openServer() throws Exception {

		Properties properties = new Properties();
		String protocol = "pop3";

		if (c_ServerType == MailServerType.IMAP)
			protocol = "imap";
		else if (c_ServerType == MailServerType.POP3)
			protocol = "pop3";

		properties.put(String.format("mail.%s.host", protocol), c_Host);
		properties.put(String.format("mail.%s.port", protocol), c_Port);
		properties.setProperty(String.format("mail.%s.starttls.enable", protocol), "true");

		 c_EmailSession = Session.getDefaultInstance(properties, null);
		 
		 try {
			 c_Store = c_EmailSession.getStore(protocol+"s");
			 c_Store.connect(c_Host, c_User, c_Password);

			 //open the email folder
			 c_EmailFolder = c_Store.getFolder("INBOX");
			 c_EmailFolder.open(Folder.READ_WRITE);
		 }
		 catch(Exception e) {
			 setErrorOnNode("Unable to connect and open folder e>"+e);
			 throw e;
		 }
	}
	private void closeServer () {
		try {
			c_EmailFolder.close(false);
			c_Store.close();
		} 
		catch (Exception e) {}
	}
	
	private Message[] getEmails() throws Exception {
		
		Message[] retMessages = new Message[0];
		
		try {
			Message[] messages = c_EmailFolder.getMessages();
			retMessages = messages;
		}
		catch(Exception e) {
			setErrorOnNode("Unable to read emails e>"+e);
			throw e;
		}

		return retMessages;
	}

	private void markAsRead(Message [] messages) throws Exception {
		
		try {
			for(Message message : messages) {
				//check multipart message as read
				Object content = message.getContent();
				if (content instanceof Multipart) {
					readAndDiscardMultipart((Multipart)content);
				}
				else {
					// if not multipart message
					MimeMessage source = (MimeMessage) message;
					new MimeMessage(source);
				}
			}

		} catch(Exception e) {
			setErrorOnNode("Unable to mark emails as read e>"+e);
			throw e;
		}
	}
	private void readAndDiscardMultipart(Multipart mp) 
			throws MessagingException, IOException {
		for (int i=0; i<mp.getCount(); i++) {
			BodyPart bodyPart = mp.getBodyPart(i);
			Object content = bodyPart.getContent();
			if (content instanceof Multipart) {
				readAndDiscardMultipart((Multipart)content);
			}
		}
	}
	private void removeFromInbox(Message [] messages) throws Exception {

		try {
			Flags deleted = new Flags(Flags.Flag.DELETED);
			c_EmailFolder.setFlags(messages, deleted, true);
			c_EmailFolder.expunge();
		} catch(Exception e) {
			setErrorOnNode("Unable to remove emails from inbox e>"+e);
			throw e;
		}

	}

}
