package com.cmcc.spring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

public class CustomPathMatchingFilterChainResolver
        extends PathMatchingFilterChainResolver {

    private CustomDefaultFilterChainManager customDefaultFilterChainManager;

    public void setCustomDefaultFilterChainManager(
            CustomDefaultFilterChainManager customDefaultFilterChainManager) {
        this.customDefaultFilterChainManager = customDefaultFilterChainManager;
        setFilterChainManager(customDefaultFilterChainManager);
    }

    // PathMatchingFilterChainResolver的默认流程
    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {

        // 1、首先获取拦截器链管理器
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        // 2、接着获取当前请求的url(不带上下文)
        String requestUrl = getPathWithinApplication(request);

        List<String> chainNames = new ArrayList<String>();
        // the 'chain names' in this implementation are actually path patterns
        // defined by the user. We just use them
        // as the chain name for the FilterChainManager's requirements
        // 3、循环拦截器管理器中的拦截器定义(拦截器链的名字就是URL模式)
        for (String pathPattern : filterChainManager.getChainNames()) {
            // 4、如果当前URL匹配拦截器名字(URL模式)
            // If the path does match, then pass on to the subclass
            // implementation for specific checks:
            if (pathMatches(pathPattern, requestUrl)) {
                // 5、将该URL模式定义的拦截器链添加到List中
                chainNames.add(pathPattern);
            }
        }

        if (chainNames.size() == 0) {
            return null;
        }

        /*
         * 和默认的PathMatchingFilterChainResolver的区别就在这，此处得到所有匹配的拦截器链，然后通过
         * 调用customDefaultFilterChainManager.proxy(originalChain,
         * chainNames)进行合并处理
         */
        return customDefaultFilterChainManager.proxy(originalChain, chainNames);
    }

}
