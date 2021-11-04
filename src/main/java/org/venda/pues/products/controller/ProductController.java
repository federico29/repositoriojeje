package org.venda.pues.products.controller;

import dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.venda.pues.products.service.ProductServices;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
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

    @GetMapping("/{userId}")
    public ResponseEntity<?> all(@PathVariable String userId) {
        return ResponseEntity.ok(productServices.all(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productServices.update(id, productDto));
    }

    @PutMapping("/increase-stock/{id}")
    public ResponseEntity<?> addNewUnits(@PathVariable String id, @RequestParam int units) {
        return ResponseEntity.ok(productServices.increaseStock(id, units));
    }

    @PutMapping("/decrease-stock/{id}")
    public ResponseEntity<?> sellUnits(@PathVariable String id, @RequestParam int units) {
        return ResponseEntity.ok(productServices.decreaseStock(id, units));
    }
}
