function createHeader(vote, user) {
    const header = document.createElement('header');
    header.classList.add('profile');
    header.style.marginBottom = '10px';

    const leftDiv = document.createElement('div');
    leftDiv.style.float = 'left';

    const img = document.createElement('img');
    img.setAttribute('src', '/images/avatar.jpg');
    img.style.height = '48px';
    leftDiv.appendChild(img);

    const section = document.createElement('section');
    section.style.float = 'left';
    section.style.paddingLeft = '10px';

    const nickname = document.createElement('div');
    nickname.style.fontWeight = 'bold';
    nickname.textContent = user.nickname;

    const userId = document.createElement('div');
    userId.style.fontWeight = 'bold';
    userId.textContent = user.userId;

    section.appendChild(nickname);
    section.appendChild(userId);

    const rightDiv = document.createElement('div');
    rightDiv.style.float = 'right';
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
    a.style.color = 'black!important';
    li.appendChild(a);

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
    header.appendChild(leftDiv);
    header.appendChild(section);
    header.appendChild(rightDiv);

    return header;
}

function createContent(vote, option) {
    // 본문 생성
    const content = document.createElement("div");
    content.classList.add("m-2");
    content.style.paddingTop = "5px";

    const contentTitle = document.createElement("div");
    contentTitle.classList.add("timeline-content-title");
    contentTitle.id = "vote-content-" + vote.id;
    contentTitle.textContent = vote.content;

    content.appendChild(contentTitle);

    // 투표 섹션 생성
    const voteSection = document.createElement("section");
    voteSection.classList.add("vote" + vote.id + "outer", "vote-background-1");
    voteSection.addEventListener("click", function () {
        show(vote.id);
    });

    const voteTitleDiv = document.createElement("div");

    const voteTitleP = document.createElement("p");
    voteTitleP.classList.add("vote-background-p");
    voteTitleP.id = "vote-title-" + vote.id;
    voteTitleP.textContent = "vote.title";

    voteTitleDiv.appendChild(voteTitleP);

    const voteCardDate = document.createElement("div");
    voteCardDate.classList.add("vote-card-date");
    voteCardDate.textContent = vote.createdAt;

    voteTitleDiv.appendChild(voteCardDate);

    voteSection.appendChild(voteTitleDiv);

    // 투표 클릭시 섹션 생성
    const voteClicked = document.createElement("section");
    voteClicked.classList.add("vote-background-2", "vote" + vote.id + "inner", "vote-background-2");
    voteClicked.id = "vote-background-section-clicked";
    voteClicked.style.display = "none";
    voteClicked.setAttribute("voteId", vote.id);

    const voteClickedDiv1 = document.createElement("div");
    voteClickedDiv1.classList.add("test" + vote.id);
    voteClickedDiv1.style.paddingBottom = "30px";
    voteClickedDiv1.style.width = "85%";
    voteClickedDiv1.style.float = "left";

    const voteClickedDiv2 = document.createElement("div");

    const voteSubmitBtn = document.createElement("button");
    voteSubmitBtn.classList.add("vote-submit-btn", "vote-submit-btn" + vote.id);
    voteSubmitBtn.addEventListener("click", function () {
        submitPick(vote.id);
    });

    const voteSubmitBtnImg = document.createElement("img");
    voteSubmitBtnImg.src = "/images/icons/arrow-right.svg";
    voteSubmitBtnImg.style.width = "25px";
    voteSubmitBtnImg.style.height = "30px";

    voteSubmitBtn.appendChild(voteSubmitBtnImg);

    voteClickedDiv2.appendChild(voteSubmitBtn);

    const voteAnalyzeBtn = document.createElement("button");
    voteAnalyzeBtn.classList.add("vote-analyze-btn" + vote.id);
    voteAnalyzeBtn.style.border = "none";
    voteAnalyzeBtn.style.display = "none";
    voteAnalyzeBtn.style.marginTop = "5px";
    voteAnalyzeBtn.style.backgroundColor = "transparent";

    const voteAnalyzeBtnImg = document.createElement("img");
    voteAnalyzeBtnImg.src = "/images/icons/analyze.svg";
    voteAnalyzeBtnImg.style.width = "25px";
    voteAnalyzeBtnImg.style.height = "30px";
    voteAnalyzeBtnImg.style.fill = "#FFFFFF";

    voteAnalyzeBtn.appendChild(voteAnalyzeBtnImg);

    voteClickedDiv2.appendChild(voteAnalyzeBtn);

    const optionDiv = document.createElement("div");
    optionDiv.style.marginTop = "20px";

    for (let i = 0; i < option.length; i++) {
        optionDiv.append(
            $("<div>", {class: "vote-button-check", id: "vote" + vote.id + "options"}).append(
                $("<input>", {
                    class: "vote-select-btn",
                    type: "radio",
                    id: function () {
                        return "vote" + vote.id + "option" + option[i].id;
                    },
                    name: function () {
                        return "vote" + vote.id + "option";
                    },
                    value: option[i].id,
                })
            ).append(
                $("<label>", {for: "", text: option[i].content})
            ).append(
                $("<div>", {class: "vote-result" + vote.id + " vote-option-tl"})
            )
        );
    }
    voteClicked.append($("<div>", {class: "test" + vote.id, style: "display: block; height:3rem;"}));

    // 최종 결과물
    const contentDiv = $("<div>").append(content).append(voteSection).append(voteClicked);
    return contentDiv;
}

function createIcons(vote, comments) {
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
    bmkSvg.classList.add('bi', 'bi-bookmark', 'bookmark-off');
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

    const img = document.createElement("img");
    img.src = "/images/avatar.jpg";
    img.className = "comment-circle";
    flDiv.appendChild(img);

    const div90 = document.createElement("div");
    div90.style.width = "90%";
    commentArea.appendChild(div90);

    const cmText = document.createElement("section");
    cmText.className = "cm-text";
    div90.appendChild(cmText);

    const commentId = document.createElement("div");
    commentId.className = "comment-id";
    commentId.textContent = comment.userDto.userId;
    cmText.appendChild(commentId);

    const cmBlock = document.createElement("div");
    cmBlock.className = "cm-block";
    cmBlock.addEventListener("click", function() {
        location.href = vote.id + "/detail";
    });
    cmText.appendChild(cmBlock);

    const commentContent = document.createElement("div");
    commentContent.textContent = comment.content;
    commentContent.addEventListener("click", function() {
        location.href = vote.id + "/detail";
    });
    cmText.appendChild(commentContent);

    const hr = document.createElement("hr");
    commentArea.appendChild(hr);

    return commentArea;
}