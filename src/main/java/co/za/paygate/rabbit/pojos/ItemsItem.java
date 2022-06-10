package co.za.paygate.rabbit.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemsItem{

	@JsonProperty("product")
	private Product product;

	public Product getProduct(){
		return product;
	}
}