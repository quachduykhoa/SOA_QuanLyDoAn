# Hệ thống Quản lý Đồ án Tốt nghiệp (SOA Monorepo)

Hệ thống quản lý đồ án tốt nghiệp xây dựng theo Kiến trúc Hướng Dịch vụ (SOA) với Spring Boot 3, Spring Cloud Gateway, OpenFeign và MySQL 8.0.

---

## 1. Cấu trúc Dịch vụ & Cổng mạng (Ports)

| Dịch vụ | Thư mục Module | Cổng (Port) | Cơ sở dữ liệu | Vai trò |
| :--- | :--- | :---: | :--- | :--- |
| **API Gateway** | `api-gateway/` | `8080` | - | Cổng tập trung, tiếp nhận toàn bộ request |
| **Sinh Viên Service** | `sinhvien-service/` | `8081` | `sinhvien_db` | CRUD thông tin sinh viên |
| **Đề Tài Service** | `detai-service/` | `8082` | `detai_db` | CRUD danh mục đề tài |
| **Đăng Ký Service** | `dangky-service/` | `8083` | `dangky_db` | CRUD đăng ký, xác thực qua OpenFeign |
| **MySQL Database** | - | `3307` / `3306` | 3 DB độc lập | Lưu trữ dữ liệu |

---

## 2. Yêu cầu Môi trường

Trước khi chạy, máy tính cần chuẩn bị:

1. **Java Development Kit (JDK 21) - Bắt buộc:**
   - Kiểm tra bằng lệnh:
     ```cmd
     java -version
     ```
   - Yêu cầu kết quả hiển thị phiên bản `21` (ví dụ: `openjdk version "21.x.x"`).
2. **Docker Desktop - Bắt buộc khi chạy hệ thống:**
   - Kiểm tra bằng lệnh:
     ```cmd
     docker compose version
     ```

> 💡 **Lưu ý:** Không cần cài đặt Maven rời, dự án đã tích hợp sẵn **Maven Wrapper** (`.\mvnw.cmd`).

---

## 3. Hướng dẫn Khởi chạy Hệ thống (Bằng Docker)

Hệ thống được đóng gói và vận hành đồng bộ cả 5 containers (MySQL 8.0, 3 services nghiệp vụ và API Gateway) qua 2 bước:

### Bước 1: Đóng gói mã nguồn thành file .jar
Mở cửa sổ Terminal tại thư mục gốc dự án và chạy:
```cmd
.\mvnw.cmd clean package -DskipTests
```
*(Quá trình đóng gói diễn ra trong khoảng 5–10 giây).*

### Bước 2: Khởi chạy toàn bộ hệ thống
```cmd
docker compose up -d
```
Sau khi lệnh chạy xong, hệ thống đã sẵn sàng tiếp nhận request tại cổng tập trung:
> **Cổng truy cập chính (API Gateway):** `http://localhost:8080`

### Các lệnh quản trị container:
```cmd
# Xem trạng thái hoạt động của 5 container
docker compose ps

# Xem log hệ thống thời gian thực
docker compose logs -f

# Dừng và tắt toàn bộ container sau khi demo xong
docker compose down
```

---

## 4. Kiểm thử Hệ thống (Testing & Verification)

### 4.1. Kiểm thử Tự động (Unit & Slice Tests với Maven)
Dùng để kiểm tra nhanh tính toàn vẹn và các quy tắc nghiệp vụ trong code. Các bài test chạy hoàn toàn in-memory trên RAM (khoảng 10 giây), không cần bật Docker hay MySQL.

- **Chạy toàn bộ 33 bài test của hệ thống:**
  ```cmd
  .\mvnw.cmd test
  ```
- **Chạy kiểm thử riêng từng module:**
  ```cmd
  .\mvnw.cmd -pl sinhvien-service test
  .\mvnw.cmd -pl detai-service test
  .\mvnw.cmd -pl dangky-service test
  ```

---

### 4.2. Kiểm thử Trực quan qua Postman (Dành cho Báo cáo Giảng viên)

Bộ sưu tập kiểm thử API được cấu hình sẵn tại:
`postman/SOA_Graduation_Thesis.postman_collection.json`

#### Cách nạp (Import) vào Postman:
1. Mở **Postman** $\rightarrow$ Bấm nút **Import** ở góc trên bên trái.
2. Chọn tệp `postman/SOA_Graduation_Thesis.postman_collection.json`.
3. Sử dụng thư mục **`4. API Gateway (Port 8080)`** để demo các request tập trung.

#### Kịch bản Demo 4 bước chuẩn qua Gateway (Port 8080):

- **Bước 1: Thêm mới một Sinh viên**
  - `POST http://localhost:8080/api/v1/sinhvien`
  - Body:
    ```json
    {
      "maSv": "SV001",
      "hoTen": "Nguyễn Văn A",
      "ngaySinh": "2002-05-20",
      "queQuan": "Quy Nhơn",
      "chuyenNganh": "Công nghệ thông tin",
      "gpa": 3.5,
      "trangThai": "DANG_HOC"
    }
    ```
  - Kết quả: `201 Created` (Dữ liệu đã lưu vào `sinhvien_db`).

- **Bước 2: Thêm mới một Đề tài**
  - `POST http://localhost:8080/api/v1/detai`
  - Body:
    ```json
    {
      "maDeTai": "DT001",
      "tenDeTai": "Xây dựng hệ thống quản lý đồ án theo kiến trúc SOA",
      "moTa": "Nghiên cứu kiến trúc hướng dịch vụ với Spring Boot và Docker",
      "gvhd": "TS. Trần Văn B"
    }
    ```
  - Kết quả: `201 Created` (Dữ liệu đã lưu vào `detai_db`).

- **Bước 3: Đăng ký đề tài (Trọng tâm phối hợp liên dịch vụ SOA)**
  - `POST http://localhost:8080/api/v1/dangky`
  - Body:
    ```json
    {
      "maSv": "SV001",
      "maDeTai": "DT001",
      "ghiChu": "Đăng ký đề tài tốt nghiệp đợt 1"
    }
    ```
  - Kết quả: `201 Created` (Ghi nhận đăng ký vào `dangky_db`).
  - *Giải thích cho giảng viên:* `dangky-service` tự động gọi OpenFeign sang `sinhvien-service:8081` và `detai-service:8082` để xác thực sinh viên và đề tài tồn tại trước khi lưu vào CSDL.

- **Bước 4: Xem danh sách kết quả đăng ký**
  - `GET http://localhost:8080/api/v1/dangky`
  - Kết quả: `200 OK` hiển thị danh sách các lượt đăng ký thành công.

#### Kịch bản kiểm thử ngoại lệ (Validation liên dịch vụ):
- Gửi `POST http://localhost:8080/api/v1/dangky` với mã sinh viên không tồn tại (`SV999`):
  ```json
  {
    "maSv": "SV999",
    "maDeTai": "DT001",
    "ghiChu": "Thử đăng ký mã sai"
  }
  ```
- Kết quả: `400 Bad Request` kèm thông báo lỗi: `"Không tìm thấy sinh viên với mã: SV999"`.
