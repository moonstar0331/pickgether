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

        // 투표 내용에서 해시태그 추출해서 색 변경
        let split_content = $('#vote_content').val().split(/(#[^\s#]+)/g);
        let hashtag = [];
        for (let i = 0; i < split_content.length; i++) {
            if (split_content[i].includes('#')) {
                hashtag[hashtag.length] = split_content[i];
            }
        }
        // TODO : hashtag 색 변경하기
    });
});

// voteOption 태그 생성 및 삭제 count
let voteOptionCount = 1;

// voteOption 태그 생성
function create_voteOption() {
    voteOptionCount++;

    // 태그 생성
    let area = document.getElementById('voteOptionArea');
    let new_div = document.createElement('div');
    let new_input = document.createElement('input');
    let new_img1 = document.createElement('img');
    let new_img2 = document.createElement('img');

    // 태그 css
    new_div.className = 'col-12 mb-3';
    new_input.className = 'w-100 h-100 border-none rounded focus-none p-2 vote-option';
    new_input.type = 'text';
    new_input.name = 'voteOptions[' + voteOptionCount + '].content';
    new_input.placeholder = '항목을 입력하세요';
    new_input.required = true;
    new_img1.className = 'position-absolute vote-option-img1';
    new_img2.className = 'position-absolute vote-option-img2';

    // 태그 추가
    new_div.appendChild(new_input);
    new_div.appendChild(new_img1);
    new_div.appendChild(new_img2);
    area.appendChild(new_div);
}

// voteOption 태그 삭제
function delete_voteOption() {

}