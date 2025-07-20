const jwt = localStorage.getItem('jwt');
if (!jwt) window.location.href = '/apilagebanking/login.html';

const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${jwt}`
};

// Utility to render into main content area
function render(html) {
    document.querySelector('main').innerHTML = html;
}

// Utility: Fetch JSON or show error
async function apiFetch(path, opts = {}) {
    const res = await fetch(`/apilage-banking/api/${path}`, {
        headers,
        ...opts
    });
    if (!res.ok) throw new Error(await res.text());
    return res.json();
}

/*** Admin Handlers ***/

async function showAdminDashboard() {
    try {
        const summary = await apiFetch('admin/reports/summary');
        render(`
      <h2 class="text-warning">System Summary</h2>
      <div>Total Users: ${summary.totalUsers}</div>
      <div>Total Transactions: ${summary.totalTransactions}</div>
    `);
    } catch (e) {
        render(`<div class="text-danger">Error: ${e.message}</div>`);
    }
}

async function showAllUsers() {
    try {
        const users = await apiFetch('admin/users');
        const rows = users.map(u => `
      <tr>
        <td>${u.id}</td>
        <td>${u.username}</td>
        <td>${u.email}</td>
        <td>${u.enabled}</td>
        <td>
          <button data-id="${u.id}" class="block-btn btn btn-sm btn-warning">${u.enabled ? 'Block' : 'Unblock'}</button>
        </td>
      </tr>
    `).join('');
        render(`
      <h2 class="text-warning">All Users</h2>
      <table class="table table-dark">
        <thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Enabled</th><th>Action</th></tr></thead>
        <tbody>${rows}</tbody>
      </table>
    `);
        document.querySelectorAll('.block-btn').forEach(btn => btn.addEventListener('click', async () => {
            await apiFetch(`admin/users/${btn.dataset.id}/block`, { method: 'PUT' });
            showAllUsers();
        }));
    } catch (e) {
        render(`<div class="text-danger">Error: ${e.message}</div>`);
    }
}

async function showCreateUserForm() {
    render(`
    <h2 class="text-warning">Add New User</h2>
    <form id="createUserForm">
      <input class="form-control mb-2" name="username" placeholder="Username" required>
      <input type="email" class="form-control mb-2" name="email" placeholder="Email" required>
      <input type="password" class="form-control mb-2" name="password" placeholder="Password" required>
      <select multiple class="form-select mb-2" name="roles">
        <option value="USER">USER</option>
        <option value="ADMIN">ADMIN</option>
      </select>
      <button class="btn btn-warning">Create User</button>
      <div id="formMsg" class="mt-2"></div>
    </form>
  `);
    document.querySelector('#createUserForm').addEventListener('submit', async e => {
        e.preventDefault();
        const data = {
            username: e.target.username.value,
            email: e.target.email.value,
            password: e.target.password.value,
            roles: [...e.target.roles.options].filter(o=>o.selected).map(o=>o.value)
        };
        try {
            await apiFetch('admin/create-user', { method: 'POST', body: JSON.stringify(data) });
            document.querySelector('#formMsg').textContent = 'User created!';
            e.target.reset();
        } catch (err) {
            document.querySelector('#formMsg').textContent = `Error: ${err.message}`;
        }
    });
}

// Other admin functions to implement similar patterns:
// showCreateAccount, showEditAccounts, showTransactions, showRollbackForm, showNewLoan…

/*** User Handlers ***/

async function showUserDashboard() {
    try {
        const summary = await apiFetch('accounts/my-summary');
        const recent = summary.recentTransactions || [];
        const rows = recent.map(t => `
      <tr><td>${t.type}</td><td>${t.amount}</td><td>${t.createdAt}</td></tr>
    `).join('');
        render(`
      <h2 class="text-warning">Your Account</h2>
      <div>Balance: ${summary.balance}</div>
      <h3 class="mt-3">Last Transactions</h3>
      <table class="table table-dark"><tbody>${rows}</tbody></table>
    `);
    } catch (e) {
        render(`<div class="text-danger">Error: ${e.message}</div>`);
    }
}

async function showAllUserTransactions() {
    try {
        const txns = await apiFetch('transactions/mine');
        const rows = txns.map(t => `<tr><td>${t.createdAt}</td><td>${t.type}</td><td>${t.amount}</td></tr>`).join('');
        render(`
      <h2 class="text-warning">All Transactions</h2>
      <table class="table table-dark"><tbody>${rows}</tbody></table>
    `);
    } catch (e) {
        render(`<div class="text-danger">Error: ${e.message}</div>`);
    }
}

async function showNewTransactionForm() {
    render(`
    <h2 class="text-warning">New Transaction</h2>
    <form id="txnForm">
      <input class="form-control mb-2" name="toAcc" placeholder="To Account" required>
      <input class="form-control mb-2" name="amount" type="number" step="0.01" placeholder="Amount" required>
      <button class="btn btn-warning">Send</button>
      <div id="txnMsg" class="mt-2"></div>
    </form>
  `);
    document.querySelector('#txnForm').addEventListener('submit', async e => {
        e.preventDefault();
        const body = {
            toAccount: e.target.toAcc.value,
            amount: e.target.amount.value
        };
        try {
            await apiFetch('transactions/transfer', { method: 'POST', body: JSON.stringify(body) });
            document.querySelector('#txnMsg').textContent = 'Transaction successful!';
            e.target.reset();
        } catch (err) {
            document.querySelector('#txnMsg').textContent = `Error: ${err.message}`;
        }
    });
}

// Similar for standing orders...

/*** Sidebar click listeners ***/

document.querySelectorAll('.sidebar a').forEach(link => {
    link.addEventListener('click', e => {
        e.preventDefault();
        const text = link.textContent.trim();
        if (text === 'Dashboard') showAdminDashboard();
        else if (text === 'View All Users') showAllUsers();
        else if (text === 'Add New User') showCreateUserForm();
        /*
        else if (...) other admin actions
        else if (text === 'Dashboard' && current user) showUserDashboard()
        else if ...
        */
    });
});

// Initial load
const isAdmin = JSON.parse(atob(jwt.split('.')[1])).roles.includes('ADMIN');
if (isAdmin) showAdminDashboard(); else showUserDashboard();
