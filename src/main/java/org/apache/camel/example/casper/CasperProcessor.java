package org.apache.camel.example.casper;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CasperProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

	Map<String,Object> map = 	exchange.getMessage().getHeaders();
	

	for (String key : map.keySet()) {
        System.out.println(key + ":" + map.get(key));
    }
	
	}

}
