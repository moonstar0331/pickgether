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