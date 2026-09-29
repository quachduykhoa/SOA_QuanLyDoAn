# Demo SOA - Hệ thống quản lý đồ án tốt nghiệp

## 1. Giới thiệu

Đây là project demo môn học nhằm minh họa cách xây dựng **dịch vụ web theo kiến trúc hướng dịch vụ (SOA)** bằng **Spring Boot**.

### Bài toán

Khoa cần xây dựng hệ thống quản lý đồ án tốt nghiệp cho sinh viên.

Hệ thống có 3 nhóm dữ liệu chính:

- `SINHVIEN`: thông tin sinh viên.
- `DETAI`: thông tin đề tài tốt nghiệp.
- `DANGKY`: thông tin sinh viên đăng ký đề tài.

Các dịch vụ trao đổi dữ liệu với nhau thông qua:

- HTTP
- REST API
- JSON

Mục tiêu của demo:

1. Xây dựng các Web Service bằng Spring Boot.
2. Thể hiện đặc trưng cơ bản của SOA.
3. Các service tương đối độc lập.
4. Giao tiếp thông qua giao thức chuẩn HTTP/REST.
5. Có thể tái sử dụng các service.
6. Thực hiện CRUD cơ bản.

---

# 2. Kiến trúc tổng thể

Project được tổ chức thành 3 service chính:

```text
                         Client
                           |
                           | HTTP/REST
                           v
                 +----------------------+
                 |   REST API Services  |
                 +----------------------+
                    /        |        \
                   /         |         \
                  v          v          v
        +-------------+ +-------------+ +-------------+
        | SinhVien    | | DeTai       | | DangKy     |
        | Service     | | Service     | | Service    |
        +-------------+ +-------------+ +-------------+
               |               |               |
               v               v               v
        +-------------+ +-------------+ +-------------+
        | SINHVIEN    | | DETAI       | | DANGKY     |
        +-------------+ +-------------+ +-------------+
                    \       Database       /
                     \____________________/
```

> **Lưu ý:** Trong demo này có thể sử dụng một CSDL chung được cung cấp sẵn để đơn giản hóa việc thực hành. Sự độc lập được thể hiện chủ yếu ở tầng service: mỗi service có nhiệm vụ và API riêng.

---

# 3. Các Service

## 3.1. SinhVien Service

### Nhiệm vụ

Quản lý thông tin sinh viên.

Phụ trách bảng:

```text
SINHVIEN
```

Các chức năng:

- Thêm sinh viên.
- Xem danh sách sinh viên.
- Xem sinh viên theo ID.
- Cập nhật sinh viên.
- Xóa sinh viên.

Ví dụ base URL:

```text
http://localhost:8081/api/sinhvien
```

---

## 3.2. DeTai Service

### Nhiệm vụ

Quản lý thông tin đề tài tốt nghiệp.

Phụ trách bảng:

```text
DETAI
```

Các chức năng:

- Thêm đề tài.
- Xem danh sách đề tài.
- Xem đề tài theo ID.
- Cập nhật đề tài.
- Xóa đề tài.

Ví dụ base URL:

```text
http://localhost:8082/api/detai
```

---

## 3.3. DangKy Service

### Nhiệm vụ

Quản lý việc sinh viên đăng ký đề tài.

Phụ trách bảng:

```text
DANGKY
```

Các chức năng:

- Đăng ký đề tài.
- Xem danh sách đăng ký.
- Xem đăng ký theo ID.
- Cập nhật đăng ký.
- Hủy đăng ký.

Ví dụ base URL:

```text
http://localhost:8083/api/dangky
```

Service này có thể gọi sang:

```text
SinhVien Service
DeTai Service
```

thông qua HTTP/REST để kiểm tra sinh viên và đề tài tồn tại trước khi đăng ký.

---

# 4. Cấu trúc thư mục toàn project

Khuyến nghị tổ chức repository như sau:

```text
soa-thesis-management/
│
├── README.md
│
├── database/
│   └── database.sql
│
├── sinhvien-service/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com.example.sinhvien/
│       │   │       ├── SinhVienApplication.java
│       │   │       │
│       │   │       ├── controller/
│       │   │       │   └── SinhVienController.java
│       │   │       │
│       │   │       ├── service/
│       │   │       │   ├── SinhVienService.java
│       │   │       │   └── SinhVienServiceImpl.java
│       │   │       │
│       │   │       ├── repository/
│       │   │       │   └── SinhVienRepository.java
│       │   │       │
│       │   │       └── entity/
│       │   │           └── SinhVien.java
│       │   │
│       │   └── resources/
│       │       └── application.properties
│       │
│       └── test/
│
├── detai-service/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com.example.detai/
│       │   │       ├── DeTaiApplication.java
│       │   │       ├── controller/
│       │   │       │   └── DeTaiController.java
│       │   │       ├── service/
│       │   │       │   ├── DeTaiService.java
│       │   │       │   └── DeTaiServiceImpl.java
│       │   │       ├── repository/
│       │   │       │   └── DeTaiRepository.java
│       │   │       └── entity/
│       │   │           └── DeTai.java
│       │   │
│       │   └── resources/
│       │       └── application.properties
│       │
│       └── test/
│
└── dangky-service/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── com.example.dangky/
        │   │       ├── DangKyApplication.java
        │   │       │
        │   │       ├── controller/
        │   │       │   └── DangKyController.java
        │   │       │
        │   │       ├── service/
        │   │       │   ├── DangKyService.java
        │   │       │   └── DangKyServiceImpl.java
        │   │       │
        │   │       ├── repository/
        │   │       │   └── DangKyRepository.java
        │   │       │
        │   │       ├── entity/
        │   │       │   └── DangKy.java
        │   │       │
        │   │       └── client/
        │   │           ├── SinhVienClient.java
        │   │           └── DeTaiClient.java
        │   │
        │   └── resources/
        │       └── application.properties
        │
        └── test/
```

---

# 5. Cấu trúc bên trong một Service

Ví dụ `sinhvien-service`:

```text
sinhvien-service
│
├── controller
│   └── SinhVienController
│
├── service
│   ├── SinhVienService
│   └── SinhVienServiceImpl
│
├── repository
│   └── SinhVienRepository
│
└── entity
    └── SinhVien
```

Luồng xử lý:

```text
Client
   |
   | HTTP Request
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
Database
```

Ví dụ:

```text
GET /api/sinhvien/1
        |
        v
SinhVienController
        |
        v
SinhVienService
        |
        v
SinhVienRepository
        |
        v
     SINHVIEN
```

---

# 6. Vai trò của từng Layer

## Controller

Nhận HTTP request và trả HTTP response.

Ví dụ:

```java
@GetMapping("/{id}")
public ResponseEntity<SinhVien> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.getById(id));
}
```

Controller không nên chứa toàn bộ business logic.

---

## Service

Chứa nghiệp vụ của hệ thống.

Ví dụ:

```text
Kiểm tra sinh viên tồn tại
Kiểm tra dữ liệu hợp lệ
Xử lý nghiệp vụ
Gọi Repository
```

---

## Repository

Làm việc với database.

Với Spring Data JPA:

```java
public interface SinhVienRepository
        extends JpaRepository<SinhVien, Integer> {
}
```

---

## Entity

Đại diện cho bảng trong database.

Ví dụ:

```java
@Entity
@Table(name = "SINHVIEN")
public class SinhVien {

    @Id
    private Integer maSV;

    private String hoTen;

    private String email;
}
```

Tên field cần điều chỉnh theo đúng CSDL được giảng viên cung cấp.

---

# 7. Database

Project sử dụng CSDL đã cho:

```text
SINHVIEN
DETAI
DANGKY
```

Quan hệ khái quát:

```text
SINHVIEN
   |
   | 1
   |
   | N
 DANGKY
   |
   | N
   |
   | 1
 DETAI
```

Có thể hiểu:

```text
Một sinh viên
    |
    +---- có thể đăng ký đề tài

Một đề tài
    |
    +---- có thể có thông tin đăng ký
```

Cấu trúc cột chính xác phải lấy theo database được cung cấp, không tự thay đổi tên cột nếu đề bài đã quy định.

---

# 8. REST API

## 8.1. SinhVien Service

Base URL:

```text
http://localhost:8081/api/sinhvien
```

### Lấy tất cả sinh viên

```http
GET /api/sinhvien
```

### Lấy sinh viên theo ID

```http
GET /api/sinhvien/{id}
```

Ví dụ:

```http
GET /api/sinhvien/1
```

### Thêm sinh viên

```http
POST /api/sinhvien
Content-Type: application/json
```

Body ví dụ:

```json
{
    "maSV": 1,
    "hoTen": "Nguyen Van A",
    "email": "a@example.com"
}
```

### Cập nhật

```http
PUT /api/sinhvien/{id}
```

### Xóa

```http
DELETE /api/sinhvien/{id}
```

---

# 9. DeTai Service

Base URL:

```text
http://localhost:8082/api/detai
```

Các API:

```text
GET     /api/detai
GET     /api/detai/{id}
POST    /api/detai
PUT     /api/detai/{id}
DELETE  /api/detai/{id}
```

Ví dụ JSON:

```json
{
    "maDeTai": 1,
    "tenDeTai": "Xay dung he thong quan ly do an"
}
```

Các field cần thay đổi theo CSDL thực tế.

---

# 10. DangKy Service

Base URL:

```text
http://localhost:8083/api/dangky
```

Các API:

```text
GET     /api/dangky
GET     /api/dangky/{id}
POST    /api/dangky
PUT     /api/dangky/{id}
DELETE  /api/dangky/{id}
```

Ví dụ đăng ký:

```json
{
    "maSV": 1,
    "maDeTai": 1
}
```

Khi tạo đăng ký, `DangKy Service` có thể thực hiện:

```text
Client
   |
   | POST /api/dangky
   v
DangKy Service
   |
   +---- HTTP GET ----> SinhVien Service
   |                    |
   |                    +-- Sinh viên tồn tại?
   |
   +---- HTTP GET ----> DeTai Service
                        |
                        +-- Đề tài tồn tại?
   |
   v
Tạo đăng ký
```

Đây là phần giúp minh họa rõ hơn việc **các service phối hợp với nhau qua HTTP/REST**.

---

# 11. Giao tiếp giữa các Service

Có thể sử dụng `RestClient` của Spring để gọi service khác.

Ví dụ:

```java
RestClient restClient = RestClient.create();

SinhVien sinhVien = restClient.get()
        .uri("http://localhost:8081/api/sinhvien/" + maSV)
        .retrieve()
        .body(SinhVien.class);
```

Tương tự với `DeTai Service`:

```text
http://localhost:8082/api/detai/{id}
```

Trong demo đơn giản, có thể dùng `RestClient` để minh họa rõ HTTP/REST.

---

# 12. Công nghệ sử dụng

```text
Java
Spring Boot
Spring Web
Spring Data JPA
Hibernate
Maven
REST API
JSON
MySQL / SQL Server / PostgreSQL
Postman
Git
```

Database driver phụ thuộc vào CSDL được giảng viên cung cấp.

Ví dụ MySQL:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

Spring Web:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Spring Data JPA:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

---

# 13. Cấu hình Port

Ba service phải chạy trên các port khác nhau.

## SinhVien Service

```properties
server.port=8081
```

## DeTai Service

```properties
server.port=8082
```

## DangKy Service

```properties
server.port=8083
```

Ví dụ:

```text
SinhVien Service -> 8081
DeTai Service    -> 8082
DangKy Service   -> 8083
```

Nhờ vậy các service có thể chạy đồng thời.

---

# 14. Cấu hình Database

Ví dụ với MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/soa_thesis
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Nếu database đã được cung cấp sẵn, nên dùng:

```properties
spring.jpa.hibernate.ddl-auto=none
```

hoặc:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

để tránh Spring Boot tự ý tạo hoặc thay đổi cấu trúc bảng.

---

# 15. CRUD

## Create

```text
POST
```

Tạo mới:

```text
Sinh viên
Đề tài
Đăng ký
```

## Read

```text
GET
```

Lấy:

```text
Danh sách
Chi tiết theo ID
```

## Update

```text
PUT
```

Cập nhật thông tin.

## Delete

```text
DELETE
```

Xóa dữ liệu.

Tổng quát:

```text
CREATE -> POST
READ   -> GET
UPDATE -> PUT
DELETE -> DELETE
```

---

# 16. Ví dụ luồng demo hoàn chỉnh

Một kịch bản demo trên lớp có thể thực hiện như sau.

### Bước 1: Khởi động database

```text
Database
    |
    +-- SINHVIEN
    +-- DETAI
    +-- DANGKY
```

### Bước 2: Khởi động 3 Spring Boot Service

```text
SinhVien Service -> localhost:8081
DeTai Service    -> localhost:8082
DangKy Service   -> localhost:8083
```

### Bước 3: Tạo sinh viên

```http
POST http://localhost:8081/api/sinhvien
```

### Bước 4: Tạo đề tài

```http
POST http://localhost:8082/api/detai
```

### Bước 5: Đăng ký đề tài

```http
POST http://localhost:8083/api/dangky
```

DangKy Service kiểm tra:

```text
Sinh viên có tồn tại?
        |
       YES
        |
Đề tài có tồn tại?
        |
       YES
        |
Tạo đăng ký
```

### Bước 6: Kiểm tra dữ liệu

```http
GET http://localhost:8081/api/sinhvien
GET http://localhost:8082/api/detai
GET http://localhost:8083/api/dangky
```

---

# 17. Minh họa đặc trưng SOA

## 17.1. Service độc lập

Mỗi service có:

```text
Controller
Service
Repository
Entity
Application
Port riêng
```

Ví dụ:

```text
SinhVien Service
        |
        +-- 8081

DeTai Service
        |
        +-- 8082

DangKy Service
        |
        +-- 8083
```

Một service có thể phát triển và chạy riêng.

---

## 17.2. Giao tiếp bằng giao thức chuẩn

Các service sử dụng:

```text
HTTP
REST
JSON
```

Ví dụ:

```text
DangKy Service
      |
      | HTTP GET
      v
SinhVien Service
```

---

## 17.3. Tái sử dụng

`SinhVien Service` không chỉ phục vụ màn hình quản lý sinh viên.

Các hệ thống khác cũng có thể gọi:

```http
GET /api/sinhvien/{id}
```

Ví dụ:

```text
DangKy Service
BaoCao Service
ThongKe Service
Frontend
```

đều có thể sử dụng SinhVien Service.

---

# 18. SOA và Spring Boot

Spring Boot đóng vai trò là framework để xây dựng các service.

```text
SOA
 |
 +-- Service độc lập
 |
 +-- Communication
 |
 +-- Reusability
 |
 +-- Loose Coupling
 |
 +-- Standard Protocol
```

Spring Boot hỗ trợ triển khai các đặc điểm trên bằng:

```text
Spring Web
     |
     +-- REST Controller
     +-- HTTP
     +-- JSON

Spring Data JPA
     |
     +-- Database access
```

Có thể hiểu đơn giản:

```text
SOA = Kiến trúc / cách tổ chức hệ thống

Spring Boot = Framework dùng để triển khai service
```

---

# 19. Vì sao không gom tất cả vào một Service?

Nếu gom tất cả vào:

```text
One Spring Boot Application
    |
    +-- SinhVien
    +-- DeTai
    +-- DangKy
```

thì vẫn có thể làm CRUD và REST API, nhưng khó thể hiện rõ ý tưởng **các dịch vụ độc lập** của SOA.

Với demo này:

```text
Application 1
SinhVien Service

Application 2
DeTai Service

Application 3
DangKy Service
```

sẽ dễ minh họa hơn khi thuyết trình.

---

# 20. Sequence Diagram khái quát

```text
Client          DangKy        SinhVien        DeTai       Database
  |                |              |             |             |
  | POST /dangky   |              |             |             |
  |--------------->|              |             |             |
  |                | GET /sv/1    |             |             |
  |                |------------->|             |             |
  |                |              |---- DB ---->|             |
  |                |              |<------------|             |
  |                |<-------------|             |             |
  |                | GET /detai/1 |             |             |
  |                |--------------------------->|             |
  |                |                            |--- DB ----->|
  |                |                            |<------------|
  |                |<---------------------------|             |
  |                |              |             |             |
  |                | INSERT DANGKY               |             |
  |                |----------------------------------------->|
  |                |<-----------------------------------------|
  |<---------------|              |             |             |
```

---

# 21. Kiểm thử bằng Postman

Có thể tạo Postman Collection:

```text
SOA Thesis Management
│
├── SinhVien
│   ├── GET All
│   ├── GET By ID
│   ├── POST
│   ├── PUT
│   └── DELETE
│
├── DeTai
│   ├── GET All
│   ├── GET By ID
│   ├── POST
│   ├── PUT
│   └── DELETE
│
└── DangKy
    ├── GET All
    ├── GET By ID
    ├── POST
    ├── PUT
    └── DELETE
```

---

# 22. Thứ tự xây dựng project

Nên thực hiện theo thứ tự:

```text
1. Chuẩn bị database
        ↓
2. Tạo SinhVien Service
        ↓
3. Mapping bảng SINHVIEN
        ↓
4. Làm CRUD SinhVien
        ↓
5. Test bằng Postman
        ↓
6. Tạo DeTai Service
        ↓
7. Mapping bảng DETAI
        ↓
8. Làm CRUD DeTai
        ↓
9. Test bằng Postman
        ↓
10. Tạo DangKy Service
        ↓
11. Mapping bảng DANGKY
        ↓
12. Làm CRUD DangKy
        ↓
13. Gọi SinhVien Service từ DangKy Service
        ↓
14. Gọi DeTai Service từ DangKy Service
        ↓
15. Test toàn bộ flow
```

---

# 23. Cách chạy project

## Bước 1

Clone project:

```bash
git clone <repository-url>
```

## Bước 2

Mở 3 project bằng IntelliJ IDEA.

## Bước 3

Kiểm tra database connection trong:

```text
application.properties
```

## Bước 4

Chạy:

```text
SinhVienApplication
DeTaiApplication
DangKyApplication
```

## Bước 5

Kiểm tra port:

```text
8081
8082
8083
```

## Bước 6

Mở Postman và thực hiện CRUD.

---

# 24. Kết quả cần đạt khi demo

Sau khi hoàn thành, project cần thể hiện được:

- [x] Spring Boot REST API.
- [x] Kết nối CSDL có sẵn.
- [x] CRUD SINHVIEN.
- [x] CRUD DETAI.
- [x] CRUD DANGKY.
- [x] Các service chạy độc lập.
- [x] Mỗi service có API riêng.
- [x] Service giao tiếp bằng HTTP/REST.
- [x] Dữ liệu trao đổi bằng JSON.
- [x] Có thể tái sử dụng service.
- [x] Có thể demo bằng Postman.

---

# 25. Nội dung thuyết trình ngắn

Có thể trình bày:

> "Project của em xây dựng hệ thống quản lý đồ án tốt nghiệp theo kiến trúc SOA bằng Spring Boot. Hệ thống được chia thành ba service chính là SinhVien Service, DeTai Service và DangKy Service. Mỗi service có trách nhiệm riêng và chạy trên một port khác nhau. Các service giao tiếp với nhau thông qua HTTP/REST và trao đổi dữ liệu dưới dạng JSON. Mỗi service thực hiện các chức năng CRUD đối với dữ liệu tương ứng trong cơ sở dữ liệu. Đặc biệt, DangKy Service có thể gọi SinhVien Service và DeTai Service để kiểm tra dữ liệu trước khi tạo đăng ký. Qua đó project minh họa các đặc trưng cơ bản của SOA như tính độc lập của service, giao tiếp thông qua giao thức chuẩn và khả năng tái sử dụng service."

---

# 26. Lưu ý quan trọng

Project này là **demo SOA đơn giản phục vụ mục đích học tập**.

Không cần triển khai các thành phần phức tạp như:

```text
Service Registry
API Gateway
Message Broker
Docker
Kubernetes
Authentication Server
Circuit Breaker
```

nếu đề bài chỉ yêu cầu:

```text
SOA
+
HTTP/REST
+
CRUD
+
Database
```

Có thể bổ sung các thành phần trên nếu giảng viên yêu cầu mở rộng.

---

# 27. Tóm tắt kiến trúc

```text
                         CLIENT
                            |
                     HTTP / REST / JSON
                            |
          +-----------------+-----------------+
          |                 |                 |
          v                 v                 v
   SINHVIEN SERVICE   DETAI SERVICE    DANGKY SERVICE
        :8081              :8082             :8083
          |                 |                 |
          v                 v                 v
      SINHVIEN            DETAI             DANGKY
          \                 |                 /
           \________________|________________/
                            |
                         DATABASE
```

**Ý tưởng chính:**

```text
SOA
 ↓
Chia hệ thống thành các service
 ↓
Mỗi service có trách nhiệm riêng
 ↓
Service giao tiếp qua HTTP/REST
 ↓
Service có thể được tái sử dụng
 ↓
Thực hiện CRUD trên dữ liệu
```
