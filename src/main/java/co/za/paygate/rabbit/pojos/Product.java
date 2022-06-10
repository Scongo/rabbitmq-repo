package co.za.paygate.rabbit.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product{

	@JsonProperty("price")
	private int price;

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private int id;

	@JsonProperty("desc")
	private String desc;

	public int getPrice(){
		return price;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public String getDesc(){
		return desc;
	}
}