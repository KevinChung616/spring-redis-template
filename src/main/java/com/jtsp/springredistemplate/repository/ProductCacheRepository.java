package com.jtsp.springredistemplate.repository;

import com.jtsp.springredistemplate.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCacheRepository {
    public static final String HASH_KEY = "Product";
    private final RedisTemplate redisTemplate;

    @Autowired
    public ProductCacheRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Product save(Product product) {
        redisTemplate.opsForHash().put(HASH_KEY, product.getId(), product);
        return product;
    }

    public List<Product> findAll () {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public Product findProductById(Long id) {
        return (Product) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public String deleteProductById(Long id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
        return "product " + id + " removed!";
    }

}
