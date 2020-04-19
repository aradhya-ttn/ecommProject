package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.CategoryDto;
import com.bootcamp2020.ecommerceProject.dto.CategoryMetadatfieldValuesDto;
import com.bootcamp2020.ecommerceProject.dto.CustomerCategoryDto;
import com.bootcamp2020.ecommerceProject.dto.categorySellerDtos.CategoryAndSubCategoryDto;
import com.bootcamp2020.ecommerceProject.dto.categorySellerDtos.MetadatafieldNameAndValuesDto;
import com.bootcamp2020.ecommerceProject.entities.Category;
import com.bootcamp2020.ecommerceProject.entities.CategoryMetadataField;
import com.bootcamp2020.ecommerceProject.entities.CategoryMetadataFieldAndValues;
import com.bootcamp2020.ecommerceProject.entities.CategoryMetadataFieldValues;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.exceptions.UserNotFoundException;
import com.bootcamp2020.ecommerceProject.repositories.CategoryMetadataFieldRepository;
import com.bootcamp2020.ecommerceProject.repositories.CategoryMetadataFieldValuesRepository;
import com.bootcamp2020.ecommerceProject.repositories.CategoryRepository;
import com.bootcamp2020.ecommerceProject.utils.SetConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@Component
public class CategoryDao {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CategoryMetadataFieldRepository fieldRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

    public String  saveMetadataField(String name, WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        if(name!=null && name!=""){
            CategoryMetadataField categoryMetadataField=fieldRepository.findByName(name);
            if(categoryMetadataField!=null){
                String message=messageSource.getMessage("msg.metadata.alreadyExist",null,locale);
                return message+" with Id "+categoryMetadataField.getId();

            }else{
                CategoryMetadataField categoryMetadataField1=new CategoryMetadataField();
                categoryMetadataField1.setName(name);
                fieldRepository.save(categoryMetadataField1);
                String message=messageSource.getMessage("msg.metadata.saved",null,locale);
                return message;
            }
        }else{
            String message=messageSource.getMessage("msg.metadata.null",null,locale);
            throw new NullPointerException(message);
        }
    }
    public List<CategoryMetadataField> viewAllMetadata(String offset,  String max,  String field, String order){
        Integer offSetPage=Integer.parseInt(offset);
        Integer sizeOfPage= Integer.parseInt(max);
        Pageable pageable;
        if(order.equalsIgnoreCase("Lowest to Maximum") || order.equalsIgnoreCase("Ascending")) {
             pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.ASC, field);
        }else{
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.DESC, field);
        }
        List<CategoryMetadataField> all = fieldRepository.findAll(pageable);
        return all;
    }
    public String addCategory(String name,Long parentId ,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Category category=new Category();
        if(parentId!=null){
            if(name!=null && name!=""){
                Category category1 = categoryRepository.findByid(parentId);
                if(category1==null){
                    String message=messageSource.getMessage("msg.category.parent.notExist",null,locale);
                    throw new UserNotFoundException(message);
                }else if(category1.getName().equals(name)){
                    String message=messageSource.getMessage("msg.category.alreadyExist",null,locale);
                    throw new UserNotFoundException(message+" with id = "+category1.getId());
                }else {
                    category.setName(name);
                    category.setParentId(parentId);
                    categoryRepository.save(category);
                    String message=messageSource.getMessage("msg.category.save",null,locale);
                    return category.getName()+" "+message+" with Category Id = "+category.getId();
                }
            }else{
                String message=messageSource.getMessage("msg.category.null",null,locale);
                throw  new UserNotFoundException(message);
            }
        }else{
            category.setName(name);
            category.setParentId(parentId);
            categoryRepository.save(category);
            String message=messageSource.getMessage("msg.category.save",null,locale);
            return category.getName()+" "+message+" with Category Id = "+category.getId();
        }
    }
    public CategoryDto viewCategory(Long categoryId,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Category category=categoryRepository.findByid(categoryId);
       if(category!=null){
           List<String> categories=new ArrayList<>();
           Long id=category.getId();
           List<Category> child=categoryRepository.findByParentId(id);
           List<String>categoriesValues=getParents(id,categories);
           CategoryDto categoryDto=new CategoryDto();
           categoryDto.setId(id);
           categoryDto.setName(category.getName());
           categoryDto.setParentCategory(categoriesValues);
           if(!child.isEmpty()) {
               categoryDto.setSubCategory(child.get(0).getName());
           }else{
               categoryDto.setSubCategory("do not have child");
           }
           return categoryDto;
       }else{
           String message=messageSource.getMessage("msg.category.notExist",null,locale);
           throw new UserNotFoundException(message+" with "+categoryId);
       }
    }

    private List<String> getParents(Long categoryId, List<String> parents){
        Category category=categoryRepository.findByid(categoryId);
        Long id=category.getParentId();
        parents.add(category.getName());
        if(id!=null){
             return getParents(id,parents);
        }else{
            return parents;
        }
    }
    public List<CategoryDto> viewAllCategory(String offset,  String max,  String field, String order,WebRequest webRequest) {
        Integer offSetPage = Integer.parseInt(offset);
        Integer sizeOfPage = Integer.parseInt(max);
        Pageable pageable;
        if(order.equalsIgnoreCase("Ascending")) {
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.ASC, field);
        }else{
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.DESC, field);
        }
        List<Category> categories=categoryRepository.findAll(pageable);
        List<CategoryDto> categoryDtos=new ArrayList<>();
        categories.forEach(category -> {categoryDtos.add(viewCategory(category.getId(),webRequest));
        });
           return categoryDtos;
    }
    public  String deleteCategory(Long categoryId, WebRequest webRequest){
        Locale locale= webRequest.getLocale();
        List<Category> categories=categoryRepository.findByParentId(categoryId);
        Category category=categoryRepository.findByid(categoryId);
       if(category!=null){
           if(categories.isEmpty()){
               categoryRepository.deleteById(categoryId);
               String message=messageSource.getMessage("msg.Category.Deleted",null,locale);
               return message;
           }else{
               String message=messageSource.getMessage("msg.Category.notDeleted",null,locale);
               throw new EmailException(message);
           }
       }else{
           String message=messageSource.getMessage("msg.category.notExist",null,locale);
           throw new UserNotFoundException(message+" with "+categoryId);
       }
    }

    public String updateCategory(Long categoryId, String categoryName, WebRequest webRequest){
        Locale locale= webRequest.getLocale();
        Category category=categoryRepository.findByid(categoryId);
        if(category!=null){
            category.setName(categoryName);
            categoryRepository.save(category);
            String message= messageSource.getMessage("msg.category.updated",null,locale);
            return message;
        }else{
            String message=messageSource.getMessage("msg.category.notExist",null,locale);
            throw new UserNotFoundException(message+" with "+categoryId);
        }
    }

    public String addCategoryMetadataFieldValues(CategoryMetadatfieldValuesDto categoryMetadatfieldValuesDto,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Set<String> values=categoryMetadatfieldValuesDto.getFieldValues();
        Long categoryId=categoryMetadatfieldValuesDto.getCategoryId();
        Long fieldId=categoryMetadatfieldValuesDto.getMetadataFieldId();
        Optional<Category> category=categoryRepository.findById(categoryId);
        Optional<CategoryMetadataField> field = fieldRepository.findById(fieldId);
        if(category.isPresent()){
            if(field.isPresent()){
                CategoryMetadataFieldValues categoryMetadataFieldValues=new CategoryMetadataFieldValues();
                CategoryMetadataFieldAndValues categoryMetadataFieldAndValues=new CategoryMetadataFieldAndValues();
                categoryMetadataFieldAndValues.setCategoryId(category.get().getId());
                categoryMetadataFieldAndValues.setCategoryMetadataFieldId(field.get().getId());
                categoryMetadataFieldValues.setCategoryMetadataFieldAndValues(categoryMetadataFieldAndValues);

                categoryMetadataFieldValues.setCategory(category.get());
                categoryMetadataFieldValues.setCategoryMetadataField(field.get());
                String value=SetConverter.convertToString(values);
                categoryMetadataFieldValues.setValue(value);
                categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
                String messsage=messageSource.getMessage("msg.fieldValues.success",null,locale);
                return messsage;
            }
        }
        return  null;
    }
    private  String validateFieldValues(Set<String> fieldValues ,Long fieldId, Long categoryId, WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        String value= categoryMetadataFieldValuesRepository.getCategoryAndMetadatafield(categoryId,fieldId).getValue();
        if(!fieldValues.isEmpty()){
            Set<String> valueSet = SetConverter.convertToSet(value);
            Set<String> intersectionSet=getDuplicateValue(fieldValues,valueSet);
            if(!intersectionSet.isEmpty()){
                String duplicateValues=SetConverter.convertToString(intersectionSet);
                String message=messageSource.getMessage("msg.fieldValues.exist",null,locale);
                throw new EmailException(duplicateValues+" " +message);
            }else{
                String updatedvalues=value+","+SetConverter.convertToString(fieldValues);
                return updatedvalues;
            }

        }else{
            String message=messageSource.getMessage("msg.fieldValues.empty",null,locale);
            throw new EmailException(message);
        }
    }
    private  Set<String> getDuplicateValue(Set<String> set1,Set<String> set2){
        Set<String> intersection=new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection;
    }

    public String updateCategoryMetadataFieldValues(CategoryMetadatfieldValuesDto categoryMetadatfieldValuesDto,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Set<String> values=categoryMetadatfieldValuesDto.getFieldValues();
        Long categoryId=categoryMetadatfieldValuesDto.getCategoryId();
        Long fieldId=categoryMetadatfieldValuesDto.getMetadataFieldId();
        CategoryMetadataFieldValues categoryAndMetadatafield = categoryMetadataFieldValuesRepository.getCategoryAndMetadatafield(categoryId, fieldId);
        if(categoryAndMetadatafield!=null){
            String newValues=validateFieldValues(values,fieldId,categoryId,webRequest);
            categoryAndMetadatafield.setValue(newValues);
            categoryMetadataFieldValuesRepository.save(categoryAndMetadatafield);
            String message=messageSource.getMessage("msg.field.updated",null,locale);
            return message;
        }else{
            String message=messageSource.getMessage("msg.field.valid",null,locale);
            throw new EmailException(message);
        }
    }
    public List<CategoryAndSubCategoryDto> getAllCategoryForSeller(){
        List<CategoryAndSubCategoryDto> categoryAndSubCategoryDtoList=new ArrayList<>();
        List<Category> allLeafChild = categoryRepository.getAllLeafChild();
        for (Category category: allLeafChild) {
            Long categoryId=category.getId();
            List<String> parents=new ArrayList<>();
            parents =getParents(category.getParentId(),parents);
            CategoryAndSubCategoryDto categoryAndSubCategoryDto=new CategoryAndSubCategoryDto();
            List<MetadatafieldNameAndValuesDto> nameAndValuesDtos=new ArrayList<>();
            categoryAndSubCategoryDto.setCategoryId(categoryId);
            categoryAndSubCategoryDto.setParentName(parents);
            categoryAndSubCategoryDto.setCategoryName(category.getName());
            List<Object[]> metadataNameAndValues = categoryRepository.getMetadataNameAndValues(categoryId);
            for (Object[] object:  metadataNameAndValues) {
                MetadatafieldNameAndValuesDto metadatafieldNameAndValuesDto=new MetadatafieldNameAndValuesDto();
                metadatafieldNameAndValuesDto.setMetadataFieldName((String) object[0]);
                metadatafieldNameAndValuesDto.setMetadataFieldValues((String)object[1]);
                nameAndValuesDtos.add(metadatafieldNameAndValuesDto);
            }
            categoryAndSubCategoryDto.setMetadatafieldNameAndValues(nameAndValuesDtos);
            categoryAndSubCategoryDtoList.add(categoryAndSubCategoryDto);
        }
        return categoryAndSubCategoryDtoList;
    }

    public List<CustomerCategoryDto> getAllCategoryForCustomer(Long categoryId){
        List<CustomerCategoryDto> customerCategoryDtos=new ArrayList<>();
        if(categoryId==null){
            List<Category> rootParent = categoryRepository.getRootParent();
            for (Category category: rootParent) {
                CustomerCategoryDto customerCategoryDto=new CustomerCategoryDto();
                customerCategoryDto.setCategoryid(category.getId());
                customerCategoryDto.setCategoryName(category.getName());
                customerCategoryDtos.add(customerCategoryDto);
            }
        }else{
            List<Category> categoryList = categoryRepository.findByParentId(categoryId);
            for (Category category: categoryList) {
                CustomerCategoryDto customerCategoryDto=new CustomerCategoryDto();
                customerCategoryDto.setCategoryid(category.getId());
                customerCategoryDto.setCategoryName(category.getName());
                customerCategoryDtos.add(customerCategoryDto);
            }
        }
        return customerCategoryDtos;
    }
}
