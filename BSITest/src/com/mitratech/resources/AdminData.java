/**
 * Mitratech Software
 * 
 * @author Software Engineering : Andre Palacios ,
 *         Software Engineering : Lisandro Rafaelano
 * @version Created Oct 08, 2015
 */
package com.mitratech.resources;

import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.mitratech.bsi.Main;
import com.mitratech.metadata.Lookup;
import com.mitratech.metadata.MFDataType;
import com.mitratech.metadata.ObjectCreationInfo;
import com.mitratech.metadata.PropertyValue;
import com.mitratech.metadata.TypedValue;
import com.mitratech.metadata.UploadInfo;

public class AdminData {
	
/*
 * Method : doPost create a new request with post method.
 */
	public JSONObject doPost(String uri,String request,String token, String matterName,Logger logger) throws Exception {
		JSONObject JSONResponse = null;
		JSONObject JSONErrorResponse = null;
		String message="";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = null;
		   try{
		    	httppost = new HttpPost(uri);
		    	httppost.addHeader("Content-type", "application/json");
		    	
		    	if(token!=null)
		    	  httppost.addHeader("X-Authentication", token);
		    	
		    	StringEntity requestEntity = new StringEntity(request);
		    	requestEntity.setContentEncoding("UTF-8");
		    	requestEntity.setContentType("application/json");
		    	
		    	httppost.setEntity(requestEntity);
		    	HttpResponse response = httpclient.execute(httppost);
		    	int status = response.getStatusLine().getStatusCode();
		    	if(status!=200 && status!=503){
		    		
		    		HttpEntity entity=response.getEntity();
			    	 if (entity != null) {
			    	       
			    	     try{   
			    	    	 JSONErrorResponse = new JSONObject(EntityUtils.toString(entity));
			    	     } catch(Exception e){}
			    	     
			    	 }
		    		if(request.contains("VaultGuid")){
		    			if(JSONErrorResponse == null){
		    				message="M-Files error message not available, please check your configuration or M-Files server probably unreacheable";
		    			}
		    			else if(JSONErrorResponse != null){
		    				Object exception = JSONErrorResponse.get("Message");
			    			message = exception.toString();
		    			}
		    			
		    			logger.info("HTTP status code:"+status+", couldn't start session on M-Files, Error message: "+message);
		    			httpclient.close();
		    			System.exit(0);
		    		}
		    		else if (request.contains("PropertyValues")){
		    			if(JSONErrorResponse == null){
		    				message="M-Files error message not available, please check your configuration or M-Files server probably unreacheable";
		    			}
		    			else if(JSONErrorResponse != null){
		    				Object exception = JSONErrorResponse.get("Message");
			    			message = exception.toString();
		    			}
		    			
		    			logger.info("HTTP status code:"+status+", couldn't create Document related to Matter: "+matterName+", M-Files error message: "+message);
		    			JSONResponse = null;
		    			
		    		} 	
		    		
		    	}
		    	else if (status == 503){
		    		if(request.contains("VaultGuid")){
		    			logger.info("HTTP status code:"+status+", couldn't start session on M-Files, M-Files server unreacheable");
			    		JSONResponse = null;
			    		httpclient.close();
		    			System.exit(0);
		    		}
		    		else if (request.contains("PropertyValues")){
		    			logger.info("HTTP status code:"+status+", couldn't create Document related to Matter: "+matterName+", M-Files server unreacheable");
			    		JSONResponse = null;
		    		}
		    		
		    	}
		    	else if (status == 200){
		    		HttpEntity entity=response.getEntity();
		    		if (entity != null) {
		    	        try{	
		    	        	JSONResponse = new JSONObject(EntityUtils.toString(entity));
		    	        } catch(Exception e){}
		    	    }
		    	}
		    	 
		    }	catch(IOException e){
		    		System.out.println("Problem communicating with M-Files server, possible bad configuration or server unreachable.");
		    		JSONResponse = null;
		    }
	
		return JSONResponse;
	}
	
/*
 * Method : doGet create a new request whit get method
 */

	public JSONObject doGet(String uri, String token,Logger logger) throws Exception {
		JSONObject response = null;
		JSONObject JSONErrorResponse = null;
		String message="";
		int status;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(uri);
			httpGet.addHeader("X-Authentication", token);
			CloseableHttpResponse responseGet = httpclient.execute(httpGet);
			status = responseGet.getStatusLine().getStatusCode();
	    	if(status!=200){
	    		HttpEntity entity=responseGet.getEntity();
	    		if (entity != null) {
	    	        try{	
	    	        	JSONErrorResponse = new JSONObject(EntityUtils.toString(entity));
	    	        } catch(Exception e){}
	    	    }
	    		if(JSONErrorResponse == null){
    				message="M-Files error message not available, please check your configuration or M-Files server probably unreacheable";
    			}
    			else if(JSONErrorResponse != null){
    				Object exception = JSONErrorResponse.get("Message");
	    			message = exception.toString();
    			}
	    		
	    		logger.info("HTTP status code:"+status+", couldn't retrieve Matters metadata from M-Files server. Error message: "+message);
    			httpclient.close();
    			System.exit(0);
	    	}
	    	else if(status == 503){
	    		logger.info("HTTP status code:"+status+", M-Files server unreacheable");
	    		httpclient.close();
    			System.exit(0);
	    	}
	    	else if(status == 200){
	    		HttpEntity entity=responseGet.getEntity();
	    		if (entity != null) {
	    	        
	    	        try{	
	    	        	response = new JSONObject(EntityUtils.toString(entity));
	    	        } catch(Exception e){}
	    	    }
	    	}
	    	 
		} catch(Exception e){
			logger.info("Problem communicating with M-Files server, possible bad configuration or server unreachable.");
			httpclient.close();	
    		System.exit(0);
		}
		
	 return response;
	}
	
/*
 * Method : SendFile , uplpoad a file into the mfile server and return a UploadInfo Object	
 */
	
	public UploadInfo sendFile(String uri, String token,File file,Logger logger) throws IOException,Exception{
		UploadInfo uploadInfoFile= null;
		JSONObject responsePostUploadTemp = null;
		JSONObject JSONErrorResponse = null;
		String message = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = null;
		   try{
		    	httppost = new HttpPost(uri);
		    	//httppost.addHeader("Content-type", "application/json");
		    	if(token!=null)
		    	  httppost.addHeader("X-Authentication", token);
		    	
		    	FileBody fileBody = new FileBody(file) ;
		    	StringBody comment = new StringBody("A binary file of some kind,but Mfile should can to handle", ContentType.MULTIPART_FORM_DATA);
		    	HttpEntity reqEntity = MultipartEntityBuilder.create()
	                    .addPart("bin", fileBody)
	                    .addPart("comment", comment)
	                    .build();
		    	httppost.setEntity(reqEntity);
		    	CloseableHttpResponse response = httpclient.execute(httppost);
		    			    			    	
		    	if (response != null){
		    		int status = response.getStatusLine().getStatusCode();
		    		if (status == 200){			    		
				    	  try{
				    		  HttpEntity resEntity = response.getEntity();
				                if (resEntity != null) {
				                    //System.out.println("Response content length: " + resEntity.getContentLength());
				                    responsePostUploadTemp = new JSONObject(EntityUtils.toString(resEntity));
				                    //GsonBuilder gsonBuilder = new GsonBuilder();
				                    uploadInfoFile = new UploadInfo(responsePostUploadTemp.getInt("UploadID"),responsePostUploadTemp.getString("Title"),responsePostUploadTemp.getString("Extension"),responsePostUploadTemp.getLong("Size"));
				                    				
				                }
				                else {
				                	uploadInfoFile = null;
				                }
				    	  }
				          catch(IOException e){
				        	  logger.info("Problem communicating with M-Files server, possible bad configuration or server unreachable.");
			                  }
		    		}
		    		else{
		    			HttpEntity entity=response.getEntity();
			    		if (entity != null) {
			    	        try{	
			    	        	JSONErrorResponse = new JSONObject(EntityUtils.toString(entity));
			    	        } catch(Exception e){}
			    	    }
			    		if(JSONErrorResponse == null){
		    				message="M-Files error message not available, please check your configuration or M-Files server probably unreacheable";
		    			}
		    			else if(JSONErrorResponse != null){
		    				Object exception = JSONErrorResponse.get("Message");
			    			message = exception.toString();
		    			}
			    		
			    		logger.info("HTTP status code:"+status+", couldn't upload file to M-Files server. Error message: "+message);
		    			uploadInfoFile = null;
		    		}
		    	}
		    	
		    	else {
		    		//System.out.println("File couldn´t be uploaded to M-Files server.");
		    		logger.info("Problem communicating with M-Files server, File couldn´t be uploaded to M-Files server, possible bad configuration or server unreachable..");
		    		uploadInfoFile = null;
		    	}
		    	 
		   } catch(IOException e){
			   logger.info("Problem communicating with M-Files server, File couldn´t be uploaded to M-Files server, possible bad configuration or server unreachable.");
			 }
	    
		return uploadInfoFile; 
	}
	
/*
 * Method : createObjectDocument
 */
	
		
	public ObjectCreationInfo createObjectDocument(String documentName,int documentClass,ArrayList<UploadInfo> listUploadInfoFile,int objectVersionID){
        
		TypedValue typedValue1 = new TypedValue(MFDataType.Text,documentName);
		Lookup documentLookup = new Lookup(documentName, objectVersionID);
		Lookup[] lookups = new Lookup[1];
		lookups[0] = documentLookup;
		TypedValue typedValue7 = new TypedValue(MFDataType.MultiSelectLookup, lookups);
		TypedValue typedValue9 = new TypedValue(MFDataType.Lookup,new Lookup(documentClass));
		PropertyValue propertyValue1 = new PropertyValue(Main.PROPERTY_DEF_NAME,typedValue1);
		PropertyValue propertyValue7 = new PropertyValue(Main.PROPERTY_DEF_MATTER,typedValue7);
		PropertyValue propertyValue9 = new PropertyValue(Main.PROPERTY_DEF_TYPE_CLASS,typedValue9);
		PropertyValue[] proValue= new PropertyValue[3];
		proValue[0]=propertyValue1;
		proValue[1]=propertyValue7;
		proValue[2]=propertyValue9;
		UploadInfo[]  uploadInfo= new UploadInfo[listUploadInfoFile.size()];
		for(int up=0;up<listUploadInfoFile.size();up++){
			uploadInfo[up]=listUploadInfoFile.get(up);
		}
		ObjectCreationInfo obj = new ObjectCreationInfo();
		obj.PropertyValues=proValue;
		obj.UploadInfo = uploadInfo;
				
	return obj;
	}
	
	

}



