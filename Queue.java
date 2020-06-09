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

/*
* Copyright 2020 EUROPEAN UNION  
* Licensed under the EUPL, Version 1.2 or - as soon as they will be approved by 
* the European Commission - subsequent versions of the EUPL (the "Licence").  
* You may not use this work except in compliance with the Licence.  
* You may obtain a copy of the Licence at:
* 
* https://ec.europa.eu/isa2/solutions/european-union-public-licence-eupl_en
* 
* Unless required by applicable law or agreed to in writing, software 
* distributed under the Licence is distributed on an "AS IS" basis, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
* See the Licence for the specific language governing permissions and 
* limitations under the Licence.
* 
* Date: 2020/06/08  
* Authors: 
* European Commission, Joint Research Centre - jrc-inspire-support@ec.europa.eu
* 
* This work was supported by the Interoperability solutions for public 
* administrations, businesses and citizens programme (http://ec.europa.eu/isa2)
* through Action 2016.10: 
* European Location Interoperability Solutions for e-Government (ELISE)
*/