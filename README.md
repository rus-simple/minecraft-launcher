# Настройка конфигурации

## 1. Создание конфигурационного файла

Скопируйте файл `config.example.properties` и переименуйте его в `config.properties`:

## 2. Информацию для заполнения можно получить из логов старого лаунча:

C:\Users\ВашеИмя\AppData\Roaming\.minecraft\logs

## 3. Заполнение конфигурации

Откройте `config.properties` и заполните поля:

### Обязательные поля для онлайн-режима:
```properties
# Имя вашего аккаунта Minecraft
username=ВашИгровойАккаунт

# UUID вашего аккаунта (можно получить через официальный лаунчер)
uuid=ваш-уникальный-uuid-аккаунта

# Access Token (временный ключ доступа)
accessToken=ваш-access-token

# Client ID (идентификатор клиента)
clientId=ваш-client-id

# XUID (Xbox Live идентификатор)
xuid=ваш-xuid

# Путь к папке .minecraft
gameDir=C:/Users/ВашеИмя/AppData/Roaming/.minecraft

# Версия Minecraft для запуска
version=1.21.8
