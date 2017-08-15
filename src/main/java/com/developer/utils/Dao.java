package com.developer.utils;

import java.io.IOException;
import java.util.List;

import com.developer.data.Commodity;

public interface Dao {
	public void storeCommodities(String id, double startPrice) throws IOException;
	
	public List<Commodity> getCommodities(String mpn, int availability, int pricesort) throws IOException;
}
