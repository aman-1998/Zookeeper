package personal.learning.zk.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import personal.learning.zk.queue.Queue;
import personal.learning.zk.util.Helper;
import personal.learning.zk.util.QueueFactory;

public class Consumer3 {
	
	//private static final Logger log = LoggerFactory.getLogger(Consumer3.class);
	
		public static void main(String[] args) throws Exception {
			
			String lockType = StringUtils.EMPTY;
			if(args.length > 0) {
				lockType = args[0];
				if(!StringUtils.equalsIgnoreCase(lockType, "LOCK")
						&& !StringUtils.equalsIgnoreCase(lockType, "NOLOCK")) {
					System.out.println("Invalid arguments");
					System.exit(1);
				}
			}
			
			// Connect to the ZooKeeper instance running on localhost:2181
	        ZooKeeper zk = new ZooKeeper("localhost:2181", 20000, null);
	        
	        Helper.createNodeIfNotExists(zk, "/queue", "Dummy data of no use", CreateMode.PERSISTENT);
	        
	        if(StringUtils.equalsIgnoreCase(lockType, "LOCK")) {
	        	Helper.createNodeIfNotExists(zk, "/lock", "Dummy data of no use", CreateMode.PERSISTENT);
	        }
	        
	        QueueFactory queueFactory = new QueueFactory(zk, "/queue");
	        
	        // Create a distributed queue
	        Queue queue = queueFactory.getQueue(lockType);
	        
	        while (true){
	            String val = queue.dequeue();
	            if(val != null){
	            	System.out.println("========> Consumed by consumer3: " + val);
	            }
	        }
		}
	
}
