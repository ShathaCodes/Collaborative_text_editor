package tp1;

	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;

	import com.rabbitmq.client.Channel;
	import com.rabbitmq.client.Connection;
	import com.rabbitmq.client.ConnectionFactory;

	public class Producer  extends JFrame{
	private String msg;
	private JTextArea txtInput;
	public Producer(){
	    
	    MyKeyListener myKeyListener = new MyKeyListener();
	    txtInput = new JTextArea(10,40);
	    txtInput.addKeyListener(myKeyListener);
	    add(new JScrollPane(txtInput), BorderLayout.NORTH);
	   
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    pack();
	    setVisible(true);
	    txtInput.requestFocusInWindow();
	}
	static int x=0;
	private void addInputToConsole() throws Exception{
	    String input = txtInput.getText().trim();
	    if(input.equals("")) return;
	    msg =input;
	    if ( x>0)
	    msg =input.substring(input.lastIndexOf("\n")+1);
	    x++;
		//msg="";
	    try (Connection connection = Connexion.factory.newConnection();
				Channel channel =connection.createChannel())
			{
				channel.queueDeclare(QUEUE_NAME, false,false,false,null);
				String message =msg;
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				System.out.println("[x] sent '"+message +"'");
			}
	}
	private static class Connexion {
		private static ConnectionFactory factory ;
		public Connexion() throws Exception {
			 factory = new ConnectionFactory();
		     factory.setHost("localhost");
		     /*factory.setHost("10.13.0.95");
		 	factory.setUsername("chadha");
		 	factory.setPassword("1234");
		 	//factory.setPort(5672);
		 	factory.setVirtualHost("/");*/
		}
	}


	// You don't need a keylistener to listen on enter-presses. Use a
	// actionlistener instead, as shown below.
	public String getMsg() {return msg;}
	private class MyKeyListener extends KeyAdapter {
	    @Override
	    public void keyTyped(KeyEvent e) {
	        if(e.getKeyChar() == KeyEvent.VK_ENTER)
	            try {addInputToConsole()  ;}
	        catch(Exception e1) {System.out.print(e1.getStackTrace());}
	    }
	}



	private final static String QUEUE_NAME= "t1" ;
	public static void main(String [] args) throws Exception {
		new Producer();
		new Connexion();
		
	}

	}
