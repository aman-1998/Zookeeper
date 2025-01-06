package personal.learning.zk.util;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class Helper {
	
	public static void createNodeIfNotExists(ZooKeeper zk, String nodePath, String data, CreateMode createMode) {
		
		try {
			if(zk.exists(nodePath, false) == null) {
				zk.create(nodePath, data.getBytes(), OPEN_ACL_UNSAFE, createMode);
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
