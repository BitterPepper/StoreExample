package com.developer.utils;

import java.io.IOException;

public class FillDatabase {
	public static void main(String[] args) throws IOException {
		System.out.println("Start of filling database");

		Dao dao = new DaoRedis(); 

		dao.storeCommodities("1", 90);
		dao.storeCommodities("2", 150);
		dao.storeCommodities("3", 30);

		System.out.println("Operation successful");
	}
}
