package com.ndpar.utils.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Extension for apache commons <code>PropertyUtils</code>.
 * 
 * Two reasons of its existence:
 * - wrap checked exceptions and throw unchecked ones;
 * - provide missing methods.
 * 
 * @author Andrey Paramononov
 * @since 1.0
 */
public abstract class ExtendedPropertyUtils {

    /**
     * <p>Copy property values from the "source" bean to the "destination" bean.
     * 
     * @exception IllegalArgumentException if underlying class throws any exception
     * @see PropertyUtils#copyProperties(Object, Object)
     */
    public static void copyBeanProperties(Object source, Object destination) {
        try {
            PropertyUtils.copyProperties(destination, source);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot merge " + source + " -> " + destination, e);
        }
    }

    /**
     * Retrieve the property descriptors for the specified class and property type.
     * @see PropertyUtils#getPropertyDescriptors(Class)
     */
    @SuppressWarnings("unchecked")
    public static List<PropertyDescriptor> getPropertyDescriptors(Class beanClass, Class propertyClass) {
        PropertyDescriptor[] allPropertyDescriptors = PropertyUtils.getPropertyDescriptors(beanClass);
        List<PropertyDescriptor> result = new ArrayList<PropertyDescriptor>();
        for (PropertyDescriptor propertyDescriptor : allPropertyDescriptors) {
            if (propertyClass.equals(propertyDescriptor.getPropertyType())) {
                result.add(propertyDescriptor);
            }
        }
        return result;
    }

    /**
     * Replace oldValue by newValue on the given bean for all properties of propertyClass class.
     * Supports null for both oldValue and newValue.
     */
    @SuppressWarnings("unchecked")
    public static void replacePropertyValue(Object bean, Class propertyClass, Object oldValue, Object newValue) {
        try {
            List<PropertyDescriptor> pds = getPropertyDescriptors(bean.getClass(), propertyClass);
            for (PropertyDescriptor propertyDescriptor : pds) {
                Method readMethod = propertyDescriptor.getReadMethod();
                Object propertyValue = readMethod.invoke(bean);
                if ((propertyValue == null && oldValue == null) || (propertyValue != null && propertyValue.equals(oldValue))) {
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(bean, newValue);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Replace oldValue by newValue on the given bean for all properties of type Double.
     */
    public static void replacePropertyValue(Object bean, Double oldValue, Double newValue) {
        replacePropertyValue(bean, Double.class, oldValue, newValue);
    }

    /**
     * Replace oldValue by newValue on the given bean for all properties of type Integer.
     */
    public static void replacePropertyValue(Object bean, Integer oldValue, Integer newValue) {
        replacePropertyValue(bean, Integer.class, oldValue, newValue);
    }
}
