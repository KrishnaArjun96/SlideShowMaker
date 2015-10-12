var imgs=new Array();
var caps =new Array();
var indexOfSlide=1;
var timeOfSlide = 3000;
var slideShowTimer;
var play=false;
imgs[1]='img/YosemiteCalifornia.jpg';
caps[1]="California";
imgs[2]='img/SignalHillNewfoundland.jpg';
caps[2]="Hills";
imgs[3]='img/ArchesUtah.jpg';
caps[3]="Rocks";
imgs[4]='img/CarhengeNebraska.jpg';
caps[4]="Dead cars";
function nextSlide(){
    indexOfSlide++;
    if(indexOfSlide>4)
        indexOfSlide=1;
    document.images.slideShow.src=imgs[indexOfSlide];
    document.getElementById("caption").innerHTML=caps[indexOfSlide]; 
}

function prevSlide(){
    indexOfSlide--;
    if(indexOfSlide<1)
        indexOfSlide=4;
    document.images.slideShow.src=imgs[indexOfSlide];
    document.getElementById("caption").innerHTML=caps[indexOfSlide];
}

function decidor(){
    if(play===false){
            document.getElementById("flip").innerHTML= "<img src=\"img/pause.png\"  alt=\"Image doesn't exist\" style=\"width:20px;height:20px;\">"; 
            playSlide();
            play=true;
    }
    else
    {
        document.getElementById("flip").innerHTML= "<img src=\"img/play.png\"  alt=\"Image doesn't exist\" style=\"width:20px;height:20px;\">"; 
        pause();
        play=false;
    }
        
}
function playSlide() {
     nextSlide();
     var recur="playSlide()";
      slideShowTimer=setTimeout(recur,timeOfSlide);

}


function pause(){
    clearTimeout(slideShowTimer);
}