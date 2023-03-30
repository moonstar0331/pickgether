function addListener(btn) {
    btn.addEventListener('click', function () {
        if (this.innerText == '팔로우' && this.classList.contains("btn-follow")) {
            this.classList.remove("btn-follow");
            this.classList.add("btn-following");
            this.innerText = '팔로잉';
        } else if (this.innerText == '팔로잉' && this.classList.contains("btn-following")) {
            this.classList.remove("btn-following");
            this.classList.add("btn-follow");
            this.innerText = '팔로우';
        } else if (this.innerText == '언팔로우' && this.classList.contains("btn-unfollow")) {
            this.classList.remove("btn-unfollow");
            this.classList.add("btn-follow");
            this.innerText = '팔로우';
        }
    });

    btn.addEventListener('mouseover', function () {
        if (this.innerText == '팔로잉' && this.classList.contains("btn-following")) {
            this.classList.remove("btn-following");
            this.classList.add("btn-unfollow");
            this.innerText = '언팔로우';
        }
    });

    btn.addEventListener('mouseout', function () {
        if (this.innerText == '언팔로우' && this.classList.contains("btn-unfollow")) {
            this.classList.remove("btn-unfollow");
            this.classList.add("btn-following");
            this.innerText = '팔로잉';
        }
    });
}