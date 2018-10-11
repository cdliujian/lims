package com.lanshan.web.admin.d7auth;



import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.widget.base.toolbar.ToolBar;
@org.springframework.stereotype.Component("sm.ToolbarFilter")
public class ToolbarFilter implements IComponentFilter {
	public void filter(String url, com.bstek.dorado.view.widget.Component component) throws Exception {
		ToolBar toolbar = (ToolBar) component;
		boolean authority = true;
		String id = toolbar.getId();
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority) {
			toolbar.setIgnored(true);
			return;
		}
	}

	public boolean support(com.bstek.dorado.view.widget.Component component) {
		return component instanceof ToolBar;
	}

}