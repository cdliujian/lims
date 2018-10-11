package com.lanshan.web.admin.d7auth;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
 
@Component
public class ComponentLoad implements InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub		
		System.out.println("========================执行====ComponentLoad afterPropertiesSet=========================================");
		try {
//			因为不拆分的情况下，此时spring的bean还没有初始化，不适用这种初始化数据的方式
//			UrlMetadataSource.init();
//			ComponentMetadataSource.init();	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
