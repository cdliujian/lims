package com.lanshan.web.admin.d7auth;



import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.widget.form.AbstractEditor;
@org.springframework.stereotype.Component("sm.EditorFilter")
public class EditorFilter implements IComponentFilter {
	public void filter(String url, com.bstek.dorado.view.widget.Component component) throws Exception {
		AbstractEditor editor = (AbstractEditor) component;
		String id = editor.getId();
		boolean authority = true;
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority) {
			editor.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, id);
		}
		if (!authority)
			editor.setReadOnly(true);
	}

	public boolean support(com.bstek.dorado.view.widget.Component component) {
		return component instanceof AbstractEditor;
	}
}