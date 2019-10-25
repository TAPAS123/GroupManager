package group.manager;

//New Change 6 Oct 2015
public class UnCommonProperties {
	
	private String MainClientID="";/////// if want remove group code in login page
	private String Service_PackageName="group.manager.Service_Call_New";
	private String AppTitle="Group Manager";
	private String LocationApp="https://play.google.com/store/apps/details?id=group.manager&hl=en";
	private String Addgrp="Yes";
	private String HeadName="Group Manager";//it display in login xml for heading 
	private String PackageName="group.manager";//it display package name in export data 
	public boolean ShowAllEventOption=false;//it Allow to ShowAll Event CheckBox or Not In Event
	
	public String GET_MainClientID()
	{
		return MainClientID;
	}
	
	public String GET_headName()
	{
		return HeadName;
	}

	public String GET_Service_PackageName()
	{
		return Service_PackageName;
	}
	
	public String GET_AppTitle()
	{
		return AppTitle;
	}
	
	public String GET_Addgroup()
	{
		return Addgrp;
	}
	
	public String GET_AppLocation()
	{
		return LocationApp;
	}
	
	public String GET_PackageName()
	{
		return PackageName;
	}
	
}
