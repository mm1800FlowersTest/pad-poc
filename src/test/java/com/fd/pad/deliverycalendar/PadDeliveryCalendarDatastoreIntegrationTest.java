package com.fd.pad.deliverycalendar;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

public class PadDeliveryCalendarDatastoreIntegrationTest extends Assert
{
	@Test
	public void test1() throws IOException, InterruptedException 
	{
	    Datastore ds = DatastoreOptions.newBuilder().setHost("http://localhost:8081").setProjectId("pad-dev1").build().getService();
	    Entity entityAdded = null;
	    
	    String key = "myKey" + Calendar.getInstance().getTimeInMillis();
	    String value = "test" + Calendar.getInstance().getTimeInMillis();
	    
	    entityAdded = ds.put(
	    	Entity.newBuilder( 
	    		ds.newKeyFactory().setKind("Product").newKey(key))
	    		.set("partNum", value)
	    		.set("p1", "Hello World!").build());

	    System.out.println("entityAdded");
	    System.out.println(entityAdded);
	    
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("Product").setFilter(PropertyFilter.eq("partNum", value)).build();
		QueryResults<Entity> results = ds.run(query);
		Entity entity = null;
		while (results.hasNext()) 
		{
			entity = results.next();
			System.out.println("entity Found");
			System.out.println(entity);
		}
		
		assertNotNull("Entity not found", entity);
	}

}
