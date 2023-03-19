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
 */

function signupValidation() {
    var id = $("#username").val();
    var pw = $("#password").val();
    var pw_check = $("#password_check").val();
    var email = $("#email").val();

    // 1. 빈칸 확인
    if (id === "" || pw === "" || pw_check === "" || email === "") {
        alert("빈칸이 있습니다.");
    }
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
        document.getElementById("pwCheckLabel").innerHTML = success_message;
    } else {    // 비밀번호 확인 실패
        document.getElementById("pw_check_img").src = "/images/icons/cross-small.svg";
        document.getElementById("pwCheckLabel").innerHTML = fail_message;
    }
}