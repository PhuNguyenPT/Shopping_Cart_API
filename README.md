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

```bash
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
```bash
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
```
   https://localhost:443/api/v1/auth/login
```
   - Example in Request Body Raw:

```
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

2. GET: https://localhost/api/v1/products/search/{product-id} `(NON-USER)` <br/>
   - Search Product by `product-id` <br/><br/>
   - Response Body 
     + Content-Type = application/json <br/><br/>

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
```
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

2. GET: https://localhost/api/v1/carts `(USER ONLY)` <br/><br/>
   - Retrieve `Shopping Cart` by User authenticated `Bearer Token` in `AUTHORIZATION` <br/><br/> 
   - Response Body 
     + Content-Type = application/json  <br/><br/>
 
### Category

1. GET: https://localhost/api/v1/categories `(NON-USER)` <br/><br/>
   - Return all the categories available <br/><br/> 
   - Response Body 
     + Content-Type = application/json <br/><br/>
     + Example Response Body Raw: 
```
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
       * `categoryRequestDTOList`:
         * `categoryId`  <br/><br/>
         
     + Attributes constraints for Category filter: 
       * `pageSize` : *NotNull*, *Min(1)*, *Max(20)
       * `pageNumber` : *NotNull*, *Min(1)*
       * `categoryRequestDTOList`:
         * `categoryId` *NotNull*, *Min(1)*  <br/><br/>
         
     + Attributes explaination for Category filter: 
       * `pageSize` : maximum number of Products in a page
       * `pageNumber` : page number of which page
       * `categoryRequestDTOList`:
         * `categoryId` : category id  <br/><br/>
         
     + Example Response Body Raw: <br/>
```
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
