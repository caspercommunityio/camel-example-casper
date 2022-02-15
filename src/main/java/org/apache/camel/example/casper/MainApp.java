package org.apache.camel.example.casper;

import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class MainApp {
   
	/**
	 * 
	 * @param args : main rags
	 * @throws Exception : exception
	 */
   public static void main(String...args) throws Exception
   {
		   
      Main main = new Main();

      main.configure().addRoutesBuilder(new CasperRouteBuilder());
      main.run(args);
   }
}
