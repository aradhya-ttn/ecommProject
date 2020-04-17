package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryMetadataFieldValuesRepository extends CrudRepository<CategoryMetadataFieldValues,Long> {

    @Query(value = "select * from category_metadata_field_values where category_id =:CategoryId  and category_metadata_field_id=:FieldId",nativeQuery = true)
    public CategoryMetadataFieldValues getCategoryAndMetadatafield(@Param("CategoryId") Long categoryId,@Param("FieldId") Long fieldId);

}
