# DIMU-API


## Overview

DIMU-API is the backend service powering the DIMU application. This API facilitates seamless data management and interaction for the DIMU platform.

## Features

* **RESTful Endpoints**: Provides a set of RESTful APIs to interact with the DIMU application.
* **Data Management**: Handles operations related to data storage, retrieval, and manipulation.
* **Authentication & Authorization**: Ensures secure access to the API endpoints.
* **Scalability**: Designed to scale efficiently to handle increasing loads.

## Technologies Used

* **Java**: The primary programming language for the backend logic.
* **Spring Boot**: Framework used to build the RESTful API.
* **Docker**: Containerization of the application for consistent environments.
* **Maven**: Dependency management and build automation.([GitHub][1])

## Installation

### Prerequisites

* Java 11 or higher
* Maven
* Docker (optional, for containerized deployment)

### Steps

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/tylher/DIMU-API.git
   cd DIMU-API
   ```



2. **Build the Project**:

   ```bash
   mvn clean install
   ```



3. **Run the Application**:

   ```bash
   mvn spring-boot:run
   ```



By default, the application will run on [http://localhost:8080](http://localhost:8080).

### Docker Deployment (Optional)

1. **Build the Docker Image**:

   ```bash
   docker buildx build --platform linux/amd64,linux/arm64 -t diimu-api:1.0 .

   ```



2. **Run the Docker Container**:

   ```bash
   docker run -p 8080:8080 diimu-api
   ```



The application will be accessible at [http://localhost:8080](http://localhost:8080).

## Usage

Once the application is running, you can interact with the API endpoints. Refer to the API documentation for detailed information on available endpoints and their usage.


## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/tylher/DIMU-API/blob/main/LICENSE) file for details.

---

Feel free to adjust any sections as per your project's specific requirements.

[1]: https://github.com/NordicMuseum?utm_source=chatgpt.com "Nordic Museum - GitHub"
