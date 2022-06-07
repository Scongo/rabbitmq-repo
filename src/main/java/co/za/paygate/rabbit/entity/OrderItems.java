package co.za.paygate.rabbit.entity;

import javax.persistence.*;

/**
 * Created by AlecE on 6/15/2017.
 */
@Entity
@Table(name = "ZZZZ_Order_Items")
public class OrderItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "int")
	private Integer id;

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "PRODUCT_ID")
	private Products products;

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	public Integer getId() {
		return id;
	}

	public OrderItems setId(Integer id) {
		this.id = id;
		return this;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	//	public static List<OrderItems> findProducts(EntityManager em) {
//		List<OrderItems> results = em.createQuery("SELECT o FROM Products AS o", OrderItems.class)
//				.getResultList();
//		return results;
//	}
}
