package com.bootcamp2020.ecommerceProject.repositories;

import com.bootcamp2020.ecommerceProject.entities.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryMetadataFieldValuesRepository extends CrudRepository<CategoryMetadataFieldValues,Long> {

    @Query(value = "select * from category_metadata_field_values where category_id =:CategoryId  and category_metadata_field_id=:FieldId",nativeQuery = true)
    public CategoryMetadataFieldValues getCategoryAndMetadatafield(@Param("CategoryId") Long categoryId,@Param("FieldId") Long fieldId);

    @Query(value = "select f.name from category_metadata_field f inner join category_metadata_field_values v on f.id =v.category_metadata_field_id where v.category_id =:categoryId",nativeQuery = true)
    public List<Object> getMetadataFieldName(@Param("categoryId") Long categoryId);

    @Query(value = "select Value from category_metadata_field_values where category_id =:categoryId and category_metadata_field_id =:fieldId",nativeQuery = true)
    public List<Object> getValueFromCategoryAndField(@Param("categoryId") Long categoryId,@Param("fieldId") Long fieldId);
}
