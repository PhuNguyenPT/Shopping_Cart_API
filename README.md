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
docker-compose up -d
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

## Rest API
1. POST: https://localhost/api/v1/products/upload <br/>
   - Request Body: <br/>
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

