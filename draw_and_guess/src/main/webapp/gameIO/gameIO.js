let url = location.href;
url=url.split('/');
url="ws:"+"//"+url[2]+"/game.io";

//////////////////////////////url

const PidInfoSet = {mypid : null, hostpid : null}

const players ={arr : []};

const profiles = document.querySelectorAll(".profile");

const dfhSet = {dfh : null};


function emitter_setting(sock){ //채팅창, 정답창, 플레이버튼 등등 세팅.
    playbtn_setting(sock);
    chatbtn_setting(sock);
    ansbtn_setting(sock);
}

function playbtn_setting(sock){

    $(".playbtn").click(()=>{

        $.ajax({
            type: "POST",
            url: "/ajax/play",
            data: {"quiz" : $(".anns").val()},
            success: ()=>{
                $(".anns").val("");
            },
            
          });

    });

    
    $(".anns").keydown(
        (k)=>{

            if(k.keyCode==13){
            	
                k.preventDefault();

                let an =  $(".anns").val();
                let obj = {
                    h:7,a:1,b:an,p:PidInfoSet.mypid,
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
            h:5,a:0,b:ch,p:PidInfoSet.mypid,
          }
        sock.send(JSON.stringify(obj));
        $("#chat").val("");

    })

    $("#chat").keydown(

        (k)=>{

            if(k.keyCode==13){
                k.preventDefault();
                let ch = $("#chat").val(); 
    
                let obj = {
                    h:5,a:0,b:ch,p:PidInfoSet.mypid,
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
            h:0,a:1,b:an,p:PidInfoSet.mypid,
          }

        sock.send(JSON.stringify(obj));
        $("#ans").val("");

    })

    $("#ans").keydown(
        (k)=>{

            if(k.keyCode==13){
            	
                k.preventDefault();

                let an = $("#ans").val(); 

                let obj = {
                    h:0,a:1,b:an,p:PidInfoSet.mypid,
                    }

                sock.send(JSON.stringify(obj));
                $("#ans").val("");
              
            }
            
    
        }
    )

   

}




function getMyPid(){
    

    return new Promise(function(resolve,reject){
        
        $.ajax("/ajax/mypid").done(
            (e)=>{
                e=JSON.parse(e);
                PidInfoSet.mypid=e.msg;
                resolve(true);
            }
        )
    })
    
}


function getHostPid(){
    

    return new Promise(function(resolve,reject){
        
        $.ajax("/ajax/hostpid").done(
            (e)=>{
                e=JSON.parse(e);
                PidInfoSet.hostpid=e.msg;
                resolve(true);
            }
        )
    })
    
}

function getPlayers(){

    players.arr=[];

    return new Promise(function(resolve,reject){
        
        $.ajax("/ajax/players").done(
            (e)=>{
                e=JSON.parse(e);
                console.log(e)
               
                e.result.forEach((ele)=>{players.arr[ele.pid]=ele});
                resolve(true);
            }
        )
    })

}

function host_menu_visible(){
    $(".playbtn").css('visibility','visible');
    $(".anns").css('visibility','visible');
}

function host_menu_unvisible(){
    $(".playbtn").css('visibility','hidden');
    $(".anns").css('visibility','hidden');
}


async function refresh(dfh){

    await getMyPid();
    await getHostPid();
    await getPlayers();
    resetprofiles();
    

    if(PidInfoSet.hostpid == PidInfoSet.mypid){
        
        host_menu_visible();

        if(dfh==null){
            return;
        }

        dfh.reset(false);

    }else{
    
        host_menu_unvisible();

        if(dfh==null){
            return;
        }

        dfh.reset(false);
    }

    return new Promise((function(resolve, reject){resolve(true)}))
}


function resetprofiles(){ //플레이어 사진 등록

    profiles.forEach((e)=>{$(e).css('visibility','hidden');})

    let count=0;

    players.arr.forEach((e)=>{

        e.profile = profiles[count];
        $(profiles[count]).find(".name").text(e.name);
        $(profiles[count]).find(".avatar").html("<img src='/gameIO/profile_ex.jpg' alt='아바타사진'>");
        $(profiles[count++]).css('visibility','visible');
        
    })

    
    if(PidInfoSet.hostpid==PidInfoSet.mypid){
        
        players.arr.forEach(e=>{

            $(e.profile).find(".hostbtn").html("<button>방장 위임</button>");
            $(e.profile).find("button").click(()=>{

                $.ajax({
                    type: "POST",
                    url: "/ajax/host",
                    data: {"pid" : e.pid},
                    success: ()=>{console.log("방장 위임 요청")},
                  });

            })
        })


    }else{
	
		 players.arr.forEach(e=>{

            $(e.profile).find(".hostbtn").text("");
            
		})
		
	}



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

        console.log("send event!!")

        this.sock.send(
            JSON.stringify(
                {   h : 3,

                    p : PidInfoSet.mypid,

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


    reset(bool){ 

        
        this.canvas.getContext("2d").clearRect(0,0,1000,1000);
        
        
        
        if(PidInfoSet.hostpid==PidInfoSet.mypid&&bool){
          
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

        this.mode =PidInfoSet.hostpid==PidInfoSet.mypid;
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
            
           let n = players.arr[d.p].name;
           $(".chatlog").append("<p> ## "+n+"님이 정답을 맞췄습니다."+"</p>");
           $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
            

        }else if(d.a==1){
            //정답칸에 메시지 표시
            $(players.arr[d.p].profile).find(".anslog")
            .append("<p>"+players.arr[d.p].name +" : "+d.b+"</p>"); 

            $(players.arr[d.p].profile).find(".anslog").animate({scrollTop : $(players.arr[d.p].profile).find(".anslog")[0].scrollHeight},0);
            
        }
    };      
    
    onplay(d){
        


        this.stop=false;
        this.mode= PidInfoSet.hostpid==PidInfoSet.mypid;
        this.interface.dfh.reset(PidInfoSet.hostpid==PidInfoSet.mypid);
        
   
        $(".chatlog").append("<p>"+"## 호스트가 게임을 시작합니다. 그림을 맞추세요."+"</p>");
        $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
        
    
    };    

    onstop(d){this.mode=false; this.stop=true;
        
        this.canvas.width=this.canvas.width; //setter를 이용한 초기화
        this.interface.dfh.reset();

        if(d.b==1){
            $(".chatlog").append("<p>"+"## 호스트가 중간에 퇴장하여 비정상적으로 게임이 중지되었습니다.."+"</p>");
        $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
        }else if(d.b==0){
            $(".chatlog").append("<p>"+"## 게임 종료."+"</p>");
        $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
        }
        
    };

    onenter(d){
        refresh(dfhSet.dfh);
    };    // 들어온 사람 ajax로 조회해서 표시.

    onout(d){

        refresh(dfhSet.dfh);
    };      // 나간 사람 표시.

    onchat(d){

        $(".chatlog").append("<p>"+players.arr[d.p].name +" : "+d.b+"</p>");
        $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);

    };     // 채팅창에 채팅 표시

    ondraw(d){ //그림 표시

        
        if(this.mode||this.stop){ 
            return;}

        this.dfc.listen(d); 


    };     

    async onhostchanged(d){ //호스트 바뀐 소식 알려주기.

        await refresh(dfhSet.dfh);

        this.canvas.width=this.canvas.width; //setter를 이용한 초기화

        if(d.p==PidInfoSet.mypid){
            this.mode=true;
            $(".chatlog").append("<p>"+"## 당신이 호스트입니다. 문제를 내고 그림을 그리세요."+"</p>");
            $(".chatlog").animate({scrollTop : $(".chatlog")[0].scrollHeight},0);
        }else{
            this.mode=false;
            
            let order;
            let count = 0;
            
            $(".chatlog").append("<p>"+"## 호스트가"+players.arr[PidInfoSet.hostpid].name+"(으)로 변경 되었습니다."+"</p>");
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

        this.sock=new WebSocket(url);

        this.sock.onopen =()=>{console.log("소켓 시작");this.onopen();}

        this.sock.onmessage= (e) =>{ console.log(this); this.onmessage(e);}

        this.sock.onclose = ()=>{
            alert("서버와 접속 끊김...")
        }

        
        this.dfh = new drawer_for_host(this.canvas, this.sock);
        dfhSet.dfh=this.dfh;
        
        this.router=new Router(this.canvas, this);

        
        

        this.mode=false; // client mode : false, host mode : true

    }


    eventMake(){
		console.log("onmsg event make....")
		
        this.onmessage=(e)=>{
      			
      			console.log("message event!")
      			
                if(e==null){
                    return;
                }
                
                let data = JSON.parse(e.data);
                let arr = data.arr;

                arr=JSON.parse(arr);
                console.log("데이터가 옴.",data);

                while(arr.length!=0){

                    let o = arr.shift();
                    o=JSON.parse(o);
                    let header = o.h;
                    let action = o.a? o.a : 0;
                    
                    console.log(o);

                    this.router.dispatcher[header][action](o); //h,a변수에 따라서 router에 적절하게 보내줌.

                }
      

        }


        this.onclose=()=>{


        }

        this.onopen=()=>{

            refresh(dfhSet.dfh);

            if(PidInfoSet.hostpid==PidInfoSet.mypid){

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


function make_chat_event(sock){

    $("#chatemit").click(
        ()=>{
            
            let txt = $("#chat").text();
            $("#chat").text("");
            let jsonmsg={
    
                h:0, p:PidInfoSet.mypid, a:0, b:txt,
            }

            sock.send(JSON.stringify(jsonmsg));
    
        })

}


async function start(){

    await refresh();
    const CANVAS = document.querySelector(".canvas1");
    const interface = new SocketInterface(CANVAS);
}

start();