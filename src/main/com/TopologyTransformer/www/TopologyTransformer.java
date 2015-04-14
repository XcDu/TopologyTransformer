package com.TopologyTransformer.www;

public class TopologyTransformer {
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("Main.");
		try{
			SnmpManager snmpManager=new SnmpManager();
			snmpManager.addVlanID(17);
		}catch(Exception e){
			System.err.println(e);
		}
	}

}
