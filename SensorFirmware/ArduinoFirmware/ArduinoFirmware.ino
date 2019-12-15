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

String AP = "WIFI_NAME";  
String PASS = "PASSWORD"; 
String HOST = "func-weather.herokuapp.com";
int PORT = 80;
int countTimeCommand;
boolean found = false;

SoftwareSerial esp8266(RX, TX);
ESP8266 wifi(esp8266);

void setup() {
    Serial.begin(9600);
    esp8266.begin(115200);
    
    pinMode(11, OUTPUT);
    pinMode(7, INPUT);
    digitalWrite(11, HIGH);
 
    sendCommand("AT", 5, "OK");
    sendCommand("AT+CWMODE=1", 5, "OK");
    sendCommand("AT+CWJAP=\"" + AP + "\",\"" + PASS + "\"", 20, "OK");
  
    Serial.println(F("BME280 тест"));
 
    if (!bme.begin()) {
        Serial.println("Не удалось подключить датчик");
        while (1);
    }
    Serial.println("Датчик параметров подключен");
    delay(100); 
}
 
void loop() { 
    printValues();
    if (wifi.createTCP(HOST, PORT)) {
        String data = "GET /?loc=SVAO&temp=";
        data += bme.readTemperature();
        data += "&pressure=";
        data += bme.readPressure() * 0.0075F;
        data += "&humidity=";
        data += bme.readHumidity();
        data += " HTTP/1.1\r\nHost: ";
        data += HOST;
        data += "\r\n\r\n";
        wifi.send(data.c_str(), data.length());
        if (digitalRead(7) == HIGH) {
            tone(11, 18000, 300);
            delay(500);
            tone(11, 20000, 300);
            delay(500);
            tone(11, 10000, 300);
            delay(500);
        }
        digitalWrite(11, HIGH);
        wifi.releaseTCP();
        Serial.println("Запрос отправлен");
    } else 
        Serial.println("Запрос не был отправлен");   
    delay(5000);
}
 
void printValues() {
    Serial.println();
    Serial.print("Температура = ");
    Serial.print(bme.readTemperature());
    Serial.print(" *C :: ");
    Serial.print("давление = ");
    Serial.print(bme.readPressure() * 0.0075F);
    Serial.print(" hPa :: ");
    Serial.print("влажность = ");
    Serial.print(bme.readHumidity());
    Serial.print(" %");
    Serial.println();
}

void sendCommand(String command, int maxTime, char readReplay[]) {
  Serial.print("выполняю >> ");
  Serial.print(command);
  Serial.print(" ");
  
  while (countTimeCommand < maxTime) {
    esp8266.println(command);
    if (esp8266.find(readReplay)) {
      found = true;
      break;
    }
  }
  
  Serial.println(found ? "Успешно" : "Провал");
  found = false;
}
