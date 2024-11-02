# Employee REST API project

This project implements a simple REST API using [Quarkus](https://quarkus.io/) framework.

Check out the companion blog post, [Get started with Quarkus by building a simple REST API](https://chrischiedo.github.io/quarkus-rest-api-tutorial), for a guided walkthrough of the code.

## Running the application in dev mode

First, visit this [page](https://quarkus.io/guides/cli-tooling) to install the Quarkus CLI tool for your platform.

After installing the CLI tool, you can then run the application in dev mode (enables live coding) using:

```bash
$ quarkus dev
```


## Check OpenAPI/Swagger documentation 

Visit http://localhost:8080/q/swagger-ui on your browser to see the Swagger documentation for the API.

## Tests

To run tests for the project use:

```bash
$ quarkus test
```