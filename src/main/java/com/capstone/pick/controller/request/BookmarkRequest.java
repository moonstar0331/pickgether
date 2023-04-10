package com.capstone.pick.controller.request;

import com.capstone.pick.domain.constant.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkRequest {
    private Long voteId;
    private Category category;
}
