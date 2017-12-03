package vdab.extnodes.mail;


import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.constants.IconCategory;

public class TextTarget extends MailService {
	/**
	Alltel: @message.alltel.com
	Nextel: @messaging.nextel.com
	Sprint: @messaging.sprintpcs.com
	SunCom: @tms.suncom.com
	T-mobile: @tmomail.net
	VoiceStream: @voicestream.net
	**/
	private Long c_PhoneNumber;
	// ATTRIBUTES ---------------------------------------------------\
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
	public synchronized void processEvent(AnalysisEvent event){
			StringBuilder sb = new StringBuilder();
			sb.append(c_PhoneNumber);
			sb.append("@vtext.com");
			set_Recipient(sb.toString());
			super.processEvent(event);
   	}


}
