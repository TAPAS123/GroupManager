package group.manager;

import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceCall {
	
	 private final String  NAMESPACE = "http://www.easy-sms.in/";
	 private final String URL = "http://www.easy-sms.in/gensms.asmx";
	 String SOAP_ACTION;
	 String METHOD_NAME;
	 String pro="some tech";
	 int TimeOut_MiliSec=30000; // TimeOut Time (10000 MiliSec=10 sec)//30sec 15/10/15 said by sunil sir for otp.
	 
	 // Member Login
	 public String loginD(String club,String mob,String passwrd,String Iemi,String RegId,String TempVer)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_Login";
		 METHOD_NAME="Club_Login";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(club);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempUser");
       PI.setValue(mob);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempPassword");
       PI.setValue(passwrd);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempG_ID");//Used for GCM RegID
       PI.setValue(RegId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempVer");//Used for App Version Name
       PI.setValue(TempVer);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL,TimeOut_MiliSec);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Connection TimeOut";    
       }
       catch(Exception ex)
       {
    	   String Err="Connection Problem !";
    	   if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }
    	   return Err;
       }
	 }
	 
	 public String loginOTP(String client,String TempUser,String TempMobile,String Iemi)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/OTP_VerAcc";
		 METHOD_NAME="OTP_VerAcc";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(client);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempUser");
       PI.setValue(TempUser);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMobile");
       PI.setValue(TempMobile);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.1.2015");
       PI.setType(String.class);
       request.addProperty(PI);
         
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL,TimeOut_MiliSec);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Connection TimeOut";    
       }
       catch(Exception ex)
       {
    	   String Err="Error";
    	   /*if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }*/
    	   return Err;
       }
	 }
	 
	 public String ResendSMSOTP(String client,String TempUser,String TempMobile,String Iemi)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/OTP_Again";
		 METHOD_NAME="OTP_Again";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(client);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempUser");
       PI.setValue(TempUser);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempPassword");
       PI.setValue(TempMobile);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("tempCode");
       PI.setValue("mdA.21.2015");
       PI.setType(String.class);
       request.addProperty(PI);
         
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL,TimeOut_MiliSec);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Connection TimeOut";    
       }
       catch(Exception ex)
       {
    	   String Err="Connection Problem !";
    	   if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }
    	   return Err;
       }
	 }
	 
	 public String Registration(String client,String Iemi)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_Reg_Master";
		 METHOD_NAME="Club_Reg_Master";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(client);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("Club_Reg_1.234");
       PI.setType(String.class);
       request.addProperty(PI);
         
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL,TimeOut_MiliSec);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Connection TimeOut";    
       }
       catch(Exception ex)
       {
    	   String Err="Connection Problem !";
    	   if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }
    	   return Err;
       }
	 }
	 
	 // Guest Login
	 public String login_Guest(String Cid,String GuestName,String Mob,String Email,String Iemi,String RegId,String TempVer)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_GuestLogin";
		 METHOD_NAME="Club_GuestLogin";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(Cid);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempUser");
       PI.setValue(GuestName);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMobile");
       PI.setValue(Mob);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempeMail");
       PI.setValue(Email);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
         
       PI =new PropertyInfo();
       PI.setName("TempG_ID");
       PI.setValue(RegId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("tempVer");// App Version Name
       PI.setValue(TempVer);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL,TimeOut_MiliSec);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Connection TimeOut"; 
       }
       catch(Exception ex)
       {
    	   String Err="Connection Problem !";
    	   if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }
    	   return Err;
       }
	 }
	 
	 
	 
	// Registered Or UnRegistered GCM Registered Id On Server
	public String GCMReg_Server(String ClientId,String IEMI,String RegId,String TempReg)
	{
		 SOAP_ACTION= "http://www.easy-sms.in/Club_Notif";
		 METHOD_NAME="Club_Notif";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
        
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_1.7733");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempIEMI");
       PI.setValue(IEMI);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempGID");
       PI.setValue(RegId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempReg");
       PI.setValue(TempReg);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage();
       }
	 }
	 
	 
	 public String clubDetail(String club,String Iemi,String Recordno,String Dir)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_Direct";
		 METHOD_NAME="Club_Direct";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(club);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempCrit");
       PI.setValue("*");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempREcNo1");
       PI.setValue(Recordno);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempDir");
       PI.setValue(Dir);
       PI.setType(String.class);
       request.addProperty(PI);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage();
       }
		 
	 }
	 
	 
	 //Call a webservice to get All Running Apps Data
	 public String RApps(String ClientId)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_AppRun";
		 METHOD_NAME="Club_AppRun";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_1.7739");
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	 
	//Call a webservice to get SMS Balance groupwise
	 public String Get_Sms_Bal(String ClientId)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_SMS_Balance";
		 METHOD_NAME="Club_SMS_Balance";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("SCODE");
       PI.setValue("SMS_Bal_Enq");
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	 
	//Call a webservice to get Pending SMS
	 public String Get_PendingSMS(String ClientId)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_SMS_PendDetails";
		 METHOD_NAME="Club_SMS_PendDetails";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("SCODE");
       PI.setValue("SMS_Pend");
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	 
	//Call a webservice to Delate Pending SMS
	 public String Delete_PendingSMS(String ClientId,String SmsUids)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_SMS_RecDelete";
		 METHOD_NAME="Club_SMS_RecDelete";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("SCODE");
       PI.setValue("SMS_Del");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempUid");
       PI.setValue(SmsUids);
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	//Call a webservice to get Send SMS to group members
	 public String SEND_SMS(String ClientId,String Msg,String GrpIds,String WithOutApp)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_SMS_Send";
		 METHOD_NAME="Club_SMS_Send";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("SCODE");
       PI.setValue("SMS_Bal_Enq");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempGRPID");
       PI.setValue(GrpIds);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMsg");
       PI.setValue(Msg);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempTbl3");
       PI.setValue(WithOutApp);
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	 
	//Call a webservice to Update News/Event Read
	public String Read_NewsEvents(String ClientId,String LogId,String TempMS,String TempNE,String TempNE_id)
	{
	   SOAP_ACTION= "http://www.easy-sms.in/Club_ReadNewsEvent";
	   METHOD_NAME="Club_ReadNewsEvent";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_1.2080");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMid");
       PI.setValue(LogId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMS");
       PI.setValue(TempMS);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempNE");
       PI.setValue(TempNE);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempNEid");
       PI.setValue(TempNE_id);
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString()+TempNE;
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	
	 
	//Call a webservice to  Read Or Unread News/Event Report
	public String Read_NewsEventsRPT(String ClientId,String TempNE,String TempNE_id)
	{
	   SOAP_ACTION= "http://www.easy-sms.in/Club_ReadNewsEventRPT";
	   METHOD_NAME="Club_ReadNewsEventRPT";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_1.4060");
       PI.setType(String.class);
       request.addProperty(PI);
      
       PI =new PropertyInfo();
       PI.setName("TempNE");
       PI.setValue(TempNE);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempNEid");
       PI.setValue(TempNE_id);
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	
	 
	//Call a webservice to Activate/DeActivate Running Apps
	 public String RApps_Allow(String ClientId,String MID,String Activate)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_MemAcNotAC";
		 METHOD_NAME="Club_MemAcNotAC";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_1.3333");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMid");
       PI.setValue(MID);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempYN");
       PI.setValue(Activate);
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	 
	 
	//Call a webservice to get All Group/Subgroup To Send News/Event 
	 public String GetAllGroup(String ClientId)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_GroupList";
		 METHOD_NAME="Club_GroupList";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.3933");
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	 
	 //Call a webservice to Create/Edit/Delete Group/Subgroup
	 public String Add_Edit_Delete_Group(String ClientId,String GrpName,String GrpIds,String DType)
	 {
		 /// Note ///
		 /* Here DType=A/E/D (i.e A-> Add Group , E-> Edit Group , D-> Delete Group) */
		 
		 SOAP_ACTION= "http://www.easy-sms.in/Club_AddGroup";
		 METHOD_NAME="Club_AddGroup";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Group.Add");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempGrpNM");
       PI.setValue(GrpName);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempGRmID");
       PI.setValue(GrpIds);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempType");
       PI.setValue(DType);//For Add/Edit/Delete group
       PI.setType(String.class);
       request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	 
	 public String clubREGSave(String club,String Iemi,String TempStr1,String TempStr2)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_Reg_Data_Save";
		 METHOD_NAME="Club_Reg_Data_Save";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(club);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_Reg_1.7732");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempSTR1");
       PI.setValue(TempStr1);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempStr2");
       PI.setValue(TempStr2);
       PI.setType(String.class);
       request.addProperty(PI);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage().toString();
       }
		 
	 }
	 
	 public String clubFouthEvent(String club,String Iemi,String Recordno,String Dir)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_Event";
		 METHOD_NAME="Club_Event";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(club);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempREcNo1");
       PI.setValue(Recordno);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempDir");
       PI.setValue(Dir);
       PI.setType(String.class);
       request.addProperty(PI);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage().toString();
       }
		 
	 }
	 
	 public String clubProp(String TempM_S,String tempclient,String Iemi,String tempm_id,
			 String M_Id,String M_Name,String M_Add1,String M_Add2,String M_Add3,
			 String M_City,String M_Pin,String M_Email,String M_DOB_D,String M_DOB_M,String M_DOB_Y,
			 String Ann_D,String Ann_M,String Ann_Y,String M_Mob,String M_Mob2,String M_Land1,
			 String M_Land2,String M_Email2,String M_Country,String M_State,
			 String M_BG,byte[] M_Pic,String PicB,String Loc_Res,String Loc_Off,String AdditionalData)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_AddPro_New";
		 METHOD_NAME="Club_AddPro_New";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(tempclient);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_1.7731");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM_ID");
       PI.setValue(tempm_id);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_Id");
       PI.setValue(M_Id);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_Name");
       PI.setValue(M_Name);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_Add1");
       PI.setValue(M_Add1);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_Add2");
       PI.setValue(M_Add2);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_Add3");
       PI.setValue(M_Add3);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_City");
       PI.setValue(M_City);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_Email");
       PI.setValue(M_Email);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_DOB_D");
       PI.setValue(M_DOB_D);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_DOB_M");
       PI.setValue(M_DOB_M);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_DOB_Y");
       PI.setValue(M_DOB_Y);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_Mob");
       PI.setValue(M_Mob);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("M_BG");
       PI.setValue(M_BG);
       PI.setType(String.class);
       request.addProperty(PI);

       /*PI =new PropertyInfo();
       PI.setName("M_Pic");
       PI.setValue(M_Pic);
       PI.setType(Byte[].class);
       request.addProperty(PI);*/
       request.addProperty("M_Pic",M_Pic); // Used For Byte Array
       
       PI =new PropertyInfo();
       PI.setName("PicB");
       PI.setValue(PicB);
       PI.setType(String.class);
       request.addProperty(PI);
 
       PI =new PropertyInfo();
       PI.setName("TempAdd1");
       PI.setValue(AdditionalData);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMrgD");
       PI.setValue(Ann_D);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMrgM");
       PI.setValue(Ann_M);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMrgY");
       PI.setValue(Ann_Y);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM_S");
       PI.setValue(TempM_S);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM_SndMob");
       PI.setValue(M_Mob2);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM_Land1");
       PI.setValue(M_Land1);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM_Land2");
       PI.setValue(M_Land2);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM_Email1");
       PI.setValue(M_Email2);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempC3_Name");
       PI.setValue(M_Country);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempC3_EMail");
       PI.setValue(M_State);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Temp_PIN");
       PI.setValue(M_Pin);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempLOC_Res");
       PI.setValue(Loc_Res);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempLOC_Off");
       PI.setValue(Loc_Off);
       PI.setType(String.class);
       request.addProperty(PI);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       
       // Add These Code if we Pass byte Array as a Parameter in webservice
        new MarshalBase64().register(envelope);   //serialization
        envelope.encodingStyle = SoapEnvelope.ENC;
       ////////////////////////////////////////////////////////////////
 
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage();
       }
		 
	 }
	 
	 public String SyncDT_GetJullian()
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/GetJullian";
		 METHOD_NAME="GetJullian";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "CatchError";
       }
	 }

	 public String clubSugg(String ClubCode,String Iemi,String tempm_id,String temptxt1,String tempadd1,String tempmail)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_Sugg";
		 METHOD_NAME="Club_Sugg";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClubCode);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_Sugg_1.34");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM_ID");
       PI.setValue(tempm_id);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempText1");
       PI.setValue(temptxt1);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempADD1");
       PI.setValue(tempadd1);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempeMail");
       PI.setValue(tempmail);
       PI.setType(String.class);
       request.addProperty(PI);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage();
       }
		 
	 }
	 
	 // Update Add_News Records from Mobile to server
	 public String Sync_Add_News(String GrpName,String TDate,String Title,String Desc,String GrpIds,String SendSMS)
	 {
	   SOAP_ACTION= "http://www.easy-sms.in/Group_News";
	   METHOD_NAME="Group_News";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Group.News");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TGrpNM");
       PI.setValue(GrpName);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TDate");
       PI.setValue(TDate);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TTitle");
       PI.setValue(Title);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TDescr");
       PI.setValue(Desc);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempGRPID");
       PI.setValue(GrpIds);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempSMSID");
       PI.setValue(SendSMS);
       PI.setType(String.class);
       request.addProperty(PI);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	  return ex.getMessage();
	   }
    }
	 
	 
	 public String Mda_Booking(String Tempclient,String frm,String to,String status,String Itm,String TT)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Mda_Booking";
		 METHOD_NAME="Mda_Booking";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.91415");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(Tempclient);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempFrDT");
       PI.setValue(frm);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempToDT");
       PI.setValue(to);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempStatus");
       PI.setValue(status);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempItm");
       PI.setValue(Itm);
       PI.setType(String.class);
       request.addProperty(PI);

       PI =new PropertyInfo();
       PI.setName("TempTT");
       PI.setValue(TT);
       PI.setType(String.class);
       request.addProperty(PI);
       

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Error";    
       }
       catch(Exception ex)
       {
    	   String Err="Error";
    	   if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }
    	   System.out.println(Err);
    	   return "Error";
       }
		 
	 }
	 
	 
	 public String clubbookingSync(String Tempclient,String Iemi)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_BookingSync";
		 METHOD_NAME="Club_BookingSync";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("CAWN_72_54.");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(Tempclient);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Error";    
       }
       catch(Exception ex)
       {
    	   String Err="Error";
    	   if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }
    	   System.out.println(Err);
    	   return "Error";
       }
		 
	 }
	 
	 
	 
	 public String clubbookinginfo(String Tempclient,String Iemi,String TempItemID,String TempSlotID,String TempDate)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_BookingInfo_GM";
		 METHOD_NAME="Club_BookingInfo_GM";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("CAWN_54_36.");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(Tempclient);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempItemID");
       PI.setValue(TempItemID);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempSlotID");
       PI.setValue(TempSlotID);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempDate");
       PI.setValue(TempDate);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Error";    
       }
       catch(Exception ex)
       {
    	   String Err="Error";
    	   if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }
    	   System.out.println(Err);
    	   return "Error";
       }
		 
	 }
	 
	 
	 
	 public String clubbooking(String Tempclient,String Iemi,String ItemCode,String TempSlotCode,String TempMonth,String TempYear)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_BookingCheck";
		 METHOD_NAME="Club_BookingCheck";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(Tempclient);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("CAWN_12_34.");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("ItemCode");
       PI.setValue(ItemCode);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempSlotCode");
       PI.setValue(TempSlotCode);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMonth");
       PI.setValue(TempMonth);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempYear");
       PI.setValue(TempYear);
       PI.setType(String.class);
       request.addProperty(PI);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage().toString();
       }
		 
	 }
	 
	 
	 public String clubbookingNew(String Tempclient,String Iemi,String ItemCode,String TempSlotCode,String TempMemno,String TempDate,String UserType,String MemName)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_BookingNew";
		 METHOD_NAME="Club_BookingNew";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("CAWN_21_43.");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(Tempclient);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(Iemi);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("ItemCode");
       PI.setValue(ItemCode);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempSlotCode");
       PI.setValue(TempSlotCode);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMemID");
       PI.setValue(TempMemno);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempDate");
       PI.setValue(TempDate);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("UserType");
       PI.setValue(UserType);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMName");
       PI.setValue(MemName);
       PI.setType(String.class);
       request.addProperty(PI);
       

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage().toString();
       }
		 
	 }
	 
	 
	 // Update Add_Event Records from Mobile to server
	 public String Sync_Add_Events(String ClientId, String EDateTime, String EName,String EDesc,String EVenue,String GrpIds,String EventConfirmWithGroupIDs,String EmailFormat,String EventDirectrsData)
	 {
		   SOAP_ACTION= "http://www.easy-sms.in/Group_CreateEvent";
		   METHOD_NAME="Group_CreateEvent";
	       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	         
	       PropertyInfo PI;
	       PI =new PropertyInfo();
	       PI.setName("Tempcode");
	       PI.setValue("Group.Event");
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TGrpNM");
	       PI.setValue(ClientId);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TDate");
	       PI.setValue(EDateTime);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TempEveNM");
	       PI.setValue(EName);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TempEveDescr");
	       PI.setValue(EDesc);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TempEveVenue");
	       PI.setValue(EVenue);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TempConfirm");
	       PI.setValue(EventConfirmWithGroupIDs);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TempGRPID");
	       PI.setValue(GrpIds);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("tempEveDir");
	       PI.setValue(EventDirectrsData);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TempEmailFormat");
	       PI.setValue(EmailFormat);
	       PI.setType(String.class);
	       request.addProperty(PI);

	       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	       envelope.dotNet = true;
	       envelope.setOutputSoapObject(request);
	       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
	     
	       try { 
	       	   aHttpTransport.call(SOAP_ACTION, envelope);
	           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
	           return response.toString();
	       }
	       catch(Exception ex)
	       {
	    	  return "Error#"+ex.getMessage();
		   }
	 }
	 
	 
	 
	// Sync Opinion Poll Records from Mobile to server
	 public String Sync_OpinionPoll_MS(String ClientId,String TempFullData)
	 {
	   SOAP_ACTION= "http://www.easy-sms.in/Club_QuAns";
	   METHOD_NAME="Club_QuAns";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.1203");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempStr");
       PI.setValue(TempFullData);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	  return "Error";
	   }
    }
	 
	 
	 
	// Get_Quiz_ScoreSheet Report
	 public String Get_Quiz_ScoreSheet(String ClientId,String Op1_ID)
	 {
	   SOAP_ACTION= "http://www.easy-sms.in/Club_QuAnsRPT";
	   METHOD_NAME="Club_QuAnsRPT";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.1504");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempOp1ID");
       PI.setValue(Op1_ID);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	  return "Error";
	   }
    }
	 
	 
	 
	// Get_Quiz_ScoreSheet Report
	 public String Get_OpinionPoll_ScoreSheet(String ClientId,String Op1_ID)
	 {
	   SOAP_ACTION= "http://www.easy-sms.in/Club_OpinionPollRPT";
	   METHOD_NAME="Club_OpinionPollRPT";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.2704");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempOp1ID");
       PI.setValue(Op1_ID);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	  return "Error";
	   }
    }
	 
	 
	 
	// Change Login Paswword
	 public String Change_Password(String ClientId,String LogId,String NewPass)
	 {
	   SOAP_ACTION= "http://www.easy-sms.in/Group_ChangePass";
	   METHOD_NAME="Group_ChangePass";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.0806");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(ClientId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMid");
       PI.setValue(LogId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempNPass");
       PI.setValue(NewPass);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	  return ex.getMessage();
	   }
    }
	 
	 
	 
	// GET BME LMERates 
	 public String Get_LMERates()
	 {
	   String NAMESPACE1 = "mdasoft.in";
	   String Url1 = "http://emanagement.in/MDAservice.asmx";
	   SOAP_ACTION= "mdasoft.in/GetLMEDataNew";
	   METHOD_NAME="GetLMEDataNew";
       SoapObject request = new SoapObject(NAMESPACE1, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("SCode");
       PI.setValue("MDA  GetLMEExchangeData");
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(Url1);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	  return "Error";
	   }
    } 
	 
	 
	 
	// GET BME Exchange rates 
	 public String Get_ExchangeRates()
	 {
	   String NAMESPACE1 = "mdasoft.in";
	   String Url1 = "http://emanagement.in/MDAservice.asmx";
	   SOAP_ACTION= "mdasoft.in/GetExchangeRates";
	   METHOD_NAME="GetExchangeRates";
       SoapObject request = new SoapObject(NAMESPACE1, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("SCode");
       PI.setValue("MDA  GetRbiOrg12");
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(Url1);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	  return "Error";
	   }
    } 
	 
	 
	 
	// GET MCX Market Watch Rates 
		 public String Get_MCXRates()
		 {
		   String NAMESPACE1 = "mdasoft.in";
		   String Url1 = "http://emanagement.in/MDAservice.asmx";
		   SOAP_ACTION= "mdasoft.in/GetMCXData";
		   METHOD_NAME="GetMCXData";
	       SoapObject request = new SoapObject(NAMESPACE1, METHOD_NAME);
	         
	       PropertyInfo PI;
	       PI =new PropertyInfo();
	       PI.setName("SCode");
	       PI.setValue("MDA  GetMCXRData234");
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	       envelope.dotNet = true;
	       envelope.setOutputSoapObject(request);
	       HttpTransportSE aHttpTransport = new HttpTransportSE(Url1);
	     
	       try { 
	       	   aHttpTransport.call(SOAP_ACTION, envelope);
	           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
	           return response.toString();
	       }
	       catch(Exception ex)
	       {
	    	  return "Error";
		   }
	    } 
	 
		 

		// GET PlayStore App Version 
		 public String Get_PlayStore_version(String PkgName)
		 {
		   String NAMESPACE1 = "mdasoft.in";
		   String Url1 = "http://emanagement.in/MDAservice.asmx";
		   SOAP_ACTION= "mdasoft.in/Get_App_PlayStore_Version";
		   METHOD_NAME="Get_App_PlayStore_Version";
	       SoapObject request = new SoapObject(NAMESPACE1, METHOD_NAME);
	         
	       PropertyInfo PI;
	       PI =new PropertyInfo();
	       PI.setName("SCode");
	       PI.setValue("MDA  GetPlay_Version");
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("PkgName");
	       PI.setValue(PkgName);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	       envelope.dotNet = true;
	       envelope.setOutputSoapObject(request);
	       HttpTransportSE aHttpTransport = new HttpTransportSE(Url1);
	     
	       try { 
	       	   aHttpTransport.call(SOAP_ACTION, envelope);
	           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
	           return response.toString();
	       }
	       catch(Exception ex)
	       {
	    	  return "Error";
		   }
	    } 		 
		 
		 
	 
	 //Get quiz Summary Added On 26-04-2017
	 public String Get_Quiz_Summary(String ClientId,String Op1_ID)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_QuAnsQWiseCorrRPT";
		   METHOD_NAME="Club_QuAnsQWiseCorrRPT";
	       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	         
	       PropertyInfo PI;
	       PI =new PropertyInfo();
	       PI.setName("TempCode");
	       PI.setValue("mdA.2404");
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("Tempclient");
	       PI.setValue(ClientId);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       PI =new PropertyInfo();
	       PI.setName("TempOp1ID");
	       PI.setValue(Op1_ID);
	       PI.setType(String.class);
	       request.addProperty(PI);
	       
	       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	       envelope.dotNet = true;
	       envelope.setOutputSoapObject(request);
	       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
	     
	       try { 
	       	   aHttpTransport.call(SOAP_ACTION, envelope);
	           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
	           return response.toString();
	       }
	       catch(Exception ex)
	       {
	    	  return "Error";
		   }
	 }
	 
	 
	 // Get Combos(Spinner) Data 
	 public String Get_FillCombo(String Client,String IEMI,String ComboType)
	 {
	   SOAP_ACTION= "http://www.easy-sms.in/Club_FillCombo";
	   METHOD_NAME="Club_FillCombo";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(Client);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("IEMI");
       PI.setValue(IEMI);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("Club_CBO_1.1234");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempType");
       PI.setValue(ComboType);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	  return ex.getMessage();
	   }
    }
	 
	 public String familymemUpdate(String tempclient,String tempm_id,String TempMemId,String TempName,String TempRela,String TempFath,String TempMoth,String TempCurrLoca,String TempMob1,String TempMob2,String TempEmail,
			 String TempD,String TempM,String TempY,String TempEdu,String TempWorkProf,String TempGender,String TempShare,String TempDesig,String TempGotra,String TempBirth_Time,String TempBirth_Place,String TempNative,
			 String TempHeight,String text6,String text7,String text8,String text9,String text10,String text11,String text12,String text13,String text14,byte[] M_Pic,String PicB)
	 {
	   SOAP_ACTION= "http://www.easy-sms.in/Club_Family";
	   METHOD_NAME="Club_Family";
       SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(tempclient);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_1.7732");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM_ID");
       PI.setValue(tempm_id);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMemId");
       PI.setValue(TempMemId);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempName");
       PI.setValue(TempName);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempRela");
       PI.setValue(TempRela);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempFath");
       PI.setValue(TempFath);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMoth");
       PI.setValue(TempMoth);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempCurrLoca");
       PI.setValue(TempCurrLoca);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMob1");
       PI.setValue(TempMob1);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMob2");
       PI.setValue(TempMob2);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempEmail");
       PI.setValue(TempEmail);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempD");
       PI.setValue(TempD);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempM");
       PI.setValue(TempM);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempY");
       PI.setValue(TempY);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempEdu");
       PI.setValue(TempEdu);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempWorkProf");
       PI.setValue(TempWorkProf);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempGender");
       PI.setValue(TempGender);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempShare");
       PI.setValue(TempShare);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempDesig");
       PI.setValue(TempDesig);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempGotra");
       PI.setValue(TempGotra);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempBirth_Time");
       PI.setValue(TempBirth_Time);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempBirth_Place");
       PI.setValue(TempBirth_Place);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempNative");
       PI.setValue(TempNative);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempHeight");
       PI.setValue(TempHeight);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text6");
       PI.setValue(text6);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text7");
       PI.setValue(text7);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text8");
       PI.setValue(text8);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text9");
       PI.setValue(text9);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text10");
       PI.setValue(text10);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text11");
       PI.setValue(text11);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text12");
       PI.setValue(text12);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text13");
       PI.setValue(text13);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("text14");
       PI.setValue(text14);
       PI.setType(String.class);
       request.addProperty(PI);
       
       /*PI =new PropertyInfo();
       PI.setName("M_Pic");
       PI.setValue(M_Pic);
       PI.setType(Byte[].class);
       request.addProperty(PI);*/
       request.addProperty("M_Pic",M_Pic); // Used For Byte Array
       
       PI =new PropertyInfo();
       PI.setName("PicB");
       PI.setValue(PicB);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       
       // Add These Code if we Pass byte Array as a Parameter in webservice
       new MarshalBase64().register(envelope);   //serialization
       envelope.encodingStyle = SoapEnvelope.ENC;
       ////////////////////////////////////////////////////////////////
       
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       }
	 }
	 	 
	 public String clubeventconfirm(String club,String TempEventID,String TempUserID,String TempAns,String TempMS)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_EventConfirm";
		 METHOD_NAME="Club_EventConfirm";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempClient");
       PI.setValue(club);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempcode");
       PI.setValue("Club_1.7731");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempEventID");
       PI.setValue(TempEventID);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempUserID");
       PI.setValue(TempUserID);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempAns");
       PI.setValue(TempAns);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempMS");
       PI.setValue(TempMS);
       PI.setType(String.class);
       request.addProperty(PI);

       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return ex.getMessage().toString();
       }
		 
	 }
	 	 
	 public String clubEvents(String TempGrp,String Evntid)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_Events";
		 METHOD_NAME="Club_Events";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.9936");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempGrp");
       PI.setValue(TempGrp);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempEvNo");
       PI.setValue(Evntid);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL,TimeOut_MiliSec);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(SocketTimeoutException se){
    	   return "Connection TimeOut";    
       }
       catch(Exception ex)
       {
    	   String Err="Connection Problem !";
    	   if(ex.getMessage()!=null)
    	   {
    	     Err=ex.getMessage();
    	   }
    	   return Err;
       }
	 }
	 
	 
	 //Call a webservice to Resend Event/News Notification
	 public String ResendNotificationNE(String ClientId,String GroupName,String TempNE,String TempNum1,String TempNoti,String TempEmail,String TempFormat,String SendTo)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Resend_GrpNews_Event";
		 METHOD_NAME="Resend_GrpNews_Event";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        
         PropertyInfo PI;
         PI =new PropertyInfo();
         PI.setName("Tempcode");
         PI.setValue("Group.NE");
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempClientID");
         PI.setValue(ClientId);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempGrpNM");
         PI.setValue(GroupName);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempNE");
         PI.setValue(TempNE);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempID");
         PI.setValue(TempNum1);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempNoti");
         PI.setValue(TempNoti);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempEmail");
         PI.setValue(TempEmail);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempFormat");
         PI.setValue(TempFormat);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempSendTo");
         PI.setValue(SendTo);
         PI.setType(String.class);
         request.addProperty(PI);
      
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       } 
	 }
	 
	 
	 public String Club_JudgeRpt(String clientID, int Op1ID)
	    {
	        SOAP_ACTION="http://www.easy-sms.in/Club_JudgeRpt";
	        METHOD_NAME="Club_JudgeRpt";

	        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

	        PropertyInfo pi=new PropertyInfo();
	        pi.setName("TempCode");
	        pi.setValue("mdA.0706");
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("Tempclient");
	        pi.setValue(clientID);
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempOp1ID");
	        pi.setValue(Op1ID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet = true;

	        envelope.setOutputSoapObject(request);

	        HttpTransportSE httpTransport = new HttpTransportSE(URL);
	        Object response=null;
	        try
	        {
	            httpTransport.call(SOAP_ACTION, envelope);
	            response = envelope.getResponse();
	            return response.toString();
	        }
	        catch (Exception exception)
	        {
	            return "Error";
	        }
	    }
	    public String Club_JudgeRpt_Descr(String clientID, int Op1ID,int Op2ID)
	    {
	        SOAP_ACTION="http://www.easy-sms.in/Club_JudgeRpt_Descr";
	        METHOD_NAME="Club_JudgeRpt_Descr";

	        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

	        PropertyInfo pi=new PropertyInfo();
	        pi.setName("TempCode");
	        pi.setValue("mdA.0706");
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("Tempclient");
	        pi.setValue(clientID);
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempOp1ID");
	        pi.setValue(Op1ID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempOp2Id");
	        pi.setValue(Op2ID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet = true;

	        envelope.setOutputSoapObject(request);

	        HttpTransportSE httpTransport = new HttpTransportSE(URL);
	        Object response=null;
	        try
	        {
	            httpTransport.call(SOAP_ACTION, envelope);
	            response = envelope.getResponse();
	            return response.toString();
	        }
	        catch (Exception exception)
	        {
	            return "Error";
	        }
	    }
	    public String Club_JudgeRpt_DescrCRITERIA(String clientID, int Op1ID,int TempOp2IdPARTIC)
	    {
	        SOAP_ACTION="http://www.easy-sms.in/Club_JudgeRpt_DescrCRITERIA";
	        METHOD_NAME="Club_JudgeRpt_DescrCRITERIA";

	        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

	        PropertyInfo pi=new PropertyInfo();
	        pi.setName("TempCode");
	        pi.setValue("mdA.0706");
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("Tempclient");
	        pi.setValue(clientID);
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempOp1ID");
	        pi.setValue(Op1ID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempOp2IdPARTIC");
	        pi.setValue(TempOp2IdPARTIC);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet = true;

	        envelope.setOutputSoapObject(request);

	        HttpTransportSE httpTransport = new HttpTransportSE(URL);
	        Object response=null;
	        try
	        {
	            httpTransport.call(SOAP_ACTION, envelope);
	            response = envelope.getResponse();
	            return response.toString();
	        }
	        catch (Exception exception)
	        {
	            return "Error";
	        }
	    }
	    public String Club_JudgeRpt_DescrCRITERIA_Mem(String clientID, int Op1ID,int TempPtpID,int judgeID)
	    {
	        SOAP_ACTION="http://www.easy-sms.in/Club_JudgeRpt_DescrCRITERIA_Mem";
	        METHOD_NAME="Club_JudgeRpt_DescrCRITERIA_Mem";

	        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

	        PropertyInfo pi=new PropertyInfo();
	        pi.setName("TempCode");
	        pi.setValue("mdA.0706");
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("Tempclient");
	        pi.setValue(clientID);
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempOp1ID");
	        pi.setValue(Op1ID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempOp2IdPARTIC");
	        pi.setValue(TempPtpID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempMemID");
	        pi.setValue(judgeID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet = true;

	        envelope.setOutputSoapObject(request);

	        HttpTransportSE httpTransport = new HttpTransportSE(URL);
	        Object response=null;
	        try
	        {
	            httpTransport.call(SOAP_ACTION, envelope);
	            response = envelope.getResponse();
	            return response.toString();
	        }
	        catch (Exception exception)
	        {
	            return "Error";
	        }
	    }
	    
	  
	    public String Group_ELEResult(String clientID, int TempOp1ID)
	    {
	        SOAP_ACTION="http://www.easy-sms.in/Group_ELEResult";
	        METHOD_NAME="Group_ELEResult";

	        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

	        PropertyInfo pi=new PropertyInfo();
	        pi.setName("TempCode");
	        pi.setValue("mdA.1006");
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempClient");
	        pi.setValue(clientID);
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempOp1ID");
	        pi.setValue(TempOp1ID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet = true;

	        envelope.setOutputSoapObject(request);

	        HttpTransportSE httpTransport = new HttpTransportSE(URL);
	        Object response=null;
	        try
	        {
	            httpTransport.call(SOAP_ACTION, envelope);
	            response = envelope.getResponse();
	            return response.toString();
	        }
	        catch (Exception exception)
	        {
	            return "Error";
	        }
	    }  
	    
	    
	    public String Group_DebtorLedger(String clientID, String TempAccID,String Type)
	    {
	    	String RName="";
	    	if(Type.equals("LEDGER"))
	    		RName="Group_DebtorLedger";
	    	else
	    		RName="Group_DebtorLedger2";
	    	
	        SOAP_ACTION="http://www.easy-sms.in/"+RName;
	        METHOD_NAME=RName;

	        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

	        PropertyInfo pi=new PropertyInfo();
	        pi.setName("TempCode");
	        pi.setValue("mdA.0806");
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempClient");
	        pi.setValue(clientID);
	        pi.setType(String.class);
	        request.addProperty(pi);

	        pi=new PropertyInfo();
	        pi.setName("TempAccID");
	        pi.setValue(TempAccID);
	        pi.setType(Integer.class);
	        request.addProperty(pi);

	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet = true;

	        envelope.setOutputSoapObject(request);

	        HttpTransportSE httpTransport = new HttpTransportSE(URL);
	        Object response=null;
	        try
	        {
	            httpTransport.call(SOAP_ACTION, envelope);
	            response = envelope.getResponse();
	            return response.toString();
	        }
	        catch (Exception exception)
	        {
	            return "Error";
	        }
	    }  
	    
	    
	///Set Poll Location in server    
    public String club_Location_Poll(String ClubCode,String TempOP1,String TempLoc,String TempRange)
	 {
		 SOAP_ACTION= "http://www.easy-sms.in/Club_OP1EleRange";
		 METHOD_NAME="Club_OP1EleRange";
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         
       PropertyInfo PI;
       PI =new PropertyInfo();
       PI.setName("TempCode");
       PI.setValue("mdA.1203");
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("Tempclient");
       PI.setValue(ClubCode);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempOp1");
       PI.setValue(TempOP1);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempLoca");
       PI.setValue(TempLoc);
       PI.setType(String.class);
       request.addProperty(PI);
       
       PI =new PropertyInfo();
       PI.setName("TempRange");
       PI.setValue(TempRange);
       PI.setType(String.class);
       request.addProperty(PI);
       
       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelope.dotNet = true;
       envelope.setOutputSoapObject(request);
       HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
     
       try { 
       	   aHttpTransport.call(SOAP_ACTION, envelope);
           SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
           return response.toString();
       }
       catch(Exception ex)
       {
    	   return "Error";
       }
	 }
       
       
     ////  Club Photo Upload Added on 04-08-2018
     public String Club_PhotoUploadData(String ClubCode,String AuthClubName,String tempm_id,String tempTitle,String TempImage)
  	 {
  		 SOAP_ACTION= "http://www.easy-sms.in/Club_PhotoUpload";
  		 METHOD_NAME="Club_PhotoUpload";
         
  		 SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
      
         PropertyInfo PI;
         PI =new PropertyInfo();
         PI.setName("TempClient");
         PI.setValue(ClubCode);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("Tempcode");
         PI.setValue("Club_Sugg_1.34");
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempAuthClub");
         PI.setValue(AuthClubName);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempM_ID");
         PI.setValue(tempm_id);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempTitle");
         PI.setValue(tempTitle);
         PI.setType(String.class);
         request.addProperty(PI);
         
         PI =new PropertyInfo();
         PI.setName("TempImage");
         PI.setValue(TempImage);
         PI.setType(String.class);
         request.addProperty(PI);

         SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
         envelope.dotNet = true;
         envelope.setOutputSoapObject(request);
         HttpTransportSE aHttpTransport = new HttpTransportSE(URL);
       
         try { 
         	  aHttpTransport.call(SOAP_ACTION, envelope);
              SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
              return response.toString();
         }
         catch(Exception ex)
         {
      	   return "Error";
         }
  	 }


    ////  Committee ClubInfo  Added on 08-03-2019
    public String Committee_ClubInfo(String ClubCode,String TType,String TVal)
    {
        SOAP_ACTION= "http://www.easy-sms.in/Club_ClubInfo";
        METHOD_NAME="Club_ClubInfo";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        PropertyInfo PI;
        PI =new PropertyInfo();
        PI.setName("Tempclient");
        PI.setValue(ClubCode);
        PI.setType(String.class);
        request.addProperty(PI);

        PI =new PropertyInfo();
        PI.setName("TempSCode");
        PI.setValue("Club_12981");
        PI.setType(String.class);
        request.addProperty(PI);

        PI =new PropertyInfo();
        PI.setName("TempClub");
        PI.setValue(TType);
        PI.setType(String.class);
        request.addProperty(PI);

        PI =new PropertyInfo();
        PI.setName("TempValue");
        PI.setValue(TVal);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE aHttpTransport = new HttpTransportSE(URL);

        try {
            aHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
            return response.toString();
        }
        catch(Exception ex)
        {
            return "Error";
        }
    }
        
}
