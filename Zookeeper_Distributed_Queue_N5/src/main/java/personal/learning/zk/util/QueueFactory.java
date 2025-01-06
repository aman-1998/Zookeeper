package personal.learning.zk.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.ZooKeeper;

import personal.learning.zk.queue.Queue;
import personal.learning.zk.queue.impl.LockQueue;
import personal.learning.zk.queue.impl.NoLockQueue;

public class QueueFactory {
	
	private ZooKeeper zk;
	
	private String queuePath;
	
	public QueueFactory(ZooKeeper zk, String queuePath) {
		this.zk = zk;
		this.queuePath = queuePath;
	}
	
	public Queue getQueue(String lockType) {
		if(StringUtils.equalsIgnoreCase(lockType, "LOCK")) {
			return new LockQueue(zk, queuePath);
		}
		return new NoLockQueue(zk, queuePath);
	} 
}
