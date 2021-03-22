package tp1;

import java.awt.FlowLayout;
import java.awt.event.*;
import java.nio.ByteBuffer;

import javax.swing.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {
	  private final static String QUEUE_NAME1 = "t1";
	  private final static String QUEUE_NAMEI1 = "i1";
	  private final static String QUEUE_NAME2 = "t2";
	  private final static String QUEUE_NAMEI2 = "i2";
	  
	  JLabel l1,l2;  
		 JTextArea area1;  
		 JTextArea area2; 
		 final JFrame frame = new JFrame("App");
		Consumer() {  
	        area1 = new JTextArea(10, 20);
	        area2 = new JTextArea(10, 20);
	        JScrollPane sp = new JScrollPane(area1);
	        JScrollPane sp2 = new JScrollPane(area2);
	 
	        frame.setLayout(new FlowLayout());
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(300, 440);
	        frame.getContentPane().add(sp);
	        frame.getContentPane().add(sp2);
	 
	        frame.setVisible(true); 
		} 
		
		public static String addChar(String str, String ch, int position) {
			if(ch.equals(""))
				return str.substring(0, position)+ str.substring(position+1);
		    return str.substring(0, position) + ch + str.substring(position);
		}
		static String msg1 = "";
		static String msg2 ="";
		static int i1 =0;
		static int i2 =0;

	  public static void main(String[] argv) throws Exception {
		  
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    
	    Consumer c = new Consumer();

	    channel.queueDeclare(QUEUE_NAME1, false, false, false, null);
	    System.out.println(" [*2] Waiting for messages2. To exit press CTRL+C");
	    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x2] Received '" + message + "'");
	        msg1=message;
	    };
	    channel.basicConsume(QUEUE_NAME1, true, deliverCallback1, consumerTag -> { });
	    
	    channel.queueDeclare(QUEUE_NAMEI1, false, false, false, null);
	    System.out.println(" [*1] reading ind");
	    
	    DeliverCallback deliverCallbacki1 = (consumerTag, delivery) -> {
	    	String message = new String(delivery.getBody(), "UTF-8");
	    	
	    	System.out.println(" [x1] recieved index '" + message + "'");
	    	i1 = Integer.parseInt(message);
	    	String text1 = c.area1.getText();
	    	c.area1.setText(addChar(text1,msg1,i1));
	    };
	    channel.basicConsume(QUEUE_NAMEI1, true, deliverCallbacki1, consumerTag -> { });
	    
	    
	    //------------------------------------------------------------------next producer
	    
	    
	    channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
	    System.out.println(" [*2] Waiting for messages2. To exit press CTRL+C");
	    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x2] Received '" + message + "'");
	        //c.area2.append(message + "\n");
	        msg2=message;
	    };
	    channel.basicConsume(QUEUE_NAME2, true, deliverCallback2, consumerTag -> { });
	    
	    channel.queueDeclare(QUEUE_NAMEI2, false, false, false, null);
	    System.out.println(" [*1] reading ind");
	    
	    DeliverCallback deliverCallbacki2 = (consumerTag, delivery) -> {
	    	String message = new String(delivery.getBody(), "UTF-8");
	    	
	    	System.out.println(" [x1] recieved index '" + message + "'");
	    	i2 = Integer.parseInt(message);
	    	String text2 = c.area2.getText();
	    	c.area2.setText(addChar(text2,msg2,i2));
	    };
	    channel.basicConsume(QUEUE_NAMEI2, true, deliverCallbacki2, consumerTag -> { });
	    
	  }
}
