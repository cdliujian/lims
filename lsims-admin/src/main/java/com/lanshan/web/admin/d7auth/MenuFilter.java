package com.lanshan.web.admin.d7auth;



import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.widget.HideMode;
import com.bstek.dorado.view.widget.base.menu.BaseMenuItem;
import com.bstek.dorado.view.widget.base.menu.Menu;
import com.bstek.dorado.view.widget.base.menu.MenuItem;
import com.bstek.dorado.view.widget.base.toolbar.MenuButton;
@org.springframework.stereotype.Component("sm.MenuFilter")
public class MenuFilter implements IComponentFilter {
	public void filter(String url, com.bstek.dorado.view.widget.Component component) throws Exception {
		Menu menu = (Menu) component;
		String id = menu.getId();
		boolean authority = true;
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority) {
			menu.setIgnored(true);
			return;
		}
		boolean f = true;
		for (BaseMenuItem menuItem : menu.getItems()) {
			filterMenuItem(url, menuItem);
			if (menuItem instanceof MenuItem) {
				MenuItem item = (MenuItem) menuItem;
				if (!item.isIgnored()) {
					f = false;
				}
			}
		}
		if (f) {
			menu.setVisible(false);
			menu.setHideMode(HideMode.display);
			menu.setIgnored(true);
			ViewElement e = menu.getParent();
			if (e instanceof MenuButton) {
				MenuButton b = (MenuButton) e;
				b.setIgnored(true);
			}
			return;
		}

	}

	private void filterMenuItem(String url, BaseMenuItem menuItem) throws Exception {
		if (!(menuItem instanceof MenuItem)) {
			return;
		}
		MenuItem item = (MenuItem) menuItem;
		boolean authority = true;
		String caption = item.getCaption();
		if (StringUtils.isNotEmpty(caption)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, caption);
		}
		if (!authority) {
			item.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(caption)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, caption);
		}
		if (!authority) {
			item.setDisabled(true);
			return;
		}

		String name = item.getName();
		if (StringUtils.isNotEmpty(name)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, name);
		}
		if (!authority) {
			item.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(name)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, name);
		}
		if (!authority) {
			item.setDisabled(true);
			return;
		}

		if (item.getItems() != null)
			for (BaseMenuItem mi : item.getItems())
				filterMenuItem(url, mi);
	}

	public boolean support(com.bstek.dorado.view.widget.Component component) {
		return component instanceof Menu;
	}
}
