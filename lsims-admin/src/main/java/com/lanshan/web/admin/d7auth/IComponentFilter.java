package com.lanshan.web.admin.d7auth;

import com.bstek.dorado.view.widget.Component;

public abstract interface IComponentFilter {

	  public abstract void filter(String paramString, Component paramComponent)
			    throws Exception;

	 public abstract boolean support(Component paramComponent);
}
