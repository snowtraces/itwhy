package com.snowtraces.itwhy.util;

import com.snowtraces.itwhy.dto.common.ParamValidateException;
import org.springframework.beans.BeanUtils;

/**
 * @author snow
 */
public class DataConverter {

    public static <T> T toBean(Object d, Class<T> entityClass) {
        try {
            T entity = entityClass.newInstance();
            BeanUtils.copyProperties(d, entity);
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ParamValidateException(e.getMessage());
        }
    }


}
