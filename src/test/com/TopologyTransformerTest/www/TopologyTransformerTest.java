package com.TopologyTransformerTest.www;

import java.io.IOException;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;

public class TopologyTransformerTest{
		SnmpManager snmpManager;
		final Mibs mibs;
		public TopologyTransformerTest() throws IOException{
			// TODO Auto-generated constructor stub
			snmpManager=new SnmpManager();
			mibs=new Mibs();
		}
		public void addvlan(int tag) throws IOException{
			//snmpManager.snmpWalk(mibs.vtpVlanState);
			snmpManager.snmpSet(mibs.vtpVlanEditOperation.append(1),new Integer32(2));
			snmpManager.snmpSet(mibs.vtpVlanEditBufferOwner.append(1),new OctetString("chaos"));
			//snmpManager.snmpWalk(mibs.vtpVlanEditState);
			snmpManager.snmpSet(mibs.vtpVlanEditRowStatus.append(1).append(tag),new Integer32(4));
			snmpManager.snmpSet(mibs.vtpVlanEditType.append(1).append(tag),new Integer32(1));
			snmpManager.snmpSet(mibs.vtpVlanEditOperation,new Integer32(3));
			snmpManager.snmpSet(mibs.vtpVlanEditOperation,new Integer32(4));
		
		}
		public void delvlan(int tag) throws IOException{
			snmpManager.snmpSet(mibs.vtpVlanEditOperation.append(1),new Integer32(2));
			snmpManager.snmpSet(mibs.vtpVlanEditBufferOwner.append(1),new OctetString("chaos"));
			snmpManager.snmpSet(mibs.vtpVlanEditRowStatus.append(1).append(tag),new Integer32(6));
			snmpManager.snmpSet(mibs.vtpVlanEditOperation,new Integer32(3));
			snmpManager.snmpSet(mibs.vtpVlanEditOperation,new Integer32(4));
		}
		public void changevlan(int tag1,int tag2) throws IOException{
			delvlan(tag1);
			addvlan(tag2);
		}
		public void addport(int port,int vlan) throws IOException{
			snmpManager.snmpSet(mibs.vmVlan.append(".101"+port), new Integer32(vlan));
		}
		public void changeport(int port,int vlan) throws IOException{
			snmpManager.snmpSet(mibs.vmVlan.append(".101"+port), new Integer32(vlan));
		}
		public void downport(int port) throws IOException{
			snmpManager.snmpSet(mibs.ifAdminStatus.append(".101"+port),new Integer32(2));
		}
		public void upport(int port) throws IOException{
			snmpManager.snmpSet(mibs.ifAdminStatus.append(".101"+port),new Integer32(1));
		}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			TopologyTransformerTest ttt=new TopologyTransformerTest();
			//ttt.addvlan(30);
//			ttt.downport(13);
			ttt.upport(13);
//			ttt.delvlan(15);
			//ttt.addport(13, 30);
			//ttt.addport(14, 30);
//			ttt.delvlan(30);
	}

}
