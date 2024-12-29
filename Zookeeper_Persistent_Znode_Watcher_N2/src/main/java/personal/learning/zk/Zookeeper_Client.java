package personal.learning.zk;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Zookeeper_Client {
	
	private static final Logger log = LoggerFactory.getLogger(Zookeeper_Client.class);
	
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        
    	DummyWatcher dummyWatcher = new DummyWatcher();
    	
    	ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 15000, dummyWatcher);
    	
    	zookeeper.create("/duck", "My dummy data".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
    	zookeeper.create("/duck/fox", "My dummy data for fox".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
    	zookeeper.create("/duck/fox/tiger", "My dummy data for fox".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
    	zookeeper.create("/duck/racoon", "My dummy data for fox".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
    	
    	Stat stat = new Stat();
    	var data = zookeeper.getData("/duck", true, stat);
    	log.info("Data: " + new String(data));
    	log.info("Version: " + stat.getVersion());
    	
    	List<String> children = zookeeper.getChildren("/duck", true);
    	children.forEach(child -> log.info("Child: " + child));
    	
    	zookeeper.setData("/duck", "This is new data".getBytes(), -1);
    	
    	data = zookeeper.getData("/duck", true, stat);
    	log.info("Data: " + new String(data));
    	
    	
    	zookeeper.delete("/duck/fox/tiger", -1);
    	log.info("Deleted: /duck/fox/tiger");
    	
    	zookeeper.delete("/duck/fox", -1);
    	log.info("Deleted: /duck/fox");
    	
    	children = zookeeper.getChildren("/duck", true);
    	
    	zookeeper.delete("/duck/racoon", -1);
    	log.info("Deleted: /duck/racoon");
    	
    	zookeeper.delete("/duck", -1);
    	log.info("Deleted: /duck");
    }
}
