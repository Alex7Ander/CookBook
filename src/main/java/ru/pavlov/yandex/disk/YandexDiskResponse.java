package ru.pavlov.yandex.disk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexDiskResponse {
	private String operation_id; 
	private String href;
	private String method; 
	private String templated;
	public String getOperation_id() {
		return operation_id;
	}
	public void setOperation_id(String operation_id) {
		this.operation_id = operation_id;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getTemplated() {
		return templated;
	}
	public void setTemplated(String templated) {
		this.templated = templated;
	}
} 

//{"message":"�� ������� ����� ����������� ������.","description":"Resource not found.","error":"DiskNotFoundError"}