// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;

public interface ITypeConverterProvider {

    <TTargetType> ITypeConverter<TTargetType> resolve(Type targetType);

}
