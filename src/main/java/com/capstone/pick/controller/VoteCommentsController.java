package com.capstone.pick.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * TODO : 댓글 상세 페이지는 각 투표 게시글에 연결되는 것이기 때문에
 *         추후에 게시글 구현이 완료되면 각 게시글에 연결하도록 하며,
 *         추가로 테스트 케이스도 수정한다. -> @PathVariable 로 연결;
 */

@Controller
public class VoteCommentsController {

    @GetMapping("/comments")
    public String readComments() {
        return "page/comments";
    }
}
