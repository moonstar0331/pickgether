$(document).ready(function () {
    hashtaging();

    // 투표 작성 버튼리스트 open
    $("#vote_btn_open").click(function () {
        $("#vote_btn_list").css("display", "inline");
        $("#vote_btn_open").css("display", "none");
    });

    // 투표 작성 버튼리스트 close
    $("#vote_btn_close").click(function () {
        $("#vote_btn_list").css("display", "none");
        $("#vote_btn_open").css("display", "block");
    });

    $('#vote_content_cnt').html("" + $('#vote_content').val().length + "/500");
    $('#vote_content').on('keyup', function () {
        // 투표 내용 글자수 제한
        $('#vote_content_cnt').html("" + $(this).val().length + "/500");

        if ($(this).val().length > 500) {
            $(this).val($(this).val().substring(0, 500));
            $('#vote_content_cnt').html("500/500");
        }
    });
});

function hashtaging() {
    // 투표 내용에서 해시태그 추출해서 색 변경
    for (let i = 0; i < $(".vote-content-none").length; i++) {
        let split_content = $(".vote-content-none").eq(i).text().split(/(#[^\s#]+)/g);
        let p = $('<div>');
        p.addClass('timeline-content-title');
        for (let j = 0; j < split_content.length; j++) {
            if (split_content[j].includes('#')) {
                // 해시태그를 가진 div를 생성하고, onclick 메서드를 추가합니다.
                let hashtagDiv = $('<div>');
                hashtagDiv.text(split_content[j]);
                hashtagDiv.css('color', '#7A57F6');
                hashtagDiv.css('display', 'inline');
                hashtagDiv.addClass('hover-cursor-pointer');
                hashtagDiv.click(function () {
                    window.location.href = '/search?hashtag=' + split_content[j].substring(1);
                });
                p.append(hashtagDiv);
            } else {
                // 해시태그가 없는 div를 생성합니다.
                let div = $('<div>');
                div.text(split_content[j]);
                div.css('display', 'inline');
                p.append(div);
            }
            $(".vote-content-none").eq(i).after(p);
        }
    }
}

$(document).ready(function () {
    $('#searchValue').on('keyup', function () {
        if ($(this).val().length === 0) {
            clearSearchResult();
        } else {
            search();
        }
    });
    $('#searchType').on('change', function () {
        clearSearchValue();
    });

    $('#commentInput').on('keyup', function (e) {
        if (e.keyCode === 13) {
            saveComment();
        }
    });
});

function noLogin_beforeCheck() {
    var answer = confirm('로그인 후 가능합니다');
    if (answer) {
        location.href = '/login';
    }
}

function href(path) {
    location.href = '/' + path;
}

function search() {
    var searchValue = $('#searchValue').val();
    var searchType = $('#searchType').val();

    $.ajax({
        url: '/search',
        type: 'POST',
        data: searchForm = {
            searchValue: searchValue,
            searchType: searchType
        },
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        }
    })
        .done(function (fragment) {
            $('#searchResult').replaceWith(fragment);
            hashtaging();
        });
}

function clickBookmark(id) {
    let voteId = id.substring(8);
    let bookmarkClass = document.getElementById(id).getElementsByTagName('svg').item(0).classList;
    if (bookmarkClass.contains('bi-bookmark-fill')) { // bookmark 저장된 상태
        deleteBookmark(voteId);
        bookmarkClass.replace('bi-bookmark-fill', 'bi-bookmark');
        bookmarkClass.replace('bookmark-on', 'bookmark-off');
    } else {
        saveBookmark(voteId);
        bookmarkClass.replace('bi-bookmark', 'bi-bookmark-fill');
        bookmarkClass.replace('bookmark-off', 'bookmark-on');
    }
}

function deleteBookmarkPost(id) {
    deleteBookmark(id);
    $('#votePost-' + id).remove();
}

function saveBookmark(voteId) {
    $.ajax({
        url: '/' + voteId + '/saveBookmark',
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        }
    });
}

function deleteBookmark(voteId) {
    $.ajax({
        url: '/' + voteId + '/deleteBookmark',
        type: "DELETE",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        }
    });
}

function deleteAllBookmark() {
    $.ajax({
        url: '/deleteAllBookmark',
        type: "DELETE",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        }
    });
    $('#voteArea').remove();
}

function show(voteId) {
    var outer = ".vote" + voteId + "outer";
    var inner = ".vote" + voteId + "inner";

    $(outer).click(function () {
        $(inner).css("display", "block");
        $(outer).css("display", "none");
    });

    const test = document.getElementsByClassName("test" + voteId);

    $(test).click(function () {
        $(outer).css("display", "block");
        $(inner).css("display", "none");
    });

    var submit = ".vote-submit-btn" + voteId;
    var analyze = ".vote-analyze-btn" + voteId;
    var result = ".vote-result" + voteId;

    $(submit).click(function () {
        $(result).css("display", "inline");
        $(submit).css("display", "none");
        $(analyze).css("display", "inline");
    });
}

function submitPick(voteId) {
    const selected = document.querySelector("#vote" + voteId + "options input[type=radio]:checked");

    if (selected !== null) {
        var data = {
            "optionId": selected.value,
            "voteId" : voteId
        };
        $.ajax({
            url: '/pick',
            data: JSON.stringify(data),
            type: "POST",
            async: true,
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8',
            beforeSend: function (jqXHR, settings) {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");
                jqXHR.setRequestHeader(header, token);
            },
            success: function (data) {
                console.log("s: " + JSON.stringify(data));
                setPickPercent(data);
            },
            error: function (data) {
                console.log("e: " + JSON.stringify(data));
            }
        });
    } else {
        console.log("vote id[" + voteId + "] : pick data is null");
    }
}

function setPickPercent(data) {
    // 1. 각 선택지에 대한 pick 개수 가져오기
    let keys = Object.keys(data.pickCountList); // 선택지 아이디 리스트
    let sum = 0;
    const voteId = data.voteId;

    // 2. 각 선택지에 대한 퍼센트 너비를 변경
    keys.forEach((optionId) => sum += parseInt(data.pickCountList[optionId]));

    keys.forEach((optionId) => {
        $('#result' + optionId)
            .css("width", Math.floor(parseInt(data.pickCountList[optionId]) / sum * 100) + '%');
        let options = '.vote-select-box' + voteId;
        $(options).css("pointer-events", "none");
    })
}

// voteOption 태그 생성 및 삭제 count
let voteOptionCount = 1;

// voteOption 태그 생성
function create_voteOption() {
    voteOptionCount++;

    // 태그 생성
    let area = document.getElementById('voteOptionArea');
    let new_div = document.createElement('div');
    let new_input = document.createElement('input');
    let new_imgBtn = document.createElement('img');
    let new_imgPreview = document.createElement('img');
    let new_fileInput = document.createElement('input');

    // 태그 css
    new_div.className = 'col-12 mb-3';
    new_input.className = 'w-100 h-100 border-none rounded focus-none p-2 vote-option';
    new_input.type = 'text';
    new_input.required = true;
    new_input.name = 'voteOptions[' + voteOptionCount + '].content';
    new_input.id = 'voteOptions[' + voteOptionCount + '].content';
    new_input.placeholder = '항목을 입력하세요';
    new_input.required = true;

    new_imgPreview.id = 'fileimg' + voteOptionCount;
    new_imgPreview.className = 'col-12 my-2';
    new_imgPreview.style.display = 'none';

    new_fileInput.type = 'file';
    new_fileInput.accept = 'image/*';
    new_fileInput.name = 'voteOptions[' + voteOptionCount + '].file';
    new_fileInput.id = 'inputfile' + voteOptionCount
    new_fileInput.style.display = 'none';
    new_fileInput.onchange = function () {
        changePreview(new_fileInput.id);
    };

    new_imgBtn.className = 'position-absolute vote-option-img1';
    new_imgBtn.id = 'file' + voteOptionCount;
    new_imgBtn.onclick = function () {
        document.getElementById(new_fileInput.id).click();
    };

    // 태그 추가
    new_div.appendChild(new_input);
    new_div.appendChild(new_imgBtn);
    new_div.appendChild(new_imgPreview);
    new_div.appendChild(new_fileInput);
    area.appendChild(new_div);
}

function saveComment() {
    var voteId = $("meta[name='voteId']").attr("content");

    var data = {
        content: $("#commentInput").val()
    }
    $.ajax({
        url: '/' + voteId + '/comments',
        type: "POST",
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function (data) {
            console.log(data);
        },
        error: function (data) {
            console.log(data);
        }
    }).done(function (fragment) {
        $("#commentInput").val('');
        $("#commentList").replaceWith(fragment);
    });
}

function updateComment(commentId) {
    var voteId = $("meta[name='voteId']").attr("content");

    var data = {
        content: $("#editContent").val()
    }

    $.ajax({
        url: '/' + voteId + '/comments/' + commentId,
        type: "PUT",
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        }
    }).done(function (fragment) {
        $("#commentList").replaceWith(fragment);
    });
}

function deleteComment(commentId) {
    var voteId = $("meta[name='voteId']").attr("content");

    $.ajax({
        url: '/' + voteId + '/comments/' + commentId,
        type: "DELETE",
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        }
    }).done(function (fragment) {
        $("#commentList").replaceWith(fragment);
    });
}

// voteOption 태그 삭제
function delete_voteOption() {
    if (voteOptionCount === 1) {
        alert("투표 선택지 항목은 2개 이상이어야 합니다.")
        return;
    }
    let removeOption = document.getElementById('voteOptions[' + voteOptionCount + '].content');
    let parent = removeOption.parentElement;

    while (parent.hasChildNodes()) {
        parent.removeChild(parent.firstChild);
    }

    parent.remove();

    voteOptionCount--;
}

function beforeCheck(m) {
    if (confirm(m)) {
        return true;
    }
    return false;
}

function click_editCommentBtn(id) {
    $("#defaultBtns" + id).css("display", "none");
    $("#content" + id).css("display", "none");
    $("#editingBtns" + id).css("display", "flex");
    $("#form" + id).css("display", "block");
}

function click_cancleEditBtn(id) {
    $("#defaultBtns" + id).css("display", "flex");
    $("#content" + id).css("display", "block");
    $("#editingBtns" + id).css("display", "none");
    $("#form" + id).css("display", "none");
}

// 투표 댓글 수정 textarea 자동 높이 조절
function autoResizeTextarea(element) {
    element.style.height = "auto";
    element.style.height = (element.scrollHeight) + "px";
}

function commentOrderBy(voteId, orderBy) {
    location.href = "/" + voteId + "/comments?sort=" + orderBy + ",desc";
}

function commentOrderBy_detail(voteId, orderBy) {
    location.href = "/" + voteId + "/detail?sort=" + orderBy + ",desc";
}

function timelineCategory(category) {
    var checkOrder = document.location.href.includes("sort");
    var checkCate = document.location.href.includes("category");

    var search = location.search;
    var cate = search.substring(search.indexOf("=") + 1, search.indexOf("&"));

    if (checkOrder && checkCate) {
        location.href = "/timeline" + search.replaceAll(cate, category);
    } else if (checkOrder && !checkCate) {
        location.href = "/timeline?category=" + category + "&" + search.replaceAll("?", "");
    } else {
        location.href = "/timeline?category=" + category;
    }
}

function timelineOrderBy(orderBy) {
    var checkOrder = document.location.href.includes("sort");
    var checkCate = document.location.href.includes("category");

    var search = location.search;

    if (checkCate && !checkOrder) {
        if (orderBy === 'modifiedAt') {
            location.href = "/timeline" + search + "&sort=" + orderBy + ",desc";
        } else {
            location.href = "/timeline" + search + "&sort=" + orderBy + ",asc";
        }
    } else if (checkCate && checkOrder) {
        if (orderBy === 'modifiedAt') {
            location.href = "/timeline" + search.split("&")[0] + "&sort=" + orderBy + ",desc";
        } else {
            location.href = "/timeline" + search.split("&")[0] + "&sort=" + orderBy + ",desc";
        }
    } else {
        if (orderBy === 'modifiedAt') {
            location.href = "/timeline?sort=" + orderBy + ",desc";
        } else {
            location.href = "/timeline?sort=" + orderBy + ",desc";
        }
    }
}

// 검색창 clear & 기존 검색 결과 clear
function clearSearchValue() {
    $("#searchValue").val("");
    clearSearchResult();
}

// 기존 검색 결과 clear
function clearSearchResult() {
    let searchResult = document.getElementById('searchResult');
    while (searchResult.hasChildNodes()) {
        searchResult.removeChild(searchResult.firstChild);
    }
}

function changePreview(id) {
    if (document.getElementById(id).files && document.getElementById(id).files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('fileimg' + id.substring(9)).style.display = 'block';
            document.getElementById('fileimg' + id.substring(9)).src = e.target.result;
        };
        reader.readAsDataURL(document.getElementById(id).files[0]);
    } else {
        document.getElementById('fileimg' + id.substring(9)).style.display = 'none';
        document.getElementById('fileimg' + id.substring(9)).src = "";
    }
}

function changeProfileImg() {
    if (document.getElementById('ChangeImg').files && document.getElementById('ChangeImg').files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('img1').src = e.target.result;
            document.getElementById('img2').src = e.target.result;
        };
        reader.readAsDataURL(document.getElementById('ChangeImg').files[0]);
    }
}

function deleteFile(id) {
    document.getElementById(id).style.display = 'none';
    document.getElementById(id).src = "";
    document.getElementById('inputfile' + id.substring(7)).value = null;
}