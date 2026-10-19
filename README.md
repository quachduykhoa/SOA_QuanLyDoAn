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
docker compose up -d --build
```
> ⚠️ **Quan trọng:** Phải dùng `--build` để Docker **build lại image từ file .jar mới nhất**. Nếu chỉ chạy `docker compose up -d`, container sẽ dùng lại image cũ (chứa code cũ) → các thay đổi trong source code **không có hiệu lực**.

Sau khi lệnh chạy xong, hệ thống đã sẵn sàng tiếp nhận request tại cổng tập trung:
> **Cổng truy cập chính (API Gateway):** `http://localhost:8080`

> 💡 **Mẹo:** Mỗi lần sửa code Java, hãy chạy lại cả 2 lệnh theo đúng thứ tự: `.\mvnw.cmd clean package -DskipTests` rồi `docker compose up -d --build`.

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
  - **Quy tắc nghiệp vụ:** Mỗi sinh viên **chỉ được đăng ký tối đa 1 đề tài**. Nếu sinh viên đã từng đăng ký rồi mà thử đăng ký thêm đề tài khác → `400 Bad Request`.

- **Bước 4: Xem danh sách kết quả đăng ký**
  - `GET http://localhost:8080/api/v1/dangky`
  - Kết quả: `200 OK` hiển thị danh sách các lượt đăng ký thành công.

#### Kịch bản kiểm thử ngoại lệ (Validation liên dịch vụ):

- **Ngoại lệ 1: Mã sinh viên không tồn tại**
  - Gửi `POST http://localhost:8080/api/v1/dangky` với mã sinh viên không tồn tại (`SV999`):
  ```json
  {
    "maSv": "SV999",
    "maDeTai": "DT001",
    "ghiChu": "Thử đăng ký mã sai"
  }
  ```
  - Kết quả: `400 Bad Request` kèm thông báo lỗi: `"Không tìm thấy sinh viên với mã: SV999"`.

- **Ngoại lệ 2: Sinh viên đã đăng ký đề tài khác rồi** (giả sử `SV001` đã đăng ký `DT001`, giờ thử đăng ký `DT002`):
  ```json
  {
    "maSv": "SV001",
    "maDeTai": "DT002",
    "ghiChu": "Thử đăng ký đề tài thứ 2"
  }
  ```
  - Kết quả: `400 Bad Request` kèm thông báo lỗi: `"Sinh viên SV001 đã đăng ký đề tài DT001 rồi. Mỗi sinh viên chỉ được đăng ký tối đa 1 đề tài."`.

- **Ngoại lệ 3: Đề tài đã hết chỗ**
  - Gửi đăng ký với đề tài đã đủ số lượng người đăng ký (ví dụ `soLuongToiDa = 1` và đã có 1 SV đăng ký):
  - Kết quả: `400 Bad Request` kèm thông báo lỗi: `"Đề tài DT001 đã hết chỗ"`.

---

### 4.3. Kiểm thử Race Condition (Đăng ký Đồng thời qua DevTools Console)

Hệ thống chống race condition bằng **3 lớp phòng thủ**:

1. **Atomic slot increment** — Câu SQL nguyên tử tại `detai-service`:
   `UPDATE detai SET so_luong_da_dang_ky = so_luong_da_dang_ky + 1 WHERE ma_de_tai = ? AND so_luong_da_dang_ky < so_luong_toi_da`
   → Chỉ 1 request giành được slot cuối cùng, request còn lại nhận về 0 hàng bị ảnh hưởng → `400 Bad Request`.
2. **Unique constraint `ma_sv`** trên bảng `dangky` — mỗi sinh viên chỉ có tối đa 1 bản ghi đăng ký, kể cả khi gửi đồng thời nhiều request.
3. **Compensating transaction** — Nếu tăng slot thành công nhưng lưu đăng ký thất bại (ví dụ vi phạm unique constraint), hệ thống tự gọi `DECREASE` hoàn slot để dữ liệu 2 database không bị lệch.

#### Chuẩn bị dữ liệu (chỉ cần 1 lần, thực hiện trên `frontend/index.html`):

1. Tab **Sinh Viên**: Thêm sinh viên `SV001` và `SV002` (trạng thái mặc định `DANG_HOC`).
2. Tab **Đề Tài**: Thêm 2 đề tài `DT001` và `DT002` (mỗi đề tài mặc định `soLuongToiDa = 1`).
3. Đảm bảo hệ thống đang chạy với **code mới nhất** (`docker compose up -d --build`).

> 📌 **Cách mở Console:** Mở trang `frontend/index.html` trên trình duyệt → bấm **F12** → chọn tab **Console** → dán code bên dưới → bấm **Enter**.

#### Test A: 2 Sinh viên khác nhau cùng đăng ký 1 đề tài chỉ còn 1 chỗ

Mục tiêu: Chỉ 1 trong 2 sinh viên giành được slot cuối cùng.

```javascript
Promise.all([
  fetch('http://localhost:8080/api/v1/dangky', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({maSv: 'SV001', maDeTai: 'DT001', ghiChu: 'test race condition'})
  }).then(r => r.json()).then(d => ({sv: 'SV001', ...d})),

  fetch('http://localhost:8080/api/v1/dangky', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({maSv: 'SV002', maDeTai: 'DT001', ghiChu: 'test race condition'})
  }).then(r => r.json()).then(d => ({sv: 'SV002', ...d}))
]).then(results => {
  results.forEach(r => console.log(r.sv, '=>', r.success ? 'THANH CONG' : 'THAT BAI', '-', r.message));
});
```

**Kết quả mong đợi** (1 dòng THANH CONG, 1 dòng THAT BAI — thứ tự có thể đảo tùy request nào đến trước):

```
SV001 => THANH CONG - Đăng ký đề tài tốt nghiệp thành công!
SV002 => THAT BAI - Đề tài DT001 đã hết chỗ
```

**Xác minh:** Vào tab **Đề Tài** → bấm **Tải lại** → cột **Slot** của `DT001` hiển thị `1/1 (HẾT)` màu đỏ.

#### Test B: Cùng 1 Sinh viên gửi 2 request đồng thời cho 2 đề tài khác nhau

Mục tiêu: Xác nhận quy tắc **mỗi sinh viên chỉ được đăng ký tối đa 1 đề tài**, kể cả khi gửi request đồng thời.

```javascript
Promise.all([
  fetch('http://localhost:8080/api/v1/dangky', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({maSv: 'SV001', maDeTai: 'DT001', ghiChu: 'test SV dang ky 2 de tai'})
  }).then(r => r.json()).then(d => ({dt: 'DT001', ...d})),

  fetch('http://localhost:8080/api/v1/dangky', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({maSv: 'SV001', maDeTai: 'DT002', ghiChu: 'test SV dang ky 2 de tai'})
  }).then(r => r.json()).then(d => ({dt: 'DT002', ...d}))
]).then(results => {
  results.forEach(r => console.log(r.dt, '=>', r.success ? 'THANH CONG' : 'THAT BAI', '-', r.message));
});
```

**Kết quả mong đợi:** Chỉ 1 trong 2 đề tài được đăng ký thành công:

```
DT001 => THANH CONG - Đăng ký đề tài tốt nghiệp thành công!
DT002 => THAT BAI - Sinh viên SV001 đã đăng ký đề tài DT001 rồi. Mỗi sinh viên chỉ được đăng ký tối đa 1 đề tài.
```

**Cơ chế hoạt động:**
- Request nhanh hơn: vượt qua kiểm tra nghiệp vụ → lưu thành công → tăng slot.
- Request chậm hơn bị chặn ở 1 trong 2 lớp:
  - **Lớp 1 — Kiểm tra nghiệp vụ:** Phát hiện SV đã có đăng ký → trả `400` kèm message rõ ràng.
  - **Lớp 2 — Unique constraint `ma_sv` ở DB:** Nếu cả 2 request cùng vượt qua kiểm tra nghiệp vụ (race condition), request thứ 2 sẽ bị MySQL từ chối khi `save` → gọi `DECREASE` hoàn slot → trả `400`.

#### Dọn dữ liệu giữa các lần test (quan trọng!)

Sau mỗi lần test race condition, cần reset dữ liệu trước khi chạy lần tiếp theo:

```cmd
docker exec -it soa-mysql mysql -uroot -proot -e "USE dangky_db; DELETE FROM dangky; USE detai_db; UPDATE detai SET so_luong_da_dang_ky = 0;"
```

Hoặc dọn trên giao diện: Tab **Đăng Ký Đồ Án** → bấm **Hủy ĐK** từng bản ghi (mỗi lần hủy hệ thống tự gọi `DECREASE` hoàn slot trên đề tài).

> ⚠️ **Lưu ý quan trọng:** Mọi thay đổi code Java đều yêu cầu build + deploy lại (`docker compose up -d --build`) trước khi test — xem lại mục 3. Nếu không, hệ thống sẽ chạy code cũ và kết quả test race condition sẽ sai lệch.
