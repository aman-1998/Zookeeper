package personal.learning.zk.application.server;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.io.IOException;
import java.util.UUID;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server1 {
	
	private static final Logger log = LoggerFactory.getLogger(Server1.class);
	
	private static String MEMBERS_NODE = "/memebers";
	
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        
    	String id = UUID.randomUUID().toString();
    	log.info("My id: " + id);
    	
    	ZooKeeper zk = new ZooKeeper("localhost:2181", 15000, null);
    	
    	String creationResponse = zk.create(MEMBERS_NODE+"/"+id, 
							    			("Data : id "+id).getBytes(), 
							    			OPEN_ACL_UNSAFE, 
							    			CreateMode.EPHEMERAL, null);
    	
    	log.info(creationResponse);
    	
    	Thread.sleep(1800000); // 30 mins
    }
}
