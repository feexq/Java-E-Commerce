package com.project.gamemarket.objects;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.common.CategoryType;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.product.ProductDetailsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class BuildProducts {

    public static final Long ID = new Random().nextLong();

    public ProductDetails buildProductDetailsMock() {
        return ProductDetails.builder()
                .id(Math.abs(ID))
                .title("witcher-3")
                .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                .price(30.0)
                .developer("CD Projekt Red")
                .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                .genres(List.of(CategoryType.RPG, CategoryType.MYTHOLOGY))
                .build();
    }

    public ProductDetailsDto buildProductDetailsDtoMock() {
        return ProductDetailsDto.builder()
                .title("witcher-3")
                .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                .price(30.0)
                .developer("CD Projekt Red")
                .deviceTypes(List.of("console", "pc"))
                .genres(List.of("rpg","mythology"))
                .build();
    }

    public ProductDetailsDto buildProductDetailsDto() {
        return ProductDetailsDto.builder()
                .title("cyberpunk-2077")
                .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                .price(30.0)
                .developer("CD Projekt Red")
                .deviceTypes(List.of("console", "pc"))
                .genres(List.of("rpg","mythology"))
                .build();
    }

    public ProductDetails buildThrowCustomValidationExceptionProductDetailsMock() {
        return ProductDetails.builder()
                .id(1L)
                .title("witcher-3")
                .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                .price(30.0)
                .developer("1C")
                .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                .genres(List.of(CategoryType.RPG, CategoryType.MYTHOLOGY))
                .build();
    }

    public ProductDetails buildThrowValidationExceptionProductDetailsMock() {
        return ProductDetails.builder()
                .id(1L)
                .title("")
                .shortDescription("")
                .price(0.0)
                .developer("")
                .deviceTypes(null)
                .genres(null)
                .build();
    }

    public List<ProductDetails> buildProductDetailsListMock() {
        return List.of(
                ProductDetails.builder()
                        .id(1L)
                        .title("witcher-3")
                        .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                        .price(30.0)
                        .developer("CD Projekt Red")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(CategoryType.RPG, CategoryType.MYTHOLOGY))
                        .build(),
                ProductDetails.builder()
                        .id(2L)
                        .title("god-of-war")
                        .shortDescription("Join Kratos, the God of War, as he embarks on a journey with his son, Atreus, through a world of Norse mythology, facing gods and monsters while uncovering a profound father-son bond.")
                        .price(40.0)
                        .developer("Santa Monica Studio")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(CategoryType.ACTION, CategoryType.ADVENTURE))
                        .build(),
                ProductDetails.builder()
                        .id(3L)
                        .title("Dark-Souls-III")
                        .shortDescription("Experience a dark fantasy world filled with challenging enemies and deep lore. As the Ashen One, players must battle formidable foes to rekindle the flames and restore the world.")
                        .price(35.0)
                        .developer("FromSoftware")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(CategoryType.ACTION, CategoryType.RPG))
                        .build(),
                ProductDetails.builder()
                        .id(4L)
                        .title("Horizon-Zero-Dawn")
                        .shortDescription("In a post-apocalyptic world overrun by robotic creatures, players control Aloy, a skilled hunter on a quest to uncover her past and save humanity from extinction.")
                        .price(45.0)
                        .developer("Guerrilla Games")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(CategoryType.ACTION, CategoryType.RPG))
                        .build(),
                ProductDetails.builder()
                        .id(5L)
                        .title("The-Legend-of-Zelda:-Breath-of-the-Wild")
                        .shortDescription("Explore the vast kingdom of Hyrule in this open-world adventure. Players control Link as he awakens from a long slumber to defeat Calamity Ganon and save Princess Zelda.")
                        .price(60.0)
                        .developer("Nintendo")
                        .deviceTypes(List.of(DeviceType.NINTENDO_SWITCH))
                        .genres(List.of(CategoryType.ACTION, CategoryType.ADVENTURE))
                        .build(),
                ProductDetails.builder()
                        .id(6L)
                        .title("Cyberpunk-2077")
                        .shortDescription("Set in the dystopian Night City, players take on the role of V, a customizable mercenary seeking immortality through a cybernetic implant while navigating a world filled with crime and corruption.")
                        .price(50.0)
                        .developer("CD Projekt Red")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(CategoryType.ACTION, CategoryType.RPG))
                        .build());
    }
}
