package com.example.shopping_cart.transaction;

import com.example.shopping_cart.order.Order;
import com.example.shopping_cart.order.OrderService;
import com.example.shopping_cart.order.Status;
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

        // Add transaction to user
        authenticatedUser.addTransaction(savedTransaction);

        TransactionResponseDTO transactionResponseDTO =
                TransactionMapper.toTransactionResponseDTO(savedTransaction);
        transactionResponseDTO.setMessage("Save transaction successfully");
        return transactionResponseDTO;
    }

    public Page<TransactionResponseDTO> findAllByAuthenticationAndPageAndDirection(
            Authentication authentication,
//            @NotNull TransactionRequestDTOFind transactionRequestDTOFind
            Integer pageNumber,
            Integer pageSize,
            String direction
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

        Sort.Direction sortDirection = TransactionMapper.toSortDirectionDefaultDesc(direction);

        TransactionSort defaultSortAttribute = TransactionMapper.toTransactionSortDefaultCreatedDate("");

        List<Transaction> sortedTransaction = sort(
                defaultSortAttribute,
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

        TransactionSort sortEnumAttribute = TransactionMapper.toTransactionSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = TransactionMapper.toSortDirectionDefaultDesc(direction);

        List<Transaction> sortedTransaction = sort(
                sortEnumAttribute,
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

    public Page<TransactionResponseDTO> findAllByUserIdAndPageAndDirection(
            UUID userID, Integer pageNumber, Integer pageSize, String direction
    ) {
        // Find User by User Id
        MyUser myUser = myUserService.findById(userID);
        if (myUser.getTransactions() == null || myUser.getTransactions().isEmpty()) {
            throw new EntityNotFoundException("Transactions not found");
        }
        // Get Transactions from User
        List<Transaction> userTransactions = myUser.getTransactions();

        Sort.Direction sortDirection = TransactionMapper.toSortDirectionDefaultDesc(direction);

        TransactionSort defaultSortAttribute = TransactionMapper.toTransactionSortDefaultCreatedDate("");

        List<Transaction> sortedTransaction = sort(
                defaultSortAttribute,
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

        TransactionSort sortEnumAttribute = TransactionMapper.toTransactionSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = TransactionMapper.toSortDirectionDefaultDesc(direction);

        List<Transaction> sortedTransaction = sort(
                sortEnumAttribute,
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

    public Page<TransactionResponseDTO> findAllByPageAndDirection(
            Integer pageNumber, Integer pageSize, String direction
    ) {
        if (findAllByDefaultCreatedDateDesc() == null ||
                findAllByDefaultCreatedDateDesc().isEmpty()) {
            throw new EntityNotFoundException("Transaction(s) not found");
        }

        TransactionSort sortEnumAttribute = TransactionMapper.toTransactionSortDefaultCreatedDate("");
        Sort.Direction sortDirection = TransactionMapper.toSortDirectionDefaultDesc(direction);

        List<Transaction> sortedTransactions = findAllByDirectionAndSortAttribute(sortDirection, sortEnumAttribute);

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

    public Page<TransactionResponseDTO> findAllByPageAndDirectionAndSortAttribute(
            Integer pageNumber, Integer pageSize, String direction,
            String sortAttribute
    ) {
        if (findAllByDefaultCreatedDateDesc() == null ||
                findAllByDefaultCreatedDateDesc().isEmpty()) {
            throw new EntityNotFoundException("Transaction(s) not found");
        }

        TransactionSort sortEnumAttribute = TransactionMapper.toTransactionSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = TransactionMapper.toSortDirectionDefaultDesc(direction);

        List<Transaction> sortedTransactions = findAllByDirectionAndSortAttribute(sortDirection, sortEnumAttribute);

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
            TransactionSort sortEnumAttribute
    ) {
        Sort sortByCreatedDateDesc;
        if (sortDirection.isAscending()) {
            // If Sort Direction is ASC
            sortByCreatedDateDesc = Sort.by(Sort.Order.asc(sortEnumAttribute.getValue()));
        } else {
            // If Sort Direction is DESC
            sortByCreatedDateDesc = Sort.by(Sort.Order.desc(sortEnumAttribute.getValue()));
        }
        return transactionRepository.findAll(sortByCreatedDateDesc);
    }

    private static List<Transaction> sort(
            @NotNull TransactionSort sortAttribute,
            List<Transaction> transactions,
            Sort.Direction direction
    ) {
        List<Transaction> sortedTransaction = new ArrayList<>();
        switch (sortAttribute) {
            case AMOUNT -> {
                // Sorted Transactions by highest AMOUNT
                 sortedTransaction = transactions.stream()
                        .sorted(Comparator.comparing(Transaction::getAmount))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            case CREATED_DATE -> {
                // Sorted Transactions by earliest CREATED_DATE
                sortedTransaction = transactions.stream()
                        .sorted(Comparator.comparing(Transaction::getCreatedDate))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            case LAST_MODIFIED_DATE -> {
                // Sorted Transactions by earliest LAST_MODIFIED_DATE
                sortedTransaction = transactions.stream()
                        .sorted(Comparator.comparing(Transaction::getLastModifiedDate))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }
        
        if (direction.isDescending()) {
            // Reverse the sorted Transaction if direction is DESC
            Collections.reverse(sortedTransaction);
        }
        return sortedTransaction;
    }
}
