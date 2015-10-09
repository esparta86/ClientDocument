package com.mitratech.metadata;

import com.mitratech.metadata.PropertyValue;
import com.mitratech.metadata.UploadInfo;

public class ObjectCreationInfo {
	
	  //! Properties for the new object.
    public PropertyValue[] PropertyValues;
    public UploadInfo[]  UploadInfo;

	public PropertyValue[] getPropertyValues() {
		return PropertyValues;
	}
	
	public UploadInfo[] getUploadInfo(){
		return UploadInfo;
	}

	public void setPropertyValues(PropertyValue[] propertyValues) {
		PropertyValues = propertyValues;
	}
	
	public void setUploadInfo(UploadInfo[] uploadInfo){
		UploadInfo = uploadInfo;
	}
	
	

	//! References previously uploaded files.
  //  public UploadInfo[] Files;
    
    
    
}
