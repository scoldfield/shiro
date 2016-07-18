package com.cmcc.spring;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.Nameable;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;
import org.springframework.util.CollectionUtils;

/*
 * 拦截器链(FilterChain)管理器
 * 
 * DefaultFilterChainManager的工作原理
 * 
 * 1、类似/authenticated = authc中，/authenticated 就是拦截器链， authc 就是拦截器名字
 * 2、DefaultFilterChainManager内部使用Map来管理URL模式-拦截器链的关系
 * 3、相同的URL模式只能定义一个拦截器链
 * 4、多个拦截器链都匹配时是无序的(因为使用map.keySet()获取拦截器链的名字)
 * 
 * DefaultFilterChainManager的主要功能：
 * 1、注册拦截器
 * 2、注册拦截器链
 * 3、对原始拦截器链生成代理之后的拦截器链
 */
public class CustomDefaultFilterChainManager extends DefaultFilterChainManager{

    //用于存储如 ShiroFilterFactoryBean 在配置文件中配置的拦截器链定义，即可以认为是默认的静态拦截器链；会自动与数据库中加载的合并
    private Map<String, String> filtersChainDefinitionMap = null;
    
    //登录地址、登录成功后默认跳转地址、未授权跳转地址，用于给相应拦截器的
    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;
    
    //调用其构造器时会自动注册默认的拦截器
    public CustomDefaultFilterChainManager() {
        setFilters(new LinkedHashMap<String, Filter>());
        setFilterChains(new LinkedHashMap<String, NamedFilterList>());
        addDefaultFilters(false);
    }
    
    public Map<String, String> getFiltersChainDefinitionMap() {
        return filtersChainDefinitionMap;
    }

    public void setFiltersChainDefinitionMap(
            Map<String, String> filtersChainDefinitionMap) {
        this.filtersChainDefinitionMap = filtersChainDefinitionMap;
    }

    //注册我们自定义的拦截器；如 ShiroFilterFactoryBean 的 filters 属性
    public void setCustomFilters(Map<String, Filter> customFilters) {
        for(Map.Entry<String, Filter> entry : customFilters.entrySet()) {
            //调用父类的addFilter()方法，将自定义的拦截器添加到filters的Map中
            addFilter(entry.getKey(), entry.getValue());
        }
    }
    
    //拦截器链的解析。从配置文件中解析出拦截器链配置，解析为相应的拦截器链
    public void setDefaultFilterChainDefinitions(String definitions) {
        Ini ini = new Ini();
        ini.load(definitions);
        //did they explicitly state a 'urls' section? Not necessary, but just in case
        Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if(CollectionUtils.isEmpty(section)) {
            //no urls section. Since this _is_ a urls chain definition property, just assume a
            //default section contains only the definitions:
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        setFiltersChainDefinitionMap(section);
    }
    
    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }
    
    /*
     * 初始化方法，spring容器启动时调用。
     * 1、首先，它会自动给相应的拦截器设置如loginUrl、successUrl、unauthorizedUrl
     * 2、其次，根据filterChainDefinitionMap构建默认的拦截器链
     */
    @PostConstruct      //构造函数执行后就执行该方法
    public void init() {
        //Apply the acquired and/or configured filters
        Map<String, Filter> filters = getFilters();
        if(!CollectionUtils.isEmpty(filters)) {
            for(Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                applyGlobalPropertiesIfNeccessary(filter);
                if(filter instanceof Nameable) {
                    ((Nameable)filter).setName(name);
                }
            }
        }
        
        //build up the chains
        Map<String, String> chains = getFiltersChainDefinitionMap();
        if(!CollectionUtils.isEmpty(chains)) {
            for(Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                createChain(url, chainDefinition);
            }
        }
    }
    
    //此处我们忽略实现 initFilter，因为交给 spring 管理了，所以 Filter 的相关配置会在 Spring 配置中完成
    @Override
    protected void initFilter(Filter filter) {
        //ignore
    }
    
    //组合多个拦截器链为一个生成一个新的 FilterChain 代理
    public FilterChain proxy(FilterChain original, List<String> chainNames) {
        NamedFilterList configured = new SimpleNamedFilterList(chainNames.toString());
        for(String chainName : chainNames) {
            configured.addAll(getChain(chainName));
        }
        return configured.proxy(original);
    }
    
    private void applyLoginUrlIfNecessary(Filter filter) {
        String loginUrl = getLoginUrl();
        if(StringUtils.hasText(loginUrl) && (filter instanceof AccessControlFilter)) {
            AccessControlFilter acFilter = (AccessControlFilter) filter;
            //only apply the login url if they haven't explicityly configured one already
            String existingLoginUrl = acFilter.getLoginUrl();
            if(AccessControlFilter.DEFAULT_LOGIN_URL.equals(existingLoginUrl)) {
                acFilter.setLoginUrl(loginUrl);
            }
        }
    }
    
    private void applySuccessUrlIfNecessary(Filter filter) {
        String successUrl = getSuccessUrl();
        if(StringUtils.hasText(successUrl) && (filter instanceof AuthenticationFilter)) {
            AuthenticationFilter authcFilter = (AuthenticationFilter) filter;
            //only apply the login url if they haven't explicityly configured one already
            String exitingSuccessUrl = authcFilter.getSuccessUrl();
            if(AuthenticationFilter.DEFAULT_SUCCESS_URL.equals(exitingSuccessUrl)) {
                authcFilter.setSuccessUrl(successUrl);
            }
        }
    }
    
    private void applyUnauthorizedUrlIfNecessary(Filter filter) {
        String unauthorizedUrl = getUnauthorizedUrl();
        if (StringUtils.hasText(unauthorizedUrl) && (filter instanceof AuthorizationFilter)) {
            AuthorizationFilter authzFilter = (AuthorizationFilter) filter;
            //only apply the unauthorizedUrl if they haven't explicitly configured one already:
            String existingUnauthorizedUrl = authzFilter.getUnauthorizedUrl();
            if (existingUnauthorizedUrl == null) {
                authzFilter.setUnauthorizedUrl(unauthorizedUrl);
            }
        }
    }
    
    private void applyGlobalPropertiesIfNeccessary(Filter filter) {
        applyLoginUrlIfNecessary(filter);
        applySuccessUrlIfNecessary(filter);
        applyUnauthorizedUrlIfNecessary(filter);
    }
}
