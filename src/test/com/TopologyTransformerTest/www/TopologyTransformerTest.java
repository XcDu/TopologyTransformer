package com.TopologyTransformerTest.www;

import java.io.IOException;

public class TopologyTransformerTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SnmpManager snmpManager=new SnmpManager();
		snmpManager.snmpWalk("1.3.6.1.4.1.9.9.46.1.3.1.1.2");
	}

}
