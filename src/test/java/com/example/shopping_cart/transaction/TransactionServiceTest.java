package com.example.shopping_cart.transaction;

import com.example.shopping_cart.order.Order;
import com.example.shopping_cart.order.OrderService;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductService;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private MyUserService myUserService;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private Authentication authentication;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository, myUserService, orderService, productService);
    }

    @Test
    void testSaveByAuthentication() {
        // Arrange
        MyUser user = new MyUser();
        Order order = new Order();
        order.setId(1L);
        order.setQuantities(Collections.emptyList());
        user.getOrders().add(order); // Add the order to the user's orders
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(1L, "type", "currency");

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);
        when(orderService.findById(order.getId())).thenReturn(order);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setCreatedDate(LocalDateTime.now());
            transaction.setCreatedTimeZone(ZoneId.systemDefault());;
            return transaction;
        });

        // Act
        TransactionResponseDTO result = transactionService.saveByAuthentication(authentication, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Save transaction successfully", result.getMessage());
    }

    @Test
    void testSaveByAuthentication_existingTransaction() {
        // Arrange
        MyUser user = new MyUser();
        Order order = new Order();
        order.setTransaction(new Transaction());
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(1L, "type", "currency");

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);
        when(orderService.findById(requestDTO.getOrderId())).thenReturn(order);

        // Act and Assert
        assertThrows(EntityExistsException.class, () -> transactionService.saveByAuthentication(authentication, requestDTO));
    }

    @Test
    void testSaveByAuthentication_orderNotFound() {
        // Arrange
        MyUser user = new MyUser();
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(1L, "type", "currency");

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);
        when(orderService.findById(requestDTO.getOrderId())).thenThrow(new EntityNotFoundException("Order id " + requestDTO.getOrderId() + " not found"));

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> transactionService.saveByAuthentication(authentication, requestDTO));
    }

    @Test
    void testFindAllByAuthenticationAndPageAndDirectionAndSortAttribute() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        MyUser user = new MyUser();
        Order order1 = new Order();
        Order order2 = new Order();
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(BigDecimal.valueOf(100.0));
        transaction1.setCreatedDate(LocalDateTime.now());
        transaction1.setCreatedTimeZone(ZoneId.systemDefault());
        transaction1.setOrder(order1);
        Transaction transaction2 = new Transaction();
        transaction2.setAmount(BigDecimal.valueOf(200.0));
        transaction2.setCreatedDate(LocalDateTime.now());
        transaction2.setCreatedTimeZone(ZoneId.systemDefault());
        transaction2.setOrder(order2);
        user.setTransactions(Arrays.asList(transaction1, transaction2));

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);

        // Act
        Page<TransactionResponseDTO> result = transactionService.findAllByAuthenticationAndPageAndDirectionAndSortAttribute(
                authentication, 0, 10, "desc", "createdDate");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(myUserService).findByUserAuthentication(authentication);
    }

    @Test
    void testFindAllByUserIdAndPageAndDirectionAndSortAttribute() {
        // Arrange
        UUID userId = UUID.randomUUID();
        MyUser user = new MyUser();
        Order order1 = new Order();
        Order order2 = new Order();
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(BigDecimal.valueOf(100.0));
        transaction1.setCreatedDate(LocalDateTime.now());
        transaction1.setCreatedTimeZone(ZoneId.systemDefault());
        transaction1.setOrder(order1);
        Transaction transaction2 = new Transaction();
        transaction2.setAmount(BigDecimal.valueOf(200.0));
        transaction2.setCreatedDate(LocalDateTime.now());
        transaction2.setCreatedTimeZone(ZoneId.systemDefault());
        transaction2.setOrder(order2);
        user.setTransactions(Arrays.asList(transaction1, transaction2));

        when(myUserService.findById(userId)).thenReturn(user);

        // Act
        Page<TransactionResponseDTO> result = transactionService.findAllByUserIdAndPageAndDirectionAndSortAttribute(
                userId, 0, 10, "desc", "createdDate");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(myUserService).findById(userId);
    }

    @Test
    void testFindAllByPageAndDirectionAndSortAttribute() {
        // Arrange
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        Order order1 = new Order();
        order1.setId(1L);
        transaction1.setOrder(order1);
        transaction1.setCreatedDate(LocalDateTime.now());
        transaction1.setCreatedTimeZone(ZoneId.systemDefault());

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        Order order2 = new Order();
        order2.setId(2L);
        transaction2.setOrder(order2);
        transaction2.setCreatedDate(LocalDateTime.now());
        transaction2.setCreatedTimeZone(ZoneId.systemDefault());

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findAll(any(Sort.class))).thenReturn(transactions);

        int pageNumber = 0;
        int pageSize = 5;
        String direction = "DESC";
        String sortAttribute = "createdDate";

        // Act
        Page<TransactionResponseDTO> result = transactionService.findAllByPageAndDirectionAndSortAttribute(pageNumber, pageSize, direction, sortAttribute);

        // Assert
        assertNotNull(result);
        assertEquals(transactions.size(), result.getNumberOfElements());
    }

    @Test
    void testFindById() {
        // Arrange
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        // Act
        Transaction result = transactionService.findById(1L);

        // Assert
        assertEquals(transaction, result);
    }

    @Test
    void testFindById_notFound() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> transactionService.findById(1L));
    }

    @Test
    void testFindAllByDefaultCreatedDateDesc() {
        // Arrange
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setCreatedDate(LocalDateTime.now());
        transaction1.setCreatedTimeZone(ZoneId.systemDefault());

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setCreatedDate(LocalDateTime.now().minusDays(1));
        transaction2.setCreatedTimeZone(ZoneId.systemDefault());

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findAll(any(Sort.class))).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.findAllByDefaultCreatedDateDesc();

        // Assert
        assertNotNull(result);
        assertEquals(transactions.size(), result.size());
        assertEquals(transaction1.getId(), result.get(0).getId()); // The transaction with the most recent date should be first
        assertEquals(transaction2.getId(), result.get(1).getId()); // The transaction with the older date should be second
    }

    @Test
    void testFindAllByDirectionAndSortAttribute() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(BigDecimal.valueOf(100.0));
        transaction1.setCreatedDate(LocalDateTime.now());
        transaction1.setOrder(order1);
        Transaction transaction2 = new Transaction();
        transaction2.setAmount(BigDecimal.valueOf(200.0));
        transaction2.setCreatedDate(LocalDateTime.now());
        transaction2.setOrder(order2);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        Sort sort = Sort.by(Sort.Order.desc("amount"));
        TransactionSort transactionSort = TransactionSort.AMOUNT;

        when(transactionRepository.findAll(sort)).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.findAllByDirectionAndSortAttribute(Sort.Direction.DESC, transactionSort);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionRepository).findAll(sort);
    }

    @Test
    void testFindAll() {
        // Arrange
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setCreatedDate(LocalDateTime.now());
        transaction1.setCreatedTimeZone(ZoneId.systemDefault());

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setCreatedDate(LocalDateTime.now().minusDays(1));
        transaction2.setCreatedTimeZone(ZoneId.systemDefault());

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findAll(any(Sort.class))).thenReturn(transactions);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        // Act
        List<Transaction> result = transactionService.findAll(sort);

        // Assert
        assertNotNull(result);
        assertEquals(transactions.size(), result.size());
        assertEquals(transaction1.getId(), result.get(0).getId()); // The transaction with the most recent date should be first
        assertEquals(transaction2.getId(), result.get(1).getId()); // The transaction with the older date should be second
    }
}