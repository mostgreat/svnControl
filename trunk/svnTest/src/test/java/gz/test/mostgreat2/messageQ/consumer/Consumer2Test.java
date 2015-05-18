package gz.test.mostgreat2.messageQ.consumer;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer2Test implements MessageListener {

	public static AmqpTemplate template;

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:META-INF/context-messageQ_consumer2.xml");
	}

	public void onMessage(Message message) {

		try {
			System.out.println(new String(message.getBody(), "utf-8"));
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
