  $(document).ready(function(){
   
  });

  function upload(){
    var input = document.getElementById("file");
    file = input.files[0];    
    if (file != undefined) {
      form = new FormData();
      form.append("jar",file);
      alert(form);
      $.ajax({
        type: "post",
        url: "upload.php",
        data: form,
        processData: false,
        contentType: false,
        success: function(data){
          alert('success');
        }
      });
    }
  }

  function browse() {
    var input = document.getElementById("file");
    if(input.value == ""){
      namefile.innerHTML = "No document uploaded...";
    }else{
      var split = input.value.split('\\');// to change when we will use ubuntu
      var name = split[split.length-1];
      namefile.innerHTML = name;
    }
  }