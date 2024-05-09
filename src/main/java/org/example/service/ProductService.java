package org.example.service;

import org.example.dto.Product;
import org.example.entity.ProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface ProductService {

//   public boolean addProduct(Product product, MultipartFile imageFile) ;

   public ProductEntity addProduct(File file , Product product);

   List<ProductEntity> getAll();

   Optional<ProductEntity> getById(Long id);

   boolean deleteById(Long id);
}
