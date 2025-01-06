package personal.learning.zk.queue;

public interface Queue {
	
	void enqueue(String item) throws Exception;
	String dequeue() throws Exception;
}
