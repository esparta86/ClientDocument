/**
 * Mitratech Software
 * 
 * @author Software Engineering : Andre Palacios ,
 *         Software Engineering : Lisandro Rafaelano
 * @version Created Oct 08, 2015
 */
package com.mitratech.bsi;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mitratech.metadata.ObjectCreationInfo;
import com.mitratech.metadata.UploadInfo;
import com.mitratech.resources.AdminData;
import com.mitratech.resources.ObjectSerializer;
import com.mitratech.resources.Time;

public class Main {
	public static String URL_MFILE_SERVER = "";
	public static String DIRECTORY_FILES = "";
	public static int NUMBER_DOCUMENTS = 0;
	public static String USER_NAME = "";
	public static String PASSWORD = "";
	public static boolean WINDOWS_USER=false;
	public static String DOMAIN="";
	public static String  VAULT_GUID="";
	public static int DOCUMENT_CLASS = 0;
	public static int PROPERTY_DEF_NAME=0;
	public static int PROPERTY_DEF_MATTER=0;
	public static int PROPERTY_DEF_TYPE_CLASS=0;
	
	public static void main(String[] args) {
		final String ROOTDIRECTORY ="C:\\rootMfileClientPerformance\\";
		final Path p1 = Paths.get(ROOTDIRECTORY);
		FileReader fileLocalSetting = null;
		FileReader fileMfileSettingAdvanced = null;
		Object[] configurationConstant= null;
		Logger logger = Logger.getLogger("MyLog"); 
		FileHandler fh;
		boolean validateArray = true;
		Time timeCreateDocument = new Time();
		
		
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(p1)){
			DateFormat dateFormat = new SimpleDateFormat(" yyyy-MM-dd 'T' HH.mm.ss aaa");//yyyy-MM-dd'T'HH:mm:ss
	    	Date date = new Date();
	      	String currentDateTime = dateFormat.format(date);
	    	fh = new FileHandler(ROOTDIRECTORY+"\\logs\\MDV"+currentDateTime+".log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	        
		  for(Path file:stream){
		    if(file.getFileName().toString().equals("local_setting.json"))
			  fileLocalSetting = new FileReader(ROOTDIRECTORY.concat(file.getFileName().toString()));
			if(file.getFileName().toString().equals("mfile_setting_advanced.json"))
			  fileMfileSettingAdvanced = new FileReader(ROOTDIRECTORY.concat(file.getFileName().toString()));
		  }
		}catch(IOException  | DirectoryIteratorException x){
		  System.err.println(x);
		}
		if(fileLocalSetting==null||fileMfileSettingAdvanced==null){
			logger.info("there is not local_setting.json. make sure if there a setting file and mfile_setting_advanced.json");
		}else{
			System.out.println("perfect.!! we can start");
			configurationConstant = readSettingLocal(fileLocalSetting,fileMfileSettingAdvanced);
			for(int pos = 0;pos < configurationConstant.length; pos++){
			  if(configurationConstant[pos]==null){
			    validateArray = false;
			    break;
			   }
			}
			
			if(validateArray){
			  URL_MFILE_SERVER = configurationConstant[0].toString();
		      DIRECTORY_FILES  = ROOTDIRECTORY+configurationConstant[1].toString();
			  NUMBER_DOCUMENTS = (int)configurationConstant[2];
			  USER_NAME = configurationConstant[3].toString();
			  PASSWORD = configurationConstant[4].toString();
			  WINDOWS_USER = (boolean)configurationConstant[5];
			  DOMAIN   = configurationConstant[6].toString();
		      VAULT_GUID = configurationConstant[7].toString();
		      DOCUMENT_CLASS = (int)configurationConstant[8]; ;
	          PROPERTY_DEF_NAME=(int)configurationConstant[9];
			  PROPERTY_DEF_MATTER=(int)configurationConstant[10];
			  PROPERTY_DEF_TYPE_CLASS=(int)configurationConstant[11];
			 
			  ArrayList<Object[]> arrayListMatterInfo = new ArrayList<Object[]>();
			  Object[] matterObject;
			  String tokenEncrypted=null;
			  try {
				
			    tokenEncrypted= getAuthenticationToken(logger);
			    arrayListMatterInfo = getMatters(tokenEncrypted,logger);
			    int prefixMatter=0;
			    int totalDocuments = arrayListMatterInfo.size()*NUMBER_DOCUMENTS;
			    int totalMattersCount = arrayListMatterInfo.size();
			    int totalMatters = arrayListMatterInfo.size();
			    for(int matter=0;matter< arrayListMatterInfo.size() ;matter++){
			      	
			      for(int doc =0 ;doc < NUMBER_DOCUMENTS ; doc++){
			    	prefixMatter++;
			        matterObject = arrayListMatterInfo.get(matter);
			        timeCreateDocument.Calculate();  
				    newDocumentObject(tokenEncrypted,"Aut Document "+(String)matterObject[0]+"#"+prefixMatter,(int)matterObject[1],logger);
			        timeCreateDocument.stop();
				    System.out.println("Remaining Documents # "+--totalDocuments);
			      }
			      System.out.println("Remaining Matters # "+--totalMattersCount+"/"+totalMatters);
			      prefixMatter=0;
			    }
			    System.out.println("Total Time in Seconds"+timeCreateDocument.getTotalUploadTime());
			    JOptionPane.showMessageDialog(null, "The program ended successfully, check the log file if there are problems");
			    
				//System.out.println("el token es: "+tokenEncrypted);
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(e.toString());
			    }
			}else{
				String msj = "cannot read. there are some fields which can not read. make sure is a correct local_setting file";
				System.out.println(msj);
			    logger.info(msj);
			}
		}
	}
	
/*
 *  Method : readSettingLocal , read a localSetting.json file 	
 */
	public static Object[] readSettingLocal(FileReader settingLocal,FileReader fileMfileSettingAdvanced){
	   String jsonData = "";
	   String jsonDataMfile ="";
	   BufferedReader bufferedReader = null;
	   JSONObject obj= null;
	   JSONObject objMfile= null;
	   Object[] arrayConfiguration = new Object[12];
	   try{
	     String line;
		 bufferedReader= new BufferedReader(settingLocal);
		 while((line=bufferedReader.readLine())!=null){
		   jsonData+=line+"\n";
		 }
		 bufferedReader =new  BufferedReader(fileMfileSettingAdvanced);
		 while((line=bufferedReader.readLine())!=null){
		   jsonDataMfile+=line+"\n";
		 }
	    }catch(IOException e){
		  e.printStackTrace();
		}finally {
			try {
				if(bufferedReader!=null){
					bufferedReader.close();
				}
			}
			 catch (IOException e) {
			  e.printStackTrace();
			}
		}
		
		try{
	    obj=new JSONObject(jsonData);
	    objMfile = new JSONObject(jsonDataMfile);
	    arrayConfiguration[0] = obj.getString("URLMfile");
	    arrayConfiguration[1] = obj.getString("DirectoryFiles");
	    arrayConfiguration[2] = obj.getInt("NumberOfDocumentsPerMatter");
	    arrayConfiguration[3] = obj.getJSONObject("credentials").getString("UserName");
	    arrayConfiguration[4] = obj.getJSONObject("credentials").getString("Password");
	    arrayConfiguration[5] = obj.getJSONObject("credentials").getBoolean("WindowsUser");
	    arrayConfiguration[6] = obj.getJSONObject("credentials").getString("Domain");
	    arrayConfiguration[7] = obj.getJSONObject("credentials").getString("VaultGuid");
	    arrayConfiguration[8] = objMfile.getInt("documentClass");
	    arrayConfiguration[9] = objMfile.getInt("propertyDefName");
	    arrayConfiguration[10] = objMfile.getInt("propertyDefMatter");
	    arrayConfiguration[11] = objMfile.getInt("propertyDefTypeClass");
	    
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		return arrayConfiguration;
	}
	
	
	
	public static String getAuthenticationToken(Logger logger ) throws Exception {
		String token = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Username", USER_NAME);
			jsonObject.put("Password", PASSWORD);
			jsonObject.put("WindowsUser", WINDOWS_USER);
			jsonObject.put("Domain", DOMAIN);
			jsonObject.put("VaultGuid", VAULT_GUID);
			AdminData admin = new AdminData();
			JSONObject response = admin.doPost(URL_MFILE_SERVER+"REST/server/authenticationtokens",jsonObject.toString(),null,null,logger);
			token = response.getString("Value");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			throw e;
		}
		return token;
	}
	
/*
 * Method:	getMatters : retrieves the active Matters and return a ArrayList that stores the title of matter and ID of Object Version
 */
	public static ArrayList<Object[]> getMatters(String token,Logger logger){
		AdminData admin = new AdminData();
		JSONObject response = new JSONObject();
		ArrayList<Object[]> listDisplayObjectId = new ArrayList<Object[]>();
		try {
			response = admin.doGet(URL_MFILE_SERVER+"REST/objects.aspx?p1076=Active", token, logger);
			JSONArray jsonMatterArray = response.getJSONArray("Items");
			for(int i=0;i< jsonMatterArray.length();i++){
			  Object[] infoMatter = new Object[2];
			  infoMatter[0] = jsonMatterArray.getJSONObject(i).getString("Title");
		      JSONObject ObjectVersion = jsonMatterArray.getJSONObject(i).getJSONObject("ObjVer");
			  infoMatter[1] = ObjectVersion.getInt("ID");
			  listDisplayObjectId.add(infoMatter);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
		}
		return listDisplayObjectId;
	}
	
/*
 * Method : newDocumentObject : Upload Files and build a new Document.
 */
	public static JSONObject newDocumentObject(String token,String matterName,int item, Logger logger) throws IOException, Exception{
		ArrayList<UploadInfo> listUploadInfo = new ArrayList<UploadInfo>();
		ArrayList<File> listFile = new ArrayList<File>();
		Path p1 = Paths.get(DIRECTORY_FILES);
		ObjectCreationInfo objCreation=null;
		AdminData admin = new AdminData();
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(p1)){
			for(Path file:stream){
				listFile.add(new File(DIRECTORY_FILES+"\\"+file.getFileName()));
			}
		}catch(IOException | DirectoryIteratorException x){
			System.err.println(x);
		}
		for(File file:listFile){
			UploadInfo temp = admin.sendFile(URL_MFILE_SERVER+"REST/files", token,file,logger);
			if(temp instanceof UploadInfo){
				listUploadInfo.add(temp);	
			}
							
		}
		objCreation = admin.createObjectDocument(matterName,DOCUMENT_CLASS,listUploadInfo,item);
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ObjectCreationInfo.class, new ObjectSerializer());
		Gson gson = gsonBuilder.create();
		final String representationJSON = gson.toJson(objCreation);
		admin.doPost(URL_MFILE_SERVER+"REST/objects/0", representationJSON, token, matterName, logger);
		
		
		/*JSONObject objectDocumentVersion = responseObjectVersion.getJSONObject("ObjVer");
		if(objectDocumentVersion.getInt("ID")>0){
		  System.out.println("Created a document with success");
		  System.out.println(representationJSON);
		  System.out.println("Document Name: "+responseObjectVersion.getString("Title"));
		}*/
						
		return null;
	}
}


