package com.developer.data;

import java.util.List;

public class Commoditybalance {
	String mpn;
	AnswerStatuses status;
	String errorDescr;
	String id;
	List<Commodity> array;
	
	public String getMpn() {
		return mpn;
	}
	
	public void setMpn(String mpn) {
		this.mpn = mpn;
	}
	
	public AnswerStatuses getStatus() {
		return status;
	}
	
	public void setStatus(AnswerStatuses status) {
		this.status = status;
	}
	
	public String getErrorDescr() {
		return errorDescr;
	}
	
	public void setErrorDescr(String errorDescr) {
		this.errorDescr = errorDescr;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public List<Commodity> getCommodities() {
		return array;
	}
	
	public void setCommodities(List<Commodity> commodities) {
		this.array = commodities;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((array == null) ? 0 : array.hashCode());
		result = prime * result + ((errorDescr == null) ? 0 : errorDescr.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mpn == null) ? 0 : mpn.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Commoditybalance other = (Commoditybalance) obj;
		if (array == null) {
			if (other.array != null)
				return false;
		} else if (!array.equals(other.array))
			return false;
		if (errorDescr == null) {
			if (other.errorDescr != null)
				return false;
		} else if (!errorDescr.equals(other.errorDescr))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mpn == null) {
			if (other.mpn != null)
				return false;
		} else if (!mpn.equals(other.mpn))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Commoditybalance [mpn=" + mpn + ", status=" + status + ", errorDescr=" + errorDescr + ", id=" + id
				+ ", commodities=" + array + "]";
	}
}