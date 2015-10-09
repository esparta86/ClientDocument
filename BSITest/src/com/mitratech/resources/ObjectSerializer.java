/**
 * Mitratech Software
 * 
 * @author Software Engineering : Andre Palacios ,
 *         Software Engineering : Lisandro Rafaelano
 * @version Created Oct 08, 2015
 */
package com.mitratech.resources;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mitratech.metadata.ObjectCreationInfo;
import com.mitratech.metadata.PropertyValue;

public class ObjectSerializer implements JsonSerializer<ObjectCreationInfo>{
	PropertyValue propertyValues;
	@Override
	public JsonElement serialize(final ObjectCreationInfo object, final Type type, final JsonSerializationContext context) {
       final JsonObject result = new JsonObject();
       final JsonElement objProperty = context.serialize(object.getPropertyValues());
       final JsonElement objUploadInfo = context.serialize(object.getUploadInfo());
       result.add("PropertyValues", objProperty);
       result.add("Files", objUploadInfo);
       return result;
    }
}
