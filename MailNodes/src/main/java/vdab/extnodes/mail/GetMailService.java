package vdab.extnodes.mail;

import java.util.*; 

import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.constants.FilePathType;
import com.lcrc.af.constants.IconCategory;
import com.lcrc.af.datatypes.AFFile;
import com.lcrc.af.file.AFFileUtility;
import com.lcrc.af.service.AnalysisServiceUsingHandler;

public class GetMailService extends AnalysisServiceUsingHandler{

	static {
		MailServerType.getEnum();
	}
	
	private String c_User;
	private String c_Password ;
	
	private String c_Host ;
	private Integer c_Port = Integer.valueOf(995);
	private Integer c_ServerType;

	private Boolean  c_MarkRead = Boolean.FALSE;
	private Boolean  c_RemoveFromInbox = Boolean.FALSE;
	private ServiceHandler_GetMail c_EventHandler;

	// ATTRIBUTES ---------------------------------------------------\
	public Integer get_IconCode(){
		return IconCategory.NODE_SENDMAIL;
	}
	public Boolean get_MarkRead() {
		return c_MarkRead;
	}
	public void set_MarkRead(Boolean read) {
		c_MarkRead = read;
	}
	public AnalysisDataDef def_MarkRead(AnalysisDataDef theDataDef){
		if (c_ServerType == null ||c_ServerType.intValue() != MailServerType.IMAP)
			theDataDef.disable();
		return theDataDef;
	}
	
	public Boolean get_RemoveFromInbox() {
		return c_RemoveFromInbox;
	}
	public void set_RemoveFromInbox(Boolean remove) {
		c_RemoveFromInbox = remove;
	}
	public AnalysisDataDef def_RemoveFromInbox(AnalysisDataDef theDataDef){
		if (c_ServerType == null ||c_ServerType.intValue() != MailServerType.IMAP)
			theDataDef.disable();
		return theDataDef;
	}
	public String get_User(){	
		return c_User;
	}
	public void set_User( String user){
		c_User = user;
	}
	public String get_Password(){
		return c_Password;
	}
	public void set_Password( String password){
		c_Password = password;
	}
	public String get_Server(){
		return c_Host;
	}
	public void set_Server(String host){
		c_Host = host;
	}
	public Integer get_Port(){
		return c_Port;
	}
	public void set_Port(Integer port){
		c_Port = port;
	}
	public Integer get_ServerType(){
		return c_ServerType;
	}
	public void set_ServerType(Integer type) {
		Integer oldType = c_ServerType;
		c_ServerType = type;
		if (oldType == null || c_ServerType.intValue() != oldType.intValue()) {
			switch (c_ServerType.intValue()) {
				case MailServerType.IMAP:
					c_Port = 993;
					break;
				case MailServerType.POP3:
					c_Port = 995;
					break;
			}
		}
	}


	// ANALYSIS NODE Methods -----------------------------------------

	public synchronized void processEvent(AnalysisEvent ev){	
		c_EventHandler = new ServiceHandler_GetMail(this, ev);
	
   	}

}
