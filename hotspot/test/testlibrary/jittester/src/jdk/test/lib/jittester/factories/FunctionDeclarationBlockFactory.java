/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk.test.lib.jittester.factories;

import java.util.ArrayList;
import jdk.test.lib.jittester.IRNode;
import jdk.test.lib.jittester.ProductionFailedException;
import jdk.test.lib.jittester.functions.FunctionDeclarationBlock;
import jdk.test.lib.jittester.functions.FunctionInfo;
import jdk.test.lib.jittester.types.TypeKlass;
import jdk.test.lib.jittester.utils.PseudoRandom;

class FunctionDeclarationBlockFactory extends Factory {
    private final int memberFunctionsLimit;
    private final int memberFunctionsArgLimit;
    private final int level;
    private final TypeKlass ownerClass;

    FunctionDeclarationBlockFactory(TypeKlass ownerClass, int memberFunctionsLimit,
            int memberFunctionsArgLimit, int level) {
        this.ownerClass = ownerClass;
        this.memberFunctionsLimit = memberFunctionsLimit;
        this.memberFunctionsArgLimit = memberFunctionsArgLimit;
        this.level = level;
    }

    @Override
    public IRNode produce() throws ProductionFailedException {
        ArrayList<IRNode> content = new ArrayList<>();
        int memFunLimit = (int) (PseudoRandom.random() * memberFunctionsLimit);
        if (memFunLimit > 0) {
            IRNodeBuilder builder = new IRNodeBuilder()
                    .setOwnerKlass(ownerClass)
                    .setMemberFunctionsArgLimit(memberFunctionsArgLimit)
                    .setFlags(FunctionInfo.ABSTRACT | FunctionInfo.PUBLIC);
            for (int i = 0; i < memFunLimit; i++) {
                try {
                    content.add(builder.setName("func_" + i).getFunctionDeclarationFactory().produce());
                } catch (ProductionFailedException e) {
                    // TODO: do we have to react here?
                }
            }
        }
        return new FunctionDeclarationBlock(ownerClass, content, level);
    }
}
