package org.apache.camel.example.casper;

import javax.xml.bind.JAXBContext;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.casper.CasperConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import org.apache.camel.model.dataformat.JaxbDataFormat;

import com.syntifi.casper.sdk.model.auction.AuctionState;
import com.syntifi.casper.sdk.model.block.JsonBlock;
import com.syntifi.casper.sdk.model.era.JsonEraValidators;
import com.syntifi.casper.sdk.model.stateroothash.StateRootHashData;
import org.apache.camel.component.jackson.JacksonDataFormat;


public class Test {

	private static void loadroute1(CamelContext cntxt, ProducerTemplate temp) throws Exception {
		cntxt.addRoutes(new RouteBuilder() {
			public void configure() throws Exception {
				from("direct:" + CasperConstants.STATE_ROOT_HASH).routeId("STATE_ROOT_HASH")
						.to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.STATE_ROOT_HASH)
						.process(new Processor() {

							@Override
							public void process(Exchange exchange) throws Exception {
								StateRootHashData state = (StateRootHashData) exchange.getIn().getBody();
								System.err.println("* Current STATE_ROOT_HASH is : " + state.getStateRootHash());
							}
						});
			}
		});

		cntxt.start();
		temp.sendBody("direct:" + CasperConstants.STATE_ROOT_HASH, "This is a test message");
		cntxt.stop();

	}

	private static void loadroute2(CamelContext cntxt, ProducerTemplate temp) throws Exception {
		cntxt.addRoutes(new RouteBuilder() {
			public void configure() throws Exception {
				from("file:src/main/resources/datas/?fileName=get_block.txt&charset=utf-8&noop=true")
						.convertBodyTo(String.class).routeId(CasperConstants.BLOCK).setHeader("BLOCK_HASH", body())
						.to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.BLOCK)

						.process(new Processor() {

							@Override
							public void process(Exchange exchange) throws Exception {
								JsonBlock block = (JsonBlock) exchange.getIn().getBody();
								String blockHash = (String) exchange.getIn().getHeader("BLOCK_HASH");
								System.err.println("* getBlock was called with parameter block Hash = " + blockHash);
								System.err.println("* getBlock retrieved a block that was minted at era ="
										+ block.getHeader().getEraId() + " and has as parent hash = "
										+ block.getHeader().getParentHash());

							}
						});

			}

		});

		cntxt.start();
		Thread.sleep(5000);
		cntxt.stop();

	}

	
	private static void loadroute3(CamelContext cntxt, ProducerTemplate temp) throws Exception {
		cntxt.addRoutes(new RouteBuilder() {
			public void configure() throws Exception {
				
			
				
				// JSON Data Format
				//JacksonXMLDataFormat jsonDataFormat = new JacksonXMLDataFormat(JsonEraValidators.class);

				// JSON Data Format
				JacksonDataFormat jsonDataFormat = new JacksonDataFormat(JsonEraValidators.class);

				
				from("file:src/main/resources/datas/?fileName=get_auction_info.txt&charset=utf-8&noop=true")
						.convertBodyTo(String.class).routeId(CasperConstants.AUCTION_INFO).setHeader("BLOCK_HEIGHT", body())
						.to("casper:http://65.21.227.180:7777/?operation=" + CasperConstants.AUCTION_INFO)

						.process(new Processor() {

							@Override
							public void process(Exchange exchange) throws Exception {
								AuctionState auctionState = (AuctionState) exchange.getIn().getBody();
								String blockHeight = (String) exchange.getIn().getHeader("BLOCK_HEIGHT");
								System.err.println("* getAuctionUnfo was called with parameter block height = " + blockHeight);
								exchange.getOut().setBody(auctionState.getEraValidators());
								System.err.println("* Save validators of this Auction into a json file under path src/main/resources/datas/");
							}
						}).marshal(jsonDataFormat).to("file:src/main/resources/datas/?fileName=era_validators.txt")
						;
			}

		});

		cntxt.start();
		Thread.sleep(5000);
		cntxt.stop();

	}
	
	
	
	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		ProducerTemplate template = context.createProducerTemplate();
		
	
		System.err.println(
				"------------------------------- this route performs a call to getStaterouteHash and print the current STATE_ROOT_HASH to console ---------------------------------");
		loadroute1(context, template);
		System.err.println(
				"------------------------------- This route reads a block hash from a file, performs a call to getBlock with the block hash-------------------------------------------");
		loadroute2(context, template);

		
		System.err.println(
				"------------------------------- This route reads a block heigh from a file, performs a call to getAuctionInfo with the block height-------------------------------------------");
		System.err.println(
				"------------------------------- Then saves the validator slot of the auction to a json file------------------------------------------------------------------------------------");
	
		
		loadroute3(context, template);
		
	}

}
