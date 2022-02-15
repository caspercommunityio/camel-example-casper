/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.casper;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.casper.CasperConstants;

/**
 * Server route
 */
public class CasperRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
    	
    	from("file:src/main/resources/datas/?fileName=get_block.txt&charset=utf-8&noop=true").convertBodyTo(String.class)
    		      .log("${body}")
    		      .setHeader("BLOCK_HASH",body())
    		      .to("casper:http://65.21.227.180:7777/?operation="+CasperConstants.BLOCK)
    		    		  .log("${body}")
    		      ;
    
    	
    	//add one route per RPC call
    	
    	//GET_ACCOUNT_INFO
    	// from("timer://simpleTimer?period=5000")
	     // .to("casper:http://65.21.227.180:7777/?operation="+CasperConstants.ACCOUNT_INFO)
	     // .log("call "+CasperConstants.ACCOUNT_INFO +" with params : blockHeight=530214 and publicKey=017d9aa0b86413d7ff9a9169182c53f0bacaa80d34c211adab007ed4876af17077   gives result = ${body}");
	   
	   
}
}