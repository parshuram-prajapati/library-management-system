document.addEventListener("DOMContentLoaded", () => {
    loadBooks();
    setupAddBookForm();
    setupSearch();
});

let allBooks = [];

/* ================= LOAD BOOKS ================= */
function loadBooks() {
    const tbody = document.getElementById("booksGrid");

    fetch("/api/books")
        .then(res => res.json())
        .then(data => {
            allBooks = data;
            renderBooks(data);
        })
        .catch(() => {
            tbody.innerHTML = `
              <tr>
                <td colspan="5" class="text-center text-danger py-4">
                  Failed to load books
                </td>
              </tr>`;
        });
}

/* ================= RENDER BOOKS ================= */
function renderBooks(books) {
    const tbody = document.getElementById("booksGrid");

    if (!books.length) {
        tbody.innerHTML = `
          <tr>
            <td colspan="5" class="text-center py-4 text-muted">
              No books found
            </td>
          </tr>`;
        return;
    }

    tbody.innerHTML = books.map(b => `
        <tr>
          <td class="ps-4 fw-semibold">#${b.id}</td>
          <td>${b.title}</td>
          <td>${b.author || "-"}</td>
          <td>
            <span class="badge ${b.issued ? 'bg-warning' : 'badge-avail'}">
              ${b.issued ? 'Issued' : 'Available'}
            </span>
          </td>
          <td class="text-end pe-4">
            <button class="btn btn-sm btn-outline-danger"
                    onclick="deleteBook('${b.id}')">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>
    `).join("");
}

/* ================= ADD BOOK ================= */
function setupAddBookForm() {
    const form = document.getElementById("addBookForm");
    const msg = document.getElementById("bookFormMsg");

    form.addEventListener("submit", e => {
        e.preventDefault();
        const formData = new FormData(form);

        fetch("/api/books", {
            method: "POST",
            body: formData
        })
        .then(res => res.text())
        .then(text => {
            msg.innerText = text;
            msg.className = "text-success small";
            form.reset();
            loadBooks();
        });
    });
}

/* ================= DELETE BOOK ================= */
function deleteBook(id) {
    if (!confirm("Delete this book?")) return;

    fetch(`/api/books/${id}`, { method: "DELETE" })
        .then(() => loadBooks());
}

/* ================= SEARCH ================= */
function setupSearch() {
    const input = document.getElementById("searchInput");

    input.addEventListener("keyup", () => {
        const term = input.value.toLowerCase();

        const filtered = allBooks.filter(b =>
            b.id.toLowerCase().includes(term) ||
            b.title.toLowerCase().includes(term) ||
            (b.author && b.author.toLowerCase().includes(term))
        );

        renderBooks(filtered);
    });
}
