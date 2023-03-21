$(document).ready(function () {
    // 투표 작성 버튼리스트 open
    $(".vote-test1").click(function () {
        $(".vote-test2").css("display", "block");
        $(".vote-test1").css("display", "none");
    });

    $(".vote-test2").click(function () {
        $(".vote-test2").css("display", "none");
        $(".vote-test1").css("display", "block");
    });
});
