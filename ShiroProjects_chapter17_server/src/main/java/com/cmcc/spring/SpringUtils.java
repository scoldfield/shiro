package com.cmcc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public final class SpringUtils implements BeanFactoryPostProcessor{

    private static ConfigurableListableBeanFactory beanFactory; //Spring应用上下文环境
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }
    
    /*
     * 获取对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) beanFactory.getBean(name);
    }

    /*
     * 获取类型为requiredType的对象
     */
    public static <T> T getBean(Class<T> clz) {
        T bean = beanFactory.getBean(clz);
        return bean;
    }
    
    /*
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     */
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }
    
    /*
     * 判断以给定名字注册的bean定义是一个singleton还是prototype。如果与给定名字相应的bean定义灭有被找到，蒋慧抛出一个异常(NoSuchBeanDefinitionException)
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }
    
    public static Class<?> getType(String name){
        return beanFactory.getType(name);
    }
    
    /*
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     */
    public static String[] getAliases(String name) {
        return beanFactory.getAliases(name);
    }
}
