package ua.strongBody.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.strongBody.models.Product;
import ua.strongBody.services.ProductService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/catalog")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String getAllProductsPage(Model model) {
        List<Product> allProducts = productService.findAll();
        model.addAttribute("productList", allProducts);
        return "product/productList";
    }

    @GetMapping("/{id}")
    public String getProductPage(@PathVariable UUID id, Model model) {
        Product product = productService.findById(id);
        product.setAvailableAmount(product.getAvailableAmount() - product.getReservedAmount());
        model.addAttribute("product", product);
        return "product/productView";
    }

    @GetMapping("/add")
    public String addProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "product/productAdd";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product) {
        productService.createProduct(product);
        return "redirect:/catalog/";
    }
}
