package vdab.extnodes.mail;

import com.lcrc.af.datatypes.AFEnum;

public class WirelessCarrier {
	public static final int UNKNOWN = 0;
	public static final int ALLTEL = 1;
	public static final int ATT = 2;
	public static final int ATTMOBILITY = 3;
	public static final int BOOST = 4;
	public static final int CRICKET = 5;
	public static final int METROPCS = 6;
	public static final int SPRINT = 7;
	public static final int STRAIGHTTALK = 8;
	public static final int TMOBILE = 9;
	public static final int USCELLULAR = 10;
	public static final int VERIZON = 11;
	public static final int VIRGIN = 12;	
	
	private static AFEnum s_WirelessCarrier = new AFEnum("WirelessCarrier")
	.addEntry(UNKNOWN, "Unknown")
	.addEntry(ALLTEL, "Alltel")
	.addEntry(ATT, "AT&T Wireless")
	.addEntry(ATTMOBILITY, "AT&T Mobility")
	.addEntry(BOOST, "Boost Mobile")
	.addEntry(CRICKET, "Cricket")
	.addEntry(METROPCS, "Metro PCS")
	.addEntry(SPRINT, "Sprint")
	.addEntry(STRAIGHTTALK, "Straight Talk")
	.addEntry(TMOBILE, "T-Mobile")
	.addEntry(USCELLULAR, "U.S.Cellular")
	.addEntry(VERIZON, "Verizon")
	.addEntry(VIRGIN, "Virgin Mobile");
	public static AFEnum getEnum(){
		return s_WirelessCarrier;
	}
}
