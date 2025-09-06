package gdl.date_manager.controller;

import gdl.date_manager.data.ProductRepository;
import gdl.date_manager.data.UserRepository;
import gdl.date_manager.model.ProductModel;
import gdl.date_manager.model.UserModel;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductWebController {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public ProductWebController(ProductRepository productRepo, UserRepository userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    // Lista com ordenação por validade, busca e filtros
    @GetMapping
    public String list(@RequestParam(value="q", required=false) String q,
                       @RequestParam(value="filter", required=false) String filter, // "1m" | "2m"
                       Model model,
                       Authentication auth) {
        UserModel user = userRepo.findByUserName(auth.getName()).orElseThrow();
        Integer userId = user.getId();

        List<ProductModel> products;
        if (q != null && !q.isBlank()) {
            products = productRepo.search(userId, q.trim());
        } else if ("1m".equalsIgnoreCase(filter) || "2m".equalsIgnoreCase(filter)) {
            LocalDate now = LocalDate.now();
            LocalDate end = now.plusMonths("1m".equalsIgnoreCase(filter) ? 1 : 2);
            products = productRepo.expiringBetween(userId, now, end);
        } else {
            products = productRepo.findByUserIdOrderByValidityAsc(userId);
        }

        LocalDate today = LocalDate.now();
        for (ProductModel p : products) {
            long days = ChronoUnit.DAYS.between(today, p.getValidity());
            p.setDaysUntilExpiration(days);
        }
        model.addAttribute("products", products);
        model.addAttribute("today", today);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("filter", filter == null ? "" : filter);
        return "products/list";
    }

    // Form de novo produto
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new ProductModel());
        return "products/form";
    }

    // Criar
    @PostMapping
    public String create(@Valid @ModelAttribute("product") ProductModel product,
                         BindingResult errors,
                         Authentication auth,
                         Model model) {
        if (errors.hasErrors()) return "products/form";
        UserModel user = userRepo.findByUserName(auth.getName()).orElseThrow();
        product.setUser(user);
        productRepo.save(product);
        return "redirect:/products";
    }

    // Editar
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Authentication auth, Model model) {
        UserModel user = userRepo.findByUserName(auth.getName()).orElseThrow();
        ProductModel p = productRepo.findById(id).orElseThrow();
        if (!p.getUser().getId().equals(user.getId())) {
            return "redirect:/products";
        }
        model.addAttribute("product", p);
        return "products/form";
    }

    // Atualizar (via metodo escondido _method=put)
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("product") ProductModel product,
                         BindingResult errors,
                         Authentication auth,
                         Model model) {
        if (errors.hasErrors()) {
            product.setId(id); // Mantém o ID no formulário
            return "products/form";
        }

        UserModel user = userRepo.findByUserName(auth.getName()).orElseThrow();
        ProductModel existing = productRepo.findById(id).orElseThrow();

        if (!existing.getUser().getId().equals(user.getId())) {
            return "redirect:/products";
        }

        // Atualiza apenas os campos permitidos
        existing.setName(product.getName());
        existing.setValidity(product.getValidity());
        existing.setBarcode(product.getBarcode());

        productRepo.save(existing);
        return "redirect:/products";
    }

    // Excluir (via form com _method=delete)
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, Authentication auth) {
        UserModel user = userRepo.findByUserName(auth.getName()).orElseThrow();
        productRepo.findById(id).ifPresent(p -> {
            if (p.getUser().getId().equals(user.getId())) productRepo.deleteById(id);
        });
        return "redirect:/products";
    }

    /* Helper disponível no template com ${#temporals} e lógica de destaque via Thymeleaf */
}
