package com.fd.pad.deliverycalendar;

import java.io.IOException;

import org.junit.Test;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;

public class PadDeliveryCalendarDatastoreTest 
{
	@Test
	public void test1() throws IOException, InterruptedException 
	{
/*	    Datastore ds = DatastoreOptions.newBuilder().setHost("http://localhost:8081").setProjectId("pad-dev1").build().getService();
	    Entity entityAdded = null;
	    
	    entityAdded = ds.put(
	    	Entity.newBuilder( 
	    		ds.newKeyFactory().setKind("Product").newKey("myKey"))
	    		.set("p1", "Hello World!").build());
*/
//	    entityAdded = ds.add(
//	    	Entity.newBuilder(
//	    		ds.newKeyFactory().setKind("Product").newKey())
//	    		.set("p1", "Hello World!").build());

//	    System.out.println(entityAdded);
	}

}
