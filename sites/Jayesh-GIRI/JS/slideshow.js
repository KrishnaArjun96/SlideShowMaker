var imgs=new Array();
var caps =new Array();
var indexOfSlide=1;
var timeOfSlide = 3000;
var slideShowTimer;
var play=false;
imgs[1]='img/OregonTrailNebraska.jpg';
caps[1]="Oregon";
imgs[2]='img/ToadstoolNebraska.jpg';
caps[2]="ToadStool";
imgs[3]='img/GreatPlainsNebraska.jpg';
caps[3]="Plains";
function nextSlide(){
    indexOfSlide++;
    if(indexOfSlide>3)
        indexOfSlide=1;
    document.images.slideShow.src=imgs[indexOfSlide];
    document.getElementById("caption").innerHTML=caps[indexOfSlide]; 
}

function prevSlide(){
    indexOfSlide--;
    if(indexOfSlide<1)
        indexOfSlide=3;
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