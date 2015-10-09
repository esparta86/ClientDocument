package com.mitratech.resources;

public class PrimitiveType <T>{
	private T Value;
	public PrimitiveType(T Value1){
		setValue(Value1);
	}
	
	public T getValue() {
		return Value;
	}
	public void setValue(T value) {
		Value = value;
	}
	
	
}
