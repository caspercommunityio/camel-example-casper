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
   public void configure() throws Exception
   {
      //add one route per RPC call


      /**
       * Call RPC command with hardcoded parameters
       * Uncomment some of them to test it
       */
      from("timer://simpleTimer?period=5000")
      .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.ACCOUNT_INFO + "&publicKey=017d9aa0b86413d7ff9a9169182c53f0bacaa80d34c211adab007ed4876af17077&blockHeight=530214")
      .log("call " + CasperConstants.ACCOUNT_INFO + " with params : blockHeight=530214 and publicKey=017d9aa0b86413d7ff9a9169182c53f0bacaa80d34c211adab007ed4876af17077   gives result = ${body}");
      // from("timer://simpleTimer?period=5000")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.LAST_BLOCK)
      // .log("Call " + CasperConstants.LAST_BLOCK + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.NETWORK_PEERS)
      // .log("Call " + CasperConstants.NETWORK_PEERS + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.NODE_STATUS)
      // .log("Call " + CasperConstants.NODE_STATUS + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.BLOCK + "&blockHash=3c897ffdd4c07761c6057126ff78a1f8da83e63458aa7561e2bf57514c4d0150")
      // .log("Call " + CasperConstants.BLOCK + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.LAST_BLOCK_TRANSFERS)
      // .log("Call " + CasperConstants.LAST_BLOCK_TRANSFERS + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.BLOCK_TRANSFERS)
      // .log("Call " + CasperConstants.BLOCK_TRANSFERS + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.STATE_ROOT_HASH)
      // .log("Call " + CasperConstants.STATE_ROOT_HASH + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.AUCTION_INFO)
      // .log("Call " + CasperConstants.AUCTION_INFO + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.ERA_INFO + "&blockHeight=530214")
      // .log("Call " + CasperConstants.ERA_INFO + " gives : ${body}")
      // .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.RPC_SCHEMA)
      // .log("Call " + CasperConstants.RPC_SCHEMA + " gives : ${body}");


      /**
       * Read data from file and use the content to make an RPC query on the blockchain
       */
      from("file:src/main/resources/datas/?fileName=get_block.txt&charset=utf-8&noop=true")
      .convertBodyTo(String.class )
      .log("${body}")
      .setHeader("BLOCK_HASH", body())
      .to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.BLOCK)
      .log("${body}");
   }
}
