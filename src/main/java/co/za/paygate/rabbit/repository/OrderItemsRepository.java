package co.za.paygate.rabbit.repository;

import co.za.paygate.rabbit.entity.OrderItems;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class OrderItemsRepository {

        private EntityManager entityManager;

        public OrderItemsRepository(EntityManager entityManager) {
            this.entityManager = entityManager;
        }
        public Optional<OrderItems> findById(Integer id) {
            OrderItems orderItem = entityManager.find(OrderItems.class, id);
            return orderItem != null ? Optional.of(orderItem) : Optional.empty();
        }
        public List<OrderItems> findAll() {
            return entityManager.createQuery("from OrderItems").getResultList();
        }

        public Optional<OrderItems> save(OrderItems orderItem) {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(orderItem);
                entityManager.getTransaction().commit();
                return Optional.of(orderItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }
}
