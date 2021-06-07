package ua.strongBody.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.facade.CartFacade;
import ua.strongBody.facade.OrderFacade;
import ua.strongBody.facade.impl.CartFacadeImpl;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartFacade cartFacade;
    private final OrderFacade orderFacade;

    public CartController(CartFacadeImpl cartFacade, OrderFacade orderFacade) {
        this.cartFacade = cartFacade;
        this.orderFacade = orderFacade;
    }

    @GetMapping("/")
    public String getCartPage(Principal principal, Model model) {
        String customerUsername = principal.getName();
        Cart cart = cartFacade.getCartByCustomerUsername(customerUsername);
        List<Booking> customerBookings = cartFacade.getCustomerBookingsByUsername(customerUsername);
        model.addAttribute("cart", cart);
        model.addAttribute("bookings", customerBookings);
        return "cart/cartView";
    }

    @GetMapping("/add/{productId}")
    public String addProductToCart(@PathVariable UUID productId, Principal principal) {
        try {
            String customerUsername = principal.getName();
            cartFacade.addProductToCartByCustomerUsername(productId, customerUsername);
        } catch (FieldNotFoundException ex) {
            return "redirect:/";
        }

        return "redirect:/catalog/";
    }

    @GetMapping("/delete/{bookingId}")
    public String removeBooking(@PathVariable UUID bookingId) {
        cartFacade.removeBookingFromCart(bookingId);
        return "redirect:/cart/";
    }

    @GetMapping("/submit")
    public String submitCart(Principal principal) {
        String customerUsername = principal.getName();

        orderFacade.createOrderFromCartByCustomerUsername(customerUsername);
        cartFacade.submitCartByCustomerUsername(customerUsername);
        return "cart/cartSubmit";
    }
}
