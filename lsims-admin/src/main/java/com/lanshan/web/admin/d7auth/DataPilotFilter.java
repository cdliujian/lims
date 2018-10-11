package com.lanshan.web.admin.d7auth;



import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.widget.datacontrol.DataPilot;
@org.springframework.stereotype.Component("sm.DataPilotFilter")
public class DataPilotFilter implements IComponentFilter {
	public void filter(String url, com.bstek.dorado.view.widget.Component component) throws Exception {
		DataPilot dataPilot = (DataPilot) component;
		String id = dataPilot.getId();
		boolean authority = true;
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(AuthorityType.read, url, id);
		}
		if (!authority) {
			dataPilot.setIgnored(true);
			return;
		}
		setSubControlAuth(url, dataPilot);
	}

	private void setSubControlAuth(String url, DataPilot pilot) throws Exception {
		String itemCodes = pilot.getItemCodes();
		if (itemCodes == null) {
			return;
		}
		if (itemCodes.contains("+")) {
			boolean addCaptionCNPermission_read = SecurityUtils.checkComponent(AuthorityType.read, url, "添加");

			boolean addCaptionENPermission_read = SecurityUtils.checkComponent(AuthorityType.read, url, "Insert");

			if ((!addCaptionCNPermission_read) || (!addCaptionENPermission_read)) {
				setSubControlNoAuth("+", pilot);
			}
		}
		if (itemCodes.contains("-")) {
			boolean delCaptionCNPermission_read = SecurityUtils.checkComponent(AuthorityType.read, url, "删除");

			boolean delCaptionENPermission_read = SecurityUtils.checkComponent(AuthorityType.read, url, "Delete");

			if ((!delCaptionCNPermission_read) || (!delCaptionENPermission_read)) {
				setSubControlNoAuth("-", pilot);
			}
		}
		if (itemCodes.contains("x")) {
			boolean cancelCaptionCNPermission_read = SecurityUtils.checkComponent(AuthorityType.read, url, "取消");

			boolean cancelCaptionENPermission_read = SecurityUtils.checkComponent(AuthorityType.read, url, "Cancel");

			if ((!cancelCaptionCNPermission_read) || (!cancelCaptionENPermission_read)) {
				setSubControlNoAuth("x", pilot);
			}
		}
	}

	private void setSubControlNoAuth(String code, DataPilot pilot) {
		String temp1 = "," + code;
		String temp2 = code + ",";
		String itemCodes = pilot.getItemCodes();
		if (itemCodes.contains(temp1)) {
			String newItemCodes = itemCodes.replace(temp1, "");
			pilot.setItemCodes(newItemCodes);
		} else if (itemCodes.contains(temp2)) {
			String newItemCodes = itemCodes.replace(temp2, "");
			pilot.setItemCodes(newItemCodes);
		}
	}

	public boolean support(com.bstek.dorado.view.widget.Component component) {
		return component instanceof DataPilot;
	}
}