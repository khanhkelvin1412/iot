# iot

Nếu sử dụng docker

```
docker-compose up -d

mysql -u root -p

create user 'hust'@'172.31.0.1' identified by '1qazXSW@';

grant all privileges ON smart_house.* to 'hust'@'172.31.0.1';

```

Hướng dẫn tạo Schema: 

```
create database smart_house;

create user 'hust'@'localhost' identified by '1qazXSW@';

grant all privileges ON smart_house.* to 'hust'@'localhost';

```

### Run project
Nếu đã tải maven và setup môi trường rồi thì chỉ cần chạy lệnh để clean trước khi build. Cần cd tới thư mục chứa project

```
mvn clean
```
Tiếp đến chạy lệnh mvn package để biên dịch và đóng gói mã nguồn thành file .jar hoặc .war

```
mvn package
```
cd tới folder tartget (folder vừa được gen ra sau khi chạy lệnh package) => ... => tới thư mục chứa file .jar hoặc .war

chạy lệnh 

```
java -jar nameFile.jar
```



