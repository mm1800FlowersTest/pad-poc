/*
 * 
 */
package com.fd.pad.deliverycalendar;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fd.pad.deliverycalendar.dao.ProductDAO;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

/**
 * 
 * @author e019904
 */
//@SpringBootApplication
@RestController
public class DeliveryCalendarController 
{
	private static final String QUEUE_NAME = "publishTest";
	private static final String HOST="localhost";
	
	@GetMapping("/product/deliveryCalendar/hello")
	public String getDeliveryCalendarHello() 
	{
		return "Hello Get Delivery Calendar";		
	}

	@GetMapping("/product/{partNumber}/deliveryCalendar")
	public String getDeliveryCalendar(@PathVariable("partNumber") String partNumber) 
	{
		ProductDAO product = new ProductDAO(partNumber);
		product.populate();
		String startDate = product.getStartDate();
		String endDate = product.getEndDate();
		return "Get Delivery Calendar for " + partNumber + " = [" + startDate + "] - [" + endDate + "]";		
	}


	@GetMapping("/product/{partNumber}/deliveryCalendar/add")
	public String addDeliveryCalendar(@PathVariable("partNumber") String partNumber, @RequestParam String startDate, @RequestParam String endDate) 
	{
		ProductDAO product = new ProductDAO(partNumber, startDate, endDate);
		product.insert();
		return " Delivery added for " + partNumber;		
	}
	
	@GetMapping("/rabbit/publish")
	public String publishToRabbit(@RequestParam String message, @RequestParam String host)
	{
		System.out.println(" [*] Received request to publish message to " + host);
		String result;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(( (host != null && !host.isEmpty()) ? host : HOST ));
		Connection connection = null;
		Channel channel = null;
		
		try 
		{
			connection = factory.newConnection();
			System.out.println(" [.] Created connection");
			channel = connection.createChannel();
			System.out.println(" [.] Created channel");

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println(" [.] Declared queue");

			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
			
			result = "Message Published";
		} 
		catch (IOException | TimeoutException e) 
		{
			System.err.println(" [!] Error publishing message");
			result = " [!] Error publishing message";
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(channel != null)
				{
					channel.close();
				}
				if(connection != null)
				{
					connection.close();
				}
			} 
			catch (IOException | TimeoutException e) 
			{
				e.printStackTrace();
			}		
		}
		
		return result + " <div><a href='/rabbit/consume?host=" + ( (host != null && !host.isEmpty()) ? host : HOST ) + "'>Consume</a></div><br/><div><a href='/TestRabbitProducer.html'>Publish More</a></div>";
	}
	
	@GetMapping("/rabbit/consume")
	public String consumeFromRabbit(@RequestParam String host)
	{
		String result;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(( (host != null && !host.isEmpty()) ? host : HOST ));
		Connection connection = null;
		Channel channel = null;
		String message = null;
		
		try 
		{
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println(" [*] Waiting for messages.");
			
			GetResponse getResponse = channel.basicGet(QUEUE_NAME, true);
			if(getResponse != null && getResponse.getBody() != null)
			{
				message = new String(getResponse.getBody(), "UTF-8");
			}
			
			result = "Message Consumed: " + message;
		} 
		catch (IOException | TimeoutException e) 
		{
			result = " [!] Error consuming message";
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if(channel != null)
				{
					channel.close();
				}
				if(connection != null)
				{
					connection.close();
				}
			} 
			catch (IOException | TimeoutException e) 
			{
				e.printStackTrace();
			}		
		}
		
		return result + "<br/><br/><div><a href='/TestRabbitProducer.html'>Publish More</a></div><br/><div><a href='/rabbit/consume?host=" + ( (host != null && !host.isEmpty()) ? host : HOST ) + "'>Consume More</a></div>";
	}
}
