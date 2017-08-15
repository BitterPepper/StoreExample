package com.developer.data;

public enum AvailabilityStatuses {
	No(0),
	FEW(1),
	ENOUGH(2);
	
	private int descr;
	
	private AvailabilityStatuses(int descr) {
		this.descr = descr;
	}
	
	@Override
	public String toString() {
		return String.valueOf(descr);
	}
	
	public int getDescr() {
		return descr;
	}

	public static AvailabilityStatuses valueOf(int value){
		if (value == 0){
			return AvailabilityStatuses.No;
		} else if (value == 1){
			return AvailabilityStatuses.FEW;
		} else {
			return AvailabilityStatuses.ENOUGH;
		}
	}
}
