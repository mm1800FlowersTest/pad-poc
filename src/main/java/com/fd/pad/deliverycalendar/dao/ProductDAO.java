/*
 * 
 */
package com.fd.pad.deliverycalendar.dao;

import java.util.Map;

import com.google.apphosting.api.ApiProxy;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

/**
 * 
 * @author e019904
 */
public class ProductDAO 
{
	private String partNum;
	private String startDate;
	private String endDate;
			
	private Datastore datastore;

	public ProductDAO(String inPartNum)
	{
		datastore = getDatastore();

		partNum = inPartNum;
	}

	public ProductDAO(String inPartNum, String inStartDate, String inEndDate)
	{
		datastore = getDatastore();

		partNum = inPartNum;
		startDate = inStartDate;
		endDate = inEndDate;
	}
	
	public String getStartDate()
	{
		return startDate;
	}
	
	public String getEndDate()
	{
		return endDate;
	}
	
	public void populate()
	{
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("Product").setFilter(PropertyFilter.eq("partNumber", partNum)).build();
		QueryResults<Entity> results = datastore.run(query);
		while (results.hasNext()) 
		{
		      Entity entity = results.next();
		      startDate = entity.getString("startDate");
		      endDate = entity.getString("endDate");		      
		}		
	}

	public void insert()
	{
		KeyFactory keyFactory = datastore.newKeyFactory().setKind("Product");	
		
		IncompleteKey key = keyFactory.newKey();	// Key will be assigned once written
	    FullEntity<IncompleteKey> incProductEntity = Entity.newBuilder(key)  // Create the Entity
	        .set("partNumber", partNum)         
	        .set("startDate", startDate)
	        .set("endDate", endDate)
	        .build();
	    datastore.add(incProductEntity); // Save the Entity
	}
	
	private Datastore getDatastore()
	{
		Datastore datastore = null;
		
		ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
		Map<String, Object> attr = env.getAttributes();
		String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");

		if(hostname.contains("localhost:"))
		{
			datastore = DatastoreOptions.newBuilder().setHost("http://localhost:8081").setProjectId("pad-dev1").build().getService();
		}
		else
		{
			datastore = DatastoreOptions.getDefaultInstance().getService();
		}
		
		return datastore;
	}
}
