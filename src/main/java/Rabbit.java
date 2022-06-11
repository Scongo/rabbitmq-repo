import co.za.paygate.rabbit.config.Config;
import co.za.paygate.rabbit.entity.Order;
import co.za.paygate.rabbit.repository.OrderRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * Created by AlecE on 6/15/2017.
 */
public class Rabbit {

	private static Logger log = LoggerFactory.getLogger(Rabbit.class);
	public static Config config;
	private static OrderRepository orderRepository;
	private static EntityManagerFactory emf = null;
	private static EntityManager em = null;
	private final static String queue_name = "myqueue";

	public static void main(String[] args) {

		config = new Config("src/main/resources/config/dev/application.properties");
		Channel channel = createChannel(new ConnectionFactory());
		config.load();
		produceMsg(channel);

		System.out.println("Start here");

		emf = Persistence.createEntityManagerFactory("paymentReport");
		em = emf.createEntityManager();
		orderRepository = new OrderRepository(em);

		List<Order> orderList = orderRepository.findAll();
		System.out.println("Orders payed for :");
		orderList.forEach(order1 -> {
			if(order1.getStatus() != null){
				if(order1.getStatus().equals(0)){
					System.out.println("This order :"+order1.getDesc()+ "has declined please pay amount :");
					Scanner scanner = new Scanner(System.in);
					int amount = scanner.nextInt();
					orderRepository.updateOrderPayment(order1.getId(), amount);
					order1.getOrderPayments().forEach(pay ->{
						pay.getStatusDesc();
						System.out.println(order1.getId() +" , "+ order1.getDesc()+" , "+
								order1.getAmount()+" , "+ pay.getStatusDesc());
					});
				}
			}
		});
	}

	private static void produceMsg(Channel channel){
		try {

			channel.queueDeclare(queue_name, false, false, false, null);
			String message = "Can you see my txt";
			channel.basicPublish("", queue_name, null, message.getBytes());
			System.out.println(" [x] Sent '" + message +"'");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static Channel createChannel(ConnectionFactory connectionFactory) {
		try {
			connectionFactory.setUri("amqp://" + config.getString("rabbit.username") + ":" + config.getString("rabbit.password") + "@" + config.getString("rabbit.host") + ":" + config.getInt(String.valueOf("rabbit.port")) + config.getString("rabbit.virtual"));
			return connectionFactory.newConnection(Executors.newFixedThreadPool(3)).createChannel();

		} catch (Exception e) {
			log.error(e.getMessage( ), e);
			System.exit(1);
			return null;
		}
	}
}
