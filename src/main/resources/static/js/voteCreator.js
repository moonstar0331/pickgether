function createMoreVote(data, votes, container) {
    for(let i=0; i<votes.numberOfElements; i++) {
        const user = votes.content[i].userDto;
        const vote = votes.content[i];
        const option = votes.content[i].voteOptionDtos;
        const comments = votes.content[i].commentDtos;
        const bookmarks = data.bookmarks;

        const voteArea = document.createElement('div');
        voteArea.setAttribute('id', 'voteArea')
        voteArea.append(createHeader(vote, user));
        if(option != null)
            voteArea.append(createContent(vote, option));
        voteArea.append(createIcons(vote, comments, bookmarks));

        if(comments != null && comments.length > 0) {
            voteArea.append(createComment(vote, comments[0]));
        }

        const hr = document.createElement("hr");
        voteArea.appendChild(hr);

        container.append(voteArea);
    }
}

function createHeader(vote, user) {
    const header = document.createElement('header');
    header.classList.add('profile');
    header.style.marginBottom = '10px';

    const profileImgDIv = document.createElement('div');
    profileImgDIv.style.float = 'left';

    <!-- 프로필 사진 -->
    const img = document.createElement('img');
    img.setAttribute('src', '/images/avatar.jpg');
    img.style.height = '48px';
    img.style.cursor = 'pointer';
    img.addEventListener("click", function() {
        location.href = 'profile?userId=' + user.userId;
    });
    profileImgDIv.appendChild(img);

    const nickNameAndIdSection = document.createElement('section');
    nickNameAndIdSection.style.float = 'left';
    nickNameAndIdSection.style.paddingLeft = '10px';

    <!-- 닉네임 -->
    const nickname = document.createElement('div');
    nickname.style.fontWeight = 'bold';
    nickname.textContent = user.nickname;
    nickname.style.cursor = 'pointer';
    nickname.addEventListener("click", function() {
        location.href = 'profile?userId=' + user.userId;
    });

    <!-- 아이디 -->
    const userId = document.createElement('div');
    userId.style.fontWeight = 'bold';
    userId.textContent = user.userId;
    userId.style.cursor = 'pointer';
    userId.addEventListener("click", function() {
        location.href = 'profile?userId=' + user.userId;
    });

    nickNameAndIdSection.appendChild(nickname);
    nickNameAndIdSection.appendChild(userId);

    const rightDiv = document.createElement('div');
    rightDiv.align = 'right';
    rightDiv.style.marginRight = '15px';

    const ulDiv = document.createElement('ul');
    ulDiv.classList.add('navbar-nav', 'me-auto', 'mb-2', 'mb-lg-0');

    const li = document.createElement('li');
    li.classList.add('nav-item', 'dropdown');

    const a = document.createElement('a');
    a.classList.add('nav-link');
    a.setAttribute('href', '#');
    a.setAttribute('role', 'button');
    a.setAttribute('data-bs-toggle', 'dropdown');
    a.setAttribute('aria-expanded', 'false');
    a.style.color = 'black !important'; // 적용안됨.
    li.appendChild(a);

    <!-- 카카오톡 공유 svg  -->
    const threeDot = document.createElement('img');
    threeDot.setAttribute('src', '/images/icons/menu-dots.svg');
    threeDot.style.width = '23px';
    threeDot.style.height = '23px';
    threeDot.style.fill = "currentColor";
    a.appendChild(threeDot);

    const dropdownMenu = document.createElement('ul');
    dropdownMenu.classList.add('dropdown-menu', 'dropdown-menu-end');
    dropdownMenu.setAttribute('aria-labelledby', 'navbarDropdown');
    dropdownMenu.style.borderRadius = '10px';
    dropdownMenu.style.zIndex = '999';

    const item1 = document.createElement('li');
    const link1 = document.createElement('a');
    link1.classList.add('dropdown-item', 'hover-cursor-pointer');
    link1.setAttribute('id', vote.id);
    link1.textContent = '카카오톡 공유';
    link1.addEventListener('click', function () {
        sendLink(vote.id);
    });
    item1.appendChild(link1);

    const item2 = document.createElement('li');
    const link2 = document.createElement('a');
    link2.classList.add('dropdown-item', 'hover-cursor-pointer');
    link2.textContent = '게시글 삭제';
    item2.appendChild(link2);

    dropdownMenu.appendChild(item1);
    dropdownMenu.appendChild(item2);

    li.appendChild(dropdownMenu);
    ulDiv.appendChild(li);
    rightDiv.appendChild(ulDiv);
    header.appendChild(profileImgDIv);
    header.appendChild(nickNameAndIdSection);
    header.appendChild(rightDiv);

    return header;
}

 function onOptionClicked(target, voteId, option) {
     if(target.checked) {
         target.checked = false;
     } else {
         for(let i=0; i<option.length; i++) {
             document.getElementById('vote' + voteId + 'option' + option[i].id).checked = false;
         }
         target.checked = true;
     }
 }

function createContent(vote, option) {
    // 게시글 본문
    const content = document.createElement("div");
    content.classList.add("m-2");
    content.style.paddingTop = "5px";

    const contentTitle = document.createElement("div");
    contentTitle.classList.add("vote-content-none", "vote-content");
    contentTitle.id = "vote-content-" + vote.id;
    /*contentTitle.textContent = vote.content;*/

    let split_content = vote.content.split(/(#[^\s#]+)/g);
    const hashtagingDiv = document.createElement("div");
    for (let i = 0; i < split_content.length; i++) {
        if (split_content[i].includes('#')) {
            const nonSharp = document.createElement("div");
            nonSharp.textContent = split_content[i];
            nonSharp.style.color = '#7A57F6';
            nonSharp.style.display = 'inline';
            nonSharp.classList.add("hover-cursor-pointer");
            nonSharp.click(function () {
                window.location.href = '/search?hashtag=' + split_content[i].substring(1);
            });
            hashtagingDiv.appendChild(nonSharp);
        } else {
            const nonSharp = document.createElement("div");
            nonSharp.textContent = split_content[i];
            nonSharp.style.display = 'inline';
            hashtagingDiv.appendChild(nonSharp);
        }
        contentTitle.appendChild(hashtagingDiv);
    }

    content.appendChild(contentTitle);

    // 투표 요소
    var section = document.createElement("section");
    section.classList.add("vote-background-1", "vote" + vote.id + "outer");
    section.setAttribute("id", vote.id);
    section.style.cursor = 'pointer';
    section.addEventListener("click", function() {
        show(vote.id);
    });

    var div1 = document.createElement("div");
    section.appendChild(div1);

    var div2 = document.createElement("div");
    div2.setAttribute("class", "wrap");
    div1.appendChild(div2);

    var div3 = document.createElement("div");
    div3.setAttribute("class", "m-2 fl");
    div2.appendChild(div3);

    var svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.setAttribute("width", "14");
    svg.setAttribute("height", "14");
    svg.setAttribute("fill", "white");
    svg.setAttribute("class", "bi bi-circle-fill vote-background-circle");
    svg.setAttribute("viewBox", "0 0 16 16");
    div3.appendChild(svg);

    var circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("cx", "8");
    circle.setAttribute("cy", "8");
    circle.setAttribute("r", "8");
    svg.appendChild(circle);

    var p = document.createElement("p");
    p.setAttribute("class", "vote-background-p");
    p.setAttribute("id", "vote-title-" + vote.id);
    p.textContent = vote.title;
    div1.appendChild(p);

    var div4 = document.createElement("div");
    div4.setAttribute("align", "right");
    div4.setAttribute("style", "color: white; margin-right: 15px; font-size: 20px; height: 3rem;");
    div4.setAttribute("text", vote.createAt);
    div1.appendChild(div4);

    // -------------- 투표 클릭시 -----------------
    const voteClicked = document.createElement('section');
    voteClicked.setAttribute('class', 'vote-background-2');
    voteClicked.setAttribute('voteId', vote.id);
    voteClicked.classList.add('vote' + vote.id + 'inner', 'vote-background-2');
    voteClicked.setAttribute('id', 'vote-background-section-clicked');
    voteClicked.setAttribute('style', 'display: none;');

    // section의 첫번째 div
    const div1_clicked = document.createElement('div');

    const wrap1 = document.createElement('div');
    wrap1.setAttribute('class', 'wrap');

    const div1_2 = document.createElement('div');
    div1_2.setAttribute('class', 'm-2 fl');

    const div1_3 = document.createElement('div');
    div1_3.setAttribute('class', 'test' + vote.id);
    div1_3.setAttribute('id', 'timeline-vote-empty-blank');
    div1_3.setAttribute('style', 'padding: 0rem;');

    const svg_clicked = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg_clicked.setAttribute('width', '14');
    svg_clicked.setAttribute('height', '14');
    svg_clicked.setAttribute('fill', 'white');
    svg_clicked.setAttribute('class', 'bi bi-circle-fill vote-background-circle');
    svg_clicked.setAttribute('viewBox', '0 0 16 16');

    const circle_clicked = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
    circle_clicked.setAttribute('cx', '8');
    circle_clicked.setAttribute('cy', '8');
    circle_clicked.setAttribute('r', '8');

    svg_clicked.appendChild(circle_clicked);
    div1_3.appendChild(svg_clicked);
    div1_2.appendChild(div1_3);
    wrap1.appendChild(div1_2);

    const div1_4 = document.createElement('div');
    div1_4.setAttribute('class', 'mr-3 mt-1 fr');

    const button1 = document.createElement('button');
    button1.classList.add('vote-submit-btn', 'vote-submit-btn' + vote.id);
    button1.setAttribute('id', vote.id);
    button1.addEventListener("click", function() {
        submitPick(vote.id);
    });

    const img1 = document.createElement('img');
    img1.setAttribute('src', '/images/icons/arrow-right.svg');
    img1.setAttribute('style', 'width: 25px; height: 30px;');

    button1.appendChild(img1);
    div1_4.appendChild(button1);

    const button2 = document.createElement('button');
    button2.setAttribute('class', 'vote-analyze-btn vote-analyze-btn' + vote.id);

    const img2 = document.createElement('img');
    img2.setAttribute('src', '/images/icons/analyze.svg');
    img2.setAttribute('style', 'width: 25px; height: 30px; fill: #FFFFFF');

    button2.appendChild(img2);
    div1_4.appendChild(button2);
    wrap1.appendChild(div1_4);
    div1_clicked.appendChild(wrap1);

    // ------------ 투표 선택지 -------------
    // section의 두 번째 div
    const div2_clicked = document.createElement("div");
    div2_clicked.classList.add("vote-option-mp");

    const div2_1 = document.createElement("div");

    // optionList for문
    for (let i = 0; i < option.length; i++) {
        const voteButtonCheck = document.createElement("div");
        voteButtonCheck.classList.add("vote-button-check");
        voteButtonCheck.id = 'vote' + vote.id + 'options';

        const voteSelectBox = document.createElement("div");
        voteSelectBox.classList.add('vote-select-box' + vote.id, 'vote-select-box');

        const voteSelectBtn = document.createElement("input");
        voteSelectBtn.classList.add("vote-select-btn");
        voteSelectBtn.type = "radio";
        voteSelectBtn.id = 'vote' + vote.id + 'option' + option[i].id;
        voteSelectBtn.name = 'vote' + vote.id + 'option';
        voteSelectBtn.value = option[i].id;
        voteSelectBtn.checked = false;
        voteSelectBtn.style.cursor = 'pointer';

        voteSelectBox.addEventListener('click', function () {
             onOptionClicked(voteSelectBtn, vote.id, option);
        });

        const optionLabel = document.createElement("label");
        optionLabel.textContent = option[i].content;
        optionLabel.classList.add('option-label');
        optionLabel.style.width = "28rem";
        optionLabel.style.cursor = 'pointer';

        const voteResult = document.createElement("div");
        voteResult.classList.add('pick-percent', 'vote-result' + vote.id);
        voteResult.id = 'result' + option[i].id;

        voteSelectBox.appendChild(voteSelectBtn);
        voteSelectBox.appendChild(optionLabel);
        voteSelectBox.appendChild(voteResult);

        voteButtonCheck.appendChild(voteSelectBox);
        div2_1.appendChild(voteButtonCheck);
    }
    voteClicked.appendChild(div1_clicked);
    div2_clicked.appendChild(div2_1);
    voteClicked.appendChild(div2_clicked);

    const contentDiv = document.createElement("div");
    contentDiv.appendChild(content);
    contentDiv.appendChild(section);
    contentDiv.appendChild(voteClicked);

    return contentDiv;
}

function createIcons(vote, comments, bookmarks) {
    const iconArea = document.createElement('div');
    iconArea.setAttribute('id', 'icon-area');
    iconArea.classList.add('icons');
    iconArea.style.marginTop = '10px';

    const leftDiv = document.createElement('div');
    leftDiv.style.cssFloat = 'left';
    iconArea.appendChild(leftDiv);

    // 투표 아이콘
    const voteIcon = document.createElement('button');
    voteIcon.setAttribute('type', 'button');
    voteIcon.addEventListener('click', function() {
        location.href = vote.id + '/participants';
    });
    leftDiv.appendChild(voteIcon);
    const voteSvg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    voteSvg.setAttribute('xmlns', 'http://www.w3.org/2000/svg');
    voteSvg.setAttribute('width', '20');
    voteSvg.setAttribute('height', '20');
    voteSvg.setAttribute('fill', 'currentColor');
    voteSvg.setAttribute('class', 'bi bi-file-earmark-check');
    voteSvg.setAttribute('viewBox', '0 0 16 16');
    const vPath1 = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    vPath1.setAttribute('d', 'M10.854 7.854a.5.5 0 0 0-.708-.708L7.5 9.793 6.354 8.646a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z');
    const vPath2 = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    vPath2.setAttribute('d', 'M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2zM9.5 3A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.5v2z');
    voteSvg.appendChild(vPath1);
    voteSvg.appendChild(vPath2);
    voteIcon.appendChild(voteSvg);
    const voteSpan = document.createElement('span');
    voteSpan.setAttribute('id', 'vote-participantNum-' + vote.id);
    voteSpan.textContent = vote.pickCount;
    leftDiv.appendChild(voteSpan);

    // 댓글 아이콘
    const cmtIcon = document.createElement('button');
    cmtIcon.setAttribute('type', 'button');
    cmtIcon.addEventListener('click', function() {
        location.href = vote.id + '/detail';
    });
    leftDiv.appendChild(cmtIcon);
    const cmtSvg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    cmtSvg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    cmtSvg.setAttribute("width", "20");
    cmtSvg.setAttribute("height", "20");
    cmtSvg.setAttribute("fill", "currentColor");
    cmtSvg.setAttribute("class", "bi bi-chat-dots");
    cmtSvg.setAttribute("viewBox", "0 0 16 16");
    const cPath1 = document.createElementNS("http://www.w3.org/2000/svg", "path");
    cPath1.setAttribute("d", "M5 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm3 1a1 1 0 1 0 0-2 1 1 0 0 0 0 2z");
    const cPath2 = document.createElementNS("http://www.w3.org/2000/svg", "path");
    cPath2.setAttribute("d", "m2.165 15.803.02-.004c1.83-.363 2.948-.842 3.468-1.105A9.06 9.06 0 0 0 8 15c4.418 0 8-3.134 8-7s-3.582-7-8-7-8 3.134-8 7c0 1.76.743 3.37 1.97 4.6a10.437 10.437 0 0 1-.524 2.318l-.003.011a10.722 10.722 0 0 1-.244.637c-.079.186.074.394.273.362a21.673 21.673 0 0 0 .693-.125zm.8-3.108a1 1 0 0 0-.287-.801C1.618 10.83 1 9.468 1 8c0-3.192 3.004-6 7-6s7 2.808 7 6c0 3.193-3.004 6-7 6a8.06 8.06 0 0 1-2.088-.272 1 1 0 0 0-.711.074c-.387.196-1.24.57-2.634.893a10.97 10.97 0 0 0 .398-2z");
    cmtSvg.appendChild(cPath1);
    cmtSvg.appendChild(cPath2);
    cmtIcon.appendChild(cmtSvg);
    const cmtSpan = document.createElement('span');
    cmtSpan.setAttribute('id', 'vote-commentNum-' + vote.id);
    cmtSpan.textContent = comments.length;
    leftDiv.appendChild(cmtSpan);

    // 북마크 아이콘
    const rightDiv = document.createElement('div');
    rightDiv.style.textAlign = 'right';
    rightDiv.style.marginRight = '15px';
    iconArea.appendChild(rightDiv);
    const bmkIcon = document.createElement('button');
    bmkIcon.setAttribute('id', 'bookmark' + vote.id);
    bmkIcon.setAttribute('type', 'button');
    bmkIcon.classList.add('vote-bookmark');
    bmkIcon.addEventListener('click', function() {
        clickBookmark(this.id);
    });
    rightDiv.appendChild(bmkIcon);
    const bmkSvg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    bmkSvg.setAttribute('width', '20');
    bmkSvg.setAttribute('height', '20');
    bmkSvg.setAttribute('fill', 'currentColor');

     if(bookmarks.includes(vote.id)) {
         bmkSvg.classList.add('bi', 'bi-bookmark', 'bookmark-on');
     } else {
         bmkSvg.classList.add('bi', 'bi-bookmark', 'bookmark-off');
     }
    bmkSvg.setAttribute('viewBox', '0 0 16 16');

    bmkIcon.appendChild(bmkSvg);
    const rightPath = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    bmkSvg.appendChild(rightPath);

    return iconArea;
}

function createComment(vote, comment) {
    const commentArea = document.createElement("section");
    commentArea.id = "comment-area";
    commentArea.className = "comment-area profile";

    const flDiv = document.createElement("div");
    flDiv.className = "fl";
    commentArea.appendChild(flDiv);

    <!-- 프로필 사진 -->
    const img = document.createElement("img");
    img.src = "/images/avatar.jpg";
    img.className = "comment-circle";
    img.style.cursor = 'pointer';
    img.addEventListener("click", function() {
        location.href = 'profile?userId=' + comment.userDto.userId;
    });
    flDiv.appendChild(img);

    const div90 = document.createElement("div");
    div90.style.width = "90%";
    commentArea.appendChild(div90);

    const cmText = document.createElement("section");
    cmText.className = "cm-text";
    div90.appendChild(cmText);

    <!-- 아이디 -->
    const commentId = document.createElement("div");
    commentId.className = "comment-id";
    commentId.textContent = comment.userDto.userId;
    commentId.style.cursor = 'pointer';
    commentId.addEventListener("click", function() {
        location.href = 'profile?userId=' + comment.userDto.userId;
    });
    cmText.appendChild(commentId);

    const cmBlock = document.createElement("div");
    cmBlock.className = "cm-block";
    cmBlock.addEventListener("click", function() {
        location.href = vote.id + "/detail";
    });
    cmText.appendChild(cmBlock);

    <!-- 댓글내용 -->
    const commentContent = document.createElement("div");
    commentContent.textContent = comment.content;
    commentContent.style.cursor = 'pointer';
    commentContent.addEventListener("click", function() {
        location.href = vote.id + "/detail";
    });
    cmText.appendChild(commentContent);

    return commentArea;
}