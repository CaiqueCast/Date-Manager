package gdl.date_manager.controller;

import gdl.date_manager.data.ProductRepository;
import gdl.date_manager.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ProductPageController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/productos")
    public String listProducts(Model model) {
        List<ProductModel> products = productRepository.findAllByOrderByValidityAsc();

        model.addAttribute("products", products);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("title", "Meus Produtos");

        return "list"; // templates/products/list.html templates/products/list.html

    }
}