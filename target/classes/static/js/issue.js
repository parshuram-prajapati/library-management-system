document.addEventListener("DOMContentLoaded", () => {
    loadIssuedBooks();

    const form = document.getElementById("issueBookForm");
    form.addEventListener("submit", issueBook);

    document.getElementById("searchInput")
        .addEventListener("input", searchIssuedBooks);
});

// ================= GLOBAL =================
let allIssuedBooks = [];

// ================= LOAD ISSUED BOOKS =================
function loadIssuedBooks() {
    fetch("/api/issues")
        .then(res => res.json())
        .then(issues => {
            // 🔥 ONLY ACTIVE ISSUES
            allIssuedBooks = issues.filter(i => !i.returnDate);
            renderIssuedBooks(allIssuedBooks);
        })
        .catch(err => console.error("Load issues failed:", err));
}

// ================= RENDER TABLE =================
function renderIssuedBooks(list) {
    const tbody = document.getElementById("issuedList");

    if (!list.length) {
        tbody.innerHTML = `
            <tr>
              <td colspan="4" class="text-center py-5 text-muted">
                No active issued books
              </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = "";

    list.forEach(issue => {
        tbody.innerHTML += `
            <tr>
              <td class="ps-4">
                <strong>${issue.bookTitle}</strong><br>
                <small class="text-muted">#${issue.bookId}</small>
              </td>

              <td>
                ${issue.studentName}<br>
                <small class="text-muted">${issue.studentId}</small>
              </td>

              <td class="text-success fw-semibold">
                ${issue.dueDate}
              </td>

              <td class="text-end pe-4">

                <!-- 🔔 REMINDER -->
                <button class="btn btn-warning btn-sm rounded-circle me-2 text-white"
                        title="Send Reminder"
                        onclick="sendReminder('${issue.bookId}')">
                  <i class="bi bi-bell-fill"></i>
                </button>

                <!-- ↩️ RETURN -->
                <button class="btn btn-outline-dark btn-sm"
                        onclick="returnBook('${issue.bookId}')">
                  Return
                </button>

              </td>
            </tr>`;
    });
}

// ================= SEARCH =================
function searchIssuedBooks(e) {
    const q = e.target.value.toLowerCase();

    const filtered = allIssuedBooks.filter(i =>
        i.bookTitle.toLowerCase().includes(q) ||
        i.bookId.toLowerCase().includes(q) ||
        i.studentName.toLowerCase().includes(q) ||
        i.studentId.toLowerCase().includes(q)
    );

    renderIssuedBooks(filtered);
}

// ================= ISSUE BOOK =================
function issueBook(e) {
    e.preventDefault();

    const studentId = document.getElementById("studentId").value.trim();
    const bookId = document.getElementById("bookId").value.trim();
    const msg = document.getElementById("issueMsg");

    if (!studentId || !bookId) {
        msg.innerText = "All fields are required";
        msg.className = "text-danger small";
        return;
    }

    const formData = new FormData();
    formData.append("studentId", studentId);
    formData.append("bookId", bookId);

    fetch("/api/issue", { method: "POST", body: formData })
        .then(res => res.text())
        .then(text => {
            msg.innerText = text;
            msg.className = "text-success small";
            document.getElementById("issueBookForm").reset();
            loadIssuedBooks();
        })
        .catch(() => {
            msg.innerText = "Failed to issue book";
            msg.className = "text-danger small";
        });
}

// ================= RETURN BOOK =================
function returnBook(bookId) {
    if (!confirm("Return this book?")) return;

    fetch(`/api/return/${bookId}`, { method: "POST" })
        .then(() => loadIssuedBooks());
}

// ================= SEND REMINDER =================
function sendReminder(bookId) {
    if (!confirm("Send reminder email to student?")) return;

    fetch(`/api/remind/${bookId}`, { method: "POST" })
        .then(res => res.text())
        .then(msg => alert(msg))
        .catch(() => alert("Failed to send reminder"));
}
