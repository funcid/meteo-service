<h1 align=center>MeteoService</h1>

Сервисная часть (Java Servlet Server)</br>
	- Активная версия: https://func-weather.herokuapp.com </br>
	
Аппаратная составляющая (Arduino Firmware) </br>
	- Пример сборки и прошивка находятся в одноименном каталоге </br>
	
Клиент (Android Java Application) </br>
	- Активная версия: https://play.google.com/store/apps/details?id=ru.func.weatherclient </br>
<h2>Описание</h2>
Датчик, собирающий данные (температура, давление, влажность), отправляет их GET запросом
на вебсервер, сервер в свою очередь запоминает данные, откуда они пришли и когда, в базу данных
MySQL, затем мобильное приложение запрашивает у сервера информацию, сервис отправляет в ответ набор 
данных в формате JSON, в конце концов мобильное приложение выводит отформатированные данные на 
красивой карте карте с маркерами. 

<h2>Как посмотреть</h2>
Серверная часть (Java Spring Boot)</br>
	<a href="https://func-weather.herokuapp.com">Перейти на вебсервер.</a></br>
Клиент (Android Java Application)</br>
	<a href="https://play.google.com/store/apps/details?id=ru.func.weatherclient">Установить мобильный клиет в Google Play.</a></br>
Аппаратная составляющая (Arduino Firmware)</br>
	<a href="https://github.com/funcid/MeteoService/tree/master/SensorFirmware">Пример сборки и прошивка. </a>

<h2>Технологии</h2>
Java 8, C++, Spring Boot, Slf4j, Gradle, Maven, Git, MySQL, Arduino, Google Maps API, Servlet, REST.

<h2>Сборка и запуск сервера</h2>

```shell
mvn clean package
java -jar WeatherSender-1.0-SNAPSHOT.jar
```

По-умолчанию, сервер запустится по адресу `localhost` на порту `8080`.  
Для изменения этих значений, нужно указать новые значения
в файле `/src/main/resources/application.properties`:

```properties
server.address=localhost
server.port=8080
```
    
<h2>Как со мной связаться.</h2></br>
<a href="https://vk.com/funcid">VK.COM</a></br>
<a href="http://funcid.ru">Личный сайт</a>

<h2>Лицензия</h2>
MIT License

Copyright (c) 2019 funcid

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.