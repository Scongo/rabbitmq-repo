package co.za.paygate.rabbit.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Response{

	@JsonProperty("Response")
	private List<ResponseItem> response;

	public List<ResponseItem> getResponse(){
		return response;
	}
}