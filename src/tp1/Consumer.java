package tp1;

import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {
	  private final static String QUEUE_NAME1 = "t1";
	  private final static String QUEUE_NAME2 = "t2";
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

	  public static void main(String[] argv) throws Exception {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    //factory.setUsername("chadha");
	    //factory.setPassword("1234");
	    //factory.setVirtualHost("/");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    
	    Consumer c = new Consumer();

	    channel.queueDeclare(QUEUE_NAME1, false, false, false, null);
	    System.out.println(" [*1] Waiting for messages1. To exit press CTRL+C");
	    
	    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
	    	int k = delivery.getBody()[0];
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x1] Received '" + message + "'");
	        //c.area1.append(message + "\n");
	        c.area1.setText(message+k);
	    };
	    channel.basicConsume(QUEUE_NAME1, true, deliverCallback1, consumerTag -> { });
	    
	    
	    channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
	    System.out.println(" [*2] Waiting for messages2. To exit press CTRL+C");
	    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x2] Received '" + message + "'");
	        //c.area2.append(message + "\n");
	        c.area2.setText(message);
	    };
	    channel.basicConsume(QUEUE_NAME2, true, deliverCallback2, consumerTag -> { });
	  }
}
