package org.venda.pues.products.service;

import dto.ProductDto;
import dto.ProductSaleDetailsDto;
import dto.SaleDto;
import error.exception.InvalidValuesException;
import error.exception.NotFoundException;
import models.ProductDocument;
import models.UserDocument;
import org.springframework.stereotype.Service;
import org.venda.pues.products.repository.ProductRepository;
import org.venda.pues.products.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServices {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductServices(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ProductDocument create(String userId, ProductDto productDto) {
        UserDocument user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            ProductDocument product = new ProductDocument(productDto);
            product = productRepository.save(product);
            user.addNewProduct(product.getId());
            updateUser(user);

            return product;
        }
        throw new NotFoundException("User not found");
    }

    public List<ProductDocument> all(String userId) {
        UserDocument user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<ProductDocument> products = new ArrayList<>();
            for (String productId: user.getProducts()) {
                products.add(productRepository.findById(productId).orElse(null));
            }

            return products;
        }
        throw new NotFoundException("User not found");
    }

    public ProductDocument findById(String id) {
        ProductDocument product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return product;
        }
        throw new NotFoundException("Product not found");
    }

    public ProductDocument update(String id, ProductDto productDto) {
        ProductDocument product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.update(productDto);
            productRepository.save(product);
            return product;
        }
        throw new NotFoundException("Product not found");
    }

    public ProductDocument increaseStock(String id, int units) {
        ProductDocument product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setStock(product.getStock() + units);
            return productRepository.save(product);
        }
        throw new NotFoundException("Product not found");
    }

    public Boolean decreaseStock(SaleDto saleData) {
        for (ProductSaleDetailsDto sale: saleData.getSaleData()) {
            ProductDocument product = productRepository.findById(sale.getProductId()).orElse(null);
            if (product != null) {
                if (product.getStock() < sale.getQuantity()) {
                    throw new InvalidValuesException("Not enough units");
                }
                product.setStock(product.getStock() - sale.getQuantity());
                productRepository.save(product);
            } else {
                throw new NotFoundException("Product not found");
            }
        }

        return true;
    }

    private void updateUser(UserDocument user) {
        userRepository.save(user);
    }

    public boolean delete(String id) {
        ProductDocument product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.deleteById(id);
            return true;
        }
        throw new NotFoundException("Product not found");
    }

    public List<String> getProductNames(List<ProductSaleDetailsDto> productIds) {
        List<String> names = new ArrayList<>();
        for (ProductSaleDetailsDto productData: productIds) {
            productRepository.findById(productData.getProductId()).ifPresent(product -> names.add(product.getProductName()));
        }
        return names;
    }

    public List<ProductDocument> getProductsOnKart(SaleDto saleDto) {
        List<ProductDocument> productsOnKart = new ArrayList<>();
        for (ProductSaleDetailsDto productSale: saleDto.getSaleData()) {
            productRepository.findById(productSale.getProductId()).ifPresent(productsOnKart::add);
        }
        return productsOnKart;
    }
}
