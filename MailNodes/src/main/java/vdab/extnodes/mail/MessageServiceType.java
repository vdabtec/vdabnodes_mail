package vdab.extnodes.mail;

import com.lcrc.af.datatypes.AFEnum;

public class MessageServiceType {

	public static final int AUTODETECT = 0;
	public static final int SMS = 1;
	public static final int MMS = 2;
	
	private static AFEnum s_MessageServiceType = new AFEnum("MessageServiceType")
	.addEntry(AUTODETECT, "Auto Detect")
	.addEntry(SMS, "SMS")
	.addEntry(MMS, "MMS");
	public static AFEnum getEnum(){
		return s_MessageServiceType ;
	}
}
