document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;
    const msgEl = document.getElementById("loginMsg");

    if (!username || !password) {
        msgEl.textContent = "❌ Please enter both username and password.";
        return;
    }

    try {
        const res = await fetch("/apilage-banking/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify({
                // username: "admin",
                // password: "adminpasswordhash"
                username: username,
                password: password
            })
        });

        const data = await res.json();

        if (res.ok) {
            const token = data.token;
            const roles = data.roles;

            // Store JWT in localStorage (note: consider using secure cookies in production)
            localStorage.setItem("jwt", token);
            localStorage.setItem("username", data.username);
            localStorage.setItem("roles", JSON.stringify(roles));

            // Redirect based on roles
            if (roles && roles.includes("ADMIN")) {
                window.location.href = "/apilagebanking/admin-dashboard.html";
            } else {
                window.location.href = "/apilagebanking/user-dashboard.html";
            }

        } else {
            msgEl.textContent = `❌ ${data.error || "Login failed"}`;
        }

    } catch (error) {
        console.error("Login error:", error);
        msgEl.textContent = "❌ Network error. Please try again.";
    }
});

// Function to check if user is logged in (utility function)
function isLoggedIn() {
    const token = localStorage.getItem("jwt");
    return token !== null;
}

// Function to get auth header for API calls
function getAuthHeader() {
    const token = localStorage.getItem("jwt");
    return token ? {"Authorization": `Bearer ${token}`} : {};
}