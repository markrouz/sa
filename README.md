Описание проекта:
Проект состоит из следующих модулей:
* REST микросервис на Spring Boot, обеспечивающий CRUD сущности "Сотрудник"
* REST микросервис на Spring Boot, запрашивающий список сотрудников у первого микросервиса и вычисляющий некоторую статистику
* Eureka Server
* Angular приложение, обеспечивающее вызов CRUD операций и отображающее статистику

Чтобы запустить проект нужно установить Docker и Docker Compose и выполнить docker-compose up
Чтобы остановить проект достаточно выполнить docker-compose down --rmi all

