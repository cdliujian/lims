package com.lanshan.web.admin.d7auth;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.ColumnGroup;
import com.bstek.dorado.view.widget.grid.DataColumn;
import com.bstek.dorado.view.widget.grid.DataGrid;
import com.bstek.dorado.view.widget.grid.GridSupport;
@org.springframework.stereotype.Component("sm.GridFilter")
public class GridFilter implements IComponentFilter {
	public void filter(String url, com.bstek.dorado.view.widget.Component component) throws Exception {
		GridSupport grid = (GridSupport) component;
		String id = grid.getId();
		boolean authority = true;
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority) {
			grid.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, id);
		}
		if (!authority) {
			grid.setReadOnly(true);
		}

		Map dataTypePropertyDefs = null;
		if ((grid instanceof DataGrid)) {
			DataGrid dataGrid = (DataGrid) grid;
			EntityDataType entityDataType = dataGrid.getDataType();
			if (entityDataType != null)
				dataTypePropertyDefs = entityDataType.getPropertyDefs();
		}
		for (Column col : grid.getColumns())
			if ((col instanceof ColumnGroup))
				filterGroupColumn(url, (ColumnGroup) col, dataTypePropertyDefs);
			else
				filterDataColumn(url, col, dataTypePropertyDefs);
	}

	private void filterDataColumn(String url, Column column, Map<String, PropertyDef> dataTypePropertyDefs)
			throws Exception {
		String name = column.getName();
		boolean authority = true;
		if (StringUtils.isNotEmpty(name)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, name);
		}
		if (!authority) {
			column.setIgnored(true);
			return;
		}
		if (((column instanceof DataColumn)) && (StringUtils.isNotEmpty(name))) {
			DataColumn dataColumn = (DataColumn) column;
			authority = SecurityUtils.checkComponent(AuthorityType.write, url, name);
			if (!authority) {
				dataColumn.setReadOnly(true);
				return;
			}
		}
		if ((column instanceof DataColumn)) {
			DataColumn dataColumn = (DataColumn) column;
			String caption = column.getCaption();
			if ((!StringUtils.isNotEmpty(column.getCaption())) && (dataTypePropertyDefs != null)) {
				PropertyDef propertydef = (PropertyDef) dataTypePropertyDefs.get(dataColumn.getProperty());
				if (propertydef != null) {
					caption = propertydef.getLabel();
				}
			}
			if (StringUtils.isNotEmpty(caption)) {
				authority = SecurityUtils.checkComponent(AuthorityType.read, url, caption);
			}
			if (!authority) {
				dataColumn.setIgnored(true);
				return;
			}
			if (StringUtils.isNotEmpty(caption)) {
				authority = SecurityUtils.checkComponent(AuthorityType.write, url, caption);
			}
			if (!authority) {
				dataColumn.setReadOnly(true);
				return;
			}
		}
	}

	private void filterGroupColumn(String url, ColumnGroup columnGroup, Map<String, PropertyDef> dataTypePropertyDefs)
			throws Exception {
		String caption = columnGroup.getCaption();
		boolean authority = true;
		if (StringUtils.isNotEmpty(caption)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, caption);
		}
		if (!authority) {
			columnGroup.setIgnored(true);
		}
		for (Column column : columnGroup.getColumns())
			if ((column instanceof ColumnGroup))
				filterGroupColumn(url, (ColumnGroup) column, dataTypePropertyDefs);
			else
				filterDataColumn(url, column, dataTypePropertyDefs);
	}

	public boolean support(com.bstek.dorado.view.widget.Component component) {
		return component instanceof GridSupport;
	}
}
