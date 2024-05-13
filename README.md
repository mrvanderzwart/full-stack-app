# Full Stack Football Application

An application that visualizes the matches that were played by a football club during a specific season. Additionally, it has a component showing the coming match, for which a prediction can be calculated. The backend is built with **Java Springboot**, and the frontend with **Angular**. For styling the Angular components, **CSS** is used. For retrieving the actual match data, **Jsoup** is used. For storing the match data, **MySQL** is used. For predicting the result of matches, a **Python** script is used that can be called from Java with a process builder. The Python script uses **Machine Learning** for determining the match results. The backend is responsible for manipulating the database holding the match data, and predicting the result of an upcoming match, whereas the frontend visualizes the data from the database and allows for making requests to the backend.

## View

The main page consists of a couple of Angular components. The first component is responsible for the match log, and it shows the match data from the database. Different match results get different colors. Besides, a button is present that can be used to manipulate the database, such that it becomes up to date.

![MP](/main-page1.PNG)

Besides, a component is created that shows the upcoming match and the corresponding data. Additionally, a button is present that can be used to compute a prediction on the match. Once clicked, it takes a couple of seconds executing a Python script, whereafter the output is visualized.

![MP](/main-page2.PNG)

## Database configuration

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/database
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## Links

[Live football data](https://fbref.com/en/)\
[MySQL server](https://www.apachefriends.org/)\
[New SpringBoot project](https://start.spring.io/)\
[Frontend framework](https://angular.io/)\
[API testing](https://www.postman.com/)

## Contact

[LinkedIn](https://www.linkedin.com/in/michael-van-der-zwart-00a21314a/)
