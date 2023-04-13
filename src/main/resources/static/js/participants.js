function addListener(btn) {
    btn.addEventListener('click', function () {
        if (this.innerText == '팔로우' && this.classList.contains("p-btn-primary")) {
            this.classList.remove("p-btn-primary");
            this.classList.add("p-btn-secondary");
            this.innerText = '팔로잉';
        } else if (this.innerText == '팔로잉' && this.classList.contains("p-btn-secondary")) {
            this.classList.remove("p-btn-secondary");
            this.classList.add("p-btn-primary");
            this.innerText = '팔로우';
        } else if (this.innerText == '언팔로우' && this.classList.contains("btn-unfollow")) {
            this.classList.remove("btn-unfollow");
            this.classList.add("p-btn-primary");
            this.innerText = '팔로우';
        }
    });

    btn.addEventListener('mouseover', function () {
        if (this.innerText == '팔로잉' && this.classList.contains("p-btn-secondary")) {
            this.classList.remove("p-btn-secondary");
            this.classList.add("btn-unfollow");
            this.innerText = '언팔로우';
        }
    });

    btn.addEventListener('mouseout', function () {
        if (this.innerText == '언팔로우' && this.classList.contains("btn-unfollow")) {
            this.classList.remove("btn-unfollow");
            this.classList.add("p-btn-secondary");
            this.innerText = '팔로잉';
        }
    });
}

function addList(pList) {
    const base = pList.participants.number * 10;
    for(let i=0; i<pList.participants.size; i++) {
        const div1 = document.createElement('div');
        div1.setAttribute('id', 'p-list');
        div1.setAttribute('class', 'p-box wrap');

        const div2 = document.createElement('div');
        div2.setAttribute('class', 'fl wrap');
        div2.setAttribute('style', 'display: flex');

        const div3 = document.createElement('div');
        div3.setAttribute('class', 'fl');

        const div4 = document.createElement('div');
        div4.setAttribute('class', 'circle-container');
        div4.setAttribute('th:onclick', location.href='#');

        const img = document.createElement('img');
        img.setAttribute('src', 'https://djpms9a1go7nf.cloudfront.net/prod/uploads/thumbnail/images/10043263/167100535142741_md.png');
        img.setAttribute('class', 'circle-img circle-big cp');

        const div5 = document.createElement('div');
        div5.setAttribute('class', 'fr ta-l mt-1');

        const div6 = document.createElement('div');
        div6.setAttribute('class', 'mb-0 w-100 small lh-sm');

        const div7 = document.createElement('div');
        div7.setAttribute('class', 'd-flex justify-content-between mb-1');

        const div8 = document.createElement('div');
        div8.setAttribute('class', 'text-dark text-bold p-subtitle cp mr-2');
        div8.textContent = pList.participants.content[i].nickname;

        const div9 = document.createElement('div');
        div9.setAttribute('class', 'mb-2 p-id cp');
        div9.textContent = pList.participants.content[i].userId;

        const div10 = document.createElement('div');
        div10.setAttribute('class', 'fr ta-r vertical-parent');
        div10.setAttribute('style', 'width: 50%; flex: 1;');

        const div11 = document.createElement('div');
        div11.setAttribute('class', 'vertical-child');

        const button = document.createElement('button');
        if(pList.followingUserIdList.includes(pList.participants.content[i].userId)) {
            button.setAttribute('type', 'button');
            button.setAttribute('class', 'p-btn p-btn-secondary');
            button.textContent = '팔로잉';
            addListener(button);
        } else {
            button.setAttribute('type', 'button');
            button.setAttribute('class', 'p-btn p-btn-primary');
            button.textContent = '팔로우';
            addListener(button);
        }

        div4.appendChild(img);
        div3.appendChild(div4);

        div7.appendChild(div8);
        div6.appendChild(div7);
        div6.appendChild(div9);
        div5.appendChild(div6);

        div11.appendChild(button);
        div10.appendChild(div11);

        div2.appendChild(div3);
        div2.appendChild(div5);

        div1.appendChild(div2);
        div1.appendChild(div10);

        document.getElementById("p-container").append(div1);
    }
}