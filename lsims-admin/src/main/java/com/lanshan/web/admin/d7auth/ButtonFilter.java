package com.lanshan.web.admin.d7auth;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.view.widget.HideMode;
import com.bstek.dorado.view.widget.base.AbstractButton;
import com.bstek.dorado.view.widget.base.Button;

@Component("sm.ButtonFilter")
public class ButtonFilter implements IComponentFilter {
	public void filter(String url, com.bstek.dorado.view.widget.Component component) throws Exception {
		AbstractButton button = (AbstractButton) component;
		button.setHideMode(HideMode.display);
		boolean authority = true;
		String id = button.getId();
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority) {
			button.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, id);
		}
		if (!authority) {
			button.setDisabled(true);
			return;
		}
		if (!(button instanceof Button)) {
			return;
		}
		Button b = (Button) button;
		String caption = b.getCaption();
		if (StringUtils.isNotEmpty(caption)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, caption);
		}
		if (!authority) {
			button.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(caption)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, caption);
		}
		if (!authority) {
			button.setDisabled(true);
			return;
		}
	}

	public boolean support(com.bstek.dorado.view.widget.Component component) {
		return component instanceof AbstractButton;
	}

}