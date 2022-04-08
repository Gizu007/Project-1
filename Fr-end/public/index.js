window.addEventListener('load', adjustNavBarAndButtons);

function adjustNavBarAndButtons()  {
        let loginBtn = document.querySelector('#login-btn');
        if (localStorage.getItem('jwt')) {
            let loginNav = document.querySelector('.login-link')
            loginNav.innerText = 'Logout';
            loginNav.setAttribute('href', '#');
            loginNav.addEventListener('click', () => {
                localStorage.removeItem('user_role_id');
                localStorage.removeItem('user_id');
                localStorage.removeItem('jwt');

                window.location = '../index.html';
            });

            let welcomeText = document.querySelector(".welcome-text");
            let welcomeHeading = document.querySelector("h1");

            welcomeHeading.innerText = `Welcome, ${localStorage.getItem('user_name')}`;

            if (localStorage.getItem('user_role') < 300) {
                welcomeText.innerText = "Manage all company reimbursements.";
                loginBtn.innerText = "Manage Tickets";
                loginBtn.addEventListener('click', () => {
                    window.location = "../managers/manager-page.html";
                });

            } else {
                welcomeText.innerText = "See the status on your tickets and request a reimbursement.";
                loginBtn.innerText = "My Tickets";
                loginBtn.addEventListener('click', () => {
                    window.location = "./employees/reimbursements-page.html";
                });
            }
        } else {
            loginBtn.addEventListener('click', () => {
                window.location = "./login/login.html";
            });
        }
    }