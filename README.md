# Zookeeper Concepts:
ZooKeeper is an open-source distributed coordination service designed to manage and coordinate large, distributed applications. 
It provides essential services like configuration management, synchronization, and distributed naming, enabling distributed applications 
to function smoothly by handling critical coordination tasks. Zookeeper was originally developed by **Yahoo!**

## Key Features of Apache ZooKeeper:
<ol>
  <li><b>Centralized Coordination:</b> Acts as a centralized repository for managing distributed applications.</li>
  <li><b>High Availability:</b> Supports a replicated, fault-tolerant architecture for reliability.</li>
  <li><b>Atomicity:</b> Ensures that updates to data are atomic.</li>
  <li><b>Sequential Consistency:</b> Guarantees order consistency for client updates.</li>
  <li><b>Eventual Consistency:</b> Allows all nodes to eventually synchronize.</li>
  <li><b>Watch Mechanism:</b> Provides a notification system for clients to subscribe to changes in the data.</li>
</ol>

## Typical Use Cases:
<ol>
<li><b>Distributed Locking:</b> Implements locks for resource coordination among distributed systems.</li>
<li><b>Leader Election:</b> Helps select a leader in distributed systems.</li>
<li><b>Configuration Management:</b> Stores configuration data for applications in a centralized way.</li>
<li><b>Service Discovery:</b> Maintains the location and availability of services.</li>
<li><b>Queue Management:</b> Manages distributed task queues.</li>
</ol>

## ZooKeeper Data Model:
ZooKeeper uses a hierarchical namespace similar to a file system:

<ul>
  <li> ZNodes: The fundamental entities in ZooKeeper, akin to directories or files in a file system.</li>
  <ul>
    <li type="disc"> Ephemeral ZNodes: Exist only as long as the session with the client is active.</li>
    <li type="disc">Persistent ZNodes: Remain until explicitly deleted.</li>
    <li type="disc">Sequential ZNodes: Have unique, sequential identifiers appended to their names. (Can be Persistent or Ephemeral)</li>
  </ul>  
</ul>

## Architecture:
ZooKeeper operates in a replicated architecture with the following roles:<br>
<ul>
  <li><b>Leader:</b> Handles all write requests and synchronizes updates with followers.</li>
  <li><b>Followers:</b> Handle read requests and participate in consensus (e.g., Paxos or Zab protocol).</li>
  <li><b>Clients:</b> Applications or systems interacting with ZooKeeper for coordination.</li>
</ul>

## Benefits:
<ul>
  <li>Simplifies the development of distributed applications.</li>
  <li>Ensures reliable and consistent coordination.</li>
  <li>Handles challenges like partial failures and synchronization in distributed environments.</li>
</ul>
  
## Integration:
ZooKeeper is widely used in technologies like:
<ul>
  <li>Apache Kafka (to maintain broker metadata and consumer group offsets).</li>
  <li>For coordination between brokers in the cluster of ActiveMQ Artemis</li>
  <li>Apache Hadoop and HBase (for resource coordination).</li>
  <li>Apache Storm and other distributed systems.</li>
</ul>

## Key Points:
<ul>
  <li type="square">Zookeeper is designed in a way that u can model your problem in terms of zookeeper.</li>
  <li type="square">Zookeeper server can run in <b>standalone</b> mode or <b>multi-node-cluster</b> mode.
    <ul>
      <li type = "circle"><b>Standalone</b> mode means when only one server is running.</li>
      <li type = "circle"><b>Multi-node-cluster</b> mode means when multiple servers are running.</li>
    </ul></li>
  <li type="square"><b>Installation and running zookeeper (Standalone mode):</b>
    <ol>
      <li>After downloading zookeeper and extracting it, we need to rename conf/zoo_sample.cfg file to conf/zoo.cfg because Zookeeper treats this as default config file.</li> 
      <li>In zoo.cfg file, we have to change the value of <b>dataDir</b> (E.g., dataDir=C:/Softwares/zookeeper/apache-zookeeper-3.8.4/data)</li>
      <li>Go to bin folder and run command <b>zkServer.cmd</b> to tun the zookeeper server.</li>
      <li>If we run <b>start zkServer.cmd</b> then this will launch ZooKeeper in a new command prompt window.</li>
      <li>Now if we want to specify configuration file in the command then the command is <b>bin\zkServer.cmd conf\zoo.cfg</b> or <b>zkServer.cmd "C:\Softwares\zookeeper\apache-zookeeper-3.8.4\conf\zoo.cfg"</b>. Specifying configuration file is optional but it will be required while starting multiple servers with different config files to form multi-node-cluster (called Zookeeper Ensemble).</li>
      <li>If this error is encountered while executing command which contains zoo.cfg => <b>java.lang.NumberFormatException: For input string: "C:zookeeper\zookeeper3.8.4\bin\..\conf\zoo.cfg"</b> then open the zkServer.cmd and add this line : <b>if not "%1"=="" (set ZOOCFG=%1)</b> in zkServer.cmd. Then find this line : <br><b><p>call %JAVA% "-Dzookeeper.log.dir=%ZOO_LOG_DIR%" "-Dzookeeper.log.file=%ZOO_LOG_FILE%" "-XX:+HeapDumpOnOutOfMemoryError" "-XX:OnOutOfMemoryError=cmd /c taskkill /pid %%%%p /t /f" -cp "%CLASSPATH%" %ZOOMAIN% "%ZOOCFG%" %*</p></b> Delete <b>%*</b> from the end. For explanation see : https://stackoverflow.com/questions/11765015/zookeeper-not-starting</li>
      <li>Zookeeper client can be run using the command <b>zkCli.cmd -server localhost:2181</b></li>
    </ol><br>
    <img src="https://github.com/aman-1998/Zookeeper_Concepts/blob/main/images/zookeeper_client_server.png"/>
  </li>
  <li type="square"><b>Running zookeeper in multi-node-cluster mode:</b>
    <ol>
      <li>Minimum recommended node is 3. Most common production setup is 5 nodes.</li>
      <li>The number of nodes should be odd.</li>
      <li>The replicated group od servers is called as <b>quorum</b>.</li>
      <li>One of the server acts as leader and other servers act as followers. As soon as leader fails new leader is elected.</li>
      <li>Create conf1/zoo.cfg, conf2/zoo.cfg and conf3/zoo.cfg. And then create data1, data2 and data3 folders and mention it in respective zoo.cfg for 3 servers. Then make <b>myid</b> file <b>(no extention)</b> in data1, data2 and data3 folders and write 1, 2 & 3 in it respectively.</li>
      <li>We have to <b>mention peer to peer connection port</b> and <b>the port of leader election</b> for all the 3 servers in zoo.cfg so that all the servers know about each other. For example server.1=localhost:2887:3887, server.2=localhost:2888:3888 & server.3=localhost:2889:3889</li>
      <li>Run the servers using command: <b>start bin\zkServer.cmd conf1\zoo.cfg</b> and <b>start bin\zkServer.cmd conf2\zoo.cfg</b> and <b>start bin\zkServer.cmd conf3\zoo.cfg</b></li>
      <li>Zookeeper client can be run using the command <b>zkCli.cmd -server localhost:2181,localhost:2182,localhost:2183</b></li>
      <li>Zookeeper client will not give error untill majority of the servers are running.</li>
      <li>Status of each server can be seen on http based interface hosted by Zookeeper Admin server. This admin port can be set in zoo.cfg using <b>admin.serverPort=8081</b>. E.g., http://localhost:8081/commands, http://localhost:8082/commands, http://localhost:8083/commands for server1, server2 and server3 respectively. If you don't configure admin.serverPort in zoo.cfg then by default it is 8080.</li>
    </ol><br>
    <img src="https://github.com/aman-1998/Zookeeper_Concepts/blob/main/images/Zookeeper_Multi_Node_Server.png"/>
  </li>
  <li type="square"><b>Zookeeper data model:</b> 
    <ol>
      <li>Each node is called is called a Znode.</li>
      <li>The data that can be stored in a Znode is of type string</li>
      <li>Maximum size data that can be stored in a Znode is 1 Mb. But general recommendation is that we should store much smaller amount of data compared to 1 Mb.</li>
      <li>Path: / separated path from root node to the present node. All characters expected except dot(.)</li>
      <li>Access control list: Allowed users/groups</li>
      <li>Stats Information: Version number, timestamps, etc</li>
      <li>After connecting zookeeper client, we can write commands using zookeeper-client to create Znode.</li>
      <li>Types of Znode:</li>
      <ul>
        <li type="circle"><b>Persistent non-sequqntial:</b> Example - create /dummyNode "This is my data"</li>
        <li type="circle"><b>Persistent sequential:</b> Example - create -s /dummyNode "This is my data"</li>
        <li type="circle"><b>Ephimeral non-sequential:</b> create -e /dummyNode "This is my data"</li>
        <li type="circle"><b>Ephimeral sequential:</b> create -e -s /dummyNode "This is my data"</li>
      </ul>
    </ol><br>
    <img src="https://github.com/aman-1998/Zookeeper_Concepts/blob/main/images/Znode_datamodel.png"/>
  </li>
  <li type="square"><b>Applications of Zookeeper:</b>
    <ol>
      <li type="1"><b>Cluster Monitoring: </b>We have 3 application servers. And we have a Cluster-monitoring server. Now, if any one of the application server will goes down then Cluster-monitor should be able to detect it. Also whenever a new server joins the cluster, the Cluster-monitoring server should be able to detect. This can be achieved using Zookeeper as cluster management service. Whenever an apllication server is up, it creates a ephemeral znode <b>eg., /members/server-1 or /members/server-2, etc</b>. The cluster monitor will watch <b>/members</b> as a result it will detect whenever a application server is up or down.<br>
      <img src="https://github.com/aman-1998/Zookeeper_Concepts/blob/main/images/Cluster_monitor.png"/>
      </li>
      <li type="1"><b>Distributed Lock: </b>When there are more than one application servers and there is a common resource that will be accessed by every application server (one at a time basis). In such cases, Zookeeper can be used. Every application will create a ephemeral sequential node <b>e.g., /lock/lock-1 or /locks/lock-2, etc</b>. Now, we can have a consensus among ourselves that which ever application server can create a node with lowest value will get the access to the resource.<br>
      <img src="https://github.com/aman-1998/Zookeeper_Concepts/blob/main/images/Distributed_Lock_1.png"/><br>
      <img src="https://github.com/aman-1998/Zookeeper_Concepts/blob/main/images/Distributed_Lock_2.png"/>
      </li>
      <li type="1"><b>Distributed Queue: </b>We have a queue and there are more than one consumers. Now when all the consumers are in action then no two consumer can consume same element from queue. We can achieve this using the concept of <b>Sequential Node</b> and <b>Distributed Locks</b>.<br>
      <img src="https://github.com/aman-1998/Zookeeper_Concepts/blob/main/images/Distributed_Queue.png"/>
      </li>
    </ol>
  </li>
</ul>
