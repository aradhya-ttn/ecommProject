package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.*;
import com.bootcamp2020.ecommerceProject.dto.categorySellerDtos.MetadatafieldNameAndValuesDto;
import com.bootcamp2020.ecommerceProject.entities.*;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.repositories.*;
import com.bootcamp2020.ecommerceProject.utils.SetConverter;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Component
public class ProductDao {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoryMetadataFieldValuesRepository valuesRepository;

    @Autowired
    private CategoryMetadataFieldRepository fieldRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ProductVariationRepository variationRepository;

    public String addProduct(ProductDto productDto, HttpServletRequest request, WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        String email=request.getUserPrincipal().getName();
        Long categoryId=productDto.getCategoryId();
        String brand = productDto.getBrand();
        String name = productDto.getName();
        Long userId = userRepository.findByEmail(email).getId();
        Seller seller = sellerRepository.findByUserId(userId);
        List<Category> categories = categoryRepository.findByParentId(categoryId);
        Category category = categoryRepository.findByid(categoryId);
        if(categories.isEmpty() && category!=null){
            Product prooduct = productRepository.getProductByName(brand, categoryId, name, userId);
            if(prooduct==null){
                Product product=new Product();
                product.setName(productDto.getName());
                product.setBrand(productDto.getBrand());
                if(productDto.getCancellable()!=null) {
                    product.setCancellable(productDto.getCancellable());
                }else{
                    product.setCancellable(false);
                }
                if(productDto.getReturnable()!=null){
                    product.setReturnable(productDto.getReturnable());
                }else{
                    product.setReturnable(false);
                }
                if(productDto.getDescription()!=null){
                    product.setDescription(productDto.getDescription());
                }
                product.setCategory(category);
                product.setSeller(seller);
                productRepository.save(product);
                sendEmailToAdmin(product,webRequest);
            }else{
                String message=messageSource.getMessage("msg.productName.exist",null,locale);
                throw new EmailException(message);
            }
        }else{
            String message=messageSource.getMessage("msg.category.notValid",null,locale);
            throw new EmailException(message);
        }
        String message=messageSource.getMessage("msg.product.saved",null,locale);
        return message;
    }
    private void sendEmailToAdmin(Product product,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        List<User> allAdmin = userRepository.getAllAdmin();
        String messageBody="Product : " +"\n" +
                " seller=" + product.getSeller().getCompanyName()+"\n" +
                " category=" + product.getCategory().getName() +"\n" +
                " name=" + product.getName() +"\n" +
                " description=" + product.getDescription() +"\n" +
                " isCancellable=" + product.getCancellable() +"\n" +
                " isReturnable=" + product.getReturnable() +"\n" +
                " brand=" + product.getBrand() + "\n"
                ;
        String confirmationUrl
                = webRequest.getContextPath() + "/admin/activateProduct?productId=" + product.getId();
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        String actMessage=messageSource.getMessage("msg.newProduct.toActivate",null,locale);
        mailMessage.setText(messageBody +"\n"+actMessage+ "\r\n" + "http://localhost:8080" + confirmationUrl);
        String message=messageSource.getMessage("msg.newProduct.received",null,locale);
        mailMessage.setSubject(message);
        mailMessage.setSentDate(new Date());
        for (User user:allAdmin) {
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom(user.getEmail());
            javaMailSender.send(mailMessage);
        }
    }

    public String addProductVariation(MultipartFile primaryImage, List<MultipartFile> secondaryImages,HttpServletRequest request, AddProductVariationDto addProductVariationDto,WebRequest webRequest) throws IOException {
        Locale locale=webRequest.getLocale();

//        List<ProductVariation> variation = variationRepository.findByProductId(addProductVariationDto.getProductId());

        String sellerEmail = request.getUserPrincipal().getName();
        String validateData = validateData(addProductVariationDto, sellerEmail, webRequest);
        if(!validateData.equalsIgnoreCase("success")){
            String message=messageSource.getMessage("msg.validation.failed",null,locale);
            throw  new EmailException(message);
        }
        ProductVariation productVariation= new ProductVariation();
        Product product = productRepository.findByid(addProductVariationDto.getProductId());
        productVariation.setPrice(addProductVariationDto.getPrice());
        productVariation.setProduct(product);
        productVariation.setPrimaryImageName(imageDao.uploadPrimaryImage(primaryImage,webRequest,addProductVariationDto.getProductId()));
        productVariation.setQuantityAvailable(addProductVariationDto.getQuantityAvailable());
        productVariation.setMetadata(addProductVariationDto.getMetadata());
        if(!secondaryImages.isEmpty()){
            Set<String> strings = imageDao.uploadSecondaryImage(secondaryImages, webRequest, addProductVariationDto.getProductId());
            String convert = SetConverter.convertToString(strings);
            productVariation.setSecondaryImageName(convert);
        }
        variationRepository.save(productVariation);
        String message=messageSource.getMessage("msg.variation.saved",null,locale);
        return message;

    }

    private String validateData(AddProductVariationDto addProductVariationDto,String sellerEmail,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Product product = productRepository.findByid(addProductVariationDto.getProductId());

        if(product==null){
            String message=messageSource.getMessage("msg.invalid.productId",null,locale);
            throw new EmailException(message);
        }
        if(!product.getActive()){
            String message=messageSource.getMessage("msg.product.inActive",null,locale);
            throw new EmailException(message);
        }
        if(!product.getSeller().getUser().getEmail().equalsIgnoreCase(sellerEmail)){
            String message=messageSource.getMessage("msg.seller.notProduct",null,locale);
            throw new EmailException(message);
        }
        if(addProductVariationDto.getQuantityAvailable()!=null){
            if(addProductVariationDto.getQuantityAvailable()<0 ){
                String message=messageSource.getMessage("msg.quantity.greater",null,locale);
                throw new EmailException(message);
            }
        }
        if(addProductVariationDto.getPrice()!=null) {
            if (addProductVariationDto.getPrice() < 0) {
                String message = messageSource.getMessage("msg.price.greater", null, locale);
                throw new EmailException(message);
            }
        }

        if(product.getDeleted()){
            String message=messageSource.getMessage("msg.product.deleted",null,locale);
            throw new EmailException(message);
        }

        Category category=product.getCategory();
        Long categoryId = category.getId();
        Map<String, String> metadata = addProductVariationDto.getMetadata();

        List<String> recievedFields =new ArrayList<>(metadata.keySet());
        List<String> actualFields= new ArrayList<>();
        List<Object> metadataFieldName = valuesRepository.getMetadataFieldName(categoryId);
        metadataFieldName.forEach((e)->{
            actualFields.add((String)e);
        });
        if(recievedFields.size()<actualFields.size()){
            String message=messageSource.getMessage("msg.field.less",null,locale);
            throw new EmailException(message);
        }
        recievedFields.removeAll(actualFields);
        if(!recievedFields.isEmpty()){
            String message=messageSource.getMessage("msg.field.more",null,locale);
            throw new EmailException(message);
        }
        List<String> metadataFields =new ArrayList<>(metadata.keySet());
        for (String fieldName: metadataFields) {
            CategoryMetadataField field = fieldRepository.findByName(fieldName);
            List<Object> valueFromCategoryAndField= valuesRepository.getValueFromCategoryAndField(categoryId, field.getId());
            String values=valueFromCategoryAndField.get(0).toString();
            Set<String> dbValue= SetConverter.convertToSet(values);

            String recievedValues= metadata.get(fieldName);
            if(recievedValues.isEmpty()){
                String message=messageSource.getMessage("msg.values.null",null,locale);
                throw new EmailException(message);
            }
            Set<String> actualSet = SetConverter.convertToSet(recievedValues);
            Set<String> union = Sets.union(actualSet, dbValue);
            if(union.size()>dbValue.size()){
                String message=messageSource.getMessage("msg.values.more",null,locale);
                throw new EmailException(message);
            }
        }
        String message=messageSource.getMessage("msg.success",null,locale);
        return message;
    }

    public ViewProductDto viewProduct(Long productId,WebRequest webRequest,HttpServletRequest request){
        String sellerEmail = request.getUserPrincipal().getName();
        Locale locale=webRequest.getLocale();
        Optional<Product> productOptional=productRepository.findById(productId);
        if(!productOptional.isPresent()){
            String message=messageSource.getMessage("msg.invalid.productId",null,locale);
            throw new EmailException(message);
        }
        Product product = productOptional.get();
        String email = product.getSeller().getUser().getEmail();
         if(!email.equalsIgnoreCase(sellerEmail)){
            String message=messageSource.getMessage("msg.seller.notProduct",null,locale);
            throw new EmailException(message);
        }
        else if(product.getDeleted()){
            String message=messageSource.getMessage("msg.product.deleted",null,locale);
            throw new EmailException(message);
        }
        ViewProductDto viewProductDto=new ViewProductDto();
        viewProductDto.setProductId(product.getId());
        viewProductDto.setProductName(product.getName());
        viewProductDto.setBrand(product.getBrand());
        viewProductDto.setDescription(product.getDescription());
        viewProductDto.setCancellable(product.getCancellable());
        viewProductDto.setReturnable(product.getReturnable());
        viewProductDto.setActive(product.getActive());

        Category category = product.getCategory();
        viewProductDto.setCategoryId(category.getId());
        viewProductDto.setCategoryName(category.getName());
        List<MetadatafieldNameAndValuesDto> nameAndValuesDtos=new ArrayList<>();
        List<Object[]> metadataNameAndValues = categoryRepository.getMetadataNameAndValues(category.getId());
        for (Object[] object:  metadataNameAndValues) {
            MetadatafieldNameAndValuesDto metadatafieldNameAndValuesDto=new MetadatafieldNameAndValuesDto();
            metadatafieldNameAndValuesDto.setMetadataFieldName((String) object[0]);
            metadatafieldNameAndValuesDto.setMetadataFieldValues((String)object[1]);
            nameAndValuesDtos.add(metadatafieldNameAndValuesDto);
        }
        viewProductDto.setMetadatafieldNameAndValues(nameAndValuesDtos);
        return viewProductDto;
    }

    public ViewProductVariationDto viewProductVariation(Long variationId , WebRequest webRequest,HttpServletRequest request){
        Locale locale=webRequest.getLocale();
        String sellerEmail = request.getUserPrincipal().getName();
        Optional<ProductVariation> variationOptional = variationRepository.findById(variationId);
        if(!variationOptional.isPresent()){
            String message=messageSource.getMessage("msg.variation.invalid",null,locale);
            throw new EmailException(message);
        }
        ProductVariation productVariation = variationOptional.get();
        Product product = productVariation.getProduct();
        if(product.getDeleted()){
            String message=messageSource.getMessage("msg.product.deleted",null,locale);
            throw new EmailException(message);
        }
        String email = product.getSeller().getUser().getEmail();
        if(!email.equalsIgnoreCase(sellerEmail)){
            String message=messageSource.getMessage("msg.seller.notProduct",null,locale);
            throw new EmailException(message);
        }
       return setVariation(product,productVariation);
    }

    private ViewProductVariationDto setVariation(Product product,ProductVariation productVariation){
        ViewProductVariationDto viewProductVariationDto=new ViewProductVariationDto();
        viewProductVariationDto.setProductId(product.getId());
        viewProductVariationDto.setProductName(product.getName());
        viewProductVariationDto.setProductBrand(product.getBrand());
        viewProductVariationDto.setProductDescription(product.getDescription());
        viewProductVariationDto.setPrice(productVariation.getPrice());
        viewProductVariationDto.setQuantityAvailable(productVariation.getQuantityAvailable());
        viewProductVariationDto.setVariationActive(productVariation.getActive());
        viewProductVariationDto.setCancellable(product.getCancellable());
        viewProductVariationDto.setReturnable(product.getReturnable());
        viewProductVariationDto.setPrimageImage(productVariation.getPrimaryImageName());
        if(productVariation.getSecondaryImageName()!=null) {
            viewProductVariationDto.setSecondaryImage(SetConverter.convertToSet(productVariation.getSecondaryImageName()));
        }
        viewProductVariationDto.setMetadata(productVariation.getMetadata());

        return viewProductVariationDto;
    }

    public List<ViewProductDto> viewAllProduct(String offset,  String max,  String field, String order,HttpServletRequest request) {
        Integer offSetPage = Integer.parseInt(offset);
        Integer sizeOfPage = Integer.parseInt(max);
        Pageable pageable;
        if(order.equalsIgnoreCase("Ascending")) {
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.ASC, field);
        }else{
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.DESC, field);
        }
        String sellerEmail = request.getUserPrincipal().getName();
        User user = userRepository.findByEmail(sellerEmail);
        List<Product> allProduct = productRepository.getAllProduct(user.getId(), pageable);
        List<ViewProductDto> viewProductDtoList =new ArrayList<>();
        for (Product product: allProduct) {
            ViewProductDto viewProductDto=new ViewProductDto();
            viewProductDto.setProductId(product.getId());
            viewProductDto.setProductName(product.getName());
            viewProductDto.setBrand(product.getBrand());
            viewProductDto.setDescription(product.getDescription());
            viewProductDto.setCancellable(product.getCancellable());
            viewProductDto.setReturnable(product.getReturnable());
            viewProductDto.setActive(product.getActive());

            Category category = product.getCategory();
            viewProductDto.setCategoryId(category.getId());
            viewProductDto.setCategoryName(category.getName());
            List<MetadatafieldNameAndValuesDto> nameAndValuesDtos=new ArrayList<>();
            List<Object[]> metadataNameAndValues = categoryRepository.getMetadataNameAndValues(category.getId());
            for (Object[] object:  metadataNameAndValues) {
                MetadatafieldNameAndValuesDto metadatafieldNameAndValuesDto=new MetadatafieldNameAndValuesDto();
                metadatafieldNameAndValuesDto.setMetadataFieldName((String) object[0]);
                metadatafieldNameAndValuesDto.setMetadataFieldValues((String)object[1]);
                nameAndValuesDtos.add(metadatafieldNameAndValuesDto);
            }
            viewProductDto.setMetadatafieldNameAndValues(nameAndValuesDtos);
            viewProductDtoList.add(viewProductDto);
        }
        return viewProductDtoList;
    }

    public List<ViewAllProductVariationDto> viewAllProductVariation(Long productId,String offset,  String max,  String field, String order,HttpServletRequest request,WebRequest webRequest) {
        Locale locale=webRequest.getLocale();
        String sellerEmail = request.getUserPrincipal().getName();
        Integer offSetPage = Integer.parseInt(offset);
        Integer sizeOfPage = Integer.parseInt(max);
        Pageable pageable;
        if(order.equalsIgnoreCase("Ascending")) {
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.ASC, field);
        }else{
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.DESC, field);
        }
        List<ProductVariation> productVariations = variationRepository.getAllVariationByProductId(productId, pageable);
        if(productVariations.isEmpty()){
            String message=messageSource.getMessage("msg.variation.notFound",null,locale);
            throw new EmailException(message);
        }
        Product product = productRepository.findByid(productId);
        List<ViewAllProductVariationDto> productVariationDtoList=new ArrayList<>();
        if(product==null) {
                String message=messageSource.getMessage("msg.invalid.productId",null,locale);
                throw new EmailException(message);
        }
        if(product.getDeleted()){
            String message=messageSource.getMessage("msg.product.deleted",null,locale);
            throw new EmailException(message);
        }
        String email = product.getSeller().getUser().getEmail();
        if(!email.equalsIgnoreCase(sellerEmail)){
            String message=messageSource.getMessage("msg.seller.notProduct",null,locale);
            throw new EmailException(message);
        }
        for (ProductVariation productVariation:productVariations) {

            ViewAllProductVariationDto productVariationDto = setAllVariation(product, productVariation);
            productVariationDtoList.add(productVariationDto);
        }
        return productVariationDtoList;
    }

    private ViewAllProductVariationDto setAllVariation(Product product,ProductVariation productVariation){
        ViewAllProductVariationDto viewProductVariationDto=new ViewAllProductVariationDto();
        viewProductVariationDto.setVariationId(productVariation.getId());
        viewProductVariationDto.setProductName(product.getName());
        viewProductVariationDto.setProductBrand(product.getBrand());
        viewProductVariationDto.setProductDescription(product.getDescription());
        viewProductVariationDto.setPrice(productVariation.getPrice());
        viewProductVariationDto.setQuantityAvailable(productVariation.getQuantityAvailable());
        viewProductVariationDto.setVariationActive(productVariation.getActive());
        viewProductVariationDto.setCancellable(product.getCancellable());
        viewProductVariationDto.setReturnable(product.getReturnable());
        viewProductVariationDto.setPrimageImage(productVariation.getPrimaryImageName());
        if (productVariation.getSecondaryImageName()!=null) {
            viewProductVariationDto.setSecondaryImage(SetConverter.convertToSet(productVariation.getSecondaryImageName()));
        }
        viewProductVariationDto.setMetadata(productVariation.getMetadata());
        return viewProductVariationDto;
    }

    public String deleteProduct(Long productId,HttpServletRequest request,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        String sellerEmail = request.getUserPrincipal().getName();
        Optional<Product> productOptional = productRepository.findById(productId);

        if(!productOptional.isPresent()){
            String message=messageSource.getMessage("msg.invalid.productId",null,locale);
            throw new EmailException(message);
        }
        Product product = productOptional.get();
        String email = product.getSeller().getUser().getEmail();
        if(!sellerEmail.equalsIgnoreCase(email)){
            String message=messageSource.getMessage("msg.seller.notProduct",null,locale);
            throw new EmailException(message);
        }
        productRepository.deleteById(productId);
        String message=messageSource.getMessage("msg.product.delete",null,locale);
        return message;
    }

    public String updateProduct(UpdateProductDto updateProductDto,HttpServletRequest request,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        String sellerEmail = request.getUserPrincipal().getName();
        Long productId = updateProductDto.getProductId();
        Optional<Product> productOptional = productRepository.findById(productId);
        if(!productOptional.isPresent()){
            String message=messageSource.getMessage("msg.invalid.productId",null,locale);
            throw new EmailException(message);
        }
        Product product = productOptional.get();
        String email = product.getSeller().getUser().getEmail();
        if(!sellerEmail.equalsIgnoreCase(email)){
            String message=messageSource.getMessage("msg.seller.notProduct",null,locale);
            throw new EmailException(message);
        }
        if(updateProductDto.getName()!=null) {
            Product productByName = productRepository.getProductByName(product.getBrand(), product.getCategory().getId(), updateProductDto.getName(), product.getSeller().getUser().getId());
            if (productByName != null) {
                String message = messageSource.getMessage("msg.productName.exist", null, locale);
                throw new EmailException(message);
            }
        }
        product.setName(updateProductDto.getName());

        if(updateProductDto.getDescription()!=null){
            product.setDescription(updateProductDto.getDescription());
        }
        if(updateProductDto.getCancellable()!=null){
            product.setCancellable(updateProductDto.getCancellable());
        }
        if(updateProductDto.getReturnable()!=null){
            product.setReturnable(updateProductDto.getReturnable());
        }
        productRepository.save(product);

        String message=messageSource.getMessage("msg.product.updated",null,locale);
        return message;
    }

    public String updateProductVariation(MultipartFile primaryImage, List<MultipartFile> secondaryImages,HttpServletRequest request, UpdateProductVariationDto updateProductVariationDto,WebRequest webRequest) throws IOException {
        Locale locale=webRequest.getLocale();
        Optional<ProductVariation> variationOptional = variationRepository.findById(updateProductVariationDto.getProductVariationId());
        String sellerEmail = request.getUserPrincipal().getName();
        String validateData = validateUpdateData(updateProductVariationDto, sellerEmail, webRequest);
        if(!validateData.equalsIgnoreCase("success")){
            String message=messageSource.getMessage("msg.validation.failed",null,locale);
            throw  new EmailException(message);
        }
        ProductVariation productVariation= variationOptional.get();
        if(updateProductVariationDto.getPrice()!=null) {
            productVariation.setPrice(updateProductVariationDto.getPrice());
        }
        if(primaryImage!=null) {
            productVariation.setPrimaryImageName(imageDao.uploadPrimaryImage(primaryImage, webRequest, productVariation.getProduct().getId()));
        }
        productVariation.setQuantityAvailable(updateProductVariationDto.getQuantityAvailable());
        productVariation.setMetadata(updateProductVariationDto.getMetadata());
        if(updateProductVariationDto.getActive()!=null) {
            productVariation.setActive(updateProductVariationDto.getActive());
        }
        if(!secondaryImages.isEmpty()){
            Set<String> strings = imageDao.uploadSecondaryImage(secondaryImages, webRequest, productVariation.getProduct().getId());
            String convert = SetConverter.convertToString(strings);
            productVariation.setSecondaryImageName(convert);
        }
        variationRepository.save(productVariation);

        String message=messageSource.getMessage("msg.variation.updated",null,locale);
        return message;

    }

    private String validateUpdateData(UpdateProductVariationDto updateProductVariationDto,String sellerEmail,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Optional<ProductVariation> variationOptional = variationRepository.findById(updateProductVariationDto.getProductVariationId());

        if(!variationOptional.isPresent()){
            String message=messageSource.getMessage("msg.variation.invalid",null,locale);
            throw new EmailException(message);
        }
        ProductVariation productVariation = variationOptional.get();
        Product product =productVariation.getProduct();
        if(!product.getActive()){
            String message=messageSource.getMessage("msg.product.inActive",null,locale);
            throw new EmailException(message);
        }
        if(!product.getSeller().getUser().getEmail().equalsIgnoreCase(sellerEmail)){
            String message=messageSource.getMessage("msg.seller.notProduct",null,locale);
            throw new EmailException(message);
        }
        if(updateProductVariationDto.getQuantityAvailable()!=null){
            if(updateProductVariationDto.getQuantityAvailable()<0 ){
                String message=messageSource.getMessage("msg.quantity.greater",null,locale);
                throw new EmailException(message);
            }
        }
        if(updateProductVariationDto.getPrice()!=null) {
            if (updateProductVariationDto.getPrice() < 0) {
                String message = messageSource.getMessage("msg.price.greater", null, locale);
                throw new EmailException(message);
            }
        }

        if(product.getDeleted()){
            String message=messageSource.getMessage("msg.product.deleted",null,locale);
            throw new EmailException(message);
        }

        Category category=product.getCategory();
        Long categoryId = category.getId();
        if(updateProductVariationDto.getMetadata()!=null) {
            Map<String, String> metadata = updateProductVariationDto.getMetadata();

            List<String> recievedFields = new ArrayList<>(metadata.keySet());
            List<String> actualFields = new ArrayList<>();
            List<Object> metadataFieldName = valuesRepository.getMetadataFieldName(categoryId);
            metadataFieldName.forEach((e) -> {
                actualFields.add((String) e);
            });
            if (recievedFields.size() < actualFields.size()) {
                String message = messageSource.getMessage("msg.field.less", null, locale);
                throw new EmailException(message);
            }
            recievedFields.removeAll(actualFields);
            if (!recievedFields.isEmpty()) {
                String message = messageSource.getMessage("msg.field.more", null, locale);
                throw new EmailException(message);
            }

            List<String> metadataFields = new ArrayList<>(metadata.keySet());
            for (String fieldName : metadataFields) {
                CategoryMetadataField field = fieldRepository.findByName(fieldName);
                List<Object> valueFromCategoryAndField = valuesRepository.getValueFromCategoryAndField(categoryId, field.getId());
                String values = valueFromCategoryAndField.get(0).toString();
                Set<String> dbValue = SetConverter.convertToSet(values);

                String recievedValues = metadata.get(fieldName);
                if (!recievedValues.isEmpty()) {
                    String message = messageSource.getMessage("msg.values.null", null, locale);
                    throw new EmailException(message);
                }
                Set<String> actualSet = SetConverter.convertToSet(recievedValues);
                Set<String> union = Sets.union(actualSet, dbValue);
                if (union.size() > dbValue.size()) {
                    String message = messageSource.getMessage("msg.values.more", null, locale);
                    throw new EmailException(message);
                }
            }
        }
        String message=messageSource.getMessage("msg.success",null,locale);
        return message;
    }

    public ViewCustomerProductDto viewCustomerProduct(Long productId,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Optional<Product> productOptional = productRepository.findById(productId);
        if(!productOptional.isPresent()){
            String message=messageSource.getMessage("msg.invalid.productId",null,locale);
            throw new EmailException(message);
        }
        Product product = productOptional.get();
        if(product.getDeleted()){
            String message=messageSource.getMessage("msg.product.deleted",null,locale);
            throw new EmailException(message);
        }
        if(!product.getActive()){
            String message=messageSource.getMessage("msg.product.inActive",null,locale);
            throw new EmailException(message);
        }
        Category category = product.getCategory();
        List<ProductVariation> variationList = variationRepository.findByProductId(productId);
        if(variationList.isEmpty()){
            String message=messageSource.getMessage("msg.variation.notFound",null,locale);
            throw  new EmailException(message);
        }

        ViewCustomerProductDto viewCustomerProductDto=new ViewCustomerProductDto();
        viewCustomerProductDto.setProductName(product.getName());
        viewCustomerProductDto.setBrand(product.getBrand());
        viewCustomerProductDto.setDescription(product.getDescription());
        viewCustomerProductDto.setCancellable(product.getCancellable());
        viewCustomerProductDto.setReturnable(product.getReturnable());
        viewCustomerProductDto.setCategoryId(category.getId());
        viewCustomerProductDto.setCategoryName(category.getName());

        List<ProductVariationDto> variationDtoList=new ArrayList<>();
        for (ProductVariation productVariation:variationList) {
            ProductVariationDto variationDto=new ProductVariationDto();
            variationDto.setQuantityAvailable(productVariation.getQuantityAvailable());
            variationDto.setPrice(productVariation.getPrice());
            variationDto.setMetadata(productVariation.getMetadata());
            variationDto.setVariationId(productVariation.getId());
            variationDto.setVariationActive(productVariation.getActive());
            variationDto.setPrimageImage(productVariation.getPrimaryImageName());
            variationDto.setSecondaryImage(SetConverter.convertToSet(productVariation.getSecondaryImageName()));
            variationDtoList.add(variationDto);
        }
         viewCustomerProductDto.setVariation(variationDtoList);
        return viewCustomerProductDto;
    }

    public List<ViewCustomerAllProductDto> viewCustomerAllProduct(Long categoryId,String offset,WebRequest webRequest, String max,  String field, String order) {

        Integer offSetPage = Integer.parseInt(offset);
        Integer sizeOfPage = Integer.parseInt(max);
        Pageable pageable;
        if(order.equalsIgnoreCase("Ascending")) {
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.ASC, field);
        }else{
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.DESC, field);
        }
        List<Category> categoryList = categoryRepository.findByParentId(categoryId);
        if(categoryList.isEmpty()){
           return getProductDetails(categoryId,pageable);
        }else{
         return null;
        }
    }
    private List<ViewCustomerAllProductDto> getProductDetails(Long categoryId, Pageable pageable){
        List<ViewCustomerAllProductDto> viewCustomerAllProductDtos=new ArrayList<>();
        List<Product> productList = productRepository.findByCategoryId(categoryId, pageable);
        for (Product product:productList) {
            if (!product.getDeleted() && product.getActive()) {
                ViewCustomerAllProductDto viewCustomerAllProductDto = setProductdata(product);
                viewCustomerAllProductDtos.add(viewCustomerAllProductDto);
            }
        }
        return viewCustomerAllProductDtos;
    }
    private ViewCustomerAllProductDto setProductdata(Product product){
        ViewCustomerAllProductDto viewCustomerAllProductDto = new ViewCustomerAllProductDto();
        viewCustomerAllProductDto.setProductId(product.getId());
        viewCustomerAllProductDto.setProductName(product.getName());
        Category category = product.getCategory();
        viewCustomerAllProductDto.setCategoryId(category.getId());
        viewCustomerAllProductDto.setCategoryName(category.getName());
        List<ProductVariation> variationList = variationRepository.findByProductId(product.getId());
        List<ViewcustomerDto> viewcustomerDtos = new ArrayList<>();
        for (ProductVariation productVariation : variationList) {
            ViewcustomerDto viewcustomerDto = new ViewcustomerDto();
            viewcustomerDto.setVariationId(productVariation.getId());
            viewcustomerDto.setMetadata(productVariation.getMetadata());
            viewcustomerDto.setPrimageImage(productVariation.getPrimaryImageName());
            viewcustomerDtos.add(viewcustomerDto);
        }
        viewCustomerAllProductDto.setVariation(viewcustomerDtos);
        return viewCustomerAllProductDto;
    }
    public List<ViewCustomerAllProductDto> viewSimilarProduct(Long productId,WebRequest webRequest,String offset,  String max,  String field, String order){
        Locale locale=webRequest.getLocale();
        Optional<Product> productOptional = productRepository.findById(productId);
        if(!productOptional.isPresent()){
            String message=messageSource.getMessage("msg.invalid.productId",null,locale);
            throw new EmailException(message);
        }
        Long categoryId = productOptional.get().getCategory().getId();
        return viewCustomerAllProduct(categoryId,offset,webRequest,max,field,order);
    }
    public ViewCustomerProductDto viewAdminProduct(Long productId,WebRequest webRequest){
        Locale locale=webRequest.getLocale();
        Optional<Product> productOptional = productRepository.findById(productId);
        if(!productOptional.isPresent()){
            String message=messageSource.getMessage("msg.invalid.productId",null,locale);
            throw new EmailException(message);
        }
        Product product = productOptional.get();
        Category category = product.getCategory();
        List<ProductVariation> variationList = variationRepository.findByProductId(productId);
        if(variationList.isEmpty()){
            String message=messageSource.getMessage("msg.variation.notFound",null,locale);
            throw  new EmailException(message);
        }
        ViewCustomerProductDto viewCustomerProductDto=new ViewCustomerProductDto();
        viewCustomerProductDto.setProductName(product.getName());
        viewCustomerProductDto.setBrand(product.getBrand());
        viewCustomerProductDto.setDescription(product.getDescription());
        viewCustomerProductDto.setCancellable(product.getCancellable());
        viewCustomerProductDto.setReturnable(product.getReturnable());
        viewCustomerProductDto.setCategoryId(category.getId());
        viewCustomerProductDto.setCategoryName(category.getName());

        List<ProductVariationDto> variationDtoList=new ArrayList<>();
        for (ProductVariation productVariation:variationList) {
            ProductVariationDto variationDto=new ProductVariationDto();
            variationDto.setQuantityAvailable(productVariation.getQuantityAvailable());
            variationDto.setPrice(productVariation.getPrice());
            variationDto.setMetadata(productVariation.getMetadata());
            variationDto.setVariationId(productVariation.getId());
            variationDto.setVariationActive(productVariation.getActive());
            variationDto.setPrimageImage(productVariation.getPrimaryImageName());
            variationDto.setSecondaryImage(SetConverter.convertToSet(productVariation.getSecondaryImageName()));
            variationDtoList.add(variationDto);
        }
        viewCustomerProductDto.setVariation(variationDtoList);
        return viewCustomerProductDto;
    }
    
    public List<ViewCustomerAllProductDto> viewAllProduct(String offset,  String max,  String field, String order){
        Integer offSetPage = Integer.parseInt(offset);
        Integer sizeOfPage = Integer.parseInt(max);
        Pageable pageable;
        if(order.equalsIgnoreCase("Ascending")) {
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.ASC, field);
        }else{
            pageable = PageRequest.of(offSetPage, sizeOfPage, Sort.Direction.DESC, field);
        }
        List<Product> productList = (List<Product>) productRepository.findAll();
        List<ViewCustomerAllProductDto> viewCustomerAllProductDtos=new ArrayList<>();
        for (Product product:productList) {
            if (!product.getDeleted() && product.getActive()) {
                ViewCustomerAllProductDto viewCustomerAllProductDto = setProductdata(product);
                viewCustomerAllProductDtos.add(viewCustomerAllProductDto);
            }
        }
        return viewCustomerAllProductDtos;
    }

}
