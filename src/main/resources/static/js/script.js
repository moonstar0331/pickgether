$(document).ready(function () {
    // 투표 작성 버튼리스트 open
    $("#vote_btn_open").click(function () {
        $("#vote_btn_list").css("display", "block");
        $("#vote_btn_open").css("display", "none");
    });

    // 투표 작성 버튼리스트 close
    $("#vote_btn_close").click(function () {
        $("#vote_btn_list").css("display", "none");
        $("#vote_btn_open").css("display", "block");
    });

    // 다이얼로그 open/close 이벤트
    $("#vote_btn_normal").click(function () {
        $("#vote_dial_normal").css("display", "block");
    });
    $("#vote_dial_normal_close").click(function () {
        $("#vote_dial_normal").css("display", "none");
        $("#vote_btn_list").css("display", "none");
        $("#vote_btn_open").css("display", "block");
    });

    // 투표 내용 글자수 제한
    $('#vote_content').on('keyup', function () {
        $('#vote_content_cnt').html("" + $(this).val().length + "/120");

        if ($(this).val().length > 120) {
            $(this).val($(this).val().substring(0, 120));
            $('#vote_content_cnt').html("120/120");
        }
    });
});