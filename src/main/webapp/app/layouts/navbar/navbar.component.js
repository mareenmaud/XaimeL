$(document).ready(function(){
    $('[data-toggle="popover"]').popover(); 
  });

  function notifications() { 
    if(tab[4].notifs == false){
      document.getElementById('bout').disabled = false; 
    }
    else{
      document.getElementById('bout').disabled = true;
    }
    } 
    })