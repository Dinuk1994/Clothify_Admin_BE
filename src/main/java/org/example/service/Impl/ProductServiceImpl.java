package org.example.service.Impl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import lombok.RequiredArgsConstructor;
import org.example.dto.Product;
import org.example.entity.ProductEntity;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_KEY_PATH = getPathToGetGoodCredentials();

    private static String getPathToGetGoodCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory,"clothify_images.json");
        return filePath.toString();
    }

    public ProductEntity addProduct(File file , Product product) {

        try{
            String folderId = "13ooxC_mupLxmalS_RIDBHGWrIHXLt12_";
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/jpeg", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent)
                    .setFields("id").execute();
            String imageUrl = "https://drive.google.com/uc?export=view&id="+uploadedFile.getId();
            System.out.println("IMAGE URL: " + imageUrl);
            ProductEntity pro = modelMapper.map(product, ProductEntity.class);
            pro.setImage(imageUrl);
            productRepository.save(pro);
            return pro;

        }catch (Exception e){
            System.out.println(e.getMessage());

        }

        return null;
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
        .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .build();

    }


//    private final Drive drive;

//    public String uploadDirectory = System.getProperty("user.dir") + "/public/Clothify_images";

//    public String uploadDirectory = "https://drive.google.com/drive/folders/13ooxC_mupLxmalS_RIDBHGWrIHXLt12_?usp=sharing";



//    @Override
//    public boolean addProduct(Product product, MultipartFile imageFile)  {
//        String imageName = imageFile.getOriginalFilename();
//        Path imagePath = Paths.get(uploadDirectory, imageName);
//        try (InputStream inputStream = imageFile.getInputStream()) {
//            Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
//        productEntity.setImage(imagePath.toString());
//
//        productRepository.save(productEntity);
//        return true;
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


