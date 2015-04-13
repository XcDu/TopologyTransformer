package com.TopologyTransformer.www;

public class SnmpManager {
	private SnmpOperations snmpOperations;
	
	public SnmpManager() throws Exception{
		snmpOperations=new SnmpOperations();
	}
	
	public void addVlanID(int tag){
		
	}
	
	public void deleteVlanID(int tag){
		
	}
	
	public void changeVlanID(int originalTag,int tag){
		
	}
	
	public void addPortToVlan(int port,int tag){
		
	}
	
	public void deletePortFromVlan(int port){
		
	}
	
	public void changePortFromVlantoVlan(int port,int originalTag,int tag){
		
	}
	
	public void upPort(int port){
		
	}
	
	public void downPort(int port){
		
	}
}
