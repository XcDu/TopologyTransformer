package com.TopologyTransformer.www;

public class Test {
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		try {
			
			TopologyTransformer topologyTransformer=new TopologyTransformer();
			topologyTransformer.deleteTunnelSW2VM(13, 30);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e);
		}
	}

}
