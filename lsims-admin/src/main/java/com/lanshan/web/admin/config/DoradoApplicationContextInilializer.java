package com.lanshan.web.admin.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.bstek.dorado.core.CommonContext;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.web.loader.DoradoLoader;

public class DoradoApplicationContextInilializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		// TODO Auto-generated method stub
		try {
			Context context = CommonContext.init(applicationContext);
			DoradoLoader.getInstance().setFailSafeContext(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
