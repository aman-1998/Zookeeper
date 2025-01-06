package personal.learning.zk.queue.impl;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import personal.learning.zk.queue.Queue;
import personal.learning.zk.util.Helper;

public class NoLockQueue implements Queue {
	
	private ZooKeeper zk;
	private String queuePath;
	
	public NoLockQueue(ZooKeeper zk, String queuePath) {
		this.zk = zk;
		this.queuePath = queuePath;
		Helper.createNodeIfNotExists(zk, queuePath, "Dummy data of no use", CreateMode.PERSISTENT);
	}

	@Override
	public void enqueue(String item) throws Exception {
		// Create an item node under the queue node with the item data
		byte[] data = item.getBytes();
		zk.create(queuePath+"/item", data, OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
	}

	@Override
	public String dequeue() throws Exception {
		
		List<String> children = zk.getChildren(queuePath, false);
		if(children.isEmpty()) {
			return null;
		}
		
		// Sort the list of item nodes in ascending order
		children = children.stream().sorted().collect(Collectors.toList());
		
		String nodePath = queuePath + "/" + children.get(0);
		byte[] data = zk.getData(nodePath, false, null);
		String item = new String(data);
		zk.delete(nodePath, -1);
		return item;
	}

}
