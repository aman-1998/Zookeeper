package personal.learning.zk;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To test:
 * ---------
 * 1. First run all servers (i.e., server1, server2 & server3)
 * 2. Then run Cluster_monitor
 * 3. Then terminate any server
 */
public class Cluster_monitor {
	
	private static final Logger log = LoggerFactory.getLogger(Cluster_monitor.class);
	
	private static String MEMBERS_NODE = "/memebers";
	
	private static ZooKeeper zk;
	
	private static List<String> children = new ArrayList<>();
	
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		
		zk = new ZooKeeper("localhost:2181", 15000, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				log.info("*****************************************");
				log.info("Got the event for node : " + event.getPath());
				log.info("Event type : " + event.getType());
				log.info("*****************************************");
				
				try {
					List<String> newChildrenList = startWatch();
					for(String child1 : children) {
						boolean flag = false;
						for(String child2 : newChildrenList) {
							if(child2.equals(child1)) {
								flag = true;
								break;
							}
						}
						
						if(flag == false) {
							log.info("Server with id [" + child1 + "] is down or crashed");
						}
					}
				} catch (KeeperException e) {
					log.info("Failed to set watcher");
					e.printStackTrace();
				} catch (InterruptedException e) {
					log.info("Failed to set watcher");
					e.printStackTrace();
				}
			}
		});
		
		if(zk.exists(MEMBERS_NODE, false) == null) {
			zk.create(MEMBERS_NODE, "data".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
		}
		
		children = startWatch();
		
		Thread.sleep(1800000);
	}
	
	private static List<String> startWatch() throws KeeperException, InterruptedException {
		
		List<String> children = new ArrayList<>();
		if(zk != null) {
			children = zk.getChildren(MEMBERS_NODE, true, null);
			log.info("List of children : ");
			children.forEach(child -> log.info(child + " | "));
		}
		return children;
	}
}
