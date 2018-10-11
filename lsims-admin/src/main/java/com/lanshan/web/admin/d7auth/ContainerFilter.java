package com.lanshan.web.admin.d7auth;

import java.util.Collection;

import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container; 
@org.springframework.stereotype.Component("sm.ContainerFilter")
public class ContainerFilter
implements IComponentFilter
{
private Collection<IComponentFilter> filters;

public ContainerFilter()
{
	// this.filters = DEBF.getAppContext().getBeansOfType(ApplicationContextAware.class).values();
}

public void filter(String url, Component component)
  throws Exception
{
	/*
  Container container = (Container)component;

  String id = container.getId();
  if (StringUtils.isNotEmpty(id)) {
    boolean authority = SecurityUtils.checkComponent( AuthorityType.read, url, id);
    if (!authority) {
      container.setIgnored(true);
      return;
    }
  }
  List children = container.getChildren();
  Iterator i$ = null;
  Component  com = null;
  if (children != null)
    for (i$ = children.iterator(); i$.hasNext(); ) { com = (com.bstek.dorado.view.widget.Component)i$.next();
      for (IComponentFilter filter : this.filters)
        if (filter.support(com)) {
          filter.filter(url, com);
          break;
        }
    }
  Button button = null;
  if ((container instanceof Panel)) {
    Panel panel = (Panel)container;
    List buttons = panel.getButtons();
    if (buttons == null) {
      return;
    }
    for (i$ = buttons.iterator(); i$.hasNext(); ) { button = (Button)i$.next();
      for (IComponentFilter filter : this.filters)
        if (filter.support(button)) {
          filter.filter(url, button);
          break;
        } }
  }
  */
}

public boolean support(com.bstek.dorado.view.widget.Component component)
{
  return component instanceof Container;
}


}