package com.tobi.mc.parser.experimental

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.parser.util.getComponents

object VariableRenamer {

    fun renameVariable(computable: Computable, from: String, to: String) {
        return computable.rename(from, to)
    }

    private fun <T : Computable> T.rename(from: String, to: String) {
        when(this) {
            is VariableReference -> this.name = getNewName(this.name, from, to)
            is FunctionDeclaration -> {
                if(name == from || parameters.any { it.name == from }) {
                    //'from' is being redefined, nothing left to do
                    return
                }
            }
            is ExpressionSequence -> {
                for(operation in operations) {
                    operation.rename(from, to)

                    //If 'from' is being redefined, nothing below this statement will need to be redefined
                    if((operation is FunctionDeclaration || operation is FunctionPrototype || operation is DefineVariable) && (operation as VariableReference).name == from) break
                }
                //Cancel normal children renaming
                return
            }
        }
        for(component in this.getComponents()) {
            component.rename(from, to)
        }
    }

    private fun getNewName(name: String, from: String, to: String): String {
        return if(name == from) to else name
    }
}