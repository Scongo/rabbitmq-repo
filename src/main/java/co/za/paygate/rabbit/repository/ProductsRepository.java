package co.za.paygate.rabbit.repository;

import co.za.paygate.rabbit.entity.Products;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ProductsRepository {

        private EntityManager entityManager;

        public ProductsRepository(EntityManager entityManager) {
            this.entityManager = entityManager;
        }
        public Optional<Products> findById(Integer id) {
            Products product = entityManager.find(Products.class, id);
            return product != null ? Optional.of(product) : Optional.empty();
        }
        public List<Products> findAll() {
            return entityManager.createQuery("from Products").getResultList();
        }

        public Optional<Products> save(Products product) {
            try {
                entityManager.getTransaction().begin();
                //entityManager.persist(product);
                entityManager.merge(product);
                entityManager.getTransaction().commit();
                return Optional.of(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }

    public List<Products> findByName(String name) {
        List<Products> product = entityManager.createNamedQuery("Products.findByName", Products.class)
                .setParameter("name", name)
                .getResultList();
        return product;
    }
}
