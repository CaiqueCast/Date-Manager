package gdl.date_manager.controller;

import gdl.date_manager.data.ProductRepository;
import gdl.date_manager.data.UserRepository;
import gdl.date_manager.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/product")
    public ResponseEntity<?> saveProduct(@RequestBody ProductModel productModel){
        if (!userRepository.existsById(productModel.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado.");
        }
        ProductModel saved = productRepository.save(productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id,
                                           @RequestBody ProductModel productModel){

        try {

            int rows = productRepository.updateForOwner(
                    productModel.getName(),
                    productModel.getValidity(),
                    productModel.getBarcode(),
                    id,
                    productModel.getUser().getId()
            );
            if (rows > 0) {
                return ResponseEntity.ok("Produto atualizado com sucesso!");
            } else if (!userRepository.existsById(productModel.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar produto: " + e.getMessage());
        }
    }
    @GetMapping("/product")
    public List<ProductModel> findAllProduct(){
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
    @GetMapping("/productdate")
    public List<ProductModel> findAllProductByDate(){
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "validity"));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id){
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return ResponseEntity.ok("Produto deletado com sucesso.");
        }  else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado");
        }
    }
}
