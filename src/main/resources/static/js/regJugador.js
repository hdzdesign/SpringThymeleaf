var newone=[]
var newtwo=[]
var newthree=[]

function add(){

var one=document.getElementById('one').value
var two=document.getElementById('two').value
var three=document.getElementById('three').value

for(var i=0;i<newone.length;i++){
if(one+two+three==newone[i-1]+newtwo[i-1]+newthree[i-1]){
alert("already exist")

return;
}

}


newone.push(one);
newtwo.push(two);
newthree.push(three);
listshow();
}





function listshow(){
var list=""
for(var i=0;i<newone.length;i++){
list+= "<tr><td>"+(i+1)+"</td>"+"<td>"+newone[i]+"</td>"+"<td>"+newtwo[i]+"</td>"+"<td>"+newthree[i]+"</td>"+"<td>"+"<button onclick='edt("+i+")'>Edit</button><button onclick='del("+i+")'>Delete</button>"+"</td></tr>"
}

document.getElementById('data').innerHTML=list


}

var load=""
function edt(edit){
load=edit
document.getElementById('one').value=newone[edit]
document.getElementById('two').value=newtwo[edit]
document.getElementById('three').value=newthree[edit]

}

function update(){
newone[load]=document.getElementById('one').value
newtwo[load]=document.getElementById('two').value
newthree[load]=document.getElementById('three').value
listshow();

}

function del(dok){
newone.splice(dok,1)
newtwo.splice(dok,1)
newthree.splice(dok,1)
listshow();
}
document.getElementById('one').val("");
document.getElementById('two').val("");
document.getElementById('three').val("");


$(document).ready(function() {

    
    var readURL = function(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('.avatar').attr('src', e.target.result);
            }
    
            reader.readAsDataURL(input.files[0]);
        }
    }
    

    $(".file-uploads").on('change', function(){
        readURL(this);
    });
});

