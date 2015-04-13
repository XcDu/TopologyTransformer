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
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpManager {
	private String community_ro,community_rw;//read-only and read-write community string
	private Address targetAddress;
	private CommunityTarget target;
	private TransportMapping<UdpAddress> transport;
	private Snmp snmp;
	public SnmpManager() throws IOException {
		
		community_ro="public";
		community_rw="private";
		//Address and port of target(161 and 162 available) for UDP 
		targetAddress=new UdpAddress("192.168.0.1/161");
		//Initialize Target
		target=new CommunityTarget();
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(SnmpConstants.version1);
		
		//Initialize the transport-mapping
		transport=new DefaultUdpTransportMapping();
		//initialize the snmp
		snmp = new Snmp(transport);
		//Start a udp progress.
		transport.listen();
	}
	
	//-------------------------------------------------------------------------
	//snmpWalk
	public void snmpWalk(OID oid) throws IOException{
		snmpWalk(oid,oid.toString());
	}
	
	
	// Specify that the variable rootOID is a criteria to stop the GETNEX(Set bellow in request).
	private void snmpWalk(OID oid,String rootOID) throws IOException{
		//Set CommunityTarget
		target.setCommunity(new OctetString(community_ro));
		//Set PDU
		PDU request = new PDU();
		request.add(new VariableBinding(oid));//iterative related.		
		request.setType(PDU.GETNEXT);
		//Get response
		ResponseEvent responseEvent=snmp.send(request, target);
		if(responseEvent!=null&&responseEvent.getResponse()!=null){
			PDU response=responseEvent.getResponse();
			if(response.getErrorIndex()==PDU.noError&&response.getErrorStatus()==PDU.noError){				
				VariableBinding vb =(VariableBinding) response.getVariableBindings().firstElement();
				OID cur_oid=vb.getOid();
				// Critical.Stop GETNEXT-operation. 
				if(cur_oid.toString().contains(rootOID)){
			
					System.out.println(cur_oid.toString()+":"+vb.getVariable().toString());
					
					snmpWalk(cur_oid,rootOID);
				}
			}else {
				System.out.println(response.getErrorStatusText());
			}
		}else{
			System.out.println("Request failed.");
		}
	}
	
	//-------------------------------------------------------------------------
	//snmpSet
	public void snmpSet(OID oid,Variable value) throws IOException{
		//Set CommunityTarget
		target.setCommunity(new OctetString(community_rw));
		//Set PDU
		PDU request=new PDU();
		request.add(new VariableBinding(oid,value));
		//Get response
		ResponseEvent responseEvent=snmp.set(request, target);
		if(responseEvent!=null&&responseEvent.getResponse()!=null){
			PDU response=responseEvent.getResponse();
			if(response.getErrorIndex()==PDU.noError&&response.getErrorStatus()==PDU.noError){
				VariableBinding vb=(VariableBinding) response.getVariableBindings().firstElement();
				Variable new_value=vb.getVariable();
				if(new_value.equals(value))
				{
					System.out.println(oid.toString()+":"+new_value+" Set successfully.");
				}else{
					System.out.println(oid.toString()+":"+new_value+" Set failed.");
				}
			}else {
				System.out.println(response.getErrorStatusText());
			}
		}else {
			System.out.println("Request failed.");
		}
	}
	
}
