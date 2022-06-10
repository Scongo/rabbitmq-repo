package co.za.paygate.rabbit.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentsItem{

	@JsonProperty("amount")
	private int amount;

	@JsonProperty("statusDesc")
	private String statusDesc;

	@JsonProperty("status")
	private String status;

	public int getAmount(){
		return amount;
	}

	public String getStatusDesc(){
		return statusDesc;
	}

	public String getStatus(){
		return status;
	}
}