package com.capstone.pick.controller.form;

import com.capstone.pick.domain.constant.SearchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchForm {
    private SearchType searchType;
    private String searchValue;
}
