/*
 * 
 */
package com.fd.pad.deliverycalendar;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fd.pad.deliverycalendar.dao.ProductDAO;

/**
 * 
 * @author e019904
 */
@SpringBootApplication
@RestController
public class DeliveryCalendarController 
{
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
	
}
