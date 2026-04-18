package com.example.bookstore;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final SalesRepository salesRepository;
    private  final PurchaseRepository purchaseRepository;

    public BookService(BookRepository bookRepository, SalesRepository salesRepository, PurchaseRepository purchaseRepository) {
        this.bookRepository = bookRepository;
        this.salesRepository = salesRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public Optional<Book> getBooks(Integer id){
       return bookRepository.findById(id);
    }

    //if there is search conditions, execute search or else execute find by all.
    public List<Book> getSearchBooks(String title, String author, String category, Integer deleted) {
        if ((title != null && !title.isEmpty()) || (author != null && !author.isEmpty()) || (category != null && !category.isEmpty())) {
            String searchTitle = (title != null && !title.isEmpty()) ? title : null;
            String searchAuthor = (author != null && !author.isEmpty()) ? author : null;
            String searchCategory = (category != null && !category.isEmpty()) ? category : null;
            return  bookRepository.searchBooks(searchTitle, searchAuthor, searchCategory, deleted);
        } else {
            return bookRepository.findByDeleted(deleted);
        }
    }

    public void sell(Integer id, Integer quantity) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();

            //Confirm stock
            if (book.getStock() < quantity) {
                throw new RuntimeException("Out of stock. current stock: " + book.getStock());
            }

            book.setStock(book.getStock() - quantity);
            bookRepository.save(book);

            Sales sales = new Sales(book.getId(), book.getTitle(), quantity, book.getPrice());
            salesRepository.save(sales);
        }
    }

    public List<Sales> getsalesList(){
        return salesRepository.findAllByOrderBySaleDateDesc();
    }

    public Integer getTodayTotalSales(){
        return salesRepository.getTodayTotalSales();
    }

    public void purchase(Integer id, Integer quantity, Integer unitPrice) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();

            //Increase stock
            book.setStock(book.getStock() + quantity);
            bookRepository.save(book);

            //Record purchases
            Purchase purchase = new Purchase(
                    book.getId(),
                    book.getTitle(),
                    quantity,
                    unitPrice
            );
            purchaseRepository.save(purchase);
        }
    }

    public List<Purchase> getpurchaseList() {
        return purchaseRepository.findAllByOrderByPurchaseDateDesc();
    }

    public Integer getTodayTotalPurchases() {
        return purchaseRepository.getTodayTotalPurchases();
    }

    public void increaseStock(Integer id, Integer amount){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book b = book.get();
            b.setStock(b.getStock() + amount);
            bookRepository.save(b);
        }
    }

    public void decreaseStock(Integer id, Integer amount){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book b = book.get();
            int newStock = b.getStock() - amount;
            b.setStock(Math.max(0, newStock)); //Not to under 0.
            bookRepository.save(b);
        }
    }

    public void add(Book book) {
        book.setDeleted(0);
        bookRepository.save(book);
    }

    public void edit(Book book) {
        book.setDeleted(0);
        bookRepository.save(book);
    }

    public void deleted(Integer id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book b = book.get();
            b.setDeleted(1);
            bookRepository.save(b);
        }
    }

    //Sales by category
    public List<Object[]> getCategorySales(){
        return salesRepository.getCategorySales();
    }

    //Sales by monthly performance(back in 6 months)
    public List<Object[]>  getMonthlySales() {
        return salesRepository.getMonthlySales();
    }

    //Calc Total Sales
    public Integer getTotalSales() {
        return salesRepository.getTotalSales();
    }

    //Calc Total Purchases
    public Integer getTotalPurchases() {
        return purchaseRepository.getTotalPurchases();
    }
}
