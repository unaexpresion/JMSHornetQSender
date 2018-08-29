package org.hornetq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;
 

public class QSender
{
   public static void main(String[] args) throws Exception
   {
      Connection connection = null;
      InitialContext initialContext = null;
      try
      {
         //Step 1. Create an initial context to perform the JNDI lookup.
         final Properties env = new Properties();
 
         env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
 
         env.put(Context.PROVIDER_URL, "remote://localhost:4447");
 
         env.put(Context.SECURITY_PRINCIPAL, "testuser");
 
         env.put(Context.SECURITY_CREDENTIALS, "Admin123*");
 
         initialContext = new InitialContext(env);
 
         //Step 2. Perfom a lookup on the queue
         Queue queue = (Queue) initialContext.lookup("jms/queue/myQueue");
 
         //Step 3. Perform a lookup on the Connection Factory
         ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("jms/RemoteConnectionFactory");
 
         //Step 4.Create a JMS Connection
         connection = cf.createConnection("testuser", "Admin123*");
 
         //Step 5. Create a JMS Session
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
         //Step 6. Create a JMS Message Producer
         MessageProducer producer = session.createProducer(queue);
 
         //Step 7. Create a Text Message and set the color property to blue
         TextMessage blueMessage = session.createTextMessage("This is a text message");
 
         blueMessage.setStringProperty("color", "BLUE");
 
         System.out.println("Sent message: " + blueMessage.getText() + " color=BLUE");
 
         //Step 8. Send the Message
         for (int i=0; i<1000; i++) {
        	 producer.send(blueMessage);
         }
 
         //Step 9. create another message and set the color property to red
         /*TextMessage redMessage = session.createTextMessage("This is a text message");
 
         redMessage.setStringProperty("color", "RED");
 
         System.out.println("Sent message: " + redMessage.getText() + " color=RED");
 
         //Step 10. Send the Message
         for (int i=0; i<200; i++) {
        	 producer.send(redMessage);
         }
          //Step 10,11 and 12 in MDBMessageSelectorExample*/
      }
      finally
      {
         //Step 13. Be sure to close our JMS resources!
         if (initialContext != null)
         {
            initialContext.close();
         }
         if(connection != null)
         {
            connection.close();
         }
      }
   }
}
