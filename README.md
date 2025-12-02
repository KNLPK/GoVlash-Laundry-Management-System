# 🧺 GoVlash Laundry Management System

GoVlash Laundry is a comprehensive desktop application designed to modernize laundry service operations. Built using **Java 11** and **JavaFX**, this application implements the **MVC (Model-View-Controller)** architecture to ensure clean code structure and maintainability.

The system handles end-to-end laundry processes, from customer registration and order placement to task assignment and transaction completion.

## 🚀 Key Features

### 👤 User Roles & Access Control
The application supports 4 distinct roles with specific permissions:
* **Admin:** Manage services (CRUD), manage employees (Register Staff/Receptionist), and view all transactions.
* **Receptionist:** View pending orders and assign them to Laundry Staff.
* **Laundry Staff:** View assigned tasks and mark orders as "Finished".
* **Customer:** Register, place new laundry orders, view transaction history, and receive notifications.

### 🛠 Technical Highlights
* **Architecture:** Pure MVC (Model-View-Controller).
* **Database:** MySQL with Singleton Connection Pattern.
* **Validation:** Strict input validation without using Regex (Manual String manipulation logic).
* **ID Generation:** Custom manual ID generator (e.g., `US001`, `TR005`) without Auto-Increment.
* **Session Management:** Singleton UserSession to handle multi-role login.

## 💻 Tech Stack
* **Language:** Java (JDK 11)
* **GUI Framework:** JavaFX
* **Database:** MySQL (via XAMPP)
* **IDE:** Eclipse / IntelliJ IDEA

## ⚙️ How to Run

1.  **Clone the Repository**
    ```bash
    git clone [https://github.com/username-anda/GoVlash-Laundry-Management-System.git](https://github.com/username-anda/GoVlash-Laundry-Management-System.git)
    ```

2.  **Setup Database**
    * Open XAMPP and start **Apache** & **MySQL**.
    * Open `phpMyAdmin` and create a database named `go_vlash_laundry`.
    * Import the SQL query provided in `database/query.sql` (or creates tables manually based on the models).

3.  **Configure Connection**
    * Check `src/database/Connect.java`.
    * Ensure the username/password matches your XAMPP config (Default: root / empty).

4.  **Run in Eclipse/IDE**
    * Make sure JavaFX SDK 11 is configured in your library path.
    * Add VM Arguments if necessary:
        ```
       --module-path "E:\Others\openjfx-17.0.7_windows-x64_bin-sdk\javafx-sdk-17.0.7\lib" --add-modules javafx.controls
        ```
    * Run `main.Main` class (or `main.Launcher` if available).

## 📸 Screenshots
*(Optional: Anda bisa upload screenshot aplikasi di sini nanti)*

---
**Created by:** [Nama Anda] & [Nama Teman Kelompok]
**Course:** Object Oriented Analysis & Design (COMP6115)
