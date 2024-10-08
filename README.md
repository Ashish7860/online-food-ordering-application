##Online Food Ordering System (Dockerized)

This project is a Dockerized Spring Boot-based Online Food Ordering System that interacts with a MySQL database. It uses Docker and Docker Compose for setting up a multi-container application.


Prerequisites

- Docker installed on your machine
- Docker Compose installed


Getting Started

1. Clone the repository and navigate to the project directory: https://github.com/Ashish7860/online-food-ordering-application

bash

git clone: https://github.com/Ashish7860/online-food-ordering-application
cd online-food-ordering-docker


2. Build the Docker image and start the containers:
   bash
docker-compose up --build


This command will:
- Set up the MySQL database in a separate container.
- Build and run the Spring Boot application in another container.


*Verifying Setup*

- Verify the status of running services:

bash

docker-compose ps


* Access the Spring Boot application at: http://localhost:8081

* MySQL database details:
   * Host: localhost
   * Port: 3306
   * Database Name: restaurant
   * Username: root
   * Password: root


## Useful Docker Commands

* View running containers: docker-compose ps
* Stop all running containers: docker-compose down
* View logs generated by containers: docker-compose logs


## Pushing to Docker Hub

1. Build and tag the Docker image:
   bash
docker build -t ashish07860/online-food-ordering-system .


1. Push the image to Docker Hub:

bash

docker push ashish07860/online-food-ordering-system:latest


3. Pull and run the image from Docker Hub:
   bash
docker pull ashish07860/online-food-ordering-system:latest
docker run -p 8081:8081 ashish07860/online-food-ordering-system:latest





##Technologies Used:

- Spring Boot for the backend application
- MySQL for the database
- Docker for containerization
- Docker Compose for setting up the multi-container environment
