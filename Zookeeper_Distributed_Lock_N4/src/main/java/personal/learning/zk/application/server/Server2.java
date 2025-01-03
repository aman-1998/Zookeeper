package personal.learning.zk.application.server;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server2 {
	
private static final Logger log = LoggerFactory.getLogger(Server2.class);
	
	private static String ROOT_NODE = "/locks";
	
	private static ZooKeeper zk = null;
	
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        
    	String id = UUID.randomUUID().toString();
    	log.info("My id: " + id);
    	
    	Watcher watcher = new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				if(event.getType() == Event.EventType.NodeChildrenChanged) {
					try {
						getLockOrWait(id);
					} catch(KeeperException ex) {
						throw new RuntimeException(ex);
					} catch(InterruptedException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
    	};
    	
    	zk = new ZooKeeper("localhost:2181", 20000, watcher);
    	
    	if(zk.exists(ROOT_NODE, false) == null) {
			zk.create(ROOT_NODE, "data".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);
		}
    	
    	zk.create(ROOT_NODE+"/"+"lock-", id.getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, null);
    	
    	getLockOrWait(id);
    }

	protected static void getLockOrWait(String id) throws KeeperException, InterruptedException {
		
		List<String> children = zk.getChildren(ROOT_NODE, false);
		children = children.stream().sorted().collect(Collectors.toList());
		byte[] data = zk.getData(ROOT_NODE+"/"+children.get(0), false, null);
		
		if(StringUtils.equals(new String(data), id)) {
			log.info("========> Server with id["+id+"] aquired the lock and will be released after 10 secs.");
			Thread.sleep(10000);
			zk.delete(ROOT_NODE+"/"+children.get(0), -1);
			log.info("========> Lock is released");
		} else {
			log.info("~~~~~~~~~> Server with id["+id+"] can't aquire lock. So, will wait.");
			zk.getChildren(ROOT_NODE, true);
		}
		
		Thread.sleep(1800000);
	}
}
