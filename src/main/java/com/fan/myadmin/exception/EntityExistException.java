package com.fan.myadmin.exception;

import org.springframework.util.StringUtils;

/**
 * @author fanweiwei
 * @create 2020-03-29 14:41
 */
public class EntityExistException extends RuntimeException {
    public EntityExistException(Class clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " existed";
    }
}
