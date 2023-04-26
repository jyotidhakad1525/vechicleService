package com.automate.vehicleservices.framework.validation.ruleengine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fact<T> {

    private String name;

    private T t;
}
