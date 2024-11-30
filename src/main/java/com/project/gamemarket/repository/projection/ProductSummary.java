package com.project.gamemarket.repository.projection;

public interface ProductSummary {
    Long getId();
    String getTitle();
    String getShortDescription();
    Double getPrice();
}
