package com.maksatbek.ecommerce;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.maksatbek.ecommerce.model.Product;
import com.maksatbek.ecommerce.repository.CategoryRepository;
import com.maksatbek.ecommerce.repository.ProductRepository;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            System.out.println("\n=== SPRING DATA JPA VERI EKLEME BAŞLADI ===");
            // 3. Veritabanındaki tüm ürünleri çekip ekrana basalım (Bizim eski getAll())
            System.out.println("--- TÜM ÜRÜNLERIN LISTESI (JPA FINDALL) ---");
            List<Product> allProducts = productRepository.findAll();
            for (Product p : allProducts) {
                System.out.println("Ürün Adı: " + p.getName() + 
                                   " | Fiyat: $" + p.getPrice() + 
                                   " | Kategori: " + p.getCategory().getName());
								   System.out.println("Resim: " + p.getImage());
								   System.out.println("ID: " + p.getId());
								   System.out.println("-------------------------------------------\n");
            }
            System.out.println("-------------------------------------------\n");
        };
    }
}
