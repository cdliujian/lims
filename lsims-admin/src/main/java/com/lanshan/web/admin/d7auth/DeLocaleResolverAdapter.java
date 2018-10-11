package com.lanshan.web.admin.d7auth;

import java.util.Locale;

import com.bstek.dorado.core.resource.LocaleResolver;

public class DeLocaleResolverAdapter implements LocaleResolver  {

	@Override
	public Locale resolveLocale() throws Exception {
		// TODO Auto-generated method stub
		return Locale.CHINA;
	}
}
