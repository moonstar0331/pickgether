/*
    회원가입 유효성 검사
    1. 빈칸 확인
    2. 비밀번호 정규식
        - 영문(대소문자) 포함
        - 숫자 포함
        - 특수 문자 포함
        - 공백 X
        - 8 ~ 20자
    3. 비밀번호 - 비밀번호 확인 동일 여부 확인
    4. 이메일 양식대로 입력했는지 확인
 */

function signupValidation() {
    var id = $("#username").val();
    var pw = $("#password").val();
    var pw_check = $("#password_check").val();
    var nickname = $("#nickname").val();
    var email = $("#email").val();

    // 1. 빈칸 확인
    if (id === "" || pw === "" || pw_check === "" || nickname === "" || email === "") {
        alert("빈칸이 있습니다.");
    }

    var data = {
        userId: $('#userId').val(),
        password: $('#password').val(),
        nickname: $('#nickname').val(),
        email: $('#email').val(),
        birthday: $('#birthday').val(),
        gender: $('input[name=\'gender\']:checked').val(),
        job: $('#job option:selected').val(),
        address: $('#address').val(),
        memo: $('#memo').val()
    }

    console.log(JSON.stringify(data));

    $.ajax({
        url: '/signup',
        type: "POST",
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log("success: " + data);
            location.replace('/login');
        },
        error: function (data) {
            console.log("error: " + data);
        }
    });
}

function pw_pwcheck_isSame() {
    var pw = $("#password").val();
    var pw_check = $("#password_check").val();

    var empty_message = ""
    var success_message = "비밀번호가 일치합니다."
    var fail_message = "동일한 비밀번호를 입력해주세요.";

    if (pw_check == "") {
        document.getElementById("pw_check_img").src = "";
        document.getElementById("pwCheckLabel").innerHTML = empty_message;
    } else if (pw == pw_check) {   // 비밀번호 확인 성공
        document.getElementById("pw_check_img").src = "/images/icons/check.svg";
        document.getElementById("pw_check_img").style.height = '13px';
        document.getElementById("pw_check_img").style.width = '13px';
        document.getElementById("pw_check_img").style.filter = "invert(66%) sepia(78%) saturate(4567%) hue-rotate(124deg) brightness(95%) contrast(95%)";
        document.getElementById("pwCheckLabel").innerHTML = success_message;
    } else {    // 비밀번호 확인 실패
        document.getElementById("pw_check_img").src = "/images/icons/cross-small.svg";
        document.getElementById("pw_check_img").style.height = '15px';
        document.getElementById("pw_check_img").style.width = '15px';
        document.getElementById("pw_check_img").style.filter = "invert(37%) sepia(21%) saturate(4534%) hue-rotate(334deg) brightness(108%) contrast(73%)";
        document.getElementById("pwCheckLabel").innerHTML = fail_message;
    }
}

function pw_validation() {
    var pw = $("#password").val();
    var num = pw.search(/[0-9]/g);
    var eng = pw.search(/[a-z]/ig);
    var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

    if (pw == "") {
        document.getElementById("pw_img").src = "/images/icons/minus.svg";
        document.getElementById("pw_img").style.height = '15px';
        document.getElementById("pw_img").style.width = '10px';
        document.getElementById("pw_img").style.filter = "invert(40%) sepia(18%) saturate(15%) hue-rotate(41deg) brightness(96%) contrast(88%)";
        document.getElementById("pwLabel").innerHTML = "영문(대소문자), 숫자, 특수문자를 혼합한 8~20자";
    }else if(pw.length < 8 || pw.length > 20){
        document.getElementById("pw_img").src = "/images/icons/cross-small.svg";
        document.getElementById("pw_img").style.height = '15px';
        document.getElementById("pw_img").style.width = '15px';
        document.getElementById("pw_img").style.filter = "invert(37%) sepia(21%) saturate(4534%) hue-rotate(334deg) brightness(108%) contrast(73%)";
        document.getElementById("pwLabel").innerHTML = "8자리 ~ 20자리 이내로 입력해주세요.";
    }else if(pw.search(/\s/) != -1){
        document.getElementById("pw_img").src = "/images/icons/cross-small.svg";
        document.getElementById("pw_img").style.height = '15px';
        document.getElementById("pw_img").style.width = '15px';
        document.getElementById("pw_img").style.filter = "invert(37%) sepia(21%) saturate(4534%) hue-rotate(334deg) brightness(108%) contrast(73%)";
        document.getElementById("pwLabel").innerHTML = "비밀번호는 공백 없이 입력해주세요.";
    }else if(num < 0 || eng < 0 || spe < 0 ){
        document.getElementById("pw_img").src = "/images/icons/cross-small.svg";
        document.getElementById("pw_img").style.height = '15px';
        document.getElementById("pw_img").style.width = '15px';
        document.getElementById("pw_img").style.filter = "invert(37%) sepia(21%) saturate(4534%) hue-rotate(334deg) brightness(108%) contrast(73%)";
        document.getElementById("pwLabel").innerHTML = "영문, 숫자, 특수문자를 혼합하여 입력해주세요.";
    }else {
        document.getElementById("pw_img").src = "/images/icons/check.svg";
        document.getElementById("pw_img").style.height = '13px';
        document.getElementById("pw_img").style.width = '13px';
        document.getElementById("pw_img").style.filter = "invert(66%) sepia(78%) saturate(4567%) hue-rotate(124deg) brightness(95%) contrast(95%)";
        document.getElementById("pwLabel").innerHTML = "성공!";
    }
}

function email_check(email) {
    let regex = new RegExp("([!#-'*+/-9=?A-Z^-~-]+(\.[!#-'*+/-9=?A-Z^-~-]+)*|\"\(\[\]!#-[^-~ \t]|(\\[\t -~]))+\")@([!#-'*+/-9=?A-Z^-~-]+(\.[!#-'*+/-9=?A-Z^-~-]+)*|\[[\t -Z^-~]*])");

    return regex.test(email);
}

function email_validation() {
    var email = $("#email").val();

    if (email == ""){
        document.getElementById("email_img").src = "/images/icons/cross-small.svg";
        document.getElementById("email_img").style.height = '15px';
        document.getElementById("email_img").style.width = '15px';
        document.getElementById("email_img").style.filter = "invert(37%) sepia(21%) saturate(4534%) hue-rotate(334deg) brightness(108%) contrast(73%)";
        document.getElementById("emailLabel").innerHTML = "이메일을 입력해주세요.";
    } else if (!email_check(email)) {
        document.getElementById("email_img").src = "/images/icons/cross-small.svg";
        document.getElementById("email_img").style.height = '15px';
        document.getElementById("email_img").style.width = '15px';
        document.getElementById("email_img").style.filter = "invert(37%) sepia(21%) saturate(4534%) hue-rotate(334deg) brightness(108%) contrast(73%)";
        document.getElementById("emailLabel").innerHTML = "이메일을 형식에 맞게 입력해주세요.";
    } else {
        document.getElementById("email_img").src = "/images/icons/check.svg";
        document.getElementById("email_img").style.height = '13px';
        document.getElementById("email_img").style.width = '13px';
        document.getElementById("email_img").style.filter = "invert(66%) sepia(78%) saturate(4567%) hue-rotate(124deg) brightness(95%) contrast(95%)";
        document.getElementById("emailLabel").innerHTML = "성공!";
    }
}