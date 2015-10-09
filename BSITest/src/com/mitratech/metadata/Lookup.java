package com.mitratech.metadata;

public class Lookup {
		
	 //! Based on M-Files API.
    public boolean Deleted;
    
    //! Based on M-Files API.
    public String DisplayValue;
    
    //! Based on M-Files API.
    public boolean Hidden;
    
    //! Based on M-Files API.
    public int Item;
    
    //! Based on M-Files API.
    public Integer Version;
    
    public Lookup(int item){
    	this.Item=item;
    }
    
    public Lookup(String displayValue){
    	this.DisplayValue = displayValue;
    }
    
    public Lookup(String displayValue, int item){
    	this.DisplayValue = displayValue;
    	this.Item = item;
    	this.Version = null; /*by default we should work with the last version.*/
    }

	public boolean isDeleted() {
		return Deleted;
	}

	public void setDeleted(boolean deleted) {
		Deleted = deleted;
	}

	public String getDisplayValue() {
		return DisplayValue;
	}

	public void setDisplayValue(String displayValue) {
		DisplayValue = displayValue;
	}

	public boolean isHidden() {
		return Hidden;
	}

	public void setHidden(boolean hidden) {
		Hidden = hidden;
	}

	public int getItem() {
		return Item;
	}

	public void setItem(int item) {
		Item = item;
	}

	public int getVersion() {
		return Version;
	}

	public void setVersion(int version) {
		Version = version;
	}
    
    

}
