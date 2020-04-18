package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.AdminDao;
import com.bootcamp2020.ecommerceProject.dao.CategoryDao;
import com.bootcamp2020.ecommerceProject.dto.AllCustomerDto;
import com.bootcamp2020.ecommerceProject.dto.AllSellerDto;
import com.bootcamp2020.ecommerceProject.dto.CategoryDto;
import com.bootcamp2020.ecommerceProject.dto.CategoryMetadatfieldValuesDto;
import com.bootcamp2020.ecommerceProject.entities.CategoryMetadataField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
   private AdminDao adminDao;

    @Autowired
    private CategoryDao categoryDao;

    @GetMapping("/customers")
    private List<AllCustomerDto> getAllCustomer(
            @RequestParam(defaultValue = "0") String pageOffset,
            @RequestParam(defaultValue = "10") String pageSize,
            @RequestParam(defaultValue = "id") String field
    ){
        return adminDao.returnCustomer(pageOffset,pageSize,field);
    }

    @GetMapping("/sellers")
    private List<AllSellerDto> getAllSeller(
            @RequestParam(defaultValue = "0") String pageOffset,
            @RequestParam(defaultValue = "10") String pageSize,
            @RequestParam(defaultValue = "id") String field
    ){
        List<AllSellerDto> allSellerDtos = adminDao.returnSeller(pageOffset, pageSize, field);
        return allSellerDtos;
    }
    @PutMapping("/activateCustomer")
    private String activateCustomer(WebRequest webRequest,@RequestParam("id") Long id){
        return adminDao.activateCustomer(id,webRequest);
    }

    @PutMapping("/deactivateCustomer")
    private String deActivateCustomer(WebRequest webRequest, @RequestParam("id") Long id){
        return adminDao.deActivateCustomer(id, webRequest);
    }
    @PutMapping("/activateSeller")
    private String activateSeller(WebRequest webRequest,@RequestParam("id") Long id){
        return adminDao.activateSeller(id,webRequest);
    }

    @PutMapping("/deactivateSeller")
    private String deActivateSeller(WebRequest webRequest, @RequestParam("id") Long id){
        return adminDao.deActivateSeller(id, webRequest);
    }

    @PostMapping("/addMetadataField")
    private String addFieldName(WebRequest webRequest,@RequestParam("fieldName") String name){
        return categoryDao.saveMetadataField(name, webRequest);
    }

    @GetMapping("/viewMetadataField")
    private List<CategoryMetadataField> viewMetadataField(
            @RequestParam(defaultValue = "0") String offset,
            @RequestParam(defaultValue = "10") String max,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue= "Ascending") String order
    ){
        return categoryDao.viewAllMetadata(offset, max, field, order);
    }

    @PostMapping("/addCategory")
    private String addCategory(@RequestParam("name") String name,@RequestParam("parentId") Long parentId,WebRequest webRequest){
        return categoryDao.addCategory(name, parentId, webRequest);
    }

    @GetMapping("/viewCategory")
    private CategoryDto viewCategory(@RequestParam("id") Long categoryId ,WebRequest webRequest){
        return categoryDao.viewCategory(categoryId,webRequest);
    }

    @GetMapping("/viewAllCategory")
    private List<CategoryDto> viewAllCategory(
            @RequestParam(defaultValue = "0") String offset,
            @RequestParam(defaultValue = "10") String max,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue= "Ascending") String order,
            WebRequest webRequest

    ){
        return categoryDao.viewAllCategory(offset, max, field, order,webRequest);
    }

    @DeleteMapping("/deleteCategory")
    private String deleteCategory(@RequestParam("id") Long id, WebRequest webRequest){
        return categoryDao.deleteCategory(id,webRequest);
    }

    @PutMapping("/updateCategory")
    private String updateCategory(@RequestParam("id") Long id,@RequestParam("name") String name,WebRequest webRequest){
        return categoryDao.updateCategory(id, name, webRequest);
    }

    @PostMapping(value = "/addCategoryMetadataFieldValue")
    private String addValues(@RequestBody CategoryMetadatfieldValuesDto categoryMetadatfieldValuesDto,WebRequest webRequest){
        return categoryDao.addCategoryMetadataFieldValues(categoryMetadatfieldValuesDto,webRequest);
    }

    @PutMapping(value = "/updateCategoryMetadataFieldValue")
    private String updateValues(@RequestBody CategoryMetadatfieldValuesDto categoryMetadatfieldValuesDto,WebRequest webRequest){
        return categoryDao.updateCategoryMetadataFieldValues(categoryMetadatfieldValuesDto,webRequest);
    }

    @PutMapping(value = "/activateProduct")
    private String activateProduct(@RequestParam("productId") Long productId,WebRequest webRequest){
        return adminDao.activateProduct(productId,webRequest);
    }
}
