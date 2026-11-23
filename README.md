# Xem CAD (Android)

Ứng dụng xem và chỉnh sửa nhẹ bản vẽ DXF trên Android. Giao diện là trang web nằm trong
`app/src/main/assets/index.html`, được bọc trong một WebView. Không cần mạng khi dùng.

## Build APK bằng GitHub (không cần cài Android Studio)

1. Tạo repository mới trên GitHub (để Private cũng được).
2. Tải toàn bộ nội dung thư mục này lên repository (nhánh `main`), giữ nguyên cả thư mục
   `.github` (đây là nơi chứa quy trình tự build).
3. Vào tab **Actions** của repository, chọn **Build APK**, bấm **Run workflow**
   (lần push đầu tiên cũng tự chạy).
4. Đợi khoảng 3 đến 6 phút. Khi có dấu tích xanh, bấm vào lần chạy đó, kéo xuống mục
   **Artifacts** và tải **XemCAD-debug-apk**. Giải nén sẽ có file `app-debug.apk`.
5. Chép file APK vào điện thoại, mở lên và cài. Android sẽ hỏi cho phép cài từ nguồn không
   rõ, hãy cho phép cho ứng dụng bạn dùng để mở file (Chrome, Files…).

## Phát hành bản có số phiên bản

Tạo tag bắt đầu bằng `v`, ví dụ `v1.0`:

    git tag v1.0
    git push origin v1.0

GitHub tự tạo mục **Releases** kèm file APK để tải.

## Ký bản release bằng khóa của bạn (tùy chọn)

Bản debug đã đủ để cài và dùng. Muốn ký bằng khóa riêng, tạo keystore rồi thêm 4 secret
trong **Settings → Secrets and variables → Actions**:

| Secret | Nội dung |
|---|---|
| `KEYSTORE_BASE64` | Nội dung file `.jks` đã mã hóa base64 (`base64 -w0 release.jks`) |
| `KEYSTORE_PASSWORD` | Mật khẩu keystore |
| `KEY_ALIAS` | Tên alias của khóa |
| `KEY_PASSWORD` | Mật khẩu của khóa |

Khi có `KEYSTORE_BASE64`, quy trình sẽ build thêm **XemCAD-release-apk**.

## Cập nhật giao diện

Sửa file `app/src/main/assets/index.html`, push lên GitHub, Actions sẽ build lại APK.

## Ghi chú

- Cần Android 7.0 trở lên (minSdk 24) và WebView/Chrome bản mới (từ khoảng bản 105) để hiển thị
  đúng thanh công cụ. Điện thoại tự cập nhật WebView qua Google Play.
- Nút **Mở file** dùng bộ chọn file của Android. File DXF không có kiểu MIME chuẩn nên bộ chọn
  hiển thị mọi loại file, bạn tự chọn file `.dxf`.
- Các thay đổi trên bản vẽ (thêm chữ, đổi màu, sao chép…) chỉ nằm trong bộ nhớ, chưa lưu ra
  file, và mất khi mở file khác hoặc thoát ứng dụng.

## Biểu tượng ứng dụng

- Icon thích ứng (Android 8 trở lên) nằm ở `res/drawable/ic_launcher_*.xml` và `res/mipmap-anydpi-v26`,
  có cả bản đơn sắc cho "biểu tượng theo chủ đề" của Android 13.
- Icon ảnh PNG cho máy cũ nằm ở `res/mipmap-*dpi`.
- Muốn đổi icon, thay các file này rồi push lại; Actions sẽ build APK mới.
