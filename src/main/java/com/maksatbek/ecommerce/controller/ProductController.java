package com.maksatbek.ecommerce.controller;

import com.maksatbek.ecommerce.model.Category;
import com.maksatbek.ecommerce.model.PagedResponse;
import com.maksatbek.ecommerce.model.Product;
import com.maksatbek.ecommerce.repository.CategoryRepository;
import com.maksatbek.ecommerce.repository.ProductRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/ecommerce_images/";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/add")
    public Product addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("stockQuantity") int stockQuantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        
        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            
            // Kategoriyi bulup ürüne bağlıyoruz
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            product.setCategory(category);
    
            // Eğer Flutter'dan bir resim dosyası gönderildiyse
            if (imageFile != null && !imageFile.isEmpty()) {
                // Klasör yoksa oluştur
                File directory = new File(UPLOAD_DIR);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
    
                // Dosya adını benzersiz yapalım (aynı isimde resim yüklenirse çakışmasın)
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                
                // Resmi klasörün içine yazıyoruz
                Files.write(path, imageFile.getBytes());
    
                // Flutter'a tam URL dönebilmek için veritabanına linki kaydediyoruz
                String fileDownloadUri = "http://localhost:8080/uploads/" + fileName;
                product.setImage(fileDownloadUri);
            }
    
            return productRepository.save(product);
    
        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RuntimeException("Resim yükleme hatası: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok("Ürün başarıyla silindi");
    }

    @GetMapping("/paged")
    public PagedResponse<Product> getProductsPaged(
        @RequestParam(defaultValue = "1") int page,  
        @RequestParam(defaultValue = "10") int size
    ) {
        int adjustedPage = (page < 1) ? 0 : page - 1; 
        
        Pageable pageable = PageRequest.of(adjustedPage, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        
        return new PagedResponse<>(productPage);
    }
}
