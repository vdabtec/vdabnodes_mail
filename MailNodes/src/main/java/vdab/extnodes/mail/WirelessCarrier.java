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
