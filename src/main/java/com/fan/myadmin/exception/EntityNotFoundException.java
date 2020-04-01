package com.fan.myadmin.exception;

import org.springframework.util.StringUtils;

/**
 * @author fanweiwei
 * @create 2020-03-29 13:27
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class clazz,String field,String val){
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), field, val));
    }
    public static String generateMessage(String entity, String field, String val){
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " does not exist";
    }
}
