package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Long> {

    Category  findByid(Long id);


    List<Category> findByParentId(Long id);

    List<Category> findAll(Pageable pageable);

    Category findByName(String name);

    @Query(value = "select * from category where id not in(select id from category where id in (select parent_id from category))",nativeQuery = true)
    public List<Category> getAllLeafChild();

    @Query(value = "select f.name,v.value from category_metadata_field_values v inner join category_metadata_field f on f.id = v.category_metadata_field_id where category_id=:Id",nativeQuery = true)
    public List<Object[]> getMetadataNameAndValues(@Param("Id") Long categoryId);

    @Query(value = "select * from category where parent_id is null",nativeQuery = true)
    public List<Category> getRootParent();


}
