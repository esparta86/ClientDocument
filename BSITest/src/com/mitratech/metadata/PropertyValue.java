package com.mitratech.metadata;

import com.mitratech.metadata.TypedValue;

public class PropertyValue {
	
	  //! Based on M-Files API.
    public int PropertyDef;
    
    //! Based on M-Files API.
    public TypedValue TypedValue;
    
    public PropertyValue(int propertyDef, TypedValue typedValue){
    	
    	this.PropertyDef=propertyDef;
    	this.TypedValue=typedValue;
    	
    }
    

	public int getPropertyDef() {
		return PropertyDef;
	}

	public void setPropertyDef(int propertyDef) {
		PropertyDef = propertyDef;
	}

	public TypedValue getTypedValue() {
		return TypedValue;
	}

	public void setTypedValue(TypedValue typedValue) {
		TypedValue = typedValue;
	}
    
    

}
