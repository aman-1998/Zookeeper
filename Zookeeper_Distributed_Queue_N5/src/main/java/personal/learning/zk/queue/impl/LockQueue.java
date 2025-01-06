package personal.learning.zk.queue.impl;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import personal.learning.zk.queue.Queue;
import personal.learning.zk.util.DistributedLock;
import personal.learning.zk.util.Helper;

public class LockQueue implements Queue {

	private ZooKeeper zk;
	private String queuePath;
	
	public LockQueue(ZooKeeper zk, String queuePath) {
		this.zk = zk;
		this.queuePath = queuePath;
		Helper.createNodeIfNotExists(zk, queuePath, "Dummy data of no use", CreateMode.PERSISTENT);
	}

	@Override
	public void enqueue(String item) throws Exception {
		byte[] data = item.getBytes();
		zk.create(queuePath+"/item", data, OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
	}

	@Override
	public String dequeue() throws Exception {
		
		while(true) {
			
			List<String> children = zk.getChildren(queuePath, false);
			if(children.isEmpty()) {
				return null;
			}
			
			// Sort the list of item nodes in ascending order
			children = children.stream().sorted().collect(Collectors.toList());
			
			for(String node : children) {
				boolean lockAcquired = false;
				DistributedLock lock = null;
				try {
					String nodePath = queuePath + "/" + node;
					lock = new DistributedLock(zk, queuePath);
					lockAcquired = lock.lock();
					
					// Check if the node still exists before processing it
                    if (lockAcquired && zk.exists(nodePath, false) != null) {

                        // Get the item data from the item node
                        byte[] data = zk.getData(nodePath, false, null);
                        String item = new String(data);

                        // Delete the item node to dequeue the item
                        zk.delete(nodePath, -1);
                        return item;
                    }
					
				} catch(Exception ex) {
					ex.printStackTrace();
				} finally {
					if(lockAcquired && lock != null) {
						lock.unlock();
					}
				}
			}
		}
	}

}
