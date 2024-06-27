package com.example.shopping_cart.transaction;

import com.example.shopping_cart.order.Order;
import com.example.shopping_cart.order.OrderService;
import com.example.shopping_cart.order.Status;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductService;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.sort.SortDirectionMapper;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MyUserService myUserService;
    private final OrderService orderService;
    private final ProductService productService;

    public TransactionResponseDTO saveByAuthentication(
            @NotNull Authentication authentication,
            @NotNull TransactionRequestDTO transactionRequestDTO
    ) {
        // Handle authenticated user
        MyUser authenticatedUser =
                myUserService.findByUserAuthentication(authentication);

        // Find order by id
        Order order =
                orderService.findById(transactionRequestDTO.getOrderId());

        // Check if transaction already exists
        Transaction existingTransaction = order.getTransaction();
        if (existingTransaction != null) {
            throw new EntityExistsException("Transaction already exists");
        }

        // Check if order is available
        if (!authenticatedUser.getOrders().contains(order)) {
            throw new EntityNotFoundException("Order id " + transactionRequestDTO.getOrderId() + " not found");
        }

        // Map to Transaction
        Transaction newTransaction = TransactionMapper.toTransactionSave(
                transactionRequestDTO,
                authenticatedUser,
                order
        );

        // Save transaction
        Transaction savedTransaction =
                transactionRepository.save(newTransaction);

        // Update order transaction and order status
        order.setTransaction(savedTransaction);
        order.setStatus(Status.PAID.name());
        List<ProductQuantity> orderQuantities = order.getQuantities();

        for (ProductQuantity productQuantity : orderQuantities) {
            Long stockQuantity = productQuantity.getProduct().getStockQuantity();
            Product product = productQuantity.getProduct();
            stockQuantity = stockQuantity - productQuantity.getQuantity();
            product.setStockQuantity(stockQuantity);
            productService.save(product);
        }

        // Add transaction to user
        authenticatedUser.addTransaction(savedTransaction);

        TransactionResponseDTO transactionResponseDTO =
                TransactionMapper.toTransactionResponseDTO(savedTransaction);
        transactionResponseDTO.setMessage("Save transaction successfully");
        return transactionResponseDTO;
    }

    public Page<TransactionResponseDTO> findAllByAuthenticationAndPageAndDirectionAndSortAttribute(
            Authentication authentication, Integer pageNumber, Integer pageSize, String direction,
            String sortAttribute
    ) {
        // Handle authenticated user
        MyUser authenticatedUser = myUserService.findByUserAuthentication(authentication);

        // If transactions is empty or null
        if (authenticatedUser.getTransactions() == null ||
                authenticatedUser.getTransactions().isEmpty()
        ) {
            throw new EntityNotFoundException("User transaction not found");
        }

        // Get user transactions
        List<Transaction> userTransactions = authenticatedUser.getTransactions();

        TransactionSort transactionSort = TransactionSortMapper.toTransactionSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = SortDirectionMapper.toSortDirectionDefaultDesc(direction);

        List<Transaction> sortedTransaction = sort(
                transactionSort,
                userTransactions,
                sortDirection
        );

        // Map to TransactionResponseDTO List
        List<TransactionResponseDTO> transactionResponseDTOList = sortedTransaction.stream()
                .map(TransactionMapper::toTransactionResponseDTO)
                .peek(transactionResponseDTO -> transactionResponseDTO.setMessage("Find successfully"))
                .toList();

        // Create pageable
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize
        );

        // Create a Page of TransactionResponseDTO
        return new PageImpl<>(
                transactionResponseDTOList, pageable, transactionResponseDTOList.size()
        );
    }

    public Page<TransactionResponseDTO> findAllByUserIdAndPageAndDirectionAndSortAttribute(
            UUID userID, Integer pageNumber, Integer pageSize, String direction,
            String sortAttribute
    ) {
        // Find User by User Id
        MyUser myUser = myUserService.findById(userID);
        if (myUser.getTransactions() == null || myUser.getTransactions().isEmpty()) {
            throw new EntityNotFoundException("Transactions not found");
        }
        // Get Transactions from User
        List<Transaction> userTransactions = myUser.getTransactions();

        TransactionSort transactionSort = TransactionSortMapper.toTransactionSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = SortDirectionMapper.toSortDirectionDefaultDesc(direction);

        List<Transaction> sortedTransaction = sort(
                transactionSort,
                userTransactions,
                sortDirection
        );

        // Map to Transaction Response DTO
        List<TransactionResponseDTO> transactionResponseDTOList = sortedTransaction.stream()
                .map(TransactionMapper::toTransactionResponseDTO)
                .toList();

        // Create a Pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return new PageImpl<>(
                transactionResponseDTOList,
                pageable,
                transactionResponseDTOList.size()
        );
    }

    public Page<TransactionResponseDTO> findAllByPageAndDirectionAndSortAttribute(
            Integer pageNumber, Integer pageSize, String direction,
            String sortAttribute
    ) {
        if (findAllByDefaultCreatedDateDesc() == null ||
                findAllByDefaultCreatedDateDesc().isEmpty()) {
            throw new EntityNotFoundException("Transaction(s) not found");
        }

        TransactionSort transactionSort = TransactionSortMapper.toTransactionSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = SortDirectionMapper.toSortDirectionDefaultDesc(direction);

        List<Transaction> sortedTransactions = findAllByDirectionAndSortAttribute(sortDirection, transactionSort);

        List<TransactionResponseDTO> transactionResponseDTOList = sortedTransactions.stream()
                .map(TransactionMapper::toTransactionResponseDTO)
                .toList();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return new PageImpl<>(
                transactionResponseDTOList,
                pageable,
                transactionResponseDTOList.size()
        );
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }

    public List<Transaction> findAllByDefaultCreatedDateDesc() {
        Sort sortByCreatedDateDesc = Sort.by(Sort.Order.desc("createdDate"));
        return transactionRepository.findAll(sortByCreatedDateDesc);
    }

    public List<Transaction> findAllByDirectionAndSortAttribute(
            @NotNull Sort.Direction sortDirection,
            TransactionSort transactionSort
    ) {
        Sort sort;
        if (sortDirection.isAscending()) {
            // If Sort Direction is ASC
            sort = Sort.by(Sort.Order.asc(transactionSort.getValue()));
        } else {
            // If Sort Direction is DESC
            sort = Sort.by(Sort.Order.desc(transactionSort.getValue()));
        }
        return transactionRepository.findAll(sort);
    }

    public List<Transaction> findAll(Sort sort) {
        return transactionRepository.findAll(sort);
    }

    private static List<Transaction> sort(
            @NotNull TransactionSort transactionSort,
            List<Transaction> transactions,
            Sort.Direction sortDirection
    ) {
        List<Transaction> sortedTransactions = new ArrayList<>();
        switch (transactionSort) {
            case AMOUNT -> // Sorted Transactions by lowest AMOUNT (filter out null amounts)
                    sortedTransactions = transactions.stream()
                            .filter(transaction -> transaction.getAmount() != null)
                            .sorted(Comparator.comparing(Transaction::getAmount))
                            .collect(Collectors.toCollection(ArrayList::new));
            case CREATED_DATE -> // Sorted Transactions by earliest CREATED_DATE (filter out null created dates)
                    sortedTransactions = transactions.stream()
                            .filter(transaction -> transaction.getCreatedDate() != null)
                            .sorted(Comparator.comparing(Transaction::getCreatedDate))
                            .collect(Collectors.toCollection(ArrayList::new));
            case LAST_MODIFIED_DATE -> // Sorted Transactions by earliest LAST_MODIFIED_DATE (filter out null modified dates)
                    sortedTransactions = transactions.stream()
                            .filter(transaction -> transaction.getLastModifiedDate() != null)
                            .sorted(Comparator.comparing(Transaction::getLastModifiedDate))
                            .collect(Collectors.toCollection(ArrayList::new));
        }
        
        if (sortDirection.isDescending()) {
            // Reverse the sorted Transaction if direction is DESC
            Collections.reverse(sortedTransactions);
        }
        return sortedTransactions;
    }
}
