package com.mitratech.metadata;

import com.mitratech.metadata.Lookup;

public class TypedValue {
	
	 //! Specifies the type of the value.
    public int DataType;
    
    //! Specifies whether the typed value contains a real value.
    //public boolean HasValue;
    
    //! Specifies the string, number or boolean value when the DataType is not a lookup type.
    public Object Value;
    
    //! Specifies the lookup value when the DataType is Lookup.
    public Lookup Lookup;
    
    
    //! Specifies the collection of \type{Lookup}s when the DataType is MultiSelectLookup.
    private Lookup[] Lookups;
    
    //! Provides the value formatted for display.
    //public String DisplayValue;
    
    //! Provides a key that can be used to sort \type{TypedValue}s
    //public String SortingKey;
    
    //! Provides the typed value in a serialized format suitable to be used in URIs.
   // public String SerializedValue;
    
    public TypedValue(int dataType, Object value){
    	
    	this.DataType=dataType;
    	this.Value=value;
    	
    }
    
   public TypedValue(int dataType, Lookup lookup){
    	
    	this.DataType=dataType;
    	this.Lookup=lookup;
    	
    }
   
   public TypedValue(int dataType, Lookup[] lookups){
   	
   	this.DataType=dataType;
   	this.Lookups = lookups;
   	
   }

	public int getDataType() {
		return DataType;
	}

	public void setDataType(int dataType) {
		DataType = dataType;
	}

	public Object getValue() {
		return Value;
	}

	public void setValue(Object value) {
		Value = value;
	}

	public Lookup getLookup() {
		return Lookup;
	}

	public void setLookup(Lookup lookup) {
		Lookup = lookup;
	}

	public Lookup[] getLookups() {
		return Lookups;
	}

	public void setLookups(Lookup[] lookups) {
		Lookups = lookups;
	}
    
    

}
