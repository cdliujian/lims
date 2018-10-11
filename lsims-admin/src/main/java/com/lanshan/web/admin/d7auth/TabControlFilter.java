package com.lanshan.web.admin.d7auth;



import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.widget.base.tab.AbstractTabControl;
import com.bstek.dorado.view.widget.base.tab.Tab;

public class TabControlFilter
implements IComponentFilter
{
public void filter(String url, com.bstek.dorado.view.widget.Component component)
  throws Exception
{
  AbstractTabControl tabControl = (AbstractTabControl)component;
  String id = tabControl.getId();
  boolean authority = true;
  if (StringUtils.isNotEmpty(id)) {
    authority = SecurityUtils.checkComponent( AuthorityType.read, url, id);
  }
  if (!authority) {
    tabControl.setIgnored(true);
    return;
  }
  for (Tab tab : tabControl.getTabs()) {
    String caption = tab.getCaption();
    if (StringUtils.isNotEmpty(caption)) {
      authority = SecurityUtils.checkComponent( AuthorityType.read, url, caption);
    }
    if (!authority) {
      tab.setIgnored(true);
    }
    else {
      if (StringUtils.isNotEmpty(caption)) {
        authority = SecurityUtils.checkComponent( AuthorityType.write, url, caption);
      }
      if (!authority)
        tab.setDisabled(true); 
    }
  }
}

public boolean support(com.bstek.dorado.view.widget.Component component) { return component instanceof AbstractTabControl; }

}