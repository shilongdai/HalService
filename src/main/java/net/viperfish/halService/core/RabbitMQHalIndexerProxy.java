package net.viperfish.halService.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitMQHalIndexerProxy implements HalIndexer, AutoCloseable {

	private String queueName;
	private String host;
	private Channel rabbitMQChannel;
	private Connection rabbitMQConn;
	private ObjectMapper mapper;

	public RabbitMQHalIndexerProxy(String host, String queueName) {
		this.queueName = queueName;
		this.host = host;
		mapper = new ObjectMapper();
	}

	@Override
	public void init() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		try {
			rabbitMQConn = factory.newConnection();
			rabbitMQChannel = rabbitMQConn.createChannel();
			rabbitMQChannel.queueDeclare(queueName, true, false, false, null);
		} catch (TimeoutException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void index(SearchEngineCrawledData data) throws IOException {
		String json = mapper.writeValueAsString(data);
		rabbitMQChannel.basicPublish("", queueName,
			MessageProperties.PERSISTENT_TEXT_PLAIN,
			json.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public void close() throws Exception {
		this.rabbitMQChannel.close();
		this.rabbitMQConn.close();
	}
}
