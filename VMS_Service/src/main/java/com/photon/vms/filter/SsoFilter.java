package com.photon.vms.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SsoFilter implements Filter{
	
	@Autowired 
	private Environment env;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		// Get init parameter
	}
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		String ssoEnabled = env.getProperty("sso.enabled");

		if(ssoEnabled.equals("false")){
			chain.doFilter(req, res);
    	}else{
    		HttpServletRequest request = (HttpServletRequest) req;
    		String userId= request.getHeader("employeeNumber");
    		//To allowing cross domain access 
    		HttpServletResponse response = (HttpServletResponse) res;
    		response.setHeader("Access-Control-Allow-Origin", "*");
    		response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET,POST,PUT");
    		response.setHeader("Access-Control-Max-Age", "360");
    		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Origin, Content-Type, Accept, access-control-allow-headers, authToken");
    		response.setHeader("Access-Control-Allow-Credentials", "true");
    		
    		if(userId==null||"".equals(userId)||"null".equals(userId)){
    			response.sendError(1000,"Invalid Session");
        		chain.doFilter(req, response);
        	}else{
        		chain.doFilter(req, res);
        	}
    	}
    }
	public void destroy(){
		
	}

}
