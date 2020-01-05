/* 
 * https://github.com/S1mpleFunc/WeatherStation 
 *  
 * MIT License
 * 
 * Copyright (c) 2019 S1mpleFunc
 *  
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */


#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <ESP8266.h>
#include <SoftwareSerial.h>


#define RX 2
#define TX 3
 
#define SEALEVELPRESSURE_HPA (1013.25)
 
Adafruit_BME280 bme;

/* Название WIFI сети */
String AP = "WIFI_NAME";  
/* Пароль необходимой WIFI сети */
String PASS = "PASSWORD"; 
/* Сервер на который будут оправленны данные */
String HOST = "func-weather.herokuapp.com";
/* Порт сервера */
int PORT = 80;
/* Прочие переменные */
int countTimeCommand;
boolean found = false;

SoftwareSerial esp8266(RX, TX);
ESP8266 wifi(esp8266);

void setup() 
{
    Serial.begin(9600);
    esp8266.begin(115200);

    /* Проверка микросервиса и подключние к сети WIFI */
    sendCommand("AT", 5, "OK");
    sendCommand("AT+CWMODE=1", 5, "OK");
    sendCommand("AT+CWJAP=\"" + AP + "\",\"" + PASS + "\"", 20, "OK");

    /* Подключение датчика температуры / давления / влажности */
    Serial.println(F("BME280 тест"));
 
    if (!bme.begin()) 
    {
        Serial.println("Не удалось подключить датчик");
        while (1);
    }
    Serial.println("Датчик параметров подключен");
    delay(100); 
}
 
void loop() 
{ 
    /* Вывод в порт полученные значения */
    printValues();

    /* Если удалось установить соединение с сервером */
    if (wifi.createTCP(HOST, PORT)) 
    {
        /* Отправка данных GET запросом вида
         *  GET /?loc=SVAO&temp=ТЕМПЕРАТУРА&pressure=ДАВЛЕНИЕ&humidity=ВЛАЖНОСТЬ
        */
        String data = "GET /?loc=SVIBLOVO&temprature=";
        data += bme.readTemperature();
        data += "&pressure=";
        data += bme.readPressure() * 0.0075F;
        data += "&humidity=";
        data += bme.readHumidity();
        data += " HTTP/1.1\r\nHost: ";
        data += HOST;
        data += "\r\n\r\n";
        
        /* Отправка запроса */
        wifi.send(data.c_str(), data.length());

        /* Прекращает соединение с сервером */
        wifi.releaseTCP();
        Serial.println("Запрос отправлен");
    } 
    else
    { 
        Serial.println("Запрос не был отправлен");   
    }
    delay(5000);
}
 
void printValues() 
{
    /* Вывод данных о температуре */
    Serial.println();
    Serial.print("Температура = ");
    Serial.print(bme.readTemperature());
    Serial.print(" *C :: ");
    
    /* Вывод данных о давлении */
    Serial.print("давление = ");
    Serial.print(bme.readPressure() * 0.0075F);
    Serial.print(" torr :: ");

    /* Вывод данных о влажности */
    Serial.print("влажность = ");
    Serial.print(bme.readHumidity());
    Serial.print(" %");
    Serial.println();
}

/* Повторяющаяся отправка команд на Wifi-модуль */
void sendCommand(String command, int maxTime, char readReplay[])
{
    Serial.print("выполняю >> ");
    Serial.print(command);
    Serial.print(" ");
    
    while (countTimeCommand < maxTime) 
    {
        esp8266.println(command);
        if (esp8266.find(readReplay)) 
        {
            found = true;
            break;
        }
    }
    Serial.println(found ? "Успешно" : "Провал");
    found = false;
}
