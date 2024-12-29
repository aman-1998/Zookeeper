package personal.learning.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyWatcher implements Watcher {
	
	private static final Logger log = LoggerFactory.getLogger(Zookeeper_Client.class);

	@Override
	public void process(WatchedEvent event) {
		
		log.info("**********************************************");
		log.info("Got the event for node : " + event.getPath());
		log.info("The event type : " + event.getType());
		log.info("**********************************************");
	}

}
