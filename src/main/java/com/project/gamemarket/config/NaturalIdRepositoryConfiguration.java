package com.project.gamemarket.config;

import com.project.gamemarket.repository.impl.NaturalIdRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = NaturalIdRepositoryImpl.class, basePackages = "com.project.gamemarket.repository")
public class NaturalIdRepositoryConfiguration {
}
