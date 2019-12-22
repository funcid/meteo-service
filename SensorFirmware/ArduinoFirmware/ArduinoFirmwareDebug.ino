/*
 * Проект         Метеостанция
 * Автор проекта  Царюк Артем
 * Сайт автора    funcid.ru
 * Сервер проекта func-weather.herokuapp.com
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

    /* Установка цифровых входов и выходов */
    pinMode(11, OUTPUT);
    pinMode(7, INPUT);
    /* Отключение звука на пищалке */
    digitalWrite(11, LOW);

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
    /* Выведение в порт полученных значений с датчика */
    printValues();

    /* Если успешно получилось установить соединение с сервером */
    if (wifi.createTCP(HOST, PORT)) 
    {
        /* Отправка данных GET запросом следующего вида
         *  GET /?loc=SVAO&temp=ТЕМПЕРАТУРА&pressure=ДАВЛЕНИЕ&humidity=ВЛАЖНОСТЬ
        */
        String data = "GET /?loc=SVIBLOVO&temp=";
        data += bme.readTemperature();
        data += "&pressure=";
        data += bme.readPressure() * 0.0075F;
        data += "&humidity=";
        data += bme.readHumidity();
        data += " HTTP/1.1\r\nHost: ";
        data += HOST;
        data += "\r\n\r\n";
        
        /* Отправка запроса на удаленный сервер */
        wifi.send(data.c_str(), data.length());

        /* Открывает мелодию на пищалке если зажата кнопка отладки */
        if (digitalRead(7) == HIGH) 
        {
            tone(11, 18000, 300);
            delay(500);
            tone(11, 20000, 300);
            delay(500);
            tone(11, 10000, 300);
            delay(500);
        }
        digitalWrite(11, HIGH);

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

/* Вывод данных, полученных с датчика */
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
