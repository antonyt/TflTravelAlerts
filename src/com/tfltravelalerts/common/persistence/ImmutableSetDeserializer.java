package com.tfltravelalerts.common.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ImmutableSetDeserializer implements JsonDeserializer<ImmutableSet<?>> {
    @Override
    public ImmutableSet<?> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        final Type type2 = new ParameterizedTypeImpl(Set.class, ((ParameterizedType) type).getActualTypeArguments(), null);
        
        final Set<?> set = context.deserialize(json, type2);
        return ImmutableSet.copyOf(set);
    }
}
