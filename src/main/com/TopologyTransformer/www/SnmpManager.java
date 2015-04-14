package com.TopologyTransformer.www;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

public class SnmpManager {
	private SnmpOperations snmpOperations;
	private Mibs mibs;
	public SnmpManager() throws Exception{
		snmpOperations=new SnmpOperations();
		mibs=new Mibs();
	}
	
	public void addVlanID(int tag) throws Exception{
		Map<OID, Variable> status=new LinkedHashMap<OID, Variable>();
		snmpOperations.snmpWalk(status, mibs.vtpVlanEditState);
		for (OID oid : status.keySet()) {
			System.out.println(status.get(oid));
		}
//		System.out.println(status.size());
		if(status.size()==0){
			snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(2));
			snmpOperations.snmpSet(new OID(mibs.vtpVlanEditBufferOwner).append(1), new OctetString("TopologyTransformer"));
			snmpOperations.snmpSet(new OID(mibs.vtpVlanEditRowStatus).append(1).append(tag),new Integer32(4));
			snmpOperations.snmpSet(new OID(mibs.vtpVlanEditType).append(1).append(tag), new Integer32(1));
			snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(3));
			snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(4));
			snmpOperations.snmpWalk(status, mibs.vtpVlanState);
			if(status.containsKey(new OID(mibs.vtpVlanState).append(1).append(tag))==false){
				throw new Exception("Fail to add VLANID:"+tag);
			}
		}else{
			throw new Exception("The edition is in use by another NMS station or device");
		}
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
