package org.example.service.Impl;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import org.example.dto.Product;
import org.example.entity.ProductEntity;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
//    private final Drive drive;

//    public String uploadDirectory = System.getProperty("user.dir") + "/public/Clothify_images";

    public String uploadDirectory = "https://drive.google.com/drive/folders/13ooxC_mupLxmalS_RIDBHGWrIHXLt12_?usp=sharing";


    @Override
    public boolean addProduct(Product product, MultipartFile imageFile)  {
        String imageName = imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDirectory, imageName);
        try (InputStream inputStream = imageFile.getInputStream()) {
            Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        productEntity.setImage(imagePath.toString());

        productRepository.save(productEntity);
        return true;
    }


//    private final Drive driveService; // Inject the Google Drive service

//    @Override
//    public boolean addProduct(Product product, MultipartFile imageFile) {
//        String imageName = imageFile.getOriginalFilename();
//        java.io.File filePath = new java.io.File(imageName);
//        File fileMetadata = new File();
//        fileMetadata.setName(imageName);
//        FileContent mediaContent = new FileContent("image/jpeg", filePath);
//        try {
//            File file = driveService.files().create(fileMetadata, mediaContent)
//                    .setFields("id")
//                    .execute();
//            System.out.println("File ID: " + file.getId());
//
//            ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
//            productEntity.setImage(file.getId()); // Save the Google Drive file ID instead of the local path
//
//            productRepository.save(productEntity);
//            return true;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public List<ProductEntity> getAll() {
       return productRepository.findAll();
    }

    @Override
    public Optional<ProductEntity> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean deleteById(Long id) {
      if (productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }


}


