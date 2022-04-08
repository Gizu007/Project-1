let logoutBtn = document.querySelector("#logout-btn");

logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('user_role');
    localStorage.removeItem('user_name');
    localStorage.removeItem('jwt');

    window.location = '../index.html';
});

let welcomeText = document.querySelector('#welcome-tag');
welcomeText.innerText = `Welcome, ${localStorage.getItem('user_name')}!`;
window.addEventListener('load', (event) => {
    populateReimbursementsTable();
});

let title = document.querySelector('title');
title.innerText = `Tickets | ${localStorage.getItem('user_name').split(' ')[0]}`;

let filterBtn = document.querySelector("#filter-status");
filterBtn.addEventListener('click', populateReimbursementsTable);

let pageLink = document.querySelector(".reimb-page-link a");
if (localStorage.getItem('user_role') == 1) {
    pageLink.innerText = 'Manage Reimbursements';
    pageLink.setAttribute('href', '../managers/manager-page.html');
}

async function populateReimbursementsTable() {
    let filter = document.querySelector("#ticket-filter").value;
    let URL;
    if (filter === "Any") {
         URL = `http://localhost:8080/users/${localStorage.getItem('user_id')}/reimbursements`;
    } else if (filter === "Pending") {
        URL = `http://localhost:8080/users/${localStorage.getItem('user_id')}/reimbursements?status=Pending`;
    } else if (filter === "Approved") {
        URL = `http://localhost:8080/users/${localStorage.getItem('user_id')}/reimbursements?status=Approved`;
    } else if (filter === "Rejected") {
        URL = `http://localhost:8080/users/${localStorage.getItem('user_id')}/reimbursements?status=Rejected`;
    }
    

    let res = await fetch(URL, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwt')}` // Send over the JWT
        }

    });

    if (res.status === 200) {
        let reimbursements = await res.json();

        let tbody = document.querySelector('#reimbursement-tbl > tbody');
        tbody.innerHTML = ''; // Clear the body on reloads

        for (let ticket of reimbursements) {
            let tr = document.createElement('tr');

            let td1 = document.createElement('td');
            if (ticket.status === "Pending") {
                td1.innerText = "Pending";
            } else if (ticket.staus === "Approved") {
                td1.innerText = "Approved";
                td1.setAttribute("color", "green");
            } else {
                td1.innerText = "Denied";
            }

            let td2 = document.createElement('td');
            td2.innerText = ticket.submitTimestamp;

            let td3 = document.createElement('td');
            td3.innerText = `$${ticket.amount}`;

            let td4 = document.createElement('td');
            td4.innerText = ticket.type;

            let td5 = document.createElement('td');
            td5.innerText = ticket.description;

            let td6 = document.createElement('td');
            let aUrl = document.createElement('a');
            aUrl.setAttribute("id", "detail-link");
            aUrl.setAttribute("href", "#");
            aUrl.innerText = "Details";
            aUrl.setAttribute('data-bs-toggle',"modal");
            aUrl.setAttribute('data-bs-target', "#ticket-modal");

            td6.appendChild(aUrl);

            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            tr.appendChild(td5);
            tr.appendChild(td6);

            tbody.appendChild(tr);

            aUrl.addEventListener('click', async() => {
                try {
                    let res2 = await fetch(ticket.urlDetails, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('jwt')}`,
                        }
                    });

                    if (res2.status === 200) {
                        let reimbursement = await res2.json();

                        openDetailsModal(reimbursement);
                    }

                } catch (e) {
                    console.log(e);
                }
            });
                
        }
    }
}

async function openDetailsModal(reimbursement) {
    let modal = document.querySelector("#ticket-modal");

    modal.style.display = "block";

    let status = document.querySelector(".status");
    status.innerText = reimbursement.status;

    let submitTime = document.querySelector(".submit-timestamp");
    submitTime.innerText = reimbursement.submitTimestamp;

    let resolveTime = document.querySelector(".resolved-timestamp");
    let resolverName = document.querySelector(".resolver-name");
    let resolverContact = document.querySelector(".resolver-contact");
    if (reimbursement.resolveTimestamp ) {
        const urlUser = `http://localhost:8080/users/${reimbursement.resolverId}`;

        let response = await fetch(urlUser, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwt')}` // Send over the JWT
            }

        });
        let userInfo = null;
        if (response.status === 200) {
            userInfo = await response.json();
        }
        resolveTime.innerText = reimbursement.resolveTimestamp;
        resolverName.innerText = `${userInfo.firstName} ${userInfo.lastName}`;
        resolverContact.innerText = userInfo.email;  
    } else {
        resolveTime.innerText = "---------------";
        resolverName.innerText = "---------------";
        resolverContact.innerText = "---------------";
    }


    let description = document.querySelector(".description");
    description.innerText = reimbursement.description;

    let type = document.querySelector(".type");
    type.innerText = reimbursement.type;

    let image = document.querySelector(".modal-content img");
    image.setAttribute("src", reimbursement.receiptUrl);
    let imageLink = document.querySelector("#image-link");
    imageLink.setAttribute('href', reimbursement.receiptUrl);

    let deleteButton = document.querySelector("#delete-btn");
    if (reimbursement.status === "Pending") {
        deleteButton.style.display = "block";
        deleteButton.disabled = false;
        deleteButton.addEventListener('click', function() {deleteTicket(reimbursement.id)});
    } else {
        deleteButton.style.display = "none";
        deleteButton.disabled = true;
    }
}

let ticketBtn = document.querySelector("#add-reimbursement-btn");
ticketBtn.addEventListener('click', openFormModal);
function openFormModal() {
    let submitBtn = document.querySelector("#submit-btn");
    submitBtn.disabled = false;
    let typeInput = document.querySelector("#ticket-type");
    let descriptionInput = document.querySelector("#textbox");
    let amountInput = document.querySelector("#amount-input");
    let imageInput = document.querySelector("#image-file");

    typeInput.value = "";
    descriptionInput.value = "";
    amountInput.value = 0;
    imageInput.value="";

    
    submitBtn.addEventListener('click', async () => {
         let errorMsg = document.querySelector("#error-msg");
        submitBtn.disabled = true;

        if (typeInput.value && descriptionInput.value && amountInput.value && imageInput.files[0]){
            let formData = new FormData();
            formData.append('type', typeInput.value);
            formData.append('description', descriptionInput.value);
            formData.append('amount', amountInput.value);
            formData.append('image', imageInput.files[0]);
            console.log(imageInput.files[0]);
        
            try {
                let res = await fetch(`http://localhost:8080/users/${localStorage.getItem('user_id')}/reimbursements`, {
    
                method: 'POST',
                body: formData,
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('jwt')}`
                }
            });
            populateReimbursementsTable();
            } catch (e) {
                console.log(e);
            }
        } else {
           errorMsg.innerText = 'You must fill out all fields to submit a request.';
           submitBtn.disabled = false;
        }

       
    });
}

async function deleteTicket(ticketId) {
    const url = `http://localhost:8080/users/${localStorage.getItem('user_id')}/reimbursements/${ticketId}`;

    let res = await fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwt')}`
        }
    });
    if (res.status === 200) {
        populateReimbursementsTable();
        let modal = document.querySelector("#ticket-modal");
    }

}