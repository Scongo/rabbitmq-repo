package co.za.paygate.rabbit.entity;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;

import java.util.List;

/**
 * Created by AlecE on 6/15/2017.
 * Edited by Mthobisi
 */
@Entity
@Table(name = "ZZZZ_Order_Payments")
public class OrderPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "int")
	private Integer id;

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	@Column(name = "STATUS")
	@NotNull
	private Integer status;

	@Column(name = "STATUS_DESC")
	@NotNull
	private String statusDesc;

	@Column(name = "AMOUNT")
	@NotNull
	private Integer amount;

	public Order getOrders() {
		return order;
	}

	public void setOrders(Order order) {
		this.order = order;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public static void savePayment(EntityManager em, OrderPayment orderPayment) {
		em.getTransaction().begin();
		em.merge(orderPayment);
		em.getTransaction().commit();
	}
	public static List<OrderPayment> getOrder(EntityManager em){
		List<OrderPayment> payQuery = em.createQuery("from OrderPayment", OrderPayment.class)
				.getResultList();
		return payQuery;
	}
}
