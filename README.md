## Required pre-installed apps
1. Intellij (Community/ Ultimate Edition)
2. PostgreSQL + pgAdmin
3. Postman
4. Any web browser (Google, Microsoft Edge, Firefox)
5. Github
6. Docker

## Setup Instructions

To set up the backend of the Book Social Network project, follow these steps:
1. Clone the repository:

```bash
   git clone https://github.com/PhuNguyenPT/Shopping_Cart.git
   cd Shopping_Cart
```
   - Create a new database in pgAdmin 4 with the name `shopping_cart`
   - Change docker-compose.yml
```
   environment:
      POSTGRES_PASSWORD: to your database password
```
   - Change src/main/resources/application.yaml
```bash
   datasource:
      password: to your database password
```

2. Run the docker-compose file in a terminal

```bash
   docker-compose up
```
3. Install dependencies (assuming Maven is installed) in a second terminal:

```bash
   mvn clean install
```
4. Run the application in the second terminal

```bash
   java -jar target/shopping-cart-api-0.0.1-SNAPSHOT.jar
```
5. Run Postman and send to the register api by POST 

```bash
   https://localhost:443/api/v1/auth/register
```

   - Example in Request Body Raw:
     
```bash
{
   "firstName": "firstname",
   "lastName": "lastname",
   "email": "test@gmail.com",
   "password": "password"
}
```
6. Use a browser to access to mail server at the URL
    
```bash
   http://localhost:1080
```

7. Copy the 6-digit activation code and send to the activate-account api by GET 

```bash
   // Replace the value with the actual 6-digit activation code
   https://localhost:443/api/v1/auth/activate-account?token=value
```

8. Login to the account by POST and receive a token as response 
 
```bash
   https://localhost:443/api/v1/auth/login   
```

   - Example in Request Body Raw:

```json
   {
      "email": "test@gmail.com",
      "password": "password"
   }
```

9. Create New Environt by Environments->Create Environment <br/><br/>
    ![create-environment](https://github.com/PhuNguyenPT/Shopping_Cart/assets/154642828/fd713c17-8020-4267-ad3d-a09b46af643d) <br/><br/>
    
   - Create new variable token and Save <br/><br/>
   ![save](https://github.com/PhuNguyenPT/Shopping_Cart/assets/154642828/a4900bb7-6457-4396-b472-25fc6d7de811) <br/><br/>
   
   - Choose environment to New Environment <br/><br/>
   ![environment](https://github.com/PhuNguyenPT/Shopping_Cart/assets/154642828/6e219860-2a8c-4795-add8-35704d775949) <br/><br/>
   
   - Navigate to Response Body in step 8 to select token "value" and right-click to choose Set: New Envinronment -> token:
```json
   {
      "token": "select_this_value"
   }
```

10. Access to user home page by GET 
    
```bash
   https://localhost:443/api/v1/user/home
```
   - Go to Authorization->Auth Type->Bearer Token and enter in the box '{{token}}' and Send <br/><br/>
![user-home](https://github.com/PhuNguyenPT/Shopping_Cart/assets/154642828/5af38fb1-6374-4677-a280-43fe49288db6) <br/><br/>

## Admin Account 
   - Login by POST
```bash
   https://localhost:443/api/v1/auth/login
```
   - Example in Request Body Raw:

```json
   {
      "email": "admin@email.com",
      "password": "password"
   }
```
## Rest API
- `(USER ONLY)` : require user login
- `(ADMIN ONLY)` : require admin login
- `(NON-USER)` : no require for login

### Authentication
1. POST: https://localhost/api/v1/auth/register `(NON-USER)` <br/>
   - Register user 
   - Request Body 
     + Content-Type = application/json <br/><br/>
     + Suported attributes for Authentication: 
       * `firstName` 
       * `lastName`
       * `email`
       * `password` <br/><br/>
       
     + Attributes constraints for Authentication: 
       * `firstName` : *NotEmpty*, *NotBlank*
       * `lastName` : *NotEmpty*, *NotBlank*
       * `email` : *Email*, *NotEmpty*, *NotBlank*
       * `password` : *NotEmpty*, *NotBlank*, *Size(min=8)* <br/><br/>
       
     + Attributes explaination for Authentication:
       * `firstName` : first name 
       * `lastName` : last name
       * `email` : email
       * `password` : password <br/><br/>
       
2. GET: https://localhost:443/api/v1/auth/activate-account?token=value `(NON-USER)` <br/>
   - Use a browser to access to mail server at http://localhost:1080 <br/>
   - Get the 6-digit code to replace the `value` in `activate-account?token=value` above <br/><br/>


3. POST: https://localhost/api/v1/auth/login `(NON-USER)` <br/>
   - Login by email and password <br/>
   - Request Body 
     + Content-Type = application/json <br/><br/>
     + Suported attributes for Authentication: 
       * `email`
       * `password` <br/><br/>
       
     + Attributes constraints for Authentication: 
       * `email` : *Email*, *NotEmpty*, *NotBlank*
       * `password` : *NotEmpty*, *NotBlank*, *Size(min=8)* <br/><br/>
       
     + Attributes explaination for Authentication:
       * `email` : email
       * `password` : password <br/><br/>

### MyUser
1. POST: https://localhost/api/v1/users `(ADMIN ONLY)` <br/>
   - Find all users attributes available in database
   - Request Body:
     + Content-Type = application/json <br/><br/>
     + Suported attributes for MyUser findAll: 
       * `pageSize` 
       * `pageNumber` <br/><br/>
       
     + Attributes constraints for MyUser findAll: 
       * `pageSize` : *NotNull*, *Min(1)*, *Max(20)
       * `pageNumber` : *NotNull*, *Min(1)* <br/><br/>
       
     + Attributes explaination for Category filter: 
       * `pageSize` : maximum number of Products in a page
       * `pageNumber` : page number of which page <br/><br/>
       
   - Response Body:
     + Return Page of MyUserResponseDTOList <br/><br/>
     + Content-Type = application/json <br/><br/>
     + Suported attributes for MyUser:
```json
{
    "content": [
        {
            "message": "",
            "id": 1,
            "firstName": "firstname1",
            "lastName": "lastname1",
            "dateOfBirth": "yyyy-MM-dd",
            "phoneNumber": 987654321,
            "email": "hello1@email.com",
            "addressResponseDTO": {
                "houseNumber": "12",
                "streetName": "street1",
                "wardName": "ward1",
                "city": "city1",
                "zipCode": "zipcode1"
            } 
        },
        {
            "message": "",
            "id": 2,
            "firstName": "firstname2",
            "lastName": "lastname2",
            "dateOfBirth": "yyyy-MM-dd",
            "phoneNumber": 987654321,
            "email": "hello2@email.com",
            "addressResponseDTO": {
                "houseNumber": "12",
                "streetName": "street2",
                "wardName": "ward2",
                "city": "city2",
                "zipCode": "zipcode2"
            } 
        }
    ],
    "page": {
        "size": 20,
        "number": 1,
        "totalElements": 22,
        "totalPages": 2
    }
}
```
<br/><br/>
         
1. GET: https://localhost/api/v1/users/search/{user-id} `(ADMIN ONLY)` <br/>
   - Search user attributes with `user-id`
   - Response Body:
     + Content-Type = application/json <br/><br/>
     + Suported attributes for MyUser:
       * `message` 
       * `id`
       * `firstName`
       * `lastName`
       * `dateOfBirth`
       * `phoneNumber`
       * `email`
       * `addressResponseDTO` :
         * `houseNumber`
         * `streetName`
         * `wardName`
         * `city`
         * `zipCode` <br/><br/>
         
     + Attributes constraints for MyUser:
       * `message` : *Optional*
       * `id` : *Optional*
       * `firstName` : *Optional*
       * `lastName` : *Optional*
       * `dateOfBirth` : *Optional*
       * `phoneNumber` : *Optional*
       * `email` : *Optional*
       * `addressResponseDTO` :
         * `houseNumber` : *Optional*
         * `streetName` : *Optional*
         * `wardName` : *Optional*
         * `city` : *Optional*
         * `zipCode` : *Optional* <br/><br/>
        
     + Attributes explaination for MyUser:
       * `message` : *response message*
       * `id` : *user UUID*
       * `firstName` : *first name*
       * `lastName` : *last name*
       * `dateOfBirth` : *date of birth*
       * `phoneNumber` : *phone number*
       * `email` : *email*
       * `addressResponseDTO` :
         * `houseNumber` : *house number*
         * `streetName` : *street name*
         * `wardName` : *ward name*
         * `city` : *city*
         * `zipCode` : *zip code* <br/><br/>
        
2. GET: https://localhost/api/v1/users/account `(USER ONLY)` <br/>
   - Find user attributes by User authenticated `Bearer Token` in `AUTHORIZATION`
   - Response Body:
     + Content-Type = application/json <br/><br/>
     + Suported attributes for MyUser:
       * `message` 
       * `id`
       * `firstName`
       * `lastName`
       * `dateOfBirth`
       * `phoneNumber`
       * `email`
       * `addressResponseDTO` :
         * `houseNumber`
         * `streetName`
         * `wardName`
         * `city`
         * `zipCode` <br/><br/>
         
     + Attributes constraints for MyUser:
       * `message` : *Optional*
       * `id` : *Optional*
       * `firstName` : *Optional*
       * `lastName` : *Optional*
       * `dateOfBirth` : *Optional*
       * `phoneNumber` : *Optional*
       * `email` : *Optional*
       * `addressResponseDTO` :
         * `houseNumber` : *Optional*
         * `streetName` : *Optional*
         * `wardName` : *Optional*
         * `city` : *Optional*
         * `zipCode` : *Optional* <br/><br/>
        
     + Attributes explaination for MyUser:
       * `message` : *response message*
       * `id` : *user UUID*
       * `firstName` : *first name*
       * `lastName` : *last name*
       * `dateOfBirth` : *date of birth*
       * `phoneNumber` : *phone number*
       * `email` : *email*
       * `addressResponseDTO` :
         * `houseNumber` : *house number*
         * `streetName` : *street name*
         * `wardName` : *ward name*
         * `city` : *city*
         * `zipCode` : *zip code* <br/><br/>
              
### Product
1. POST: https://localhost/api/v1/products/upload `(ADMIN ONLY)` <br/>
   - Upload Product <br/>
   - Request Body: 
     + Content-Type = multipart/form-data <br/><br/>
     + Suported attributes for Product: 
       * `name` 
       * `price`
       * `stockQuantity`
       * `description`
       * `categoryIds`
       * `newCategoryNames` <br/><br/>
     + Attributes constraints for Product:
       * `name` : *NotNull*, *NotBlank*
       * `price` : *NotNull*, *DecimalMin(0.01)*
       * `stockQuantity` : *NotNull*, *Min(0)*
       * `description` : *Optional*
       * `categoryIds` : *Optional*
       * `newCategoryNames` : *Optional* <br/><br/>
     + Attributes explaination for Product:
       * `name` : *product name*
       * `price` : *product price*
       * `stockQuantity` : *product stock quantity*
       * `description` : *product description*
       * `categoryIds` : *saved category ids from database*
       * `newCategoryNames` : *new category names to save to database* <br/><br/>
     + Response Body
```json
{
    "message": "Save product successfully,with categories,with files",
    "id": 4,
    "name": "Product 8",
    "price": 9876.0,
    "stockQuantity": 30,
    "description": "Product 8",
    "createdDate": "2024-06-26T12:13:57.4699588+07:00",
    "lastModifiedDate": null,
    "fileResponseDTOList": [
        {
          "message": null,
          "id": 4,
          "name": "hello.png",
          "fileType": "image/png",
          "size": 674315,
          "fileByte": null
        }
    ],
    "categoryResponseDTOList": [
        {
          "id": 4,
          "name": "hello",
          "productResponseDTOList": null
        }
    ]
}
```

2. GET: https://localhost/api/v1/products/search/{product-name} `(NON-USER)` #(DEPRECATED)# <br/> 
   - Search Product by `product-name` <br/><br/>
   - Response Body
     + Content-Type = application/json <br/><br/>
```json
{
    "message": "Search product successfully,with categories,with files",
    "id": 4,
    "name": "Product 8",
    "price": 9876.0,
    "stockQuantity": 30,
    "description": "Product 8",
    "createdDate": "2024-06-26T12:13:57.4699588+07:00",
    "lastModifiedDate": null,
    "fileResponseDTOList": [
        {
          "message": null,
          "id": 4,
          "name": "image.png",
          "fileType": "image/png",
          "size": 674315,
          "fileByte": null
        }
    ],
    "categoryResponseDTOList": [
        {
          "id": 4,
          "name": "hello",
          "productResponseDTOList": null
        }
    ]
}
```

2. GET: https://localhost/api/v1/products/search?product-name={product-name}&page-size=20&page-number=1 `(NON-USER)` <br/> 
   - Search Product by `product-name` with `page-size` and `page-number` <br/><br/>
   - Request parameters:
     + Suported attributes for Product:
       * `product-name`
       * `page-size`
       * `page-number` <br/><br/>
       
     + Attributes constraints for Product:
       * `product-name`
       * `pageSize` : *NotNull*, *Min(1)*, *Max(20)
       * `pageNumber` : *NotNull*, *Min(1)* <br/><br/>

     + Attributes explaination for Product:    
       * `product-name` : *product name*
       * `pageSize` : *page size*
       * `pageNumber` : *page number* <br/><br/>     
       
   - Response body:    
     + Return ProductResponseDTO
```json
  {
  "content": [
    {
      "message": "",
      "id": 1,
      "name": "Product1",
      "price": 99,
      "stockQuantity": 99,
      "description": "Descript1",
      "createdDate": "2024-06-01T12:13:57.4699588+07:00",
      "lastModifiedDate": "",
      "categoryResponseDTOList": [
        {
          "id": "1",
          "name": "NAME1",
          "productResponseDTOList": ""
        },
        {
          "id": "2",
          "name": "Name2",
          "productResponseDTOList": ""
        }
      ],
      "fileResponseDTOList": [
        {
          "message": "",
          "id": 1,
          "name": "image.png",
          "fileType": "image/png",
          "size": 2313,
          "fileByte": ""
        }
      ]
    },
    {
      "message": "",
      "id": 2,
      "name": "Product2",
      "price": 99,
      "stockQuantity": 99,
      "description": "",
      "createdDate": "2024-06-26T12:13:57.4699588+07:00",
      "lastModifiedDate": "2024-06-26T12:13:57.4699588+07:00",
      "categoryResponseDTOList": [
        {
          "id": 2,
          "name": "name2",
          "productResponseDTOList": null
        },
        {
          "id": 3,
          "name": "name3",
          "productResponseDTOList": null
        }
      ],
      "fileResponseDTOList": [
        {
          "message": "",
          "id": 2,
          "name": "image.png",
          "fileType": "image/png",
          "size": 2222,
          "fileByte": ""
        },
        {
          "message": "",
          "id": 2,
          "name": "image.png",
          "fileType": "image/png",
          "size": 2121,
          "fileByte": ""
        }
      ]
    }
  ],
  "page": {
    "size": 20,
    "number": 1,
    "totalElements": 22,
    "totalPages": 2
  }
}
```

3. DELETE: https://localhost/api/v1/products/delete/{product-id} `(ADMIN ONLY)` <br/>
   - Delete Product by `product-id` but do not delete the `categories` <br/><br/>

4. PUT: https://localhost/api/v1/products/update/{product-id}/files/{file-id} `(ADMIN ONLY)` <br/>
   - Update Files with `file-id` of Product with `product-id` by new File <br/><br/>
   - Request Body 
     + Content-Type = multipart/form-data <br/>
     + Suported attributes for Product: 
       * `files` <br/><br/>
       
     + Attributes constraints for Product:
       * `files` : *NotNull* <br/><br/>
       
     + Attributes explaination for Product:
       * `files` : *multipart file* <br/><br/>
       
5. POST: https://localhost/api/v1/products/{productId}/files `(ADMIN ONLY)` <br/>
   - Create new File(s) for Product with `product-id` <br/><br/>
   - Request Body 
     + Content-Type = multipart/form-data <br/>
     + Suported attributes for Product: 
       * `files` <br/><br/>
       
     + Attributes constraints for Product:
       * `files` : *NotNull* <br/><br/>
       
     + Attributes explaination for Product:
       * `files` : *multipart file* <br/><br/>
       
6. PATCH: https://localhost/api/v1/products/update/{product-id} `(ADMIN ONLY)` <br/>
   - Update Product attribute(s) with `product-id` <br/><br/>   
   - Request Body 
     + Content-Type = multipart/form-data <br/>
     + Suported attributes for Product: 
       * `name` 
       * `price`
       * `stockQuantity`
       * `description`
       * `categoryIds` <br/><br/>

     + Attributes constraints for Product: 
       * `name` : *Optional*
       * `price` : *Optional*
       * `stockQuantity` : *Optional*
       * `description` : *Optional*
       * `categoryIds` : *Optional* <br/><br/>

     + Attributes explaination for Product:
       * `name` : *product name*
       * `price` : *product price*
       * `stockQuantity` : *product stock quantity*
       * `description` : *product description*
       * `categoryIds` : *saved category ids from database* <br/><br/>

### Category

1. GET: https://localhost/api/v1/categories `(NON-USER)` <br/><br/>
   - Return all the categories available <br/><br/> 
   - Response Body 
     + Content-Type = application/json <br/><br/>
     + Example Response Body Raw: 
```json
   [
       {
           "id": 1,
           "name": "Electronics",
           "productResponseDTOList": null
       },
       {
           "id": 2,
           "name": "Gadgets",
           "productResponseDTOList": null
       }
   ]
```
<br/><br/>

2. POST: https://localhost/api/v1/categories/filter `(NON-USER)` <br/><br/>
   - Filter Product(s) by CategoryRequestDTOList of `category-id` with `pageSize` and `pageNumber` <br/><br/>   
   - Request Body 
     + Content-Type = application/json <br/><br/>
     
     + Suported attributes for Product: 
       * `pageSize` 
       * `pageNumber`
       * `categoryRequestDTOList` :
         * `categoryId`  <br/><br/>
         
     + Attributes constraints for Category filter: 
       * `pageSize` : *NotNull*, *Min(1)*, *Max(20)
       * `pageNumber` : *NotNull*, *Min(1)*
       * `categoryRequestDTOList` :
         * `categoryId` *NotNull*, *Min(1)*  <br/><br/>
         
     + Attributes explaination for Category filter: 
       * `pageSize` : maximum number of Products in a page
       * `pageNumber` : page number of which page
       * `categoryRequestDTOList` :
         * `categoryId` : category id  <br/><br/>
         
     + Example Response Body Raw: <br/>
```json
   {
     "pageSize": 20,
     "pageNumber": 1,
     "categoryRequestDTOList": [
       {
         "categoryId": 1
       },
       {
         "categoryId": 2
       }
     ]
   }
```
<br/><br/>

### Shopping Cart
1. POST: https://localhost/api/v1/carts/upload `(USER ONLY)` <br/><br/>
   - Upload `List` of (`Product id`, `Quantity`) for Shopping Cart (User authenticated `Bearer Token` needed in `AUTHORIZATION`) <br/><br/>
   - Request Body 
     + Content-Type = application/json <br/><br/>
     + Suported attributes for Shopping Cart: 
       * `productId` 
       * `quantity` <br/><br/>
       
     + Attributes constraints for Shopping Cart: 
       * `productId` : *NotNull*, *Min(1)*
       * `quantity` : *NotNull*, *Min(1)* <br/><br/>
       
     + Attributes explaination for Product:
       * `productId` : *product id*
       * `quantity` : *product quantity* <br/><br/>
       
     + Example in Request Body Raw::
```json
   [
       {
           "productId": 1,
           "quantity": 7
       },
       {
           "productId": 2,
           "quantity": 9
       },
       {
           "productId": 3,
           "quantity": 1
       }
   ]
```
<br/><br/>

- Response Body:
  + Content-Type = application/json <br/><br/>
  + Suported attributes for Cart:
```json
{
    "message": "Upload cart successfully",
    "cartId": 1,
    "userId": "4b740504-11b6-4652-97dc-7f22c819990c",
    "totalAmount": 167892.0,
    "productQuantityResponseDTOList": [
        {
            "productQuantityId": 40,
            "productId": 1,
            "shoppingCartId": 1,
            "orderId": null,
            "quantity": 7,
            "totalAmount": 69132.0,
            "productResponseDTO": {
                "message": null,
                "id": null,
                "name": "Product 1",
                "price": 9876.0,
                "stockQuantity": 30,
                "description": "Product 1",
                "createdDate": null,
                "lastModifiedDate": null,
                "fileResponseDTOList": [
                    {
                        "message": null,
                        "id": 1,
                        "name": null,
                        "fileType": null,
                        "size": null,
                        "fileByte": null
                    }
                ],
                "categoryResponseDTOList": [
                  {
                    "id": 3,
                    "name": "3",
                    "productResponseDTOList": null
                  }
                ]
            }
        }
  ]
}
```

2. GET: https://localhost/api/v1/carts `(USER ONLY)` <br/><br/>
   - Retrieve `Shopping Cart` by User authenticated `Bearer Token` in `AUTHORIZATION` <br/><br/> 
   - Response Body 
     + Content-Type = application/json  <br/><br/>
     + Suported attributes for Cart:
```json
{
    "message": "Search cart successfully",
    "cartId": 1,
    "userId": "4b740504-11b6-4652-97dc-7f22c819990c",
    "totalAmount": 167892.0,
    "productQuantityResponseDTOList": [
        {
            "productQuantityId": 40,
            "productId": 1,
            "shoppingCartId": 1,
            "orderId": null,
            "quantity": 7,
            "totalAmount": 69132.0,
            "productResponseDTO": {
                "message": null,
                "id": null,
                "name": "Product 1",
                "price": 9876.0,
                "stockQuantity": 30,
                "description": "Product 1",
                "createdDate": null,
                "lastModifiedDate": null,
                "fileResponseDTOList": [
                    {
                        "message": null,
                        "id": 1,
                        "name": null,
                        "fileType": null,
                        "size": null,
                        "fileByte": null
                    }
                ],
                "categoryResponseDTOList": [
                  {
                    "id": 3,
                    "name": "3",
                    "productResponseDTOList": null
                  }
                ]
            }
        }
  ]
}
```

### Order
1. POST: https://localhost/api/v1/orders/upload `(USER-ONLY)` <br/><br/>
   - Make Order from the user data and their cart (identifying user through token, so using Bearer Token in Authorization before send)
   - Request Body
     + Content-Type = application/json <br/><br/>
     + Example Request Body Raw:
```json
   {
       "orderInfo": "good",
       "anotherField": "good",
       "phoneNumber": 123456789,
       "addressRequestDTO": {
           "houseNumber": "number",
           "streetName": "street",
           "wardName": "ward",
           "city": "city",
           "zipCode": "zipCode"
       }
   }
```

  - Response Body:
    + Content-Type = application/json <br/><br/>
    + Suported attributes for Oder:
```json
{
  "message": "Save order 29 successfully.",
  "id": 29,
  "name": "admin@email.com",
  "totalAmount": 167892.00,
  "status": "PROCESSING",
  "deliveryDate": "2024-06-26T10:46:19.753213",
  "orderInfo": "good",
  "anotherField": "good",
  "productQuantityResponseDTOList": [
    {
      "productQuantityId": 40,
      "productId": 1,
      "shoppingCartId": null,
      "orderId": 29,
      "quantity": 7,
      "totalAmount": 69132.00,
      "productResponseDTO": {
        "message": null,
        "id": null,
        "name": "Product 1",
        "price": 9876.0,
        "stockQuantity": null,
        "description": "Product 1",
        "createdDate": null,
        "lastModifiedDate": null,
        "fileResponseDTOList": [
              {
                "message": null,
                "id": 1,
                "name": null,
                "fileType": null,
                "size": null,
                "fileByte": null
              }
          ],
          "categoryResponseDTOList": null
        }
      }
    ],
    "phoneNumber": 987654321,
    "addressResponseDTO": {
      "houseNumber": "34",
      "streetName": "street",
      "wardName": "ward",
      "city": "city",
      "zipCode": "zipcode"
  }
}
```
    
2. POST: https://localhost/api/v1/orders `(USER-ONLY)` <br/><br/>
   - Get All Order infomation according to user (use token)
   - Request Body
     + Content-Type = application/json <br/><br/>
       * `pageNumber`
       * `pageSize`
     + Example Request Body Raw:
```json
  {
    "pageNumber": 1,
    "pageSize": 20
  }
```
- Response Body:
  + Content-Type = application/json <br/><br/>
  + Suported attributes for Oder:
```json
{
    "content": [
      {
        "message": null,
        "id": 29,
        "name": "admin@email.com",
        "totalAmount": 167892.00,
        "status": "PROCESSING",
        "deliveryDate": "2024-06-26T10:46:19.753213",
        "orderInfo": "good",
        "anotherField": "good",
        "productQuantityResponseDTOList": [
          {
            "productQuantityId": 40,
            "productId": 1,
            "shoppingCartId": null,
            "orderId": 29,
            "quantity": 7,
            "totalAmount": 69132.00,
            "productResponseDTO": {
              "message": null,
              "id": null,
              "name": "Product 1",
              "price": 9876.0,
              "stockQuantity": null,
              "description": "Product 1",
              "createdDate": null,
              "lastModifiedDate": null,
              "fileResponseDTOList": [
                    {
                      "message": null,
                      "id": 1,
                      "name": null,
                      "fileType": null,
                      "size": null,
                      "fileByte": null
                    }
                ],
                "categoryResponseDTOList": null
              }
            }
          ],
          "phoneNumber": 987654321,
          "addressResponseDTO": {
            "houseNumber": "34",
            "streetName": "street",
            "wardName": "ward",
            "city": "city",
            "zipCode": "zipcode"
        }
      }
    ],
    "page": {
      "size": 20,
      "number": 1,
      "totalElements": 21,
      "totalPages": 2
    }
}
```

3. GET: https://localhost/api/v1/orders/search/{order-id} `(USER-ONLY)` <br/><br/>
   - Get Order information according to user and id (also use token)
   - Request Body
     + Content-Type = application/json <br/><br/>
   - Response Body:
     + Content-Type = application/json <br/><br/>
     + Supported attributes for Order:
```json
{
  "message": "Search order 29 successfully.",
  "id": 29,
  "name": "admin@email.com",
  "totalAmount": 167892.00,
  "status": "PROCESSING",
  "deliveryDate": "2024-06-26T10:46:19.753213",
  "orderInfo": "good",
  "anotherField": "good",
  "productQuantityResponseDTOList": [
    {
      "productQuantityId": 40,
      "productId": 1,
      "shoppingCartId": null,
      "orderId": 29,
      "quantity": 7,
      "totalAmount": 69132.00,
      "productResponseDTO": {
        "message": null,
        "id": null,
        "name": "Product 1",
        "price": 9876.0,
        "stockQuantity": null,
        "description": "Product 1",
        "createdDate": null,
        "lastModifiedDate": null,
        "fileResponseDTOList": [
              {
                "message": null,
                "id": 1,
                "name": null,
                "fileType": null,
                "size": null,
                "fileByte": null
              }
          ],
          "categoryResponseDTOList": null
        }
      }
    ],
    "phoneNumber": 987654321,
    "addressResponseDTO": {
      "houseNumber": "34",
      "streetName": "street",
      "wardName": "ward",
      "city": "city",
      "zipCode": "zipcode"
  }
}
```

4. PATCH: https://localhost/api/v1/orders/update/{order-id} `(USER-ONLY)` <br/><br/>
   - Update Order information according to user and id
   - Update Order attribute(s) with `product-id` <br/><br/>
   - Request Body
     + Content-Type = application/json <br/><br/>
       * `orderInfo`
       * `anotherField`
       * `phoneNumber`
       * `addressRequestDTO`<br/><br/>

        + Example Request Body Raw:
```json
   {
       "orderInfo": "good",
       "anotherField": "good",
       "phoneNumber": 123456789,
       "addressRequestDTO": {
           "houseNumber": "number",
           "streetName": "street",
           "wardName": "ward",
           "city": "city",
           "zipCode": "zipCode"
       }
   }
```

- Response Body:
  + Content-Type = application/json <br/><br/>
  + Suported attributes for Oder:
```json
{
  "message": "Update order 29 successfully.",
  "id": 29,
  "name": "admin@email.com",
  "totalAmount": 167892.00,
  "status": "PROCESSING",
  "deliveryDate": "2024-06-26T10:46:19.753213",
  "orderInfo": "good",
  "anotherField": "good",
  "productQuantityResponseDTOList": [
    {
      "productQuantityId": 40,
      "productId": 1,
      "shoppingCartId": null,
      "orderId": 29,
      "quantity": 7,
      "totalAmount": 69132.00,
      "productResponseDTO": {
        "message": null,
        "id": null,
        "name": "Product 1",
        "price": 9876.0,
        "stockQuantity": null,
        "description": "Product 1",
        "createdDate": null,
        "lastModifiedDate": null,
        "fileResponseDTOList": [
              {
                "message": null,
                "id": 1,
                "name": null,
                "fileType": null,
                "size": null,
                "fileByte": null
              }
          ],
          "categoryResponseDTOList": null
        }
      }
    ],
    "phoneNumber": 987654321,
    "addressResponseDTO": {
      "houseNumber": "34",
      "streetName": "street",
      "wardName": "ward",
      "city": "city",
      "zipCode": "zipcode"
  }
}
```

5. DELETE: https://localhost/api/v1/orders/delete/{order-id} `(ADMIN ONLY)` <br/><br/>
   - Delete Order by `order-id` <br/><br/>

### Transaction
1. POST: https://localhost/api/v1/transactions/upload (`USER-ONLY`) <br/><br/>
   - Upload transaction of User authenticated `Bearer Token` in `AUTHORIZATION`
   - Request Body
     + Content-Type = application/json  <br/><br/>
     + Suported attributes for Transaction: 
       * `orderId` 
       * `transactionType`
       * `currency` <br/><br/>

     + Attributes constraints for Product: 
       * `orderId` : *NotNull*, *Min(1)*
       * `transactionType` : *NotNull*, *NotBlank*
       * `currency` : *NotNull*, *NotBlank* <br/><br/>

     + Attributes explaination for Product:
       * `orderId` : *order id*
       * `transactionType` : *transaction type* (can be categorized as INTERNET_BANKING, CREDIT_CARD, CASH) 
       * `currency` : *currency* (can be categorized as VND, USD, EUR) <br/><br/>
       
     + Example Response Body Raw:
```json
   {
      "orderId": 1,
      "transactionType": "CREDIT_CARD",
      "currency": "VND"
   }
```

2. GET: https://localhost/api/v1/transactions?page-number={page-number}&page-size={page-size} (`USER-ONLY`)  <br/><br/> 
   - Find all Transaction(s) as Page, of User authenticated `Bearer Token` in `AUTHORIZATION` 
   - Request Parameters
     + Suported attributes for Transaction finding: 
       * `pageSize` 
       * `pageNumber` <br/><br/>
         
     + Attributes constraints for Transaction finding: 
       * `pageSize` : *NotNull*, *Min(1)*, *Max(20)
       * `pageNumber` : *NotNull*, *Min(1)* <br/><br/>
       
     + Attributes explaination for Transaction finding: 
       * `pageSize` : maximum number of Products in a page
       * `pageNumber` : page number of which page <br/><br/>
