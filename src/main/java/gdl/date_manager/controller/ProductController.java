package gdl.date_manager.controller;

import gdl.date_manager.data.ProductRepository;
import gdl.date_manager.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/product")
    public ProductModel saveProduct(@RequestBody ProductModel productModel){
        return productRepository.save(productModel);
    }
}
