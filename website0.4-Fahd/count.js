  var countdown;

  $(document).ready(function(){      
      countdown = $('.clock').FlipClock({
        clockFace: 'HourlyCounter',
        countdown: true,
        autoStart: false,
        callbacks: {
              stop: function() {
                // demander au serveur les infos pour le pie chart
                  display();
              }
        }
      });
      $('#waiting').modal('show');
  });

  function starting(){
    var seconds = parseInt(document.getElementById("seconds").value);
    var minutes = parseInt(document.getElementById("minutes").value);
    var hours = parseInt(document.getElementById("hours").value);
    var valSec = 3600*hours + 60*minutes + seconds;
    countdown.setTime(valSec);
    countdown.start();
  }  


  var data5 = {
    labels: ["Memory usage", "Cpu usage", "We'll see"],
    datasets: [
      {
        label: "My First dataset",
        fillColor: "rgba(220,220,220,0.2)",
        strokeColor: "rgba(220,220,220,1)",
        pointColor: "rgba(220,220,220,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(220,220,220,1)",
        data: [65,59,90]
      }
    ]
  };

  var data4 = {
    labels: ["Memory usage", "Cpu usage", "We'll see"],
    datasets: [
      {
        label: "My First dataset",
        fillColor: "rgba(220,220,220,0.2)",
        strokeColor: "rgba(220,220,220,1)",
        pointColor: "rgba(220,220,220,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(220,220,220,1)",
        data: [42,5,92]
      }
    ]
  };

  var data3 = {
    labels: ["Memory usage", "Cpu usage", "We'll see"],
    datasets: [
      {
        label: "My First dataset",
        fillColor: "rgba(220,220,220,0.2)",
        strokeColor: "rgba(220,220,220,1)",
        pointColor: "rgba(220,220,220,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(220,220,220,1)",
        data: [17,85,60]
      }
    ]
  };

  var data2 = {
    labels: ["Memory usage", "Cpu usage", "We'll see"],
    datasets: [
      {
        label: "My First dataset",
        fillColor: "rgba(220,220,220,0.2)",
        strokeColor: "rgba(220,220,220,1)",
        pointColor: "rgba(220,220,220,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(220,220,220,1)",
        data: [72,9,12]
      }
    ]
  };


function closed(){
    var list = readJSON();
    createButtonList(list);
}


  function display(val){// data
    var chart = new Chart(document.getElementById("chart").getContext("2d"));
    alert(val);
    var radar = chart.Radar(window[val], {
      responsive: true
    })
  }

  function clear(list){
    for (var i = 0; i < list.length; i++) {
      list[i].className ="list-group-item list-group-item-info apps";
    };
  }

  function activate(clicked_id){
    var x = document.getElementsByClassName("apps");
    clear(x);
    document.getElementById(clicked_id).className +=" active";
  }

  function receiveJSON(){ // je recois le signal = JSON this is not how i am going to do it

  }

  function readJSON(){ // Je lis le JSON et le parse en un tableau simple mettre jsonfile en argument de la fonction
    var jsonfile = '{"0":"5","1":"4","2":"3","3":"2"}'; // because i don't know what value to take for the moment in the csv files
    var x = JSON.parse(jsonfile);
    var list = [];
    for (var i in x){
      list.push(x[i]);
    }
    return list;
  }
  
  function createButtonList(list){ // crée ma buttonlist
      for (var i = 0; i < list.length; i++) {       // go through each element in the list
        var button = document.createElement("BUTTON");        // create button for the specific element               
        var contentnode = document.createTextNode("app"+list[i]);         // Create and then add to the button its content
        button.appendChild(contentnode);           
        button.className = "list-group-item list-group-item-info apps";   // Add class name to the button
        var id = document.createAttribute("id");       // Create a "id" attribute
        id.value = list[i];                           // Set the value of the id attribute
        button.setAttributeNode(id);
        var onclick = document.createAttribute("onclick"); // Creat a "onclick" event
        onclick.value = "displayData(this.id)";    // add the correct function to call
        button.setAttributeNode(onclick);  
        document.getElementById("list").appendChild(button);     // Append the button to the buttonlist
      };
  }

  function displayData(clicked_id){ // Je display ma data quand on a clické sur un bouton
      activate(clicked_id);
      display("data"+clicked_id);
      //john's function
  }

