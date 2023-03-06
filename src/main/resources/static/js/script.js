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

    $('#vote_content').on('keyup', function () {
        // 투표 내용 글자수 제한
        $('#vote_content_cnt').html("" + $(this).val().length + "/500");

        if ($(this).val().length > 500) {
            $(this).val($(this).val().substring(0, 500));
            $('#vote_content_cnt').html("500/500");
        }

        // 투표 내용에서 해시태그 추출
        let split_content = $('#vote_content').val().split(/(#[^\s#]+)/g);
        let hashtag = [];
        for (let i = 0; i < split_content.length; i++) {
            if (split_content[i].includes('#')) {
                hashtag[hashtag.length] = split_content[i];
            }
        }
        $('#hashtag').val(hashtag);
    });
});

// 투표 선택지 추가
let voteOptionCount = 3;

function create_voteOption() {
    if (voteOptionCount <= 10) {
        document.getElementById('voteOption' + voteOptionCount).style.display = "block";
        voteOptionCount++;
    }
}