package ecommerce.platform.catalogoservice.service;


import ecommerce.platform.catalogoservice.model.Product;
import ecommerce.platform.catalogoservice.repository.ProductRepository;
import ecommerce.platform.exception.ResourceNotFoundException;
import ecommerce.platform.resource.config.AppConstants;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * @return
     */
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT+ id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = findProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        return productRepository.save(existingProduct);
    }
    
    public Product updateProductStock(Long id, int quantity) {
        Product product = findProductById(id);
        if (product != null) {
            product.setStock(product.getStock() - quantity);
            return productRepository.save(product);
        } else {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }

}