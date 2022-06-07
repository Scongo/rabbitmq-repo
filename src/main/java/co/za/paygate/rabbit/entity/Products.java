package co.za.paygate.rabbit.entity;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by AlecE on 6/15/2017.
 */
@Entity
@Table(name = "ZZZZ_Product")
@NamedQueries({
		@NamedQuery(name = "Products.findByName",
				query = "SELECT product FROM Products product WHERE product.name = :name")
})
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "int")
	private Integer id;

	@Column(name = "PRODUCT_NAME")
	@NotNull
	private String name;


	@Column(name = "PRICE")
	@NotNull
	private Integer price;

	@Column(name = "PRODUCT_DESC", columnDefinition = "text")
	@NotNull
	private String desc;

	@OneToMany(mappedBy = "products", cascade = {CascadeType.ALL})
	Set<OrderItems> orderItems = new HashSet<>();

	public Set<OrderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItems> orderItems) {
		this.orderItems = orderItems;
	}

	public Integer getId() {
		return id;
	}

	public Products setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Products setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getPrice() {
		return price;
	}

	public Products setPrice(Integer price) {
		this.price = price;
		return this;
	}

	public String getDesc() {
		return desc;
	}

	public Products setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public static void saveProduct(EntityManager em, Products products) {
		em.getTransaction().begin();
		em.persist(products);
		em.getTransaction().commit();
	}
	public static List<Products> getProducts(EntityManager em){
		List<Products> productQuery = em.createQuery("from Products", Products.class)
				.getResultList();
		return productQuery;
	}
}
