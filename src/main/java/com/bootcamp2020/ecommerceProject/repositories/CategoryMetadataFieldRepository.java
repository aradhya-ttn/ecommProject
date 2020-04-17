package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.CategoryMetadataField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryMetadataFieldRepository extends CrudRepository<CategoryMetadataField,Long> {

    CategoryMetadataField findByName(String name);

    List<CategoryMetadataField> findAll(Pageable pageable);

    Optional<CategoryMetadataField> findById(Long id);
}
