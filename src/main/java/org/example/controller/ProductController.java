package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.Product;
import org.example.entity.ProductEntity;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


//    public ResponseEntity<String> addProduct(@RequestParam("image") MultipartFile imageFile, @ModelAttribute Product product) throws IOException {
//        return  productService.addProduct(product , imageFile) ? ResponseEntity.ok("product added"): ResponseEntity.badRequest().build() ;
//
//    }

    @PostMapping("/post")
    public ResponseEntity<String> addProduct(@ModelAttribute Product product ,@RequestParam ("image") MultipartFile file) throws IOException {
        if (file.isEmpty()){
            return  ResponseEntity.ok("File not added");
        }
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        productService.addProduct(tempFile,product);
        System.out.println(product);
        return ResponseEntity.ok("Product added successfully");
    }

    @GetMapping("/get")
    public List<ProductEntity> getAll(){
       return productService.getAll();
    }

    @GetMapping("/getById/{id}")
    public Optional<ProductEntity> getById(@PathVariable Long id){
        return productService.getById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        return productService.deleteById(id) ? ResponseEntity.ok("Deleted") : ResponseEntity.notFound().build();
    }
}
