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
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.constants.IconCategory;
import com.lcrc.af.datatypes.AFFile;

public class SendTextService extends MailService {
	static {
		MessageServiceType.getEnum();
		WirelessCarrier.getEnum();
	}
	private Long c_PhoneNumber;
	private Integer c_Carrier = Integer.valueOf(WirelessCarrier.VERIZON);
    private Integer c_ServiceType = Integer.valueOf(MessageServiceType.SMS);
	// ATTRIBUTES ---------------------------------------------------\
    public Integer get_WirelessCarrier() {
    	return c_Carrier;
    }
    
    public void set_WirelessCarrier(Integer carrier) {
    	c_Carrier = carrier;
    }
    
    public Integer get_MessageType() {
    	return c_ServiceType;
    }
    
    public void set_MessageType(Integer type) {
    	c_ServiceType = type;
    }
    
	public Integer get_IconCode(){
		return IconCategory.NODE_SENDMAIL;
	}
	
	public Long get_PhoneNumber(){
		return c_PhoneNumber;
	}
	
	public void set_PhoneNumber(Long number){
		c_PhoneNumber = number;
	}
	// ANALYSIS NODE Methods -----------------------------------------
	public synchronized void processEvent(AnalysisEvent ev){
	
		// Determine service type. On AutoDetect set to MMS if it has a file in the incoming event.
		int serviceType = c_ServiceType.intValue();
		if (serviceType == MessageServiceType.AUTODETECT) {
			if (! ev.isTriggerEvent()){
				ArrayList allFileAds = ev.getAnalysisData().getArrayOfSubDataOfClass(AFFile.class);
				if (allFileAds.size() > 0)
					serviceType = MessageServiceType.MMS;
				else 
					serviceType = MessageServiceType.SMS;
			}
			else {
				serviceType = MessageServiceType.SMS; 
			}
		}
	
		set_AddAttachment(Boolean.valueOf(serviceType == MessageServiceType.MMS));
		
		StringBuilder sb = new StringBuilder();
		sb.append(c_PhoneNumber);
		sb.append(getEmailAddress(c_Carrier.intValue(), serviceType));
		set_Recipient(sb.toString());
		super.processEvent(ev);
	}
	private String getEmailAddress(int carrier, int messageType){

		switch (messageType){

		case MessageServiceType.MMS:
			switch (carrier){
			case WirelessCarrier.ALLTEL:
				return "@message.alltel.com"; //@mms.alltel.net works too for mms
				
			case WirelessCarrier.ATT:
				return "@mms.att.net";
			
			case WirelessCarrier.ATTMOBILITY:
				return "@cingularme.com";//@mobile.mycingular.com works too
				
			case WirelessCarrier.BOOST:
				return "@myboostmobile.com";
				
			case WirelessCarrier.CRICKET:
				return "@mms.mycricket.com";
			
			case WirelessCarrier.METROPCS:
				return "@mymetropcs.com";
			
			case WirelessCarrier.SPRINT:
				return "@pm.sprint.com";
				
			case WirelessCarrier.STRAIGHTTALK:
				return "@mypixmessages.com";
				
			case WirelessCarrier.USCELLULAR:
				return "@mms.uscc.net";
				
			case WirelessCarrier.VIRGIN:
				return "@vmplx.com";
			
			case WirelessCarrier.TMOBILE:
				return "@tmomail.net";

			case WirelessCarrier.VERIZON:
				return "@vzwpix.com";

			default:
				return null;
			}

		case MessageServiceType.SMS:
			switch (carrier){
			case WirelessCarrier.ALLTEL:
				return "@message.alltel.com";//@text.wireless.alltel.com works too for SMS
				
			case WirelessCarrier.ATT:
				return "@txt.att.net";
			
			case WirelessCarrier.ATTMOBILITY:
				return "@cingularme.com";//@mobile.mycingular.com works too
			
			case WirelessCarrier.BOOST:
				return "@myboostmobile.com";
				
			case WirelessCarrier.CRICKET:
				return "@sms.mycricket.com";
			
			case WirelessCarrier.METROPCS:
				return "@mymetropcs.com";
			
			case WirelessCarrier.SPRINT:
				return "@messaging.sprintpcs.com";
				
			case WirelessCarrier.STRAIGHTTALK:
				return "@vtext.COM";
				
			case WirelessCarrier.USCELLULAR:
				return "@email.uscc.net";
				
			case WirelessCarrier.VIRGIN:
				return "@vmobl.com";
			
			case WirelessCarrier.TMOBILE:
				return "@tmomail.net";

			case WirelessCarrier.VERIZON:
				return "@vtext.com";
			
			default:
				return null;
			}	
		}
		return null;
	}


}
