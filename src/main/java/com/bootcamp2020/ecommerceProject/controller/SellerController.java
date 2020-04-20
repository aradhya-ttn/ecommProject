package com.bootcamp2020.ecommerceProject.controller;

import com.bootcamp2020.ecommerceProject.dao.CategoryDao;
import com.bootcamp2020.ecommerceProject.dao.ProductDao;
import com.bootcamp2020.ecommerceProject.dao.SellerDao;
import com.bootcamp2020.ecommerceProject.dto.*;
import com.bootcamp2020.ecommerceProject.dto.categorySellerDtos.CategoryAndSubCategoryDto;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
@ApiModel(description = "All Api which can be used by seller")
@RestController
@RequestMapping(value = "/seller")
public class SellerController {

    @Autowired
    private SellerDao sellerDao;
    
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

    @GetMapping(value = "/profile")
    public SellerProfileDto getProfile(HttpServletRequest request){
        return sellerDao.getProfile(request);
    }

    @PutMapping(value = "/updateProfile")
    public String updateProfile(@RequestBody HashMap<String,Object> value, HttpServletRequest request, WebRequest webRequest){
        return sellerDao.updateProfile(value, request, webRequest);
    }
    @PutMapping(value = "/updatePassword")
    private String updatePassword( @RequestParam("password") String password,@RequestParam("confirmPassword") String confirmPassword,HttpServletRequest request,WebRequest webRequest) {
        return sellerDao.updatePassword(webRequest, request, password, confirmPassword);
    }
    @PutMapping(value = "/updateAddress")
    public String updateAddress(@RequestParam("addressId")Long id,@RequestBody HashMap<String,Object> value,HttpServletRequest request,WebRequest webRequest){
        return sellerDao.updateAddress(value,id,request,webRequest);
    }

    @GetMapping("/viewAllCategory")
    private List<CategoryAndSubCategoryDto> viewAllCategoryForSeller(){
        return categoryDao.getAllCategoryForSeller();
    }

    @PostMapping("/addProduct")
    private String addProduct(@RequestBody ProductDto productDto,HttpServletRequest request,WebRequest webRequest){
        return productDao.addProduct(productDto, request, webRequest);
    }

    @PostMapping(value = "/addProductVariation", consumes={"multipart/form-data"})
    private String addProductVariation(@RequestPart("primaryImage") MultipartFile primaryImage,
                                       @RequestPart("secondaryImage") List<MultipartFile> secondaryImage,
                                       @RequestPart("addProductVariationDto") AddProductVariationDto addProductVariationDto,
                                       WebRequest webRequest,HttpServletRequest request) throws IOException {
        return productDao.addProductVariation(primaryImage,secondaryImage,request,addProductVariationDto,webRequest);
    }

    @GetMapping(value = "/viewProduct")
    private ViewProductDto viewProduct(@RequestParam("productId") Long productId,WebRequest webRequest,HttpServletRequest request){
        return productDao.viewProduct(productId, webRequest,request);
    }

    @GetMapping(value = "/viewProductVariation")
    private ViewProductVariationDto viewProductVariation(@RequestParam("variationId") Long variationId, WebRequest webRequest, HttpServletRequest request){
        return productDao.viewProductVariation(variationId, webRequest,request);
    }

    @GetMapping(value = "/viewAllProduct")
    private List<ViewProductDto> viewAllproduct(
            @RequestParam(defaultValue = "0") String offset,
            @RequestParam(defaultValue = "10") String max,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue= "Ascending") String order,
            HttpServletRequest request
    ){
        return productDao.viewAllProduct(offset,max,field,order,request);
    }
    @GetMapping(value = "/viewAllProductVariation")
    private List<ViewAllProductVariationDto> productVariationList(
            @RequestParam("productId") Long productId,
            @RequestParam(defaultValue = "0") String offset,
            @RequestParam(defaultValue = "10") String max,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue= "Ascending") String order,
            HttpServletRequest request,WebRequest webRequest
    ){
        return productDao.viewAllProductVariation(productId, offset, max, field, order, request, webRequest);
    }
    @DeleteMapping(value = "/deleteProduct")
    private String deleteProduct(@RequestParam("productId")Long productId,HttpServletRequest request,WebRequest webRequest){
        return productDao.deleteProduct(productId, request, webRequest);
    }

    @PutMapping("/updateProduct")
    private String updateProduct(@RequestBody UpdateProductDto updateProductDto,HttpServletRequest request,WebRequest webRequest){
        return productDao.updateProduct(updateProductDto,request,webRequest);
    }
    @PutMapping(value = "/updateProductVariation",consumes ={"multipart/form-data"})
    private String updateProductVariation(@RequestPart("primaryImage") MultipartFile primaryImage,
                                       @RequestPart("secondaryImage") List<MultipartFile> secondaryImage,
                                       @RequestPart("updateProductVariationDto") UpdateProductVariationDto updateProductVariationDto,
                                       WebRequest webRequest,HttpServletRequest request ) throws IOException {
        return productDao.updateProductVariation(primaryImage,secondaryImage,request,updateProductVariationDto,webRequest);
    }

}
