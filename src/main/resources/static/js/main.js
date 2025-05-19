function handleLogout() {
    fetch('/api/v1/auth/logout', {
        method: 'POST',
        credentials: 'same-origin'
    })
        .then(() => {
            document.cookie = 'JWT=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
            window.location.href = '/';
        });
}

function handleLogin() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorDiv = document.getElementById('errorMessage');

    if (!errorDiv) {
        console.error('Error message element not found');
        return;
    }

    fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || 'Login failed');
                });
            }
            return response.json();
        })
        .then(data => {
            document.cookie = `JWT=${data.accessToken}; Path=/; Max-Age=${data.expiresIn}`;
            window.location.href = '/profile';
        })
        .catch(error => {
            errorDiv.textContent = error.message;
            errorDiv.classList.remove('hidden');
            setTimeout(() => {
                errorDiv.classList.add('hidden');
            }, 5000);
        });
}

function handleRegister() {
    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const errorDiv = document.getElementById('errorMessage');

    if (!username || !email || !password) {
        errorDiv.textContent = 'All fields are required';
        errorDiv.classList.remove('hidden');
        return;
    }

    fetch('/api/v1/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            email: email,
            password: password
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || 'Registration failed');
                });
            }
            return response.json();
        })
        .then(data => {
            return fetch('/api/v1/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            });
        })
        .then(response => {
            if (!response.ok) throw new Error('Auto-login failed');
            return response.json();
        })
        .then(data => {
            document.cookie = `JWT=${data.accessToken}; Path=/; Max-Age=${data.expiresIn}`;
            window.location.href = '/profile';
        })
        .catch(error => {
            errorDiv.textContent = error.message;
            errorDiv.classList.remove('hidden');
            setTimeout(() => errorDiv.classList.add('hidden'), 5000);
        });
}