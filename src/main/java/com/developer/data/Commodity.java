package com.developer.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Commodity {
	String mpn;
	String id;
	String providerId;
	Double price;
	AvailabilityStatuses stock;
	
	public Commodity() {
	}

	public Commodity(String mpn, String id, String providerId, Double price, int stock) {
		super();
		this.mpn = mpn;
		this.id = id;
		this.providerId = providerId;
		this.price = price;
		this.stock = AvailabilityStatuses.valueOf(stock);
	}

	public String getMpn() {
		return mpn;
	}
	
	public void setMpn(String mpn) {
		this.mpn = mpn;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getProviderId() {
		return providerId;
	}
	
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public AvailabilityStatuses getStock() {
		return stock;
	}
	
	public void setStock(AvailabilityStatuses stock) {
		this.stock = stock;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mpn == null) ? 0 : mpn.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((providerId == null) ? 0 : providerId.hashCode());
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
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
		Commodity other = (Commodity) obj;
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
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (providerId == null) {
			if (other.providerId != null)
				return false;
		} else if (!providerId.equals(other.providerId))
			return false;
		if (stock != other.stock)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Commodity [mpn=" + mpn + ", id=" + id + ", providerId=" + providerId + ", price=" + price + ", stock="
				+ stock + "]";
	}
}
