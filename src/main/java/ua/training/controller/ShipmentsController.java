package ua.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.training.controller.exception.OrderCreateException;
import ua.training.dto.OrderDto;
import ua.training.entity.user.User;
import ua.training.service.serviceImpl.OrderServiceImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class ShipmentsController {

    private final OrderServiceImpl orderService;

    public ShipmentsController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @Value("${pagination.user.shipments.size}")
    Integer USER_SHIPMENTS_SIZE;

    @Value("${pagination.admin.size}")
    Integer ADMIN_SHIPMENTS_SIZE;

    @Value("${first.page.number}")
    Integer FIRST_PAGE;

    @GetMapping("/my_shipments/page/{page}")
    public String shipmentsPage(Model model, @AuthenticationPrincipal User user,
                                @PathVariable("page") int page) {

        //insertBalanceInfo(user, model);

//        List<OrderDTO> orders = orderService.findAllUserOrder(user.getId());
//
//        insertPaginatedOrders(page, USER_SHIPMENTS_SIZE, model, orders);

        return "my_shipments";
    }

    @GetMapping("/create")
    public String createOrder(@ModelAttribute("newOrder") OrderDto order, @AuthenticationPrincipal User user,
                              Model model) {
        //insertBalanceInfo(user, model);

        model.addAttribute("newOrder", order == null ? new OrderDto() : order);

        return "new_order";
    }

    @PostMapping("/create")
    public String newOrder(@ModelAttribute OrderDto modelOrder, @AuthenticationPrincipal User user) throws OrderCreateException {

        //orderService.createOrder(modelOrder, user);
        log.info("new order creation");

        return "redirect:/my_shipments/page/1";

    }


    private void insertPaginatedOrders(int page, int size, Model model, List<OrderDto> orders) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<OrderDto> articlePage = orderService.findPaginated(pageable, orders);

        int totalPages = articlePage.getTotalPages();
        List<Integer> pageNumbers = IntStream.rangeClosed(FIRST_PAGE, totalPages).boxed().collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("orders", articlePage.getContent());
    }

}
