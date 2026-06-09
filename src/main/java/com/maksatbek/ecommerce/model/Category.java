package com.maksatbek.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // 1. Bu sınıfın veritabanında bir tablo olduğunu Spring'e söylüyoruz
@Table(name = "categories") // 2. Veritabanındaki tablo adını belirliyoruz
public class Category {

    @Id // 3. Bu alanın Primary Key (Benzersiz ID) olduğunu belirtiyoruz
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Otomatik artan ID (Serial/Identity) olmasını sağlıyoruz
    private Long id;

    private String name;

    @Column(nullable = true)
    private String image;

    // Boş Constructor (JPA arka planda verileri okurken bunu mutlaka ister)
    public Category() {
    }

    // Kolaylık olsun diye parametreli constructor
    public Category(String name) {
        this.name = name;
    }

    // Getter ve Setter Metotları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}