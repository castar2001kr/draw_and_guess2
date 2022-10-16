
let name1;
let id;

function checkCon(){
    $.ajax("/checkConnected").done((e)=>{

        e=JSON.parse(e);

        if(e.result){

            navbarDeco();
            console.log("e.result==true")
          

        }else{

            $.ajax("/logout").done(()=>{

                    $.ajax("/index/navbar.html")
                .done(function(e){
                    
                        let refind = document.createElement("div");
                        refind = $(refind).html(e).find('#logout');
                        $("body").prepend(refind);
                        
                        
                });
            })
    
            
        }

    })
}

function getCookie(name){

    let val  = document.cookie.match('(^\;)?'+ name + '=([^;]*)(;|$)');
    return val? val[2] : null;

}

function deleteCookie(name){
	document.cookie = name+"=; expires=Thu, 01 Jan 1980 00:00:01 GMT;";
}


$(document).ready(

)

function navbarDeco(){

    if(id){
    
    
        $.ajax("/index/navbar.html")
        .done(function(e){
             let refind = document.createElement("div");
             refind = $(refind).html(e).find('#loged');
             
            
               
             $("body").prepend(refind);
             $("#id").text(id);
             $("#name").text(name1);
               
               $("#out").click(()=>{
               
                   $.ajax("/logout").done(function(e){
                   
                       deleteCookie("name");
                       deleteCookie("id");
                       location.href="/";
    
                    
                       
                   }
               );
                
        });
    })
    
    
    
    }else{
    
    
        $.ajax("/logout").done(()=>{

            $.ajax("/index/navbar.html")
            .done(function(e){
               
                    let refind = document.createElement("div");
                 refind = $(refind).html(e).find('#logout');
                 $("body").prepend(refind);
                    
                   
            });

        })
    
       
        
    }
    
    
}

name1 = getCookie("name");
id = getCookie("id");

console.log(id);

checkCon();