package org.example.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Author {
    long id;
    String name;
}
