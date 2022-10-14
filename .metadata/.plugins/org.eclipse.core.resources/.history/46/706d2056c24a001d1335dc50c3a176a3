let interface;
let test;
let url = location.href;
url=url.split('/');
url="ws:"+"//"+url[2]+"/game.io";
console.log(url)

const Hostpid = [false];

const Mypid = [false]; //ajax요청으로 받아오기.

let players = [];

let profiles = [];




function emitter_setting(sock){ //채팅창, 정답창, 플레이버튼 등등 세팅.
    playbtn_setting(sock);
    chatbtn_setting(sock);
    ansbtn_setting(sock);
}

function playbtn_setting(sock){

    $(".playbtn").click(()=>{

      let an =  $(".anns").val();
    
      console.log("i pushed my play btn");
      let obj = {
        h:7,a:1,b:an,p:Mypid[0],
      }
      sock.send(JSON.stringify(obj));

      $(".anns").val("");

    });

    
    $(".anns").keydown(
        (k)=>{
            
            console.log("keypressed")

            if(k.keyCode==13){
            	
                k.preventDefault();

                let an =  $(".anns").val();
    
                console.log("i pushed my play btn");
                let obj = {
                    h:7,a:1,b:an,p:Mypid[0],
                }
                sock.send(JSON.stringify(obj));

                $(".anns").val("");
              
            }
            
    
        })


}

function chatbtn_setting(sock){

    $("#chatemit").click(()=>{
        let ch = $("#chat").val(); 

        let obj = {
            h:5,a:0,b:ch,p:Mypid[0],
          }
        sock.send(JSON.stringify(obj));
        $("#chat").val("");

    })

    $("#chat").keydown(

        (k)=>{
            console.log("chat key pressed");

            if(k.keyCode==13){
                k.preventDefault();
                let ch = $("#chat").val(); 
    
                let obj = {
                    h:5,a:0,b:ch,p:Mypid[0],
                }
                sock.send(JSON.stringify(obj));
                $("#chat").val("");
              
            }
            
    
        }
    )

    

}

function ansbtn_setting(sock){

    $("#ansemit").click(()=>{
        let an = $("#ans").val(); 

        let obj = {
            h:0,a:1,b:an,p:Mypid[0],
          }

        sock.send(JSON.stringify(obj));
        $("#ans").val("");

    })

    $("#ans").keydown(
        (k)=>{
            
            console.log("keypressed")

            if(k.keyCode==13){
            	
                k.preventDefault();

                let an = $("#ans").val(); 

                let obj = {
                    h:0,a:1,b:an,p:Mypid[0],
                    }

                sock.send(JSON.stringify(obj));
                $("#ans").val("");
              
            }
            
    
        }
    )

   

}


// <textarea class="anns"></textarea>
// <button class="playbtn"> 시작 버튼</button>


function host_menu_visible(){
    $(".playbtn").css('visibility','visible');
    $(".anns").css('visibility','visible');
}

function host_menu_unvisible(){
    $(".playbtn").css('visibility','hidden');
    $(".anns").css('visibility','hidden');
}

async function refresh(){

    await getMypids();
    await getpids();
    await gethost();
    if(Hostpid[0]==Mypid[0]){
        host_menu_visible();
        
        interface.dfh.reset(false);
        
    }else{
        interface.dfh.reset(false);
    }

    resetprofiles();
    console.log("refreshed!!!")

}


function getMypids(){ //내 pid 받아오기

    return new Promise(function(resolve,reject){
        
        $.ajax("/jquery/MyPid").done(
            (e)=>{
                e=JSON.parse(e);
                console.log(e);
                Mypid[0]=e.msg;
                resolve(true);
    
            }
        )
    })
}

function getpids(){ //플레이어들 pid, name 불러오기

   

    return new Promise(function(resolve,reject){
        
        $.ajax("/jquery/Pids").done(
            (e)=>{
                e=JSON.parse(e);
                console.log(e);
                let players_arr=e.msg;

                players=[];

                players_arr.forEach(a=>{
                    players[a.pid]=a;
                })

                resolve(true);
                
            }
        )
    })

}

function gethost(){
    

    return new Promise(function(resolve,reject){
        
        $.ajax("/jquery/host").done(
            (e)=>{
                e=JSON.parse(e);
                console.log(e);
                Hostpid[0]=e.msg;
                resolve(true);
            }
        )
    })
    
}



function resetprofiles(){ //플레이어 사진 등록

    profiles.forEach((e)=>{$(e).css('visibility','hidden');})

    let count=0;

    players.forEach((e)=>{
        console.log(e)
        e.profile = profiles[count];
        $(profiles[count]).find(".name").text(e.name);
        $(profiles[count]).find(".avatar").html("<img src='/gameIO/profile_ex.jpg' alt='아바타사진'>");
        $(profiles[count++]).css('visibility','visible');
    })


}






class drawer_for_client{
 
    constructor(canvas){

        this.ctx=canvas.getContext("2d");//t
        this.bool = false;


        this.working= false;
        this.buffer = [];

        this.funcs = [(x,y)=>{this.line_start(x,y)}, (x,y)=>{this.line_to(x,y)}, (x,y)=>{this.line_end(x,y)}];
    }

    listen(e){ // e.data.b = array 형태여야하며, 각 원소는 {x: , y: , delay: } 형태

        let arr = e.b;

        while(arr.length!=0){
            this.buffer.push(arr.shift());
        }

        if(!this.working){

            this.order();
        }
    }


    dot(x,y){
        this.ctx.fillRect(x,y,2,2);
    }


    line_start(x,y){
        
        if(!this.bool){

            console.log(this);

            this.dot(x,y);
            this.ctx.beginPath();
            this.ctx.moveTo(x,y);
            this.ctx.lineTo(x,y);
            this.bool=!this.bool;
        }

    }

    line_to(x,y){
        
        if(this.bool){
            this.ctx.lineTo(x,y);
            this.ctx.stroke();
        }
    }

    line_end(x,y){

        if(this.bool){
            this.bool=!this.bool;
        }
    }

    order(){
        if(!this.working){
            this.do();
            this.working=!this.working
        }

    }

    do(){ // 재귀적 호출
        
        let that = this;

        if(this.buffer[0]!=null){

            let e=this.buffer.shift();
            
            setTimeout(()=>{

                that.funcs[e.type](e.x,e.y);
                that.do();

            },e.delay);



        }else{
            this.working=false;
        }

    }

}



class drawer_for_host{

    constructor(canvas, sock){

        this.ctx = canvas.getContext("2d");
        this.canvas = canvas;
        this.sock = sock;

        this.buffer = [];

        this.BeforeTime = 0;
        this.started = false;

        this.bool = false;

        let that = this;

        this.set();

    }

    getT(){
        return (new Date()).getMilliseconds();
    }

    timeStamp(type,x,y){

        let delay=0;
        let now = this.getT();

        if(this.started){

            delay=Math.min(30,Math.abs(now-this.BeforeTime))
        }
        
        this.BeforeTime = now;
        
        return {
            type : type,
            x : x,
            y : y,
            delay : delay,
        }
    }


    dot(e){
        this.ctx.fillRect(e.offsetX,e.offsetY,2,2);
    }

    down(e){

        console.log("down event")

        if(!this.bool){

            this.dot(e);
            this.ctx.beginPath();
            this.ctx.moveTo(e.offsetX,e.offsetY);
            this.ctx.lineTo(e.offsetX,e.offsetY);
            this.buffer.push(this.timeStamp(0,e.offsetX,e.offsetY))
        
            this.bool = !this.bool
        }
    }

    move(e){

        if(this.bool){

            this.ctx.lineTo(e.offsetX,e.offsetY);
            this.ctx.stroke();
            this.buffer.push(this.timeStamp(1,e.offsetX,e.offsetY))
            if(this.buffer.length>30){
                this.flush();
            }
        }

    }

    up(e){
        
        if(this.bool){
       
            this.buffer.push(this.timeStamp(2,e.offsetX,e.offsetY));
            this.flush();
            this.bool=!this.bool;
        }

    }

    flush(){

        this.sock.send(
            JSON.stringify(
                {   h : 3,

                    p : Mypid[0],

                    a: 0,

                    b : this.buffer,

                }
  
            )  
        );

        this.buffer =[];
    }

    idown(e){}
    imove(e){}
    iup(e){}

    set(){ //make event

        console.log("making event now...")

        let that = this;

        this.canvas.addEventListener('mousedown',(e)=>{

            that.idown(e);
        });

        this.canvas.addEventListener('mousemove',(e)=>{
            that.imove(e);
        });

        this.canvas.addEventListener('mouseup',(e)=>{
            that.iup(e);
        });
    }


    reset(pp){ 

        
        this.canvas.getContext("2d").clearRect(0,0,1000,1000);
        
        
        
        if(Hostpid[0]==Mypid[0]&&pp){
          
            this.idown=this.down;
            this.iup=this.up;
            this.imove=this.move;
        }else{

            this.idown = (e)=>{}
            this.iup = (e)=>{}
            this.imove = (e)=>{}

        }

    }
}





class Router{

    constructor(canvas, interf){

        this.canvas = canvas;

        this.mode =Mypid[0]==Hostpid[0];
        this.stop =true;
        this.interface=interf;

        this.dfc = new drawer_for_client(this.canvas);
            
        this.dispatcher=[];
        this.dispatcher.push([(d)=>this.onans(d),(d)=>this.onans(d)]);  //0
        this.dispatcher.push([]);  //1
        this.dispatcher.push([]);
        this.dispatcher.push([(d)=>this.ondraw(d)]);//3
        this.dispatcher.push([]);
        this.dispatcher.push([(d)=>this.onchat(d)])//5

        this.dispatcher[1].push([]);
        this.dispatcher[1].push((d)=>this.onplay(d)); //1
        this.dispatcher[1].push((d)=>this.onstop(d)); //2
        this.dispatcher[1].push((d)=>this.onenter(d)); //3
        this.dispatcher[1].push(null);
        this.dispatcher[1].push((d)=>this.onout(d)); //5
        this.dispatcher[1].push(null);
        this.dispatcher[1].push((d)=>this.onhostchanged(d)); //7
    }

    onans(d){
        if(d.a==0){
            //정답 맞춘 사람 메시지 칸에 표시
            
           let n = players[d.p].name;
           $(".chatlog").append("<p> ## "+n+"님이 정답을 맞췄습니다."+"</p>");
           $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
           this.onstop();
            

        }else if(d.a==1){
            //정답칸에 메시지 표시
            console.log(d);
            $(players[d.p].profile).find(".anslog")
            .append("<p>"+players[d.p].name +" : "+d.b+"</p>"); 

            $(players[d.p].profile).find(".anslog").animate({scrollTop : $(players[d.p].profile).find(".anslog")[0].scrollHeight},0);
            
        }
    };      
    
    onplay(d){
        
        $(".chatlog").append("<p>"+"## 호스트가 게임을 시작합니다. 그림을 맞추세요."+"</p>");
        $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
          
        this.stop=false;
        this.mode= Mypid[0]==Hostpid[0];
        this.interface.dfh.reset(Mypid[0]==Hostpid[0]);
    };    

    onstop(d){this.mode=false; this.stop=true;
        
        this.canvas.width=this.canvas.width; //setter를 이용한 초기화
        this.interface.dfh.reset();
        
    };

    onenter(d){
        refresh();
    };    // 들어온 사람 ajax로 조회해서 표시.

    onout(d){

        refresh();
    };      // 나간 사람 표시.

    onchat(d){

        $(".chatlog").append("<p>"+players[d.p].name +" : "+d.b+"</p>");
        $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);

    };     // 채팅창에 채팅 표시

    ondraw(d){ //그림 표시

        
        if(this.mode||this.stop){ 
            console.log("not client")
            return;}
        
        console.log(d)
        this.dfc.listen(d); 


    };     

    onhostchanged(d){ //호스트 바뀐 소식 알려주기.

        refresh();

        this.canvas.width=this.canvas.width; //setter를 이용한 초기화

        Hostpid[0]=d.p;

        if(d.p==Mypid[0]){
            this.mode=true;
            $(".chatlog").append("<p>"+"## 당신이 호스트입니다. 문제를 내고 그림을 그리세요."+"</p>");
            $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
        }else{
            this.mode=false;
            $(".chatlog").append("<p>"+"## 호스트가"+players[Hostpid[0]].name+"(으)로 변경 되었습니다."+"</p>");
            $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
        }
        

    }
}

class SocketInterface{

    constructor(canvas){

        this.canvas= canvas;

        this.eventMake();

        this.onopen;
        this.onmessage;
        this.onclose;

        console.log(this.onopen);

        this.sock=new WebSocket(url);

        this.sock.onopen =this.onopen;

        this.sock.onmessage= this.onmessage;

        this.sock.onclose = ()=>{
            alert("서버와 접속 끊김...")
        }

        
        this.dfh = new drawer_for_host(this.canvas, this.sock);
        
        this.router=new Router(this.canvas, this);

        
        

        this.mode=false; // client mode : false, host mode : true

    }


    eventMake(){

        this.onmessage=(e)=>{
      
                if(e==null){
                    return;
                }
                
                
                let data = JSON.parse(e.data);
                let arr = data.arr;

                arr=JSON.parse(arr);

                while(arr.length!=0){

                    let o = arr.shift();
                    test=o;
                    o=JSON.parse(o);
                    let header = o.h;
                    let action = o.a? o.a : 0;
                    console.log("헤더 : ",header)
        
                    this.router.dispatcher[header][action](o); //h,a변수에 따라서 router에 적절하게 보내줌.

                }

    

                

          

           

        }


        this.onclose=()=>{


        }

        this.onopen=()=>{

            refresh();

            if(Hostpid[0]==Mypid[0]){

                console.log(Hostpid[0], Mypid[0])

                $(".chatlog").append("<p>"+"## 당신이 호스트입니다. 문제를 내고 그림을 그리세요."+"</p>");
                $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
            
            }

            emitter_setting(this.sock);
        }
        
    }


    getSock(){
        return this.sock;
    }

}

function make_msg_event(sock){

    $("#chatemit").click(
        ()=>{
            
            let txt = $("#chat").text();
            $("#chat").text("");
            let jsonmsg={
    
                h:0, p:Mypid[0], a:0, b:txt,
            }

            sock.send(JSON.stringify(jsonmsg));
    
        })

}

function make_ans_event(sock){

    $("#ansemit").click(

        ()=>{
            let txt = $("#ans").text();
            $("#ans").text("");
            let jsonmsg={
                h:3,p:Mypid[0], a:1, b:txt,
            }

            sock.send(JSON.stringify(jsonmsg));
        }

    )

}

refresh();

document.querySelectorAll(".profile").forEach((e)=>{profiles.push(e)});

let CANVAS = document.querySelector(".canvas1");
console.log(CANVAS)
interface = new SocketInterface(CANVAS);
