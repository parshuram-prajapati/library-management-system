document.addEventListener("DOMContentLoaded", () => {
    loadStudents();

    document.getElementById("addStudentForm")
        .addEventListener("submit", addStudent);

    document.getElementById("searchInput")
        .addEventListener("input", applySearch);
});

let allStudents = [];
let currentView = [];
let currentSort = { field: null, asc: true };

/* ================= LOAD STUDENTS ================= */
function loadStudents() {
    fetch("/api/students")
        .then(res => res.json())
        .then(data => {
            allStudents = data;
            currentView = [...data];
            renderStudents(currentView);
        });
}

/* ================= RENDER ================= */
function renderStudents(students) {
    const tbody = document.getElementById("studentsTableBody");

    if (!students.length) {
        tbody.innerHTML = `
          <tr>
            <td colspan="3" class="text-center text-muted py-4">
              No members found
            </td>
          </tr>`;
        return;
    }

    tbody.innerHTML = students.map(s => `
        <tr onclick="showMemberDetails('${s.id}')">
            <td class="fw-semibold">#${s.id}</td>
            <td>${s.name}</td>
            <td>${s.email}</td>
        </tr>
    `).join("");
}

/* ================= SEARCH ================= */
function applySearch(e) {
    const q = e.target.value.toLowerCase();

    currentView = allStudents.filter(s =>
        s.id.toLowerCase().includes(q) ||
        s.name.toLowerCase().includes(q) ||
        s.email.toLowerCase().includes(q)
    );

    renderStudents(currentView);
}

/* ================= SORTING ================= */
function sortBy(field) {
    if (currentSort.field === field) {
        currentSort.asc = !currentSort.asc;
    } else {
        currentSort.field = field;
        currentSort.asc = true;
    }

    currentView.sort((a, b) => {
        let x = a[field].toLowerCase();
        let y = b[field].toLowerCase();
        if (x < y) return currentSort.asc ? -1 : 1;
        if (x > y) return currentSort.asc ? 1 : -1;
        return 0;
    });

    updateSortIcons(field);
    renderStudents(currentView);
}

function updateSortIcons(active) {
    ["id", "name", "email"].forEach(f => {
        const el = document.getElementById(`sort-${f}`);
        if (!el) return;
        el.innerText =
            f === active ? (currentSort.asc ? "↑" : "↓") : "⇅";
    });
}

/* ================= MEMBER DETAIL CARD ================= */
async function showMemberDetails(studentId) {
    const student = allStudents.find(s => s.id === studentId);
    if (!student) return;

    document.getElementById("memberDetailCard").classList.remove("d-none");

    document.getElementById("mdName").innerText = student.name;
    document.getElementById("mdId").innerText = student.id;
    document.getElementById("mdEmail").innerText = student.email;

    const issues = await fetch("/api/issues").then(r => r.json());
    const myIssues = issues.filter(i => i.studentId === studentId);

    const tbody = document.getElementById("mdBooksBody");

    if (!myIssues.length) {
        tbody.innerHTML = `
          <tr>
            <td colspan="5" class="text-center text-muted">
              No books issued
            </td>
          </tr>`;
        return;
    }

    tbody.innerHTML = myIssues.map(i => {
        const overdue = !i.returnDate && new Date(i.dueDate) < new Date();
        const fine = overdue ? calculateFine(i.dueDate) : 0;

        return `
          <tr>
            <td>${i.bookTitle}</td>
            <td>${i.issueDate}</td>
            <td>${i.dueDate}</td>
            <td class="${fine ? 'text-danger fw-bold' : ''}">₹${fine}</td>
            <td>
              <span class="badge ${
                i.returnDate ? 'bg-info' :
                overdue ? 'bg-danger' : 'bg-success'
              }">
                ${i.returnDate ? 'Returned' : overdue ? 'Overdue' : 'Issued'}
              </span>
            </td>
          </tr>`;
    }).join("");
}

/* ================= FINE ================= */
function calculateFine(dueDate) {
    const days =
        Math.floor((new Date() - new Date(dueDate)) / (1000 * 60 * 60 * 24));
    return days > 0 ? days * 2 : 0;
}

/* ================= CLOSE CARD ================= */
function closeMemberCard() {
    document.getElementById("memberDetailCard").classList.add("d-none");
}

/* ================= ADD MEMBER ================= */
function addStudent(e) {
    e.preventDefault();
    const msg = document.getElementById("studentFormMsg");

    fetch("/api/students", {
        method: "POST",
        body: new FormData(e.target)
    })
    .then(res => res.text())
    .then(text => {
        msg.innerText = text;
        msg.className = "text-success small";
        e.target.reset();
        loadStudents();
    });
}
