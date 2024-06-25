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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Page<TransactionResponseDTO> findAllByAuthenticationAndPage(
            Authentication authentication,
            @NotNull TransactionRequestDTOFind transactionRequestDTOFind
    ) {
        // Handle authenticated user
        MyUser authenticatedUser = myUserService.findByUserAuthentication(authentication);

        // Get user transactions
        List<Transaction> userTransactions = authenticatedUser.getTransactions();

        // If transactions is empty or null
        if (userTransactions == null ||
                userTransactions.isEmpty()
        ) {
            throw new EntityNotFoundException("User transaction not found");
        }

        // Create pageable
        Pageable pageable = PageRequest.of(
                transactionRequestDTOFind.getPageNumber(),
                transactionRequestDTOFind.getPageSize()
        );

        // Map to TransactionResponseDTO List
        List<TransactionResponseDTO> transactionResponseDTOList = userTransactions.stream()
                .map(TransactionMapper::toTransactionResponseDTO)
                .toList();

        // Create a Page of TransactionResponseDTO
        Page<TransactionResponseDTO> transactionResponseDTOPage = new PageImpl<>(
                transactionResponseDTOList, pageable, transactionResponseDTOList.size()
        );

        return transactionResponseDTOPage;
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }
}
