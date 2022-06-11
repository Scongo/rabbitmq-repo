import co.za.paygate.rabbit.config.Config;
import co.za.paygate.rabbit.entity.Order;
import co.za.paygate.rabbit.repository.OrderRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.io.IOException;
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
    private final static String exchange_name = "myexchange";
    private static String orderDetails = "";

	public static void main(String[] args) {

		config = new Config("src/main/resources/config/dev/application.properties");
		config.load();

		System.out.println("Start here");

		emf = Persistence.createEntityManagerFactory("paymentReport");
		em = emf.createEntityManager();
		orderRepository = new OrderRepository(em);
		Scanner scanner = new Scanner(System.in);
		Order order = new Order();

		processPaymentAmount(order, scanner);
		Channel channel = createChannel(new ConnectionFactory());
		if(channel != null){
			produceMsg(channel);
		}
		channel = createChannel(new ConnectionFactory());
		consumeMsg(channel);
	}

	private static void processPaymentAmount(Order order, Scanner scanner){
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
					orderDetails = order.getId()+"/"+amount;
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
			channel.basicPublish(exchange_name, queue_name, null, orderDetails.getBytes());
			System.out.println(" [x] Sent '" + orderDetails +"'");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String consumeMsg(Channel channel){
		try {
//			channel.queueDeclare(queue_name, false, false, false, null);
//			channel.basicPublish(exchange_name, queue_name, null, orderDetails.getBytes());
//			System.out.println(" [x] Sent '" + orderDetails +"'");
//
			channel.exchangeDeclare(exchange_name, "fanout");
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, exchange_name, "");

			System.out.println("Waiting for rabbit mq messages..");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			};
			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
            return null;
		} catch (IOException e) {
			return null;
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
