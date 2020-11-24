pokaz_zadanie();
let dodaj_input_zadania = document.getElementById("dodaj_input_zadania");
let addtaskbtn = document.getElementById("addtaskbtn");
let input_daty = document.getElementById("input_daty");
let dateValue = input_daty.value;

addtaskbtn.addEventListener("click", function(){
    addtaskinputval = dodaj_input_zadania.value;
    dateValue = input_daty.value;

    var CurrentDate = new Date();
    GivenDate = new Date(dateValue);

if(GivenDate > CurrentDate){
    if(addtaskinputval.trim()!=0){
        let webtask = localStorage.getItem("localtask");
        if(webtask == null){
            taskObj = [];
        }
        else{
            taskObj = JSON.parse(webtask);
        }
        taskObj.push({'task_name':addtaskinputval, 'task_date':dateValue, 'completeStatus':false});
        localStorage.setItem("localtask", JSON.stringify(taskObj));
        dodaj_input_zadania.value = '';
    }
    pokaz_zadanie();

}else{
    alert('Uwaga! Pole nie może być puste oraz data nie może być wcześniejsza, niż dzisiaj');
}

})

// pokaz_zadanie
function pokaz_zadanie(){
    let webtask = localStorage.getItem("localtask");
    if(webtask == null){
        taskObj = [];
    }
    else{
        taskObj = JSON.parse(webtask);
    }
    let html = '';
    let addedtasklist = document.getElementById("addedtasklist");
    taskObj.forEach((item, index) => {

        if(item.completeStatus==true){
            taskCompleteValue = `<td class="completed">${item.task_name}</td>`;
            dateCompleteValue = `<td class="completed">${item.task_date}</td>`;
        }else{
            taskCompleteValue = `<td>${item.task_name}</td>`;
            dateCompleteValue = `<td>${item.task_date}</td>`;
        }
        html += `<tr>
                    <th scope="row">${index+1}</th>
                    ${taskCompleteValue}
                    ${dateCompleteValue}
                    <td><button type="button" onclick="edytuj_zadanie(${index})" class="btn btn-warning">Edytuj</button></td>
                    <td><button type="button" id=${index} class="btn-success">Gotowe</button></td>
                    <td><button type="button" onclick="usun_zadanie(${index})" class="btn btn-danger">Usuń</button></td>
                </tr>`;
    });
    addedtasklist.innerHTML = html;
}

function edytuj_zadanie(index){
    let saveindex = document.getElementById("saveindex");
    let addtaskbtn = document.getElementById("addtaskbtn");
    let savetaskbtn = document.getElementById("savetaskbtn");
    saveindex.value = index;
    let webtask = localStorage.getItem("localtask");
    let taskObj = JSON.parse(webtask); 
    
    dodaj_input_zadania.value = taskObj[index]['task_name'];
    addtaskbtn.style.display="none";
    savetaskbtn.style.display="block";
}

let savetaskbtn = document.getElementById("savetaskbtn");
savetaskbtn.addEventListener("click", function(){
    let addtaskbtn = document.getElementById("addtaskbtn");
    let webtask = localStorage.getItem("localtask");
    let taskObj = JSON.parse(webtask); 
    let saveindex = document.getElementById("saveindex").value;
    
    for (keys in taskObj[saveindex]) {
        if(keys == 'task_name'){
            taskObj[saveindex].task_name = dodaj_input_zadania.value;
        }
      }
    savetaskbtn.style.display="none";
    addtaskbtn.style.display="block";
    localStorage.setItem("localtask", JSON.stringify(taskObj));
    dodaj_input_zadania.value='';
    pokaz_zadanie();
})
// usun wiersz
function usun_zadanie(index){
    let webtask = localStorage.getItem("localtask");
    let taskObj = JSON.parse(webtask);
    taskObj.splice(index, 1);
    localStorage.setItem("localtask", JSON.stringify(taskObj));
    pokaz_zadanie();
}


// ukoncz zadanie
let addedtasklist = document.getElementById("addedtasklist");
    addedtasklist.addEventListener("click", function(e){
        
        let webtask = localStorage.getItem("localtask");
        let taskObj = JSON.parse(webtask);
        
        let mytarget = e.target;
        if(mytarget.classList[0] === "btn-success"){
        let mytargetid = mytarget.getAttribute("id");
        
                
        mytargetpresibling = mytarget.parentElement.previousElementSibling.previousElementSibling;
            
            for (keys in taskObj[mytargetid]) {
                if(keys == 'completeStatus' && taskObj[mytargetid][keys]==true){
                    taskObj[mytargetid].completeStatus = false;
                }else if(keys == 'completeStatus' && taskObj[mytargetid][keys]==false){
                    taskObj[mytargetid].completeStatus = true;
                }
              }
        localStorage.setItem("localtask", JSON.stringify(taskObj));
        pokaz_zadanie();
    }
    })

    
// usun wszystkie 
let deleteallbtn = document.getElementById("deleteallbtn");
deleteallbtn.addEventListener("click", function(){
    let savetaskbtn = document.getElementById("savetaskbtn");
    let addtaskbtn = document.getElementById("addtaskbtn");
    let webtask = localStorage.getItem("localtask");
    let taskObj = JSON.parse(webtask);
    if(webtask == null){
        taskObj = [];
    }
    else{
        taskObj = JSON.parse(webtask);
        taskObj = [];
    }
    savetaskbtn.style.display="none";
    addtaskbtn.style.display="block";
    localStorage.setItem("localtask", JSON.stringify(taskObj));
    pokaz_zadanie();

})


// szukajka
let searchtextbox = document.getElementById("searchtextbox");
searchtextbox.addEventListener("input", function(){
    let trlist = document.querySelectorAll("tr");
    Array.from(trlist).forEach(function(item){
        let searchedtext = item.getElementsByTagName("td")[0].innerText;
        let searchtextboxval = searchtextbox.value;
        let re = new RegExp(searchtextboxval, 'gi');
        if(searchedtext.match(re)){
            item.style.display="table-row";
        }
        else{
            item.style.display="none";
        }
    })
})


















