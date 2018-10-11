package com.lanshan.web.admin.d7auth;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.lanshan.core.business.IUser;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.core.util.SpringUtils;


public class GlobalComponentListener extends GenericObjectListener<View> implements ApplicationContextAware {
	private Collection<IComponentFilter> componentFilterList;

	static boolean init=false;
	public boolean beforeInit(View view) throws Exception {
		return true;
	}
	@Value("${spring.application.name}")
	 String appName;

	public void onInit(View view) throws Exception {
//		view.addClientEventListener("onReady", ClientEventOnReady.getOnReadyClientEvent());
		if(!init && componentFilterList.isEmpty()){
			componentFilterList=SpringUtils.getApplicationContext().getBeansOfType(IComponentFilter.class).values();
			init=true;
		}
		
		
		if ("debf".equals(appName))
			return;
		try {
			IUser user = SessionUtil.getSecurityUser();
			if (user == null)
				return;

		} catch (Exception e) {
			return;
		} 
		String url = getRequestURLPath();
//		url="http://127.0.0.1:8780/trans5"+url;
		Map map = ComponentMetadataSource.getMetadata(url);
		if (map == null) {
			url = url.substring(1);
			map = ComponentMetadataSource.getMetadata(url);
		}
		if (map == null)
			return;
		filterComponents(view, url);
	}

	private void filterComponents(ViewElement viewElement, String url) throws Exception {
		if ((viewElement == null) || (viewElement.getInnerElements() == null)
				|| (viewElement.getInnerElements().size() == 0)) {
			return;
		} 
		
		for (ViewElement element : viewElement.getInnerElements()) {
			com.bstek.dorado.view.widget.Component component;
			if ((element instanceof com.bstek.dorado.view.widget.Component)) {
				component = (com.bstek.dorado.view.widget.Component) element;
				for (IComponentFilter filter : this.componentFilterList) {
					if (filter.support(component)) {
						filter.filter(url, component);
					}
				}
			}
			filterComponents(element, url);
		}
	}

	private String getRequestURLPath() {

		String url = SessionUtil.getRequest().getServletPath();
		if (SessionUtil.getRequest().getPathInfo() != null) {
			url = url + SessionUtil.getRequest().getPathInfo();
		}
		return url;
	}

	@Override
	public void setApplicationContext(ApplicationContext app) throws BeansException {
		// TODO Auto-generated method stub
		componentFilterList = app.getBeansOfType(IComponentFilter.class).values();
	}

}