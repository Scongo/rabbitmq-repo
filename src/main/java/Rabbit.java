import co.za.paygate.rabbit.config.Config;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.Executors;

/**
 * Created by AlecE on 6/15/2017.
 */
public class Rabbit {

	private static Logger log = LoggerFactory.getLogger(Rabbit.class);
	public static Config config;

	public static void main(String[] args) {

		Channel channel = createChannel(new ConnectionFactory( ));
		System.out.println("Start here");
	}


	private static Channel createChannel(ConnectionFactory connectionFactory) {
		try {
			connectionFactory.setUri("amqp://" + config.getString("rabbit.username") + ":" + config.getString("rabbit.password") + "@" + config.getString("rabbit.host") + ":" + config.getString("rabbit.port") + "/" + config.getString("rabbit.virtual"));
			return connectionFactory.newConnection(Executors.newFixedThreadPool(config.getInt("rabbit.consumers").intValue( ))).createChannel( );
		} catch (Exception e) {
			log.error(e.getMessage( ), e);
			System.exit(1);
			return null;
		}
	}
}
