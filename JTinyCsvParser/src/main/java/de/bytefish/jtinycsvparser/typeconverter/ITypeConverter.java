// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;

public interface ITypeConverter<TTargetType> extends TypeConverter {

    TTargetType convert(String value);

    Type getTargetType();

}
