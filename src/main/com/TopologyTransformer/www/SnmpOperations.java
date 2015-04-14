package com.TopologyTransformer.www;

import java.util.Map;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.TransportMapping;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpOperations {
	private final String rocommunity="public";
	private final String rwcommunity="private";
	private final String address="192.168.0.1/161";
	private final int retries=1;
	private final int timeout=1000;
	private final int snmpVersion=SnmpConstants.version2c;
	
	private Address targetAddress;
	private CommunityTarget target;
	private TransportMapping<UdpAddress> transport;
	private Snmp snmp;
	
	public SnmpOperations() throws Exception{
		targetInit();
		snmpInit();
	}
	
	private void targetInit(){
		targetAddress=new UdpAddress(address);
		target=new CommunityTarget();
		target.setAddress(targetAddress);
		target.setRetries(retries);
		target.setTimeout(timeout);
		target.setVersion(snmpVersion);
	}
	private void snmpInit() throws Exception{
		transport=new DefaultUdpTransportMapping();
		snmp=new Snmp(transport);
		transport.listen();
	}
	
//	public void snmpGet(){
//		
//	}

	

	public void snmpWalk(Map<OID, Variable> status,OID targetOid) throws Exception{
		status.clear();
		
	    OID currentOid=new OID(targetOid);
		String rootOid=currentOid.toString();
		target.setCommunity(new OctetString(rocommunity));
		do {
			PDU request=new PDU();
			request.setType(PDU.GETNEXT);
		    request.add(new VariableBinding(currentOid));
		    ResponseEvent responseEvent=snmp.send(request, target);
			if(responseEvent!=null&&responseEvent.getResponse()!=null)
			{
				PDU response=responseEvent.getResponse();
				if(response.getErrorIndex()==PDU.noError&&response.getErrorStatus()==PDU.noError){
					VariableBinding variableBinding=(VariableBinding) response.getVariableBindings().firstElement();
					currentOid=variableBinding.getOid();
					if(currentOid.toString().contains(rootOid)){
						status.put(currentOid, variableBinding.getVariable());
					}else break;
				}else {
					throw new Exception(response.getErrorStatusText());
				}
			}else {
				throw responseEvent.getError();
			}
		} while (currentOid.toString().contains(rootOid));
			
	}
	
	public void snmpSet(OID targetOid,Variable value) throws Exception{
		
		target.setCommunity(new OctetString(rwcommunity));
		
		PDU request=new PDU();
		request.add(new VariableBinding(targetOid,value));
		ResponseEvent responseEvent=snmp.set(request, target);
		if(responseEvent!=null&&responseEvent.getResponse()!=null){
			PDU response=responseEvent.getResponse();
			if(response.getErrorIndex()==PDU.noError&&response.getErrorStatus()==PDU.noError){
				VariableBinding variableBinding=(VariableBinding) response.getVariableBindings().firstElement();
				Variable newValue=variableBinding.getVariable();
				if(newValue.equals(value)){
					return;
				}else {
					throw new Exception("Set "+targetOid.toString()+" failure.");
				}
			}else {
				throw new Exception(response.getErrorStatusText());
			}
		}else {
			throw responseEvent.getError();
		}
	}
}
