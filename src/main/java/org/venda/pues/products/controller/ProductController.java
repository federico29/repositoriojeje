package org.venda.pues.products.controller;

import dto.ProductDto;
import dto.ProductSaleDetailsDto;
import dto.SaleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.venda.pues.products.service.ProductServices;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/v1/product")
public class ProductController {

    private final ProductServices productServices;

    public ProductController(@Autowired ProductServices saleServices) {
        this.productServices = saleServices;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> create(@PathVariable String userId,@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productServices.create(userId, productDto));
    }

    @PostMapping("/names")
    public ResponseEntity<?> getProductNames(@RequestBody List<ProductSaleDetailsDto> productIds) {
        return ResponseEntity.ok((productServices.getProductNames(productIds)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> all(@PathVariable String userId) {
        return ResponseEntity.ok(productServices.all(userId));
    }

    @PostMapping("/on-kart")
    public ResponseEntity<?> getProductsOnKart(@RequestBody SaleDto saleDto) {
        return ResponseEntity.ok(productServices.getProductsOnKart(saleDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productServices.update(id, productDto));
    }

    @PutMapping("/increase-stock/{id}")
    public ResponseEntity<?> addNewUnits(@PathVariable String id, @RequestParam int units) {
        return ResponseEntity.ok(productServices.increaseStock(id, units));
    }

    @PutMapping("/decrease-stock")
    public ResponseEntity<?> sellUnits(@RequestBody SaleDto saleData) {
        System.out.println(saleData);
        return ResponseEntity.ok(productServices.decreaseStock(saleData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return ResponseEntity.ok(productServices.delete(id));
    }
}
