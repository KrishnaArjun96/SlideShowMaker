var imgs=new Array();
var caps =new Array();
var indexOfSlide=1;
var timeOfSlide = 2000;
var slideShowTimer;
imgs[1]='img/SignalHillNewfoundland.jpg';
caps[1]="hills";
imgs[2]='img/ArchesUtah.jpg';
caps[2]="Rocks";
imgs[3]='img/CarhengeNebraska.jpg';
caps[3]="Dead cars";
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


function playSlide() {
     var recur="playSlide()";
      slideShowTimer=setTimeout(recur,timeOfSlide);
     nextSlide();
}


function pause(){
    clearTimeout(slideShowTimer);
}