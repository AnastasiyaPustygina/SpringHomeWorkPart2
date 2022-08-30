package org.example.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Genre {
    long id;
    String name;
}
