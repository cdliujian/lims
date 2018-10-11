package com.lanshan.web.admin.d7auth;



import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.widget.tree.AbstractTree; 
@org.springframework.stereotype.Component("sm.TreeFilter")
public class TreeFilter implements IComponentFilter {
	public void filter(String url, com.bstek.dorado.view.widget.Component component) throws Exception {
		AbstractTree tree = (AbstractTree) component;
		boolean authority = true;
		String id = tree.getId();
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority)
			tree.setIgnored(true);
	}

	public boolean support(com.bstek.dorado.view.widget.Component component) {
		return component instanceof AbstractTree;
	}
}