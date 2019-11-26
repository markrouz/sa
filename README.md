Проект состоит из следующих модулей:
* REST микросервис на Spring Boot, обеспечивающий CRUD сущности "Сотрудник"(http://localhost:8080/)
* REST микросервис на Spring Boot, запрашивающий список сотрудников у первого микросервиса и вычисляющий некоторую статистику(http://localhost:8081/)
* Eureka Server(http://localhost:8761/)
* Angular приложение, обеспечивающее вызов CRUD операций и отображающее статистику (http://localhost:4200/)

Чтобы запустить проект нужно установить Docker (v17+) и Docker Compose и выполнить docker-compose up (первая сборка занимает порядка 20-30 минут. Во время cборки возможны варнинги и ошибки. Так и задумано:) )\
Чтобы остановить проект достаточно выполнить docker-compose down --rmi all

