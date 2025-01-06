package personal.learning.zk.util;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class DistributedLock {
	
	private ZooKeeper zk;
	
	private String nodePath;
	
	private String lockNode;

	public DistributedLock(ZooKeeper zk, String queuePath) {
		this.zk = zk;
		this.nodePath = "/lock" + queuePath;
		Helper.createNodeIfNotExists(zk, "/lock", "Dummy data of no use", CreateMode.PERSISTENT);
		Helper.createNodeIfNotExists(zk, nodePath, "Dummy data of no use", CreateMode.PERSISTENT);
	}
	
	public boolean lock() throws KeeperException, InterruptedException {
		// Create an ephemeral sequential znode under the lock znode
		lockNode = zk.create(nodePath+"/lock-", "Data of no use".getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		
		// Get the list of all znodes under the lock znode
		List<String> children = zk.getChildren(nodePath, false);
		
		children = children.stream().sorted().collect(Collectors.toList());
		
		int index = children.indexOf(lockNode.substring(lockNode.lastIndexOf("/") + 1));
		
		if(index == 0) {
			return true;
		} else {
			zk.delete(lockNode, -1);
			return false;
		}
	}
	
	public void unlock() throws InterruptedException, KeeperException {
        zk.delete(lockNode, -1);
    }

}
