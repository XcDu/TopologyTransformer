package com.TopologyTransformerTest.www;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpManager {
	private Address targetAddress;
	private TransportMapping<UdpAddress> transport;
	private Snmp snmp;
	private OID targetOID;
	private String rootOID;
	public SnmpManager() throws IOException {
		targetAddress=UdpAddress.parse("192.168.0.1/161");
		transport=new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		transport.listen();
		
		targetOID=new OID(new int[]{1,3,6,1,4,1,9,9,46,1,3,1,1,2});
		rootOID=new String("1.3.6.1.4.1.9.9.46.1.3.1.1.2");
	}
	public void snmpWalk(String curr_oid) throws IOException{
		
		
		CommunityTarget target =new CommunityTarget();
		target.setCommunity(new OctetString("public"));
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(SnmpConstants.version2c);
		
		PDU request = new PDU();
		request.add(new VariableBinding(targetOID));
		request.setType(PDU.GETNEXT);
		
		ResponseEvent rspEvent=snmp.send(request, target);
		if(rspEvent!=null&&rspEvent.getResponse()!=null){
			PDU response=rspEvent.getResponse();
			if(response.getErrorIndex()==PDU.noError&&response.getErrorStatus()==PDU.noError){				
				VariableBinding vb =(VariableBinding) response.getVariableBindings().firstElement();
				OID cur_oid=vb.getOid();
				String cur_str=cur_oid.toString();
				if(cur_str.contains(rootOID)){
			
					System.out.println(rootOID+vb.getVariable().toString());
					
					snmpWalk(cur_str);
				}else {
					System.out.println("Not in rootoid.");
				}
			}else {
				System.out.println(response.getErrorStatusText());
			}
		}
	}
}
