package com.example.bookstore;

import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SalesRepository salesRepository;

    //Search and Show List
    @GetMapping
    public String list(@RequestParam(required = false) String title,
                       @RequestParam(required = false) String author,
                       @RequestParam(required = false) String category,
                       Model model) {
        List<Book> books;

        //if there is search conditions, execute search or else execute find by all.
        if ((title != null && !title.isEmpty()) || (author != null && !author.isEmpty()) || (category != null && !category.isEmpty())) {
            String searchTitle = (title != null && !title.isEmpty()) ? title : null;
            String searchAuthor = (author != null && !author.isEmpty()) ? author : null;
            String searchCategory = (category != null && !category.isEmpty()) ? category : null;
            books = bookRepository.searchBooks(searchTitle, searchAuthor,searchCategory);
        } else {
            books = bookRepository.findByDeleted(0);
        }

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
        book.setDeleted(0);
        bookRepository.save(book);
        return "redirect:/";
    }

    //Show edit form
    @GetMapping("/edit")
    public String editForm(@RequestParam Integer id, Model model) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book",book.get());
            return "edit";
        }
        return "redirect:/";
    }

    //Update
    @PostMapping("/edit")
    public String edit(Book book) {
        book.setDeleted(0);
        bookRepository.save(book);
        return "redirect:/";
    }

    //Logical delete
    @PostMapping("/delete")
    public String deleted(@RequestParam Integer id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book b = book.get();
            b.setDeleted(1);
            bookRepository.save(b);
        }
        return "redirect:/";
    }

    //Increase stock
    @PostMapping("/stock/increase")
    public String increaseStock(@RequestParam Integer id, @RequestParam Integer amount) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book b = book.get();
            b.setStock(b.getStock() + amount);
            bookRepository.save(b);
        }
        return "redirect:/";
    }

    //Decrease stock
    @PostMapping("/stock/decrease")
    public String decreaseStock(@RequestParam Integer id, @RequestParam Integer amount) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()) {
            Book b = book.get();
            int newStock = b.getStock() - amount;
            b.setStock(Math.max(0, newStock));  //To not under 0.
            bookRepository.save(b);
        }
        return "redirect:/";
    }

    //Sales process
    @PostMapping("/sell")
    public String sell(@RequestParam Integer id, @RequestParam Integer quantity) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();

            //Confirm stock
            if (book.getStock() >= quantity) {
                //decrease stock
                book.setStock(book.getStock() - quantity);
                bookRepository.save(book);

                //Record to sales
                Sales sales = new Sales(
                        book.getId(),
                        book.getTitle(),
                        quantity,
                        book.getPrice()
                );
                salesRepository.save(sales);
            }
        }
        return "redirect:/";
    }

    //Show Sales record
    @GetMapping("/sales")
    public String salesHistory(Model model) {
        List<Sales> salesList = salesRepository.findAllByOrderBySaleDateDesc();
        Integer todayTotal = salesRepository.getTodayTotalSales();

        model.addAttribute("salesList", salesList);
        model.addAttribute("todayTotal", todayTotal);
        return "sales";
    }

}
