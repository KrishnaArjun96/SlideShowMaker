/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var indexOfSlide=0;
var imgs=new array();
var caps=new array();

function nextSlide(){
    indexOfSlide++;
    if(indexOfSlide==size)
        indexOfSlide=1;
    document.images.slideShow.src=imgs[indexOfSlide].src;
    document.getElementsById(caption).innerHTML=caps[indexOfSlide]; 
}

function prevSlide(){
    indexOfSlide--;
    if(indexOfSlide==0)
        indexOfSlide=5;
    document.images.slideShow.src=imgs[indexOfSlide].src;
    document.getElementsById(caption).innerHTML=caps[indexOfSlide];
}