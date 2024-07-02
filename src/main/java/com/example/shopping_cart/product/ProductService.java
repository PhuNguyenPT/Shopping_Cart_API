package com.example.shopping_cart.product;

import com.example.shopping_cart.category.Category;
import com.example.shopping_cart.category.CategoryRepository;
import com.example.shopping_cart.category.CategoryService;
import com.example.shopping_cart.file.*;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.sort.SortDirectionMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Product " + name + " not found"));
    }

    public Product findByNameOrElseNull(String name) {
        return productRepository.findByName(name)
                .orElse(null);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public ProductResponseDTO save(
            @NotNull
            ProductRequestDTO productRequestDTOS
    ) {

        // Check if the product already exists
        Product existingProduct = findByNameOrElseNull(productRequestDTOS.name());
        if (existingProduct != null) {
            throw new EntityExistsException("Product already exists");
        }

        // Handle Existing Categories
        List<Category> existingCategories = new ArrayList<>();
        if (productRequestDTOS.categoryIds() != null &&
                !productRequestDTOS.categoryIds().isEmpty()) {
            existingCategories = categoryService.findAllByIdIn(productRequestDTOS.categoryIds());
        }

        // Create and save the product
        Product product = ProductMapper.toProduct(productRequestDTOS);

        Product savedProduct = productRepository.save(product);

        // Handle new Categories
        List<Category> newCategories = new ArrayList<>();
        if (productRequestDTOS.newCategoryNames() != null &&
                !productRequestDTOS.newCategoryNames().isEmpty()) {
            newCategories = categoryService.findByNameOrElseCreateNewCategory(productRequestDTOS.newCategoryNames());
        }

        // Save new categories
        List<Category> savedNewCategories = categoryService.saveAll(newCategories);

        // Handle combine categories
        Set<Category> combinedCategories = new HashSet<>(existingCategories);
        combinedCategories.addAll(savedNewCategories);
        savedProduct.setCategories(combinedCategories.stream().toList());
        for (Category category : combinedCategories) {
            category.addProduct(savedProduct);
        }

        // Handle file uploads
        List<File> savedFiles = new ArrayList<>();
        if (productRequestDTOS.files() != null &&
                !productRequestDTOS.files().isEmpty()) {
            savedFiles = fileService.saveAllFilesByProduct(productRequestDTOS.files(), savedProduct);
        }
        savedProduct.setFiles(savedFiles);

        // Prepare response
        ProductResponseDTO productResponseDTO = ProductMapper.toProductResponseDTO(savedProduct);
        productResponseDTO.setMessage(this.buildMessage(productResponseDTO));

        return productResponseDTO;
    }

    private String buildMessage(
            @NotNull ProductResponseDTO productResponseDTO
    ) {
        StringBuilder message = new StringBuilder("Save product successfully");

        if (productResponseDTO.getCategoryResponseDTOList() != null &&
                !productResponseDTO.getCategoryResponseDTOList().isEmpty()) {
            message.append(",with categories");
        }
        if (productResponseDTO.getFileResponseDTOList() != null &&
                !productResponseDTO.getFileResponseDTOList().isEmpty()) {
            message.append(",with files");
        }
        return String.valueOf(message);
    }

    public List<Product> findByNameContainingIgnoreCase(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (products == null || products.isEmpty()) {
            throw new EntityNotFoundException("Product(s) " + name + " not found");
        }
        return products;
    }

    public Page<ProductResponseDTO> findAllByNameAndPageAndDirectionAndSortAttribute(
            String productName, Integer pageNumber, Integer pageSize, String direction,
            String sortAttribute
    ) {
        ProductSort productSort = ProductSortMapper.toProductionSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = SortDirectionMapper.toSortDirectionDefaultDesc(direction);

        List<Product> products;
        if (productName != null) {
             products = findByNameContainingIgnoreCase(productName);
        } else {
            products = new ArrayList<>();
        }

        List<Product> sortedProducts;
        if (!products.isEmpty()) {
            sortedProducts = sort(productSort, products, sortDirection);
        }
        else {
            sortedProducts = findAllByDirectionAndSortAttribute(sortDirection, productSort);
        }

        List<ProductResponseDTO> productResponseDTOList = sortedProducts.stream()
                .map(ProductMapper::toProductResponseDTO)
                .toList();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return new PageImpl<>(
                productResponseDTOList,
                pageable,
                productResponseDTOList.size()
        );
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product with id " +
                                id + " not found"));
    }

//    public Page<ProductResponseDTO> findByProductNameAndPage(
//            String productName, Integer pageNumber, Integer pageSize
//    ) {
//        List<Product> products = findByNameContainingIgnoreCase(productName);
//        List<ProductResponseDTO> productResponseDTOList = products.stream()
//                .map(ProductMapper::toProductResponseDTO)
//                .toList();
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        return new PageImpl<>(
//                productResponseDTOList,
//                pageable,
//                productResponseDTOList.size()
//        );
//    }

    @Transactional
    public ResponseEntity<?> deleteBy(Long productId) {
        Product product = findById(productId);
        product.setStockQuantity(0L);


//        product.getShoppingCarts().forEach(shoppingCart -> {
//            shoppingCart.getProducts().stream()
//                    .filter(p -> p.getId().equals(product.getId()))
//                    .findFirst()
//                    .ifPresent(p -> p.setStockQuantity(0L));
//        });
        productRepository.save(product);
//        productRepository.deleteById(product.getId());
        return ResponseEntity.ok("Product with id " + productId + " is deleted successfully");
    }

    @Transactional
    public ProductResponseDTO updateFileByProductIdAndFileId(
            Long productId,
            Long fileId,
            MultipartFile multipartFile
    ) {
        Product product = findById(productId);
        FileResponseDTO fileResponseDTO = fileService.updateFile(multipartFile, product, fileId);
        fileResponseDTO.setMessage("Update files " + fileResponseDTO.getName() + " successfully");
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(fileResponseDTO.getId())
//                .toUri();
        return ProductMapper.toProductResponseDTOUpdate(product, fileResponseDTO);
    }

    @Transactional
    public ProductResponseDTO createProductFilesByProductId(
            Long productId,
            @NotNull List<MultipartFile> multipartFiles) {
        Product product = findById(productId);
        List<FileResponseDTO> fileResponseDTOList = fileService.saveFilesByProduct(product, multipartFiles);
        return ProductMapper.toProductResponseDTOCreateFiles(product, fileResponseDTOList);
    }

    @Transactional
    public ProductResponseDTO updateProductAttributes(
            Long id,
            @NotNull ProductUpdateDTO productUpdateDTO
    ) {
        Product product = findById(id);
        if (productUpdateDTO.getName() != null) {
            product.setName(productUpdateDTO.getName());
        }
        if (productUpdateDTO.getPrice() != null) {
            product.setPrice(productUpdateDTO.getPrice());
        }
        if (productUpdateDTO.getDescription() != null) {
            product.setDescription(productUpdateDTO.getDescription());
        }
        if (productUpdateDTO.getStockQuantity() != null) {
            product.setStockQuantity(productUpdateDTO.getStockQuantity());
        }
        if (productUpdateDTO.getCategoryIds() != null &&
                !productUpdateDTO.getCategoryIds().isEmpty()) {
            List<Category> savedCategories = categoryRepository.findAllByIdIn(productUpdateDTO.getCategoryIds());
            product.setCategories(savedCategories);
        }
        Product savedProduct = productRepository.save(product);
        ProductResponseDTO productResponseDTO = ProductMapper.toProductResponseDTO(savedProduct);
        productResponseDTO.setMessage("Update successfully");
        return productResponseDTO;
    }

    public List<Product> findAllByDirectionAndSortAttribute(
            @NotNull Sort.Direction sortDirection,
            ProductSort productSort
    ) {
        Sort sort;
        if (sortDirection.isAscending()) {
            // If Sort Direction is ASC
            sort = Sort.by(Sort.Order.asc(productSort.getValue()));
        } else {
            // If Sort Direction is DESC
            sort = Sort.by(Sort.Order.desc(productSort.getValue()));
        }
        return productRepository.findAll(sort);
    }

    public List<Product> findAll(Sort sort) {
        return productRepository.findAll(sort);
    }

    private static List<Product> sort(
            @NotNull ProductSort productSort,
            List<Product> products,
            Sort.Direction sortDirection
    ) {
        List<Product> sortedProducts = new ArrayList<>();
        switch (productSort) {
            case STOCK_QUANTITY -> // Sorted Products by lowest STOCK_QUANTITY (filter out null STOCK_QUANTITY)
                    sortedProducts = products.stream()
                            .filter(product -> product.getPrice() != null)
                            .sorted(Comparator.comparing(Product::getStockQuantity))
                            .collect(Collectors.toCollection(ArrayList::new));
            case PRICE -> // Sorted Products by lowest PRICE (filter out null PRICE)
                    sortedProducts = products.stream()
                            .filter(product -> product.getPrice() != null)
                            .sorted(Comparator.comparing(Product::getPrice))
                            .collect(Collectors.toCollection(ArrayList::new));
            case CREATED_DATE -> // Sorted Products by earliest CREATED_DATE (filter out null created dates)
                    sortedProducts = products.stream()
                            .filter(product -> product.getCreatedDate() != null)
                            .sorted(Comparator.comparing(Product::getCreatedDate))
                            .collect(Collectors.toCollection(ArrayList::new));
            case LAST_MODIFIED_DATE -> // Sorted Products by earliest LAST_MODIFIED_DATE (filter out null modified dates)
                    sortedProducts = products.stream()
                            .filter(product -> product.getLastModifiedDate() != null)
                            .sorted(Comparator.comparing(Product::getLastModifiedDate))
                            .collect(Collectors.toCollection(ArrayList::new));
        }

        if (sortDirection.isDescending()) {
            // Reverse the sorted Transaction if direction is DESC
            Collections.reverse(sortedProducts);
        }
        return sortedProducts;
    }
}
