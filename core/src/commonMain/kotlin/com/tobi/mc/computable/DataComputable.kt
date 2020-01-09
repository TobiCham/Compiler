package com.tobi.mc.computable

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment

interface DataComputable : Computable {

    override fun compute(context: Context, environment: ExecutionEnvironment): Data

    override fun optimise(): DataComputable
}