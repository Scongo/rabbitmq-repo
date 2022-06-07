package co.za.paygate.rabbit.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by AlecE on 6/15/2017.
 */
@Entity
@Table(name = "ZZZZ_Orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "int")
	private Integer id;

	@Column(name = "order_no")
	private String orderNumber;


	@Column(name = "AMOUNT")
	private Integer amount;

	@Column(name = "order_desc")
	private String desc;

	@Column(name = "order_status")
	private Integer status;

	@CreationTimestamp
	@Column(name = "date_created", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	Set<OrderPayment> orderPayments = new HashSet<>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	Set<OrderItems> orderItems = new HashSet<>();

	public Set<OrderPayment> getOrderPayments() {
		return orderPayments;
	}

	public void setOrderPayments(Set<OrderPayment> orderPayments) {
		this.orderPayments = orderPayments;
	}

	public Set<OrderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItems> orderItems) {
		this.orderItems = orderItems;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public static void saveOrder(EntityManager em, Order order) {
			em.getTransaction().begin();
			em.merge(order);
			em.getTransaction().commit();
	}
	public static List<Order> getOrder(EntityManager em){
		List<Order> orderQuery = em.createQuery("from Order", Order.class)
				.getResultList();
		return orderQuery;
	}
}
