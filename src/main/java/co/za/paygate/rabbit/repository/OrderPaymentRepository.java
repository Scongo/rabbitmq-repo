package co.za.paygate.rabbit.repository;

import co.za.paygate.rabbit.entity.OrderPayment;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class OrderPaymentRepository {

        private EntityManager entityManager;

        public OrderPaymentRepository(EntityManager entityManager) {
            this.entityManager = entityManager;
        }
        public Optional<OrderPayment> findById(Integer id) {
            OrderPayment orderPayment = entityManager.find(OrderPayment.class, id);
            return orderPayment != null ? Optional.of(orderPayment) : Optional.empty();
        }
        public List<OrderPayment> findAll() {
            return entityManager.createQuery("from OrderPayment").getResultList();
        }

        public Optional<OrderPayment> save(OrderPayment orderPayment) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(orderPayment);
                entityManager.getTransaction().commit();
                return Optional.of(orderPayment);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }
}
