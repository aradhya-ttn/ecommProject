package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Long> {

    Category  findByid(Long id);


    List<Category> findByParentId(Long id);

    List<Category> findAll(Pageable pageable);
}
