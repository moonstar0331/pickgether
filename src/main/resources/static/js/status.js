function showEmptyState(container) {
    const statusContainer = document.createElement("div");
    statusContainer.id = "statusContainer";
    statusContainer.classList.add("m-5");

    const imageElement = document.createElement("img");
    imageElement.src = "/images/status/no_posts.svg";
    statusContainer.appendChild(imageElement);

    const textElement = document.createElement("div");
    textElement.textContent = "아직 작성된 게시글이 없습니다";
    textElement.classList.add("p-font");
    statusContainer.appendChild(textElement);

    container.appendChild(statusContainer);
}