package com.photon.vms.emailTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

	public String getEmailTemplate(String templateName, Map<String,Object> modelMap) throws VelocityException, IOException{
	     String result = null;
	     VelocityEngine velocity = getVelocityEngine();
	    
	     Template template = velocity.getTemplate("/emailTemplate/"+templateName);
	     VelocityContext context = new VelocityContext();
	        
	        Iterator it = modelMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<String,Object> pair = (Map.Entry<String,Object>)it.next();
	            context.put( pair.getKey(), pair.getValue());
	        }
	        StringWriter writer = new StringWriter();
	        template.merge(context, writer);
	        result = writer.toString();

	        return result;
	}
	
	public VelocityEngine getVelocityEngine() throws VelocityException, IOException {
		VelocityEngine velocity = new VelocityEngine();
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocity.init(props);
        return velocity;
    }
}
