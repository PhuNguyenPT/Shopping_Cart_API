## Setup Instructions

To set up the backend of the Book Social Network project, follow these steps:
1. Clone the repository:

```bash
   git clone https://github.com/PhuNguyenPT/Shopping_Cart.git
```

2. Run the docker-compose file in terminal

```bash
  docker-compose up -d
```
3. Install dependencies (assuming Maven is installed):

```bash
  mvn clean install
```
4. Run the application but first replace the `x.x.x` with the current version from the `pom.xml` file

```bash
  java -jar target/shopping-cart-api-0.0.1-SNAPSHOT.jar
```
