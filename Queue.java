package inspire.validator;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

public class Queue {
	public RequestConfig config;
	public CloseableHttpClient client;
	private AtomicInteger size = new AtomicInteger();
	
	public synchronized boolean incIfSmaller(int limit) {
		if (size.get() < limit) {
			size.incrementAndGet();
			return true;
		}
		else
			return false;
	}
	
	public void decrement() {
		size.decrementAndGet();
	}
	
	private static HashMap<Integer, Queue> queues = new HashMap<Integer, Queue>();
	
	public static Queue get(int nr) {
		return queues.get(nr);
	}
	
	public static Queue init(int nr) {
		Queue queue = new Queue();
		queues.put(nr, queue);
		return queue;
	}	
}

