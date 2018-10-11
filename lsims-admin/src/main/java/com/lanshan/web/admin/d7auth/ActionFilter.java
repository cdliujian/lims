package com.lanshan.web.admin.d7auth;

import org.springframework.util.StringUtils;

import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.action.Action;
@org.springframework.stereotype.Component("sm.ActionFilter")
public class ActionFilter implements IComponentFilter {
	public void filter(String url, Component component) throws Exception {
		Action action = (Action) component;
		boolean authority = true;
		String id = action.getId();
		if (!StringUtils.isEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority) {
			action.setIgnored(true);
			return;
		}
		if (!StringUtils.isEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, id);
		}
		if (!authority) {
			action.setDisabled(true);
			return;
		}
	}

	public boolean support(Component component) {
		return component instanceof Action;
	}
}