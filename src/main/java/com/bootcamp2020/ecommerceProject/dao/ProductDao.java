package com.bootcamp2020.ecommerceProject.dao;

import com.bootcamp2020.ecommerceProject.dto.AddProductVariationDto;
import com.bootcamp2020.ecommerceProject.dto.ProductDto;
import com.bootcamp2020.ecommerceProject.dto.ViewProductDto;
import com.bootcamp2020.ecommerceProject.dto.ViewProductVariationDto;
import com.bootcamp2020.ecommerceProject.dto.categorySellerDtos.MetadatafieldNameAndValuesDto;
import com.bootcamp2020.ecommerceProject.entities.*;
import com.bootcamp2020.ecommerceProject.exceptions.EmailException;
import com.bootcamp2020.ecommerceProject.repositories.*;
import com.bootcamp2020.ecommerceProject.utils.SetConverter;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
            Product prooduct = productRepository.getProoductByName(brand, categoryId, name, userId);
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
        ProductVariation variation = variationRepository.findByProductId(addProductVariationDto.getProductId());
        if(variation==null){
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
        }else{
            String message=messageSource.getMessage("msg.variation.alreadyExist",null,locale);
            return message;
        }
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
            actualFields.add(e.toString());
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
            Set<String> valueSet= SetConverter.convertToSet(values);
            String recievedValues= metadata.get(fieldName);
            Set<String> stringSet = SetConverter.convertToSet(recievedValues);

            if(!Sets.difference(valueSet,stringSet).isEmpty()){
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
       return setVariatioin(product,productVariation);
    }

    private ViewProductVariationDto setVariatioin(Product product,ProductVariation productVariation){
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
        viewProductVariationDto.setSecondaryImage(SetConverter.convertToSet(productVariation.getSecondaryImageName()));
        viewProductVariationDto.setMetadata(productVariation.getMetadata());

        return viewProductVariationDto;
    }
}
