'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

var multipleUploadForm = document.querySelector('#multipleUploadForm');

var multipleFileUploadInput1 = document.querySelector('#multipleFileUploadInput1');
var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');

function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/uploadSingleFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function addNew(){
    document.getElementById("multipleUploadForm").insertAdjacentHTML('afterend','<button id="addNew" class="warning" onclick="submitAgain()">Add..</button>');
}

function submitAgain(){
    document.getElementById("multipleUploadForm").insertAdjacentHTML('afterend','<form id="multipleUploadForm2" name="multipleUploadForm2">'+
    '<input id="multipleFileUploadInput2" type="file" name="files" class="file-input" multiple required/> '
    +'<button id="submit2" type="submit" class="primary submit-btn">Upload</button>'
+'</form>');
document.querySelector('#multipleUploadForm2').addEventListener('submit', function(event){
    var files2 = document.querySelector('#multipleFileUploadInput2').files;
    if(files2.length === 0) {
        multipleFileUploadError.innerHTML = "Please select at least one file";
        multipleFileUploadError.style.display = "block";
    }
    uploadMultipleFiles(files2);
    event.preventDefault();
}, true);
}

function uploadMultipleFiles(files) {
    var formData = new FormData();
    for(var index = 0; index < files.length; index++) {
        formData.append("files", files[index]);
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/uploadMultipleFiles");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            multipleFileUploadError.style.display = "none";
            var content = '<p>All Files Uploaded Successfully</p></br><button style="background-color:green;color:white" >Consult All docs</button>';
            /* for(var i = 0; i < response.length; i++) {
                content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
            } */
            multipleFileUploadSuccess.innerHTML = content;
            multipleFileUploadSuccess.style.display = "block";
        } else {
            multipleFileUploadSuccess.style.display = "none";
            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

/* function consult(){
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/uploads/");
} */
/* 
singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true); */


multipleUploadForm.addEventListener('submit', function(event){
    var files1 = multipleFileUploadInput1.files;
    if(files1.length === 0) {
        multipleFileUploadError.innerHTML = "Please select at least one file";
        multipleFileUploadError.style.display = "block";
    }
    uploadMultipleFiles(files1);
    event.preventDefault();
}, true);
