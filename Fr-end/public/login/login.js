let isLoggedIn = localStorage.getItem('user_role');
if(isLoggedIn) {
    if (isLoggedIn = 2) {
        window.location.pathname = "../employees/reimbursements-page.html";
    } else {
        window.location.pathname = "../managers/manager-page.html";
    }
}


let usernameText = document.querySelector("#username");
let passwordText = document.querySelector("#password");

usernameText.setAttribute("onKeyDown", "clearErrorMessage()");
passwordText.setAttribute("onKeyDown", "clearErrorMessage()");

let loginBtn = document.querySelector('#login-btn');

loginBtn.addEventListener('click', async () => {
    let usernameInput = document.querySelector("#username");
    let passwordInput = document.querySelector("#password");

    const URL = 'http://localhost:8080/login';


    const jsonString = JSON.stringify({
        "username": usernameInput.value,
        "password": passwordInput.value
    });

    let res = await fetch(URL, {
        method: 'POST',
        body: jsonString,
    });

    let token = res.headers.get('Token');
    localStorage.setItem('jwt', token);

    if (res.status === 200) { 
        let user = await res.json();

        localStorage.setItem('user_id', user.id); 
        localStorage.setItem('user_role', user.userRole.id);
        localStorage.setItem('user_name', `${user.firstName} ${user.lastName}`)
    
        if (user.userRole.id === 1) {
            window.location.pathname = '../managers/manager-page.html';
        } else if (user.userRole.id === 2) {
            window.location.pathname = '../employees/reimbursements-page.html';
        }
    } else {
        let errorMsg = await res.text();
        console.log(errorMsg);

        let errorElement = document.querySelector('#error-msg');
        errorElement.innerText = errorMsg;
        errorElement.style.color = 'red';
    }
});

function clearErrorMessage() {
    let errorElement = document.querySelector('#error-msg');
    if (errorElement.innerText) {
        errorElement.innerText = '';
    }
}