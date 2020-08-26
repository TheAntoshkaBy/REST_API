package com.epam.esm.controller.support;

import java.util.List;

public interface DtoConverter<T,V> {
   List<T> convert(List<V> var);

   V convert (T var);
}
