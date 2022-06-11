import co.za.paygate.rabbit.config.Config;
import co.za.paygate.rabbit.entity.Order;
import co.za.paygate.rabbit.repository.OrderRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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

		//config = new Config("src/main/resources/config/dev/application.properties");
		//Channel channel = createChannel(new ConnectionFactory());
		//config.load();
		//produceMsg(channel);

		System.out.println("Start here");

		emf = Persistence.createEntityManagerFactory("paymentReport");
		em = emf.createEntityManager();
		orderRepository = new OrderRepository(em);
		Scanner scanner = new Scanner(System.in);
		Order order = new Order();

		processPayment(order, scanner);

	}

	private static void processPayment(Order order, Scanner scanner){
		String tryAgain = "";
		do {
			if (tryAgain.equalsIgnoreCase("n")) {
				System.out.println("Payment closed");
				break;
			}
			System.out.println("Enter order id to verify order :");
			int orderId = scanner.nextInt();

			order = verifyOrder(orderId);
			if (order != null) {
				if (order.getStatus() != null && order.getStatus().equals(0)) {
					System.out.println("This order has no payment please pay an amount of R:" + order.getAmount());
					int amount = scanner.nextInt();
					System.out.println("Successful payment of R" + amount);
				} else {
					System.out.println("No payment required for");
				}
			} else {
				System.out.println("Order id not found");
			}
			scanner.nextLine();
			System.out.println("Would you like to make payment again(y/n)? :");
			tryAgain = scanner.nextLine();
		}while (tryAgain.equalsIgnoreCase("y") || !tryAgain.equalsIgnoreCase(""));
	}

	private static Order verifyOrder(Integer orderId){
		try {
			return orderRepository.findOrderById(orderId);
		}catch (NoResultException e){
			return null;
		}
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
