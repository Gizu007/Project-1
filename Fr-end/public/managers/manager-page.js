let logoutBtn = document.querySelector("#logout-btn");

logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('user_role');
    localStorage.removeItem('user_name');
    localStorage.removeItem('jwt');

    window.location = '../index.html';
});

let filterUser = document.querySelector('#user-filter');
filterUser.onchange = populateReimbursementsTable;

let filterStatus = document.querySelector('#status-filter');
filterStatus.onchange = populateReimbursementsTable;
let title = document.querySelector('title');
title.innerText = `Manage | ${localStorage.getItem('user_name').split(' ')[0]}`;

let welcomeText = document.querySelector('#welcome-tag');
welcomeText.innerText = `Welcome, ${localStorage.getItem('user_name')}!`;

window.addEventListener('load', (event) => {

    populateReimbursementsTable();
});

async function populateReimbursementsTable() {

    let filterUser = document.querySelector('#user-filter');

    let filterStatus = document.querySelector('#status-filter');
    
    let URL;


    if (filterStatus.value != "Any" && filterUser.value != "Any") {
        URL = `http://localhost:8080/users/${filterUser.value}/reimbursements?status=${filterStatus.value}`;
    } else if (filterUser.value != "Any") {
        URL = `http://localhost:8080/users/${filterUser.value}/reimbursements`;
    } else if (filterStatus.value != "Any") {
        URL = `http://localhost:8080/reimbursements?status=${filterStatus.value}`;
    } else {
    URL = `http://localhost:8080/reimbursements`;
    }

    let res = await fetch(URL, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwt')}` 
        }

    });

    if (res.status === 200) {
        let reimbursements = await res.json();

        let tbody = document.querySelector('#reimbursement-tbl > tbody');
        tbody.innerHTML = ''; 

        for (let ticket of reimbursements) {
            let tr = document.createElement('tr');

            let td1 = document.createElement('td');
            if (ticket.status == "Pending") {
                td1.innerText = "Pending";
            } else if (ticket.status == "Approved") {
                td1.innerText = "Approved";
                td1.setAttribute("color", "green");
            } else {
                td1.innerText = "Denied";
            }

            let td2 = document.createElement('td');
            td2.innerText = ticket.submitTimestamp;

            let td3 = document.createElement('td');
            td3.innerText = ticket.firstName;

            let td4 = document.createElement('td');
            td4.innerText = ticket.lastName;

            let td5 = document.createElement('td');
            td5.innerText = `$${ticket.amount}`;

            let td6 = document.createElement('td');
            td6.innerText = ticket.type;

            let td7 = document.createElement('td');
            td7.innerText = ticket.description;

            let td8 = document.createElement('td');
            let aUrl = document.createElement('a');
            aUrl.setAttribute("id", "detail-link");
            aUrl.setAttribute("href", "#");
            aUrl.innerText = "Details";
            aUrl.setAttribute('data-bs-toggle',"modal");
            aUrl.setAttribute('data-bs-target', "#ticket-modal");

            td8.appendChild(aUrl);

            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            tr.appendChild(td5);
            tr.appendChild(td6);
            tr.appendChild(td7);
            tr.appendChild(td8);

            tbody.appendChild(tr);

            aUrl.addEventListener('click', async() => {
                console.log(ticket.urlDetails)
                try {
                    let res2 = await fetch(ticket.urlDetails, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('jwt')}`,
                        }
                    });

                    if (res2.status === 200) {
                        let reimbursement = await res2.json();

                        openModal(reimbursement);
                    }

                } catch (e) {
                    console.log(e);
                }
            });
                
        }
    }

async function openModal(reimbursement) {
    let status = document.querySelector(".status");
    status.innerText = reimbursement.status;

    let submitTime = document.querySelector(".submit-timestamp");
    submitTime.innerText = reimbursement.submitTimestamp;

    let submitterName = document.querySelector(".submitter-name");
    submitterName.innerText = `${reimbursement.firstName} ${reimbursement.lastName}`
    let submitterContact = document.querySelector(".submitter-contact");
    submitterContact.innerText = reimbursement.email;


    let resolveTime = document.querySelector(".resolved-timestamp");
    let resolverName = document.querySelector(".resolver-name");
    let resolverContact = document.querySelector(".resolver-contact");
    if (reimbursement.resolveTimestamp) {
        try {
            const URL = `http://localhost:8080//users/${reimbursement.resolverId}`
            let res = await fetch(URL, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('jwt')}`
                }
            });
            if (res.status === 200) {
                let user = await res.json();
                resolverName.innerText = `${user.firstName} ${user.lastName}`;
                resolverContact.innerText = user.email;
            }
        } catch (e) {
            console.log(e);
        }
        resolveTime.innerText = reimbursement.resolveTimestamp;
       
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

    let approveBtn = document.querySelector("#approve-btn");
    let rejectBtn = document.querySelector("#reject-btn");
    if (reimbursement.status == "Pending" && (`${reimbursement.firstName} ${reimbursement.lastName}` != localStorage.getItem('user_name'))) {
        approveBtn.disabled = false;
        rejectBtn.disabled = false;
        approveBtn.style.display = "block";
        rejectBtn.style.display = "block";
        
        approveBtn.addEventListener('click', function() {updateRequestStatus(reimbursement.id, 2)});
        rejectBtn.addEventListener('click', function() {updateRequestStatus(reimbursement.id, 3)});

    } else {
        approveBtn.disabled = true;
        approveBtn.style.display = "none";
        rejectBtn.disabled = true;
        rejectBtn.style.display = "none"; 
    }
}

async function updateRequestStatus(reimbId, statusId) {
    const url = `http://localhost:8080/reimbursements/${reimbId}`;

    const jsonString = JSON.stringify({
        "resolverId": null,
        "statusId": statusId,
        "timestamp": null
    });

    try{
        let res = await fetch(url, {
        method: 'PATCH',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwt')}`
        },
        body: jsonString
        });
        if(res.status === 201) {
            let modal = document.querySelector("#ticket-modal");
            modal.style.display = "none";
            populateReimbursementsTable();
        }
    } catch(e) {
        console.log(e);
    }
}


    let selectSpanBox = document.querySelector("#user-filter-box");
    while (selectSpanBox.lastChild) {
        selectSpanBox.removeChild(selectSpanBox.lastChild);
    }
    let label = document.createElement('label');
    label.setAttribute('for', 'user-filter');
    label.innerText = 'User: ';
    selectSpanBox.appendChild(label);
    let selectBox = document.createElement('select');
    selectBox.setAttribute('id', 'user-filter');
    selectSpanBox.appendChild(selectBox);
    let option = document.createElement('option');
    option.setAttribute('value', 'Any');
    option.innerText = 'Any';
    selectBox.appendChild(option);
    try{
        let res = await fetch(URL, {
            method: 'GET'
        });

        if (res.status === 200) {
            let users = await res.json();

            for (let user of users) {
                let userOption = document.createElement('option');
                userOption.setAttribute('value', `${user.id}`);
                userOption.innerText = `${user.firstName} ${user.lastName}`;
                selectBox.appendChild(userOption);
            }
            selectBox.onchange = populateReimbursementsTable;
            populateReimbursementsTable();
        }
    } catch (e) {
        console.log(e);
    }
}
