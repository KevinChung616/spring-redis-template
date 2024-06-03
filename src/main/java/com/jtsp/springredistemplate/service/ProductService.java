package com.jtsp.springredistemplate.service;

import com.jtsp.springredistemplate.repository.ProductCacheRepository;
import com.jtsp.springredistemplate.entity.Product;
import com.jtsp.springredistemplate.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * The Cache-Aside Pattern is a caching strategy used to maintain the consistency between
 * a cache and the underlying data store. This pattern helps to improve the performance
 * of an application by reducing the load on the data store and providing faster access
 * to frequently accessed data. The Cache-Aside Pattern works as follows:
 *
 * 1. **Read Operation:**
 *    - When an application needs to read data, it first checks the cache.
 *    - If the data is present in the cache (cache hit), it returns the data directly from the cache.
 *    - If the data is not present in the cache (cache miss), it reads the data from the data store,
 *      stores a copy in the cache for future access, and then returns the data to the caller.
 *
 * 2. **Write Operation:**
 *    - When an application needs to update data, it first updates the data in the data store.
 *    - After updating the data store, the cache is invalidated to ensure consistency.
 *    - This ensures that subsequent reads will fetch the most up-to-date data.
 *
 * Key advantages of the Cache-Aside Pattern include:
 * - **Improved Performance:** By caching frequently accessed data, the pattern reduces the
 *   load on the data store and improves the response time for read operations.
 * - **Scalability:** The pattern allows the application to scale by distributing the load
 *   between the cache and the data store.
 * - **Simplicity:** The Cache-Aside Pattern is simple to implement and integrates well with
 *   most data stores and caching solutions.
 *
 * However, there are some considerations:
 * - **Cache Invalidation:** Ensuring cache consistency can be challenging, especially in
 *   distributed systems where multiple instances might be updating the same data.
 * - **Stale Data:** There is a risk of serving stale data if the cache is not invalidated
 *   correctly after a write operation.
 *
 * Example Scenario:
 * Suppose an application is retrieving user profiles. When a user profile is requested,
 * the application first checks if the profile is available in the cache. If not, it fetches
 * the profile from the database, stores it in the cache, and then returns it to the user.
 * When the user profile is updated, the application updates the database and invalidates
 * the cached profile to ensure consistency.
 *
 * In Java, frameworks like Spring Cache or libraries like Caffeine can be used to implement
 * the Cache-Aside Pattern effectively.
 */

@Service
public class ProductService {
    private final ProductRepository productRepository;

//    @Qualifier("product-cache-repository")
    private final ProductCacheRepository cacheRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCacheRepository cacheRepository) {
        this.productRepository = productRepository;
        this.cacheRepository = cacheRepository;
    }

//    @Autowired
//    public ProductService(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }

    public Product save(Product product) {
        Product storedProduct = productRepository.save(product);
        cacheRepository.save(product);
        return storedProduct;
    }

    public Product update(Product product) {
        Product storedProduct = productRepository.save(product);
        cacheRepository.deleteProductById(product.getId());
        return storedProduct;
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        Product cachedProduct = cacheRepository.findProductById(id);
        if (Objects.nonNull(cachedProduct))  {
            return cachedProduct;
        }

        Product persistProduct = productRepository.findById(id).get();
        cacheRepository.save(persistProduct);
        return persistProduct;
    }

    public Boolean deleteById(Long id) {
        cacheRepository.deleteProductById(id);
        productRepository.deleteById(id);
        return true;
    }
}
