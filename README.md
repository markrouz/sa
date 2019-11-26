Проект состоит из следующих модулей:
* REST микросервис на Spring Boot, обеспечивающий CRUD сущности "Сотрудник"(http://localhost:8080/)
* REST микросервис на Spring Boot, запрашивающий список сотрудников у первого микросервиса и вычисляющий некоторую статистику(http://localhost:8081/)
* Eureka Server(http://localhost:8761/)
* Angular приложение, обеспечивающее вызов CRUD операций и отображающее статистику (http://localhost:4200/)

Чтобы запустить проект нужно установить Docker и Docker Compose и выполнить docker-compose up\
Чтобы остановить проект достаточно выполнить docker-compose down --rmi all\

