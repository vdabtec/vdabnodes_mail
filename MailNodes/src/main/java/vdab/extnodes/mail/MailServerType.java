package vdab.extnodes.mail;

import com.lcrc.af.datatypes.AFEnum;

public class MailServerType {


	public static final int POP3 = 1;
	public static final int IMAP = 2;
	
	private static AFEnum s_MailServerType = new AFEnum("MailServerType")
	.addEntry(POP3, "POP3")
	.addEntry(IMAP, "IMAP");
	public static AFEnum getEnum(){
		return s_MailServerType ;
	}
}
