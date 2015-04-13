package com.TopologyTransformer.www;

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
	private final Address targetAddress=new UdpAddress("192.168.0.1/161");
	private final int retries=2;
	private final int timeout=1500;
	private final int snmpVersion=SnmpConstants.version2c;
	
	private CommunityTarget target;
	private TransportMapping<UdpAddress> transport;
	private Snmp snmp;
	
	public SnmpOperations() throws Exception{
		targetInit();
		snmpInit();
	}
	
	private void targetInit(){
		target=new CommunityTarget();
		target.setAddress(targetAddress);
		target.setRetries(retries);
		target.setTimeout(timeout);
		target.setVersion(snmpVersion);
	}
	private void snmpInit() throws Exception{
		transport=new DefaultUdpTransportMapping();
		snmp=new Snmp();
		snmp.addTransportMapping(transport);//untested.
		snmp.listen();//untested.
	}
	
//	public void snmpGet(){
//		
//	}

	
//	untested.
	public void snmpWalk(OID targetOid) throws Exception{
		
		String currentOid=targetOid.toString();
		String rootOid=currentOid;
		
		target.setCommunity(new OctetString(rocommunity));
		PDU request=new PDU();
		request.add(new VariableBinding(targetOid));
		request.setType(PDU.GETNEXT);
		do {
			ResponseEvent responseEvent=snmp.send(request, target);
			if(responseEvent!=null&&responseEvent.getResponse()!=null)
			{
				PDU response=responseEvent.getResponse();
				if(response.getErrorIndex()==PDU.noError&&response.getErrorStatus()==PDU.noError){
					VariableBinding variableBinding=(VariableBinding) response.getVariableBindings().firstElement();
					currentOid=variableBinding.getOid().toString();
					if(currentOid.contains(rootOid)){
						System.out.println(currentOid.toString()+":"+variableBinding.getVariable().toString());
					}
					else break;
				}else {
					throw new Exception(response.getErrorStatusText());
				}
			}else {
				throw responseEvent.getError();
			}
		} while (currentOid.contains(rootOid));
			
	}
	
	public boolean snmpSet(OID targetOid,Variable value) throws Exception{
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
					return true;
				}else {
					return false;
				}
			}else {
				throw new Exception(response.getErrorStatusText());
			}
		}else {
			throw responseEvent.getError();
		}
	}
	
}
