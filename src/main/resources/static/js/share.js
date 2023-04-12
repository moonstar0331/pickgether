function sendLink(id) {
    let url = 'https://pickgether.shop/' + id + '/detail';
    if (!Kakao.isInitialized()) {
        Kakao.init('31a176393c0166bd16a25e9e62aa2939');
    }
    Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
            title: document.getElementById('vote-title-' + id).innerText,
            description: document.getElementById('vote-content-' + id).innerText,
            imageUrl:
                'https://user-images.githubusercontent.com/90389517/229438674-6c51ab1c-9508-4722-a041-19179d6ba06f.png',
            link: {
                // [내 애플리케이션] > [플랫폼] 에서 등록한 사이트 도메인과 일치해야 함
                mobileWebUrl: url,
                webUrl: url,
            },
        },
        social: {
            likeCount: Number(document.getElementById('vote-participantNum-' + id).innerText),
            commentCount: Number(document.getElementById('vote-commentNum-' + id).innerText),
        },
        buttons: [
            {
                title: 'Pick Together',
                link: {
                    mobileWebUrl: url,
                    webUrl: url,
                },
            },
        ],
    })
}