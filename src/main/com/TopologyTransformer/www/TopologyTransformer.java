package com.TopologyTransformer.www;

public class TopologyTransformer{
	private SnmpManager snmpManager;
	
	public TopologyTransformer() throws Exception{
		snmpManager=new SnmpManager();
	}
	
	public String createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Exception{
		int vlanID=snmpManager.getNextFreeVlanID();		
		snmpManager.addVlan(vlanID);
		snmpManager.addPortToVlan(switchPortPeer, vlanID);
		snmpManager.addPortToVlan(peerSwitchPortPeer, vlanID);
		snmpManager.upPort(switchPortPeer);
		snmpManager.upPort(peerSwitchPortPeer);
		return "Success";
    }

    public String createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception{
    	snmpManager.addPortToVlan(switchPortPeeronTT,vmID);
    	snmpManager.upPort(switchPortPeeronTT);
        return "Success";
    }
    
    public String deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Exception {
    	snmpManager.downPort(switchPortPeeronTT);
    	snmpManager.downPort(athrSwitchPortPeeronTT);
    	int vlanid=snmpManager.isPortPeerPort(switchPortPeeronTT, athrSwitchPortPeeronTT);
    	snmpManager.deleteVlan(vlanid);
        return "Success";
    }

    public String deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception{
    	snmpManager.isPortPeerVlan(switchPortPeeronTT, vmID);
    	snmpManager.deletePortFromVlan(switchPortPeeronTT);
    	snmpManager.downPort(switchPortPeeronTT);
        return "Success";
    }
}
