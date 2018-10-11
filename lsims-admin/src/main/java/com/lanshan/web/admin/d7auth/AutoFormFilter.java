package com.lanshan.web.admin.d7auth;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.form.FormElement;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;
@org.springframework.stereotype.Component("sm.AutoFormFilter")
public class AutoFormFilter implements IComponentFilter {
	public void filter(String url, com.bstek.dorado.view.widget.Component component) throws Exception {
		boolean authority = true;
		AutoForm form = (AutoForm) component;
		String id = form.getId();
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority) {
			form.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, id);
		}
		if (!authority) {
			form.setReadOnly(true);
		}
		EntityDataType entityDataType = form.getDataType();
		Map dataTypePropertyDefs = null;
		if (entityDataType != null) {
			dataTypePropertyDefs = entityDataType.getPropertyDefs();
		}

		for (Control control : form.getElements())
			filterFormElements(url, control, dataTypePropertyDefs);
	}

	private void filterFormElements(String url, com.bstek.dorado.view.widget.Component component,
			Map<String, PropertyDef> dataTypePropertyDefs) throws Exception {
		if (!(component instanceof FormElement)) {
			return;
		}
		boolean authority = true;
		FormElement element = (FormElement) component;
		String label = getFormElementLabel(element, dataTypePropertyDefs);
		if (StringUtils.isNotEmpty(label)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, label);
		}
		if (!authority) {
			element.setIgnored(true);
			return;
		}

		if (StringUtils.isNotEmpty(label)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, label);
		}
		if (!authority) {
			element.setReadOnly(true);
			return;
		}
		if (!(element instanceof AutoFormElement)) {
			return;
		}
		AutoFormElement autoFormElement = (AutoFormElement) element;
		String name = autoFormElement.getName();
		if (StringUtils.isNotEmpty(name)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, name);
		}
		if (!authority) {
			element.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(name)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, name);
		}
		if (!authority) {
			element.setReadOnly(true);
			return;
		}
	}

	private String getFormElementLabel(FormElement element, Map<String, PropertyDef> dataTypePropertyDefs) {
		String label = element.getLabel();
		if (StringUtils.isNotEmpty(label)) {
			return label;
		}
		String property = element.getProperty();
		if ((StringUtils.isNotEmpty(property)) && (dataTypePropertyDefs != null)) {
			PropertyDef pd = (PropertyDef) dataTypePropertyDefs.get(property);
			if ((pd != null) && (StringUtils.isNotEmpty(pd.getLabel()))) {
				return pd.getLabel();
			}
		}
		return property;
	}

	public boolean support(com.bstek.dorado.view.widget.Component component) {
		return component instanceof AutoForm;
	}
}