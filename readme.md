# FinanceShow

## Setup

### Using Maven (Recommended)

1. Make sure you have Java 17 and Maven installed on your machine.

2. Clone the repository:
    ```bash
    git clone https://github.com/ecfesa/FinanceShow.git
    ```
3. Navigate to the project directory:
cd FinanceShow-demo

4. Run the following command to build the project and set up the frontend:
    ```bash
    mvn clean install  
    ```
5. Once the build is complete, you can run the application with:
    ```bash
    mvn spring-boot:run
    ```
6. The application will be available at http://localhost:8080.


### Without Maven

1. Make sure you have Java 17, Node.js, and npm installed on your machine.

2. Clone the repository:
    ```bash
    git clone https://github.com/ecfesa/FinanceShow.git
    ```

3. Navigate to the project directory:
    ```bash
    cd FinanceShow-demo
    ```

4. Install the frontend dependencies:
    ```bash
    npm install
    ```

5. Build the Tailwind CSS:
    ```bash
    npm run build
    ```

6. Run the Spring Boot application:
    ```bash
    ./mvnw spring-boot:run
    ```

7. The application will be available at http://localhost:8080