package gz.test.mostgreat2.messageQ.producer;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

public class ProducerTest{
	
	public static AmqpTemplate template;
	private static ApplicationContext context; 
	
    //private final AtomicInteger counter = new AtomicInteger();
    
    public static void main(String[] args) {
    	
    	context = new ClassPathXmlApplicationContext("classpath:META-INF/context-messageQ_producer.xml");
    	template = context.getBean(AmqpTemplate.class);
    	
    	ProducerTest a = new ProducerTest();
    	a.enterString();
    }
    
    public void enterString(){
    	
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String message = null;
    	try {
    		System.out.println("[Enter]");
			while (!(message = br.readLine()).isEmpty()) {
			String[] messages = message.split(":");
			template.convertAndSend("testExchange",messages[0].toString().trim(), messages[1]);
			}
		} catch (AmqpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    /*@Scheduled(fixedRate = 1000)
    public void execute() {
        System.out.println("Execute..");
        template.convertAndSend("testExchange", "testqueue", "hello world");

    }*/
}