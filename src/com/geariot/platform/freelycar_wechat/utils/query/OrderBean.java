package com.geariot.platform.freelycar_wechat.utils.query;

import com.geariot.platform.freelycar_wechat.entities.ProjectInfo;

public class OrderBean {
	private ProjectInfo projectInfo;
	private int remaining;
	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}
	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}
	public int getRemaining() {
		return remaining;
	}
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}
}
