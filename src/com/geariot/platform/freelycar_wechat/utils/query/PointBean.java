package com.geariot.platform.freelycar_wechat.utils.query;

import java.util.Date;

public class PointBean {
	
	public PointBean(int point, Date commentDate) {
		super();
		this.point = point;
		this.commentDate = commentDate;
	}
	private int point;
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public Date getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}
	private Date commentDate;
}
