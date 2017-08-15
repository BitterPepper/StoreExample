package com.developer.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.developer.data.Commodity;

import redis.clients.jedis.Jedis;

public class DaoRedis implements Dao {
	Jedis jedis;

	public DaoRedis() {
		jedis = new Jedis("localhost");
		System.out.println("Connection to server sucessfully");
		System.out.println("Server is running: " + jedis.ping());
	}

	@Override
	public void storeCommodities(String id, double startPrice) throws IOException {
		int available = 0;
		int providerId = 1;
		String mpn = "com" + id;
		for (int i = 0; i < 5; i++) {
			Commodity comodity = new Commodity(mpn, id, "pr" + providerId++, startPrice += 1.5, available++);
			jedis.lpush(mpn, JsonUtils.toJson(comodity));
			System.out.println("Base filled with key: " + mpn + " and map: " + comodity);
			if (available > 3) {
				available = 0;
			}
		}
	}

	@Override
	public List<Commodity> getCommodities(String mpn, int availability, int pricesort) throws IOException {
		Long mpnLength = jedis.llen(mpn);
		// Better to send to one request to database and after, process fetched
		// data.
		// processing should be inside another method, but here not so much
		// functionality and put here. Processing not optimal.
		List<String> commodityJsonList = jedis.lrange(mpn, 0, mpnLength);
		List<Commodity> commodityList = new ArrayList<>();
		for (String commodityJson : commodityJsonList) {
			Commodity commodity = JsonUtils.readValue(Commodity.class, commodityJson);
			if (availability > 0 && commodity.getStock().getDescr() < availability) {
				continue;
			}
			commodityList.add(commodity);
		}

		if (pricesort > 0) {
			if (pricesort == 1) {
				Collections.sort(commodityList, new Comparator<Commodity>() {
					@Override
					public int compare(Commodity o1, Commodity o2) {
						return Double.compare(o1.getPrice(), o2.getPrice());
					}});
			} else {
				Collections.sort(commodityList, new Comparator<Commodity>() {
					@Override
					public int compare(Commodity o1, Commodity o2) {
						return (Double.compare(o2.getPrice(), o1.getPrice()));
					}});
			}
		}
		return commodityList;
	}

	public Jedis getJedis() {
		return jedis;
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}
}