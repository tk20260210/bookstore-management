package com.example.bookstore;

import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    //Search and Show List
    @GetMapping
    public String list(@RequestParam(required = false) String title,
                       @RequestParam(required = false) String author,
                       @RequestParam(required = false) String category,
                       Model model) {
        List<Book> books;

        books = bookService.getSearchBooks(title, author, category);

        model.addAttribute("books", books);
        model.addAttribute("searchTitle", title);
        model.addAttribute("searchAuthor", author);
        model.addAttribute("searchCategory", category);
        return "list";
    }

    //Show Register Form
    @GetMapping("/add")
    public String addForm() {
        return "add";
    }

    //Register process
    @PostMapping("/add")
    public String add(Book book) {
        bookService.add(book);
        return "redirect:/";
    }

    //Show edit form
    @GetMapping("/edit")
    public String editForm(@RequestParam Integer id, Model model) {
        Optional<Book> book = bookService.getBooks(id);
        if (book.isPresent()) {
            model.addAttribute("book",book.get());
            return "edit";
        }
        return "redirect:/";
    }

    //Update
    @PostMapping("/edit")
    public String edit(Book book) {
        bookService.edit(book);
        return "redirect:/";
    }

    //Logical delete
    @PostMapping("/delete")
    public String deleted(@RequestParam Integer id) {
        bookService.deleted(id);
        return "redirect:/";
    }

    //Increase stock
    @PostMapping("/stock/increase")
    public String increaseStock(@RequestParam Integer id, @RequestParam Integer amount) {
        bookService.increaseStock(id,amount);
        return "redirect:/";
    }

    //Decrease stock
    @PostMapping("/stock/decrease")
    public String decreaseStock(@RequestParam Integer id, @RequestParam Integer amount) {
        bookService.decreaseStock(id,amount);
        return "redirect:/";
    }

    //Sales process
    @PostMapping("/sell")
    public String sell(@RequestParam Integer id, @RequestParam Integer quantity,
                       RedirectAttributes redirectAttributes) {
         try {
             bookService.sell(id, quantity);
         } catch(RuntimeException e) {
             redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
         }
         return "redirect:/";
    }

    //Show Sales record
    @GetMapping("/sales")
    public String salesHistory(Model model) {
        List<Sales> salesList = bookService.getsalesList();
        Integer todayTotal = bookService.getTodayTotalSales();
        model.addAttribute("salesList", salesList);
        model.addAttribute("todayTotal", todayTotal);
        return "sales";
    }

    //Purchase process
    @PostMapping("/purchase")
    public String purchase(@RequestParam Integer id,
                           @RequestParam Integer quantity,
                           @RequestParam Integer unitPrice) {
        bookService.purchase(id, quantity, unitPrice);
        return "redirect:/";
    }

    //Show Purchase history
    @GetMapping("/purchases")
    public String purchaseHistory(Model model) {
        List<Purchase> purchaseList = bookService.getpurchaseList();
        Integer todayTotal = bookService.getTodayTotalPurchases();
        model.addAttribute("purchaseList", purchaseList);
        model.addAttribute("todayTotal", todayTotal);
        return "purchases";
    }

    // ========== Function: Statistics and report ==========

    //Show report
    @GetMapping("/report")
    public String report(Model model) {
        List<Object[]> categorySales = bookService.getCategorySales();
        List<Object[]> monthlySales = bookService.getMonthlySales();
        Integer totalSales = bookService.getTotalSales();
        Integer totalPurchases = bookService.getTotalPurchases();

        Integer profit = (totalSales != null ? totalSales : 0) - (totalPurchases != null ? totalPurchases : 0);

        model.addAttribute("categorySales", categorySales);
        model.addAttribute("monthlySales", monthlySales);
        model.addAttribute("totalSales", totalSales != null ? totalSales: 0);
        model.addAttribute("totalPurchases", totalPurchases != null ? totalPurchases: 0);
        model.addAttribute("profit", profit);
        return "report";
    }
}
