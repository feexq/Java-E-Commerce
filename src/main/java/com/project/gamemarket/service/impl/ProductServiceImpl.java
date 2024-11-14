package com.project.gamemarket.service.impl;

import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.common.GenreType;
import com.project.gamemarket.common.KeyActivationStatus;
import com.project.gamemarket.domain.ProductDetails;
import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import com.project.gamemarket.dto.key.KeyActivationResponseDto;
import com.project.gamemarket.service.KeyActivationService;
import com.project.gamemarket.service.ProductService;
import com.project.gamemarket.service.exception.KeyActivationFailedProcessActivation;
import com.project.gamemarket.service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final KeyActivationService keyActivationService;

    private final Long ID = new Random().nextLong();

    private final List<ProductDetails> products = buildProductDetailsMock();

    @Override
    public ProductDetails addProduct(ProductDetails product) {
        return ProductDetails.builder()
                .id(ID)
                .title(product.getTitle())
                .shortDescription(product.getShortDescription())
                .price(product.getPrice())
                .developer(product.getDeveloper())
                .deviceTypes(product.getDeviceTypes())
                .genres(product.getGenres())
                .build();
    }

    @Override
    public ProductDetails updateProduct(ProductDetails product) {
        return getProductById(product.getId()).toBuilder()
                .id(product.getId())
                .title(product.getTitle())
                .shortDescription(product.getShortDescription())
                .price(product.getPrice())
                .developer(product.getDeveloper())
                .deviceTypes(product.getDeviceTypes())
                .genres(product.getGenres())
                .build();
    }

    @Override
    public void deleteProduct(Long id) {
    }

    @Override
    public ProductDetails getProductByKeyActivation(KeyActivationRequestDto context) {
        log.info("Getting product for with key: {}", context.getCustomerId());
        KeyActivationResponseDto keyActivationResponseDto = keyActivationService.processKeyActivation(context);
        if (KeyActivationStatus.EXPIRED.equals(keyActivationResponseDto.getStatus())
                || KeyActivationStatus.NOT_EXISTENT.equals(keyActivationResponseDto.getStatus())) {
            throw new KeyActivationFailedProcessActivation(keyActivationResponseDto.getKey().replace("\"", ""));
        }

        return getProductById(Long.parseLong(keyActivationResponseDto.getProductId()));
    }

    @Override
    public ProductDetails getProductById(Long id) {
        return Optional.of(products.stream()
                        .filter(product -> product.getId().equals(id)).findFirst())
                .get()
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ProductNotFoundException(id);
                });
    }

    @Override
    public List<ProductDetails> getProducts() {
        return products;
    }

    private List<ProductDetails> buildProductDetailsMock() {
        return List.of(
                ProductDetails.builder()
                        .id(1L)
                        .title("Witcher 3")
                        .shortDescription("The game takes place in a fictional fantasy world based on Slavic mythology. Players control Geralt of Rivia, a monster slayer for hire known as a Witcher, and search for his adopted daughter, who is on the run from the otherworldly Wild Hunt.")
                        .price(30.0)
                        .developer("CD Projekt Red")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(GenreType.RPG,GenreType.MYTHOLOGY))
                        .build(),
                ProductDetails.builder()
                        .id(2L)
                        .title("God of War")
                        .shortDescription("Join Kratos, the God of War, as he embarks on a journey with his son, Atreus, through a world of Norse mythology, facing gods and monsters while uncovering a profound father-son bond.")
                        .price(40.0)
                        .developer("Santa Monica Studio")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(GenreType.ACTION, GenreType.ADVENTURE))
                        .build(),
                ProductDetails.builder()
                        .id(3L)
                        .title("Dark Souls III")
                        .shortDescription("Experience a dark fantasy world filled with challenging enemies and deep lore. As the Ashen One, players must battle formidable foes to rekindle the flames and restore the world.")
                        .price(35.0)
                        .developer("FromSoftware")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(GenreType.ACTION, GenreType.RPG))
                        .build(),
                ProductDetails.builder()
                        .id(4L)
                        .title("Horizon Zero Dawn")
                        .shortDescription("In a post-apocalyptic world overrun by robotic creatures, players control Aloy, a skilled hunter on a quest to uncover her past and save humanity from extinction.")
                        .price(45.0)
                        .developer("Guerrilla Games")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(GenreType.ACTION, GenreType.RPG))
                        .build(),
                ProductDetails.builder()
                        .id(5L)
                        .title("The Legend of Zelda: Breath of the Wild")
                        .shortDescription("Explore the vast kingdom of Hyrule in this open-world adventure. Players control Link as he awakens from a long slumber to defeat Calamity Ganon and save Princess Zelda.")
                        .price(60.0)
                        .developer("Nintendo")
                        .deviceTypes(List.of(DeviceType.NINTENDO_SWITCH))
                        .genres(List.of(GenreType.ACTION, GenreType.ADVENTURE))
                        .build(),
                ProductDetails.builder()
                        .id(6L)
                        .title("Cyberpunk 2077")
                        .shortDescription("Set in the dystopian Night City, players take on the role of V, a customizable mercenary seeking immortality through a cybernetic implant while navigating a world filled with crime and corruption.")
                        .price(50.0)
                        .developer("CD Projekt Red")
                        .deviceTypes(List.of(DeviceType.CONSOLE, DeviceType.PC))
                        .genres(List.of(GenreType.ACTION, GenreType.RPG))
                        .build());
    }
}
