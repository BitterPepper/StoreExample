package com.developer.data;

public enum AnswerStatuses{
	OK("ok"),
	ERROR("error");
	
	private String descr;
	
	private AnswerStatuses(String descr) {
		this.descr = descr;
	}
	
	@Override
	public String toString() {
		return descr;
	}
}