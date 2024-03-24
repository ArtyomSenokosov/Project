Project
=========

The project of an internet store was developed as part of a training course from the IT academy. Spring boot application with role-based authorization and authentication using the most popular Java tools and technologies: Maven, Spring MVC, Security, JPA (Hibernate), REST API, Bootstrap (css, js), Thymeleaf, Java 11, MySQL database storage, and migrating data to a database using liquibase.

## Table of Contents

* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Screenshots](#screenshots)
* [Setup](#setup)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Acknowledgements](#acknowledgements)
* [Contact](#contact)

## <a name="general-information"></a> General information

The project of an internet store was developed as part of a training course from the IT academy. Spring boot 
application with role-based authorization and authentication using the most popular Java tools and technologies: Maven,
Spring MVC, Security, JPA (Hibernate), REST API, Bootstrap (css, js), Thymeleaf, Java 11, MySQL database storage, and
migrating data to a database using liquibase.

## <a name="technologies-used"></a> Technologies Used

* Java - version 11
* Hibernate - version 5.4.27
* Spring boot starter - version 2.4.2
* Liquibase - version 3.10.3
* MySQL - version 8.0.22
* Lombok - version 1.18.16

## <a name="features"></a> Features

The project was developed according to 4 technical specifications:_

* [First specification](#first)
* [Second specification](#second)
* [Third specification](#third)
* [Four specification](#four)

## <a name="first"></a> First specification

### User with administrator role:

#### _Page title: Login page_

The page should display a form to enter the web version of the site. The following data should be displayed:

* Email
* Password

#### _Page Title: Users_

The page should display a list of users. The following data should be displayed:

* Full name
* Email
* Role

Entries should be sorted alphabetically by email. If there are more than 10 records, paging should be performed. Each
entry must be marked for deletion (checkbox). Below the list-tables there should be a “Delete” button, when you click on
it, all marked records are deleted. One of the administrators with the "administrator" role must be unavailable for
deletion (and must be blocked from being able to "lower" privileges) so that all users cannot be deleted. For each
entry, it should be possible to change the password, which is automatically generated randomly and sent to the user's
email. Each entry must be able to change privileges.

#### _Page Title: Add Users_

The page should display a form to add a user. The following data must be filled in:

* Surname (40 characters, only Latin letters).
* Name (20 characters, only Latin letters).
* Patronymic (40 characters, only Latin letters).
* Email (50 characters, standard template).
* Role (administrator, merchant, client, secured REST API)

#### _Page Title: Reviews_

The page should display a list of reviews. The following data should be displayed:

* Full name
* Feedback
* Date added
* "Show" status (checkbox)

If there are more than 10 records, paging should be performed. Also, on this page it should be possible to delete the
review.

## <a name="second"></a> Second specification

### User with role Customer user:

#### _Page Title: Articles_

The page must display a list of news. The following data should be displayed:

* Date
* Title
* Name and Surname of the author
* Content (200 characters)
* More link that leads to the article page

Entries must be sorted by date in descending order. If there are more than 10 records, paging should be done.

#### _Page Title: Article_

One news item must be displayed on the page The following data should be displayed:

* Date
* Title
* Full content (up to 1000 characters)
* Name and Surname of the author

The page should display comments from previous users:

* First and Last name of the user
* Date
* Full content (up to 200 characters)

Entries must be sorted by date in descending order.

#### _Page Title: Profile Page_

The page needs to display a user profile form The following data should be displayed:

* Name
* Surname
* Residence address
* Telephone

The page should be able to change all fields and user password

### REST API (Basic authentication) for a user with the SECURE REST API role:

| URL | METHOD | ACTION |
| ------ | ------ | ------ |
| /api/users | POST | Add new user |  
| /api/articles | GET | Show list of all articles |  
| /api/articles/{id} | GET | Show article with id |  
| /api/articles | POST | Add new article |  
| /api/articles/{id} | DELETE | Delete article with id |  

## <a name="third"></a> Third specification

### A user with the Sale User role:

#### _Page Title: Articles Page_

The page must display a list of news. The following data should be displayed:

* Date
* Title
* Name and Surname of the author
* Content (200 characters)
* More link that leads to the article page

Entries must be sorted by date in descending order. If there are more than 10 records, paging should be done. It should
be possible to delete the article.

#### _Page Title: new article Page_

The page must display fields to create a new article The following data should be displayed:

* Date
* Title
* Full content (up to 1000 characters)

#### _Page Title: Page article_

One news item must be displayed on the page The following data should be displayed:

* Date
* Title
* Full content (up to 1000 characters)
* Name and Surname of the author

The page should display comments from previous users:

* First and Last name of the user
* Date
* Full content (up to 200 characters)
  Entries must be sorted by date in descending order. It should be possible to change the title and content of the
  article, the date of the article must change. It should be possible to delete any user's comment

#### _Page Title: Items Page_

The page needs to display a list of items. The following data should be displayed:

* Title
* Unique number (optional format)
* Price
* Details link that leads to the subject page

Entries should be sorted by title. If there are more than 10 records, paging should be done. It should be possible to
delete or copy an item.

#### _Page Title: Item Page_

The item must be displayed on the page. The following data should be displayed:

* Title
* Unique number (optional format)
* Price
* Short description (up to 200 characters)

### REST API (Basic authentication) for a user with the SECURE REST API role:

| URL | METHOD | ACTION |
| ------ | ------ | ------ |
| /api/items | GET | Show list of all items |
| /api/items/{id} | GET | Show item with id |  
| /api/items | POST | Add new item |  
| /api/items/{id} | DELETE | Delete item with id |  

## <a name="four"></a> Four specification

### A user with the Sale User role:

#### _Page Title: Orders Page_

The page must display a list of orders. The following data should be displayed:

* Order number
* Order status (NEW, IN_PROGRESS, DELIVERED, REJECTED)
* Name of ordered item
* Number of items
* Total price

Entries must be sorted by date in descending order. If there are more than 10 records, paging should be done. It should
be possible to go to order details

#### _Page Title: Order Page_

The following data should be displayed:

* Order number
* Order status (NEW, IN_PROGRESS, DELIVERED, REJECTED)
* Name of ordered item
* ID of the user who made the order
* Phone of the user who made the order
* Number of items
* Total price

It should be possible to change the order status

### User with role Customer User:

#### _Page Title: Item Page_

The page needs to display a list of items. The following data should be displayed:

* Title
* Unique number (optional format)
* Price
* Details link that leads to the item page

Entries should be sorted by title. If there are more than 10 records, paging should be done. It should be possible to
order an item by specifying the quantity.

### _Page Title: Orders Page_

The page needs to display a list of orders for the user. The following data should be displayed:

* Order number
* Order status (NEW, IN_PROGRESS, DELIVERED, REJECTED)
* Name of ordered item
* Number of items
* Total price

Entries must be sorted by date in descending order. If there are more than 10 records, paging should be done.

### _Page Title: Create Review Page_

The following data should be displayed:

* Review form

| URL | METHOD | ACTION |
| ------ | ------ | ------ |
| /api/orders | GET | Show list of all orders |
| /api/items/{id} | GET | Show order with id |  

## <a name="screenshots"></a> Screenshots

## <a name="setup"></a> Setup

### 1. Install Apache Maven

#### _build with:_ ```mvn -clean -package```

### 2. Install Docker

#### _run docker with settings:_

```
version: '3.8'
services:
  db:
    image: mysql:latest
    restart: always
    environment:
      MYSQL_DATABASE: 'project_db'
      MYSQL_ROOT_PASSWORD: 'root'
    ports:
      - '3306:3306'
    expose:
      - '3306'
```

## <a name="project-status"></a> Project Status

Project is: _complete_.

## <a name="room-for-improvement"></a> Room for Improvement

- Interaction with the storage when placing an order
- Sorting and filters for the product list

## <a name="acknowledgements"></a> Acknowledgements

Many thanks to: _IT Academy, my teacher Artsiom Peravoznikau and my groupmates._

## <a name="contact"></a> Contact

Created by _artem.senokosov@mail.ru_ - feel free to contact me!
