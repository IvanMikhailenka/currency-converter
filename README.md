# Currency Converter


Currency Converter is a small helper app designed to perform currency exchange rate conversions. It serves as a starting point for adding currency conversion functionality to the entire platform, enabling the business to expand into new regions.

## Key Features

- Convert currency from one to another with a provided amount
- Utilizes real-time exchange rates for accurate conversions

## Installation

To set up the Currency Converter project, follow these steps:

1. Clone the repository to your local machine.
2. Open a terminal and navigate to the project directory.
3. Run the following command to start the application using Maven Wrapper:

   `cd currency-converter-server/ && ../mvnw spring-boot:run`

    Alternatively, you can build a Docker image and run it:

    `./mvnw clean package && docker build -t currency-converter . && docker run -p 8080:8080 --name currency-converter currency-converter`

4. The application will be accessible at [http://localhost:8080](http://localhost:8080).

## Usage

To use the Currency Converter app, follow these steps:

1. Navigate to [http://ec2-44-193-117-151.compute-1.amazonaws.com](http://ec2-44-193-117-151.compute-1.amazonaws.com) to access the Swagger UI.
2. Use the provided endpoints to convert currencies by providing the required input.

## Future Plans

In the future, we have the following plans for the Currency Converter project:

- Add authentication functionality to secure sensitive operations.
- Implement Wiremock for integration tests to ensure reliable and accurate currency conversion.


## Acknowledgments

We would like to acknowledge the following resources for their contributions to this project:

- [ExchangeRate API](https://exchangerate.host/) - Provides real-time exchange rate data for accurate currency conversions.
- [OpenAPI + Swagger](https://swagger.io) - Used for API documentation and exploration.

