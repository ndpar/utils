package com.ndpar.utils.bean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.List;

import org.junit.Test;

public class ExtendedPropertyUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfArgumentsAreNull() {
        ExtendedPropertyUtils.copyBeanProperties(null, null);
    }

    @Test
    public void myTestBeanHasOneDoubleProperty() {
        List<PropertyDescriptor> pds = ExtendedPropertyUtils.getPropertyDescriptors(MyTestBean.class, Double.class);
        assertEquals(1, pds.size());
    }

    @Test
    public void myTestBeanHasNoListProperty() {
        List<PropertyDescriptor> pds = ExtendedPropertyUtils.getPropertyDescriptors(MyTestBean.class, List.class);
        assertEquals(0, pds.size());
    }

    @Test
    public void doublePropertyIsNotReplacedBecauseDoesntMatch() {
        MyTestBean bean = new MyTestBean();
        bean.setDoubleProperty(99D);

        ExtendedPropertyUtils.replacePropertyValue(bean, 12D, 13D);
        assertEquals(99D, bean.getDoubleProperty().doubleValue(), 0.1);
    }

    @Test
    public void doublePropertyIsReplacedProperly() {
        MyTestBean bean = new MyTestBean();
        bean.setDoubleProperty(12D);

        ExtendedPropertyUtils.replacePropertyValue(bean, 12D, 13D);
        assertEquals(13D, bean.getDoubleProperty().doubleValue(), 0.1);
    }

    @Test
    public void doublePropertyIsReplacedByNullProperly() {
        MyTestBean bean = new MyTestBean();
        bean.setDoubleProperty(12D);

        ExtendedPropertyUtils.replacePropertyValue(bean, 12D, null);
        assertNull(bean.getDoubleProperty());
    }

    @Test
    public void doublePropertyNaNIsReplacedProperly() {
        MyTestBean bean = new MyTestBean();
        bean.setDoubleProperty(Double.NaN);

        ExtendedPropertyUtils.replacePropertyValue(bean, Double.NaN, 0D);
        assertEquals(0D, bean.getDoubleProperty().doubleValue(), 0.1);
    }

    @Test
    public void doublePropertyNullIsReplacedProperly() {
        MyTestBean bean = new MyTestBean();

        ExtendedPropertyUtils.replacePropertyValue(bean, null, 0D);
        assertEquals(0D, bean.getDoubleProperty().doubleValue(), 0.1);
    }

    @Test
    public void doublePropertyNullIsReplacedByNaN() {
        MyTestBean bean = new MyTestBean();

        ExtendedPropertyUtils.replacePropertyValue(bean, null, Double.NaN);
        assertEquals(Double.NaN, bean.getDoubleProperty().doubleValue(), 0.1);
    }

    @Test
    public void doublePropertyNaNIsReplacedByNull() {
        MyTestBean bean = new MyTestBean();
        bean.setDoubleProperty(Double.NaN);

        ExtendedPropertyUtils.replacePropertyValue(bean, Double.NaN, null);
        assertNull(bean.getDoubleProperty());
    }

    @Test
    public void integerPropertyNullIsReplacedProperly() {
        MyTestBean bean = new MyTestBean();

        ExtendedPropertyUtils.replacePropertyValue(bean, null, 0);
        assertEquals(0, bean.getIntegerProperty().intValue());
    }

    @Test
    public void integerPropertyIsReplacedByNullProperly() {
        MyTestBean bean = new MyTestBean();
        bean.setIntegerProperty(12);

        ExtendedPropertyUtils.replacePropertyValue(bean, 12, null);
        assertNull(bean.getIntegerProperty());
    }


    public class MyTestBean {
        private Double doubleProperty;
        private Integer integerProperty;

        public Double getDoubleProperty() {
            return doubleProperty;
        }

        public void setDoubleProperty(Double doubleProperty) {
            this.doubleProperty = doubleProperty;
        }

        public Integer getIntegerProperty() {
            return integerProperty;
        }

        public void setIntegerProperty(Integer integerProperty) {
            this.integerProperty = integerProperty;
        }
    }
}
