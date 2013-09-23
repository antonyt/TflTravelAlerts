package com.tfltravelalerts.common.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ImmutableListDeserializer implements JsonDeserializer<ImmutableList<?>> {
    @Override
    public ImmutableList<?> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        final Type type2 = new ParameterizedTypeImpl(List.class, ((ParameterizedType) type).getActualTypeArguments(), null);
        
        final List<?> list = context.deserialize(json, type2);
        return ImmutableList.copyOf(list);
    }
}
