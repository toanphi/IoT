package com.uet.iot.base;

public class BaseResponse{
	
	public BaseResponse(Object data) {
		this.data = data;
	}
	public BaseResponse() {}
	private Object data;

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
