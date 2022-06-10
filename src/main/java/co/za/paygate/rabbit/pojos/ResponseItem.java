package co.za.paygate.rabbit.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseItem{

	@JsonProperty("amount")
	private int amount;

	@JsonProperty("orderId")
	private String orderId;

	@JsonProperty("created")
	private long created;

	@JsonProperty("items")
	private List<ItemsItem> items;

	@JsonProperty("desc")
	private String desc;

	@JsonProperty("payments")
	private List<PaymentsItem> payments;

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public void setItems(List<ItemsItem> items) {
		this.items = items;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setPayments(List<PaymentsItem> payments) {
		this.payments = payments;
	}

	public int getAmount(){
		return amount;
	}

	public String getOrderId(){
		return orderId;
	}

	public long getCreated(){
		return created;
	}

	public List<ItemsItem> getItems(){
		return items;
	}

	public String getDesc(){
		return desc;
	}

	public List<PaymentsItem> getPayments(){
		return payments;
	}

}