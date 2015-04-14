javac -classpath libs/snmp4j-2.3.0.jar:libs/snmp4j-smi.jar src/main/com/TopologyTransformer/www/*.java -d bin
java -classpath bin:libs/snmp4j-2.3.0.jar:libs/snmp4j-smi.jar com.TopologyTransformer.www.TopologyTransformer
