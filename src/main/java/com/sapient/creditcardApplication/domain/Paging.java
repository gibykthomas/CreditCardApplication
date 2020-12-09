package com.sapient.creditcardApplication.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Paging {
    @Min(1)
    private Integer size = Integer.valueOf(10);
    @Min(0)
    private Integer page = Integer.valueOf(0);
}

