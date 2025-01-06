package personal.learning.zk.test;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import personal.learning.zk.queue.Queue;
import personal.learning.zk.queue.impl.NoLockQueue;

public class Producer {
	
	//private static final Logger log = LoggerFactory.getLogger(Producer.class);
	
	public static void main(String[] args) throws Exception {
		
		// Connect to the ZooKeeper instance running on localhost:2181
		ZooKeeper zk = new ZooKeeper("localhost:2181", 20000, null);
		
		// Create a distributed queue and enqueue items
		Queue queue = new NoLockQueue(zk, "/queue");
		
		int i = 0;
        while (i < Integer.MAX_VALUE){
        	System.out.println("=========> Producing: dummyData"+i);
            queue.enqueue("dummyData"+i);
            Thread.sleep(1000);
            i++;
        }
        
        zk.close();
	}

}
