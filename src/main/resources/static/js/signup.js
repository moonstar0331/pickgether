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