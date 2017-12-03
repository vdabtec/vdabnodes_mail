package vdab.extnodes.mail;

import java.util.*; 

import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.constants.FilePathType;
import com.lcrc.af.constants.IconCategory;
import com.lcrc.af.datatypes.AFFile;
import com.lcrc.af.file.AFFileUtility;
import com.lcrc.af.service.AnalysisServiceUsingHandler;

public class MailService extends AnalysisServiceUsingHandler{

	private String c_User;
	private String c_Password ;

	private String c_Host ;
	private Integer c_Port = Integer.valueOf(587);
	private String c_Recipient ;
	private String c_Subject ;
	private String c_Body ;
	private Boolean c_DeliveryReceipt = Boolean.FALSE;
	private Boolean c_AddAttachment = Boolean.FALSE; 	
	private String c_Filename ;
	private String c_Directory ;
	private Integer c_FilePathType = new Integer(FilePathType.ABSOLUTE);
	private ServiceHandler_Mail c_EventHandler;

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
	public String get_SMTPHost(){
		return c_Host;
	}
	public void set_SMTPHost(String host){
		c_Host = host;
	}
	public Integer get_SMTPPort(){
		return c_Port;
	}
	public void set_SMTPPort(Integer port){
		c_Port = port;
	}
	public void set_Recipient(String to){
		c_Recipient=to;
	}
	public String get_Recipient(){
		return c_Recipient;
	}
	public void set_Subject(String subject){
		c_Subject = subject;
	}
	public String get_Subject(){
		return c_Subject;
	}
	public void set_Body(String body){
		c_Body = body;
	}
	public String get_Body(){
		return c_Body;
	}
	public void set_DeliveryReceipt(Boolean receipt){
		c_DeliveryReceipt = receipt;
	}
	public Boolean get_DeliveryReceipt(){
		return c_DeliveryReceipt;
	}
	public void set_AddAttachment(Boolean add){
		c_AddAttachment = add;
	}
	public Boolean get_AddAttachment(){
		return c_AddAttachment;
	}
	// OPTIONAL FILE STUFF FOR ATTACHMENT
	public String get_Filename(){
		return c_Filename;
	}
	public void set_Filename(String name){
		 c_Filename = name;
	}

	public AnalysisDataDef def_Filename(AnalysisDataDef theDataDef){
		if (!c_AddAttachment.booleanValue()){
			theDataDef.disable();
			return theDataDef;
		}
		AFFile[] files = AFFileUtility.getAllAFFiles(FilePathType.ABSOLUTE, c_Directory);
		ArrayList<String> l = new ArrayList<String>();
		for (AFFile file: files)
			l.add(file.getFilenameAndExt());		
		theDataDef.setAllPickValues(l.toArray(new String[l.size()]));
		return theDataDef;
	}
	public String get_Directory(){
		return c_Directory;
	}
	public void set_Directory(String dir){
		 c_Directory = dir;
	}
	public AnalysisDataDef def_Directory(AnalysisDataDef theDataDef){
		
		if (!c_AddAttachment.booleanValue()){
			theDataDef.disable();
			return theDataDef;
		}
		String[] dirs = AFFileUtility.getAllDirectories(FilePathType.ABSOLUTE, c_Directory);
		theDataDef.setAllPickValues(dirs);
		return theDataDef;
	}
	public AFFile getAFFile(){	
		if (c_Directory == null || c_Filename == null)
			return null;
		return new AFFile(c_Directory, c_Filename, null, FilePathType.ABSOLUTE);
	}
	// ANALYSIS NODE Methods -----------------------------------------

	public synchronized void processEvent(AnalysisEvent ev){	
		c_EventHandler = new ServiceHandler_Mail(this, ev);
	
   	}

}
