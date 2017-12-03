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

	private String c_User;
	private String c_Password ;

	private String c_Host ;
	private Integer c_Port = Integer.valueOf(995);

	private ServiceHandler_GetMail c_EventHandler;

	// ATTRIBUTES ---------------------------------------------------\
	public Integer get_IconCode(){
		return IconCategory.NODE_SENDMAIL;
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
	public String get_POP3Server(){
		return c_Host;
	}
	public void set_POP3Server(String host){
		c_Host = host;
	}
	public Integer get_POP3Port(){
		return c_Port;
	}
	public void set_POP3Port(Integer port){
		c_Port = port;
	}

	// ANALYSIS NODE Methods -----------------------------------------

	public synchronized void processEvent(AnalysisEvent ev){	
		c_EventHandler = new ServiceHandler_GetMail(this, ev);
	
   	}

}
