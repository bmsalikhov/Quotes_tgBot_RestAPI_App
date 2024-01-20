# Небольшое учебное Spring приложение

## Что делает приложение:
- Парсит цитаты с сайта http://ibash.org.ru/
- Выводит их пользователю в телеграмм боте
- Выводит их пользователю с помощью запросов http

## Dependencies
- [JavaTelegramBotApi](https://mvnrepository.com/artifact/com.github.pengrad/java-telegram-bot-api/6.9.1)
- [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web/3.2.2)
- [Spring Boot Starter Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa/3.2.2)
- [PostgreSQL JDBC Driver](https://mvnrepository.com/artifact/org.postgresql/postgresql/42.7.1)
- [Jsoup Java HTML Parser](https://mvnrepository.com/artifact/org.jsoup/jsoup/1.17.2)

## Структура БД
![somedb - public](https://github.com/bmsalikhov/Quotes_tgBot_RestAPI_App/assets/153372291/7edd110b-15f5-470c-8726-af7f549614fa)

- В таблицe Quotes храним все цитаты, спарсенные с сайта
- В таблице Chats храним данные по чатам с ботом в ТГ

## Packages
### controllers
#### Классы
- ApiController - организует взаимодействие с пользователем с помощью http-запросов
- BotController - организует взаимодействие с пользователем с помощью телеграм бота
### models
#### Классы
- Chat - модель Chat для взаимодействия с БД
- Quote - модель Quote для взаимодействия с БД

Модели для взаимодействия с БД
### repositories
#### Классы
- ChatRepository
- QuoteRepository
  
Классы для взаимодействия с БД (сохранение, поиск и т.д.)
### services
#### Классы
- BashParser - сервис для парсинга цитат с сайта
- QuoteService - сервис для работы с цитатами
