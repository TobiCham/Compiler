//----------------------------------------------------
// The following code was generated by CUP v0.11b 20160615 (GIT 4ac7450)
//----------------------------------------------------
package com.tobi.mc.parser.ast.parser

import com.tobi.mc.computable.Context
import com.tobi.mc.parser.ast.parser.runtime.LRParser
import com.tobi.mc.parser.ast.parser.runtime.Scanner
import com.tobi.mc.parser.ast.parser.runtime.Symbol
import com.tobi.mc.parser.ast.parser.runtime.SymbolFactory
import com.tobi.mc.util.Stack

/**
 * CUP v0.11b 20160615 (GIT 4ac7450) generated parser.
 */
internal class Parser(
    scanner: Scanner,
    factory: SymbolFactory,
    symbolIdNameMapping: (Int) -> String,
    defaultContext: Context
) : LRParser(scanner, factory, symbolIdNameMapping) {

    private val action_obj = ParserActions(this, defaultContext)

    override fun production_table() = PRODUCTION_TABLE

    override fun action_table() = ACTION_TABLE

    override fun reduce_table() = REDUCE_TABLE

    override fun do_action(
        act_num: Int,
        parser: LRParser,
        stack: Stack<Symbol>,
        top: Int
    ): Symbol {
        return action_obj.doAction(act_num, parser, stack, top)
    }

    override fun start_state() = 0

    override fun start_production() = 0

    /**
     * `EOF` Symbol index.
     */
    override fun EOF_sym() = 0

    /**
     * `error` Symbol index.
     */
    override fun error_sym() = 1

    private companion object {
        /**
         * Production table.
         */
        val PRODUCTION_TABLE = arrayOf(
            shortArrayOf(0,2),shortArrayOf(22,1),shortArrayOf(0,1),shortArrayOf(0,1),shortArrayOf(0,1),shortArrayOf(0,1),shortArrayOf(0,1),shortArrayOf(1,1),shortArrayOf(2,1),shortArrayOf(3,2),shortArrayOf(4,1),shortArrayOf(4,3),shortArrayOf(7,1),shortArrayOf(8,3),shortArrayOf(9,4),shortArrayOf(9,2),shortArrayOf(11,3),shortArrayOf(11,1),shortArrayOf(11,1),shortArrayOf(11,1),shortArrayOf(11,3),shortArrayOf(11,1),shortArrayOf(12,1),shortArrayOf(12,2),shortArrayOf(12,2),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(12,3),shortArrayOf(10,4),shortArrayOf(10,3),shortArrayOf(6,1),shortArrayOf(6,3),shortArrayOf(5,6),shortArrayOf(5,5),shortArrayOf(14,1),shortArrayOf(14,1),shortArrayOf(14,1),shortArrayOf(14,1),shortArrayOf(14,1),shortArrayOf(14,1),shortArrayOf(13,2),shortArrayOf(13,1),shortArrayOf(13,1),shortArrayOf(13,1),shortArrayOf(16,1),shortArrayOf(16,2),shortArrayOf(15,2),shortArrayOf(15,3),shortArrayOf(15,1),shortArrayOf(20,5),shortArrayOf(21,7),shortArrayOf(21,5),shortArrayOf(19,2),shortArrayOf(19,1),shortArrayOf(18,1),shortArrayOf(17,1)
        )

        /**
         * Parse-action table.
         */
        val ACTION_TABLE = arrayOf(
            shortArrayOf(2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,33,30,35,9,-1,0),shortArrayOf(5,-18,6,-18,7,-18,8,-18,21,-18,22,-18,23,-18,24,-18,25,-18,26,-18,27,-18,28,-18,29,-18,33,-18,34,-18,36,-18,-1,0),shortArrayOf(29,-64,-1,0),shortArrayOf(0,-50,2,-50,3,-50,4,-50,10,-50,11,-50,12,-50,13,-50,14,-50,15,-50,16,-50,17,-50,18,-50,19,-50,20,-50,22,-50,32,-50,33,-50,35,-50,-1,0),shortArrayOf(2,-4,-1,0),shortArrayOf(33,104,-1,0),shortArrayOf(5,-8,6,-8,7,-8,8,-8,21,-8,22,-8,23,-8,24,-8,25,-8,26,-8,27,-8,28,-8,29,-8,33,-8,34,-8,36,-8,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,22,58,23,65,24,61,25,60,26,62,27,69,28,63,29,-45,33,68,36,67,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(5,-13,6,-13,7,-13,8,-13,22,-13,23,-13,24,-13,25,-13,26,-13,27,-13,28,-13,29,-13,30,101,33,-13,36,-13,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,29,-62,33,30,35,9,-1,0),shortArrayOf(2,-5,-1,0),shortArrayOf(33,94,-1,0),shortArrayOf(0,93,-1,0),shortArrayOf(0,-2,2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,33,30,35,9,-1,0),shortArrayOf(29,-48,-1,0),shortArrayOf(2,-3,-1,0),shortArrayOf(29,-46,-1,0),shortArrayOf(5,-22,6,-22,7,-22,8,-22,21,-22,22,-22,23,-22,24,-22,25,-22,26,-22,27,-22,28,-22,29,-22,33,-22,34,-22,36,-22,-1,0),shortArrayOf(0,-53,2,-53,3,-53,4,-53,10,-53,11,-53,12,-53,13,-53,14,-53,15,-53,17,-53,18,-53,19,-53,20,-53,22,-53,32,-53,33,-53,35,-53,-1,0),shortArrayOf(0,-51,2,-51,3,-51,4,-51,10,-51,11,-51,12,-51,13,-51,14,-51,15,-51,16,-51,17,-51,18,-51,19,-51,20,-51,22,-51,32,-51,33,-51,35,-51,-1,0),shortArrayOf(29,92,-1,0),shortArrayOf(29,-47,-1,0),shortArrayOf(2,-7,-1,0),shortArrayOf(29,-63,-1,0),shortArrayOf(29,-44,-1,0),shortArrayOf(5,-20,6,-20,7,-20,8,-20,21,-20,22,-20,23,-20,24,-20,25,-20,26,-20,27,-20,28,-20,29,-20,33,-20,34,-20,36,-20,-1,0),shortArrayOf(0,-52,2,-52,3,-52,4,-52,10,-52,11,-52,12,-52,13,-52,14,-52,15,-52,16,-52,17,-52,18,-52,19,-52,20,-52,22,-52,32,-52,33,-52,35,-52,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,37,-1,0),shortArrayOf(5,-23,6,-23,7,-23,8,-23,21,-23,22,-23,23,-23,24,-23,25,-23,26,-23,27,-23,28,-23,29,-23,33,-23,34,-23,36,-23,-1,0),shortArrayOf(5,-19,6,-19,7,-19,8,-19,21,-19,22,-19,23,-19,24,-19,25,-19,26,-19,27,-19,28,-19,29,-19,33,-19,34,-19,36,-19,-1,0),shortArrayOf(5,-9,6,-9,7,-9,8,-9,21,-9,22,-9,23,-9,24,-9,25,-9,26,-9,27,-9,28,-9,29,-9,33,-9,34,-9,36,-9,-1,0),shortArrayOf(29,-43,-1,0),shortArrayOf(2,-6,-1,0),shortArrayOf(29,-16,30,38,33,39,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(10,24,11,5,12,17,13,36,14,12,34,41,-1,0),shortArrayOf(21,52,34,53,-1,0),shortArrayOf(2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,31,45,33,30,35,9,-1,0),shortArrayOf(2,44,-1,0),shortArrayOf(21,-11,34,-11,-1,0),shortArrayOf(21,-10,34,-10,-1,0),shortArrayOf(2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,32,49,33,30,35,9,-1,0),shortArrayOf(0,-57,2,-57,3,-57,4,-57,10,-57,11,-57,12,-57,13,-57,14,-57,15,-57,16,-57,17,-57,18,-57,19,-57,20,-57,22,-57,32,-57,33,-57,35,-57,-1,0),shortArrayOf(0,-42,2,-42,3,-42,4,-42,10,-42,11,-42,12,-42,13,-42,14,-42,15,-42,16,-42,17,-42,18,-42,19,-42,20,-42,22,-42,32,-42,33,-42,35,-42,-1,0),shortArrayOf(2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,32,51,33,30,35,9,-1,0),shortArrayOf(0,-55,2,-55,3,-55,4,-55,10,-55,11,-55,12,-55,13,-55,14,-55,15,-55,16,-55,17,-55,18,-55,19,-55,20,-55,22,-55,32,-55,33,-55,35,-55,-1,0),shortArrayOf(0,-54,2,-54,3,-54,4,-54,10,-54,11,-54,12,-54,13,-54,14,-54,15,-54,17,-54,18,-54,19,-54,20,-54,22,-54,32,-54,33,-54,35,-54,-1,0),shortArrayOf(0,-56,2,-56,3,-56,4,-56,10,-56,11,-56,12,-56,13,-56,14,-56,15,-56,16,-56,17,-56,18,-56,19,-56,20,-56,22,-56,32,-56,33,-56,35,-56,-1,0),shortArrayOf(10,24,11,5,12,17,13,36,14,12,-1,0),shortArrayOf(2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,31,45,33,30,35,9,-1,0),shortArrayOf(0,-41,2,-41,3,-41,4,-41,10,-41,11,-41,12,-41,13,-41,14,-41,15,-41,16,-41,17,-41,18,-41,19,-41,20,-41,22,-41,32,-41,33,-41,35,-41,-1,0),shortArrayOf(21,-12,34,-12,-1,0),shortArrayOf(5,-13,6,-13,7,-13,8,-13,21,-13,22,-13,23,-13,24,-13,25,-13,26,-13,27,-13,28,-13,29,-13,33,-13,34,-13,36,-13,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,22,58,23,65,24,61,25,60,26,62,27,69,28,63,29,-15,33,68,36,67,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,34,74,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(5,-32,6,-32,7,-32,8,-32,21,-32,22,58,23,65,24,61,25,60,26,62,27,-32,28,-32,29,-32,33,68,34,-32,36,-32,-1,0),shortArrayOf(5,-34,6,-34,7,-34,8,-34,21,-34,22,58,23,65,24,61,25,60,26,62,27,-34,28,-34,29,-34,33,68,34,-34,36,-34,-1,0),shortArrayOf(21,76,34,77,-1,0),shortArrayOf(5,-38,6,-38,7,-38,8,-38,21,-38,22,-38,23,-38,24,-38,25,-38,26,-38,27,-38,28,-38,29,-38,33,-38,34,-38,36,-38,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,21,-39,22,58,23,65,24,61,25,60,26,62,27,69,28,63,33,68,34,-39,36,67,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(5,-37,6,-37,7,-37,8,-37,21,-37,22,-37,23,-37,24,-37,25,-37,26,-37,27,-37,28,-37,29,-37,33,-37,34,-37,36,-37,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,21,-40,22,58,23,65,24,61,25,60,26,62,27,69,28,63,33,68,34,-40,36,67,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,21,-21,22,58,23,65,24,61,25,60,26,62,27,69,28,63,29,-21,33,68,34,-21,36,-21,-1,0),shortArrayOf(5,-31,6,-31,7,-31,8,-31,21,-31,22,58,23,65,24,61,25,60,26,62,27,-31,28,-31,29,-31,33,68,34,-31,36,-31,-1,0),shortArrayOf(5,-26,6,-26,7,-26,8,-26,21,-26,22,58,23,-26,24,61,25,60,26,62,27,-26,28,-26,29,-26,33,68,34,-26,36,-26,-1,0),shortArrayOf(5,-36,6,-36,7,-36,8,-36,21,-36,22,58,23,65,24,61,25,60,26,62,27,-36,28,-36,29,-36,33,68,34,-36,36,-36,-1,0),shortArrayOf(5,-33,6,-33,7,-33,8,-33,21,-33,22,58,23,65,24,61,25,60,26,62,27,-33,28,-33,29,-33,33,68,34,-33,36,-33,-1,0),shortArrayOf(5,-30,6,-30,7,-30,8,-30,21,-30,22,58,23,-30,24,-30,25,-30,26,-30,27,-30,28,-30,29,-30,33,68,34,-30,36,-30,-1,0),shortArrayOf(5,-28,6,-28,7,-28,8,-28,21,-28,22,58,23,-28,24,-28,25,-28,26,-28,27,-28,28,-28,29,-28,33,68,34,-28,36,-28,-1,0),shortArrayOf(5,-29,6,-29,7,-29,8,-29,21,-29,22,58,23,-29,24,-29,25,-29,26,-29,27,-29,28,-29,29,-29,33,68,34,-29,36,-29,-1,0),shortArrayOf(5,-35,6,-35,7,-35,8,-35,21,-35,22,58,23,65,24,61,25,60,26,62,27,-35,28,-35,29,-35,33,68,34,-35,36,-35,-1,0),shortArrayOf(5,-27,6,-27,7,-27,8,-27,21,-27,22,58,23,-27,24,-27,25,-27,26,-27,27,-27,28,-27,29,-27,33,68,34,-27,36,-27,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,22,58,23,65,24,61,25,60,26,62,27,69,28,63,33,68,34,90,36,67,-1,0),shortArrayOf(5,-17,6,-17,7,-17,8,-17,21,-17,22,-17,23,-17,24,-17,25,-17,26,-17,27,-17,28,-17,29,-17,33,-17,34,-17,36,-17,-1,0),shortArrayOf(5,-24,6,-24,7,-24,8,-24,21,-24,22,58,23,-24,24,-24,25,-24,26,-24,27,-24,28,-24,29,-24,33,68,34,-24,36,-24,-1,0),shortArrayOf(0,-49,2,-49,3,-49,4,-49,10,-49,11,-49,12,-49,13,-49,14,-49,15,-49,16,-49,17,-49,18,-49,19,-49,20,-49,22,-49,32,-49,33,-49,35,-49,-1,0),shortArrayOf(0,-1,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,22,58,23,65,24,61,25,60,26,62,27,69,28,63,33,68,34,96,36,67,-1,0),shortArrayOf(2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,31,45,33,30,35,9,-1,0),shortArrayOf(0,-60,2,-60,3,-60,4,-60,10,-60,11,-60,12,-60,13,-60,14,-60,15,-60,16,98,17,-60,18,-60,19,-60,20,-60,22,-60,32,-60,33,-60,35,-60,-1,0),shortArrayOf(2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,31,45,33,30,35,9,-1,0),shortArrayOf(0,-59,2,-59,3,-59,4,-59,10,-59,11,-59,12,-59,13,-59,14,-59,15,-59,16,-59,17,-59,18,-59,19,-59,20,-59,22,-59,32,-59,33,-59,35,-59,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,22,58,23,65,24,61,25,60,26,62,27,69,28,63,29,-61,33,68,36,67,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,22,58,23,65,24,61,25,60,26,62,27,69,28,63,29,-14,33,68,36,67,-1,0),shortArrayOf(5,-25,6,-25,7,-25,8,-25,21,-25,22,58,23,-25,24,-25,25,-25,26,-25,27,-25,28,-25,29,-25,33,68,34,-25,36,-25,-1,0),shortArrayOf(2,56,3,7,4,34,22,29,33,30,35,9,-1,0),shortArrayOf(5,59,6,64,7,66,8,70,22,58,23,65,24,61,25,60,26,62,27,69,28,63,33,68,34,106,36,67,-1,0),shortArrayOf(2,10,3,7,4,34,10,24,11,5,12,17,13,36,14,12,15,13,17,6,18,3,19,25,20,11,22,29,31,45,33,30,35,9,-1,0),shortArrayOf(0,-58,2,-58,3,-58,4,-58,10,-58,11,-58,12,-58,13,-58,14,-58,15,-58,16,-58,17,-58,18,-58,19,-58,20,-58,22,-58,32,-58,33,-58,35,-58,-1,0)
        )

        /**
         * `reduce_goto` table.
         */
        val REDUCE_TABLE = arrayOf(
            shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,19,14,21,16,14,17,22,18,17,19,15,20,20,21,27,22,13,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,102,12,18,-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,99,12,18,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,49,14,21,17,22,18,17,19,15,20,20,21,27,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,90,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,88,12,18,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,56,12,18,-1,-1),shortArrayOf(0,41,3,42,4,39,-1,-1),shortArrayOf(-1,-1),shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,45,14,21,15,46,17,22,18,17,19,15,20,20,21,27,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,19,14,21,16,47,17,22,18,17,19,15,20,20,21,27,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,49,14,21,17,22,18,17,19,15,20,20,21,27,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(0,41,3,54,-1,-1),shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,45,14,21,15,53,17,22,18,17,19,15,20,20,21,27,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,87,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,86,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,85,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,84,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,83,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,82,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,81,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,80,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,79,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,78,12,18,-1,-1),shortArrayOf(1,31,2,1,6,72,7,26,10,32,11,74,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,71,12,18,-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,70,12,18,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,77,12,18,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,94,12,18,-1,-1),shortArrayOf(-1,-1),shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,45,14,21,15,96,17,22,18,17,19,15,20,20,21,27,-1,-1),shortArrayOf(-1,-1),shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,45,14,21,15,98,17,22,18,17,19,15,20,20,21,27,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,101,12,18,-1,-1),shortArrayOf(-1,-1),shortArrayOf(-1,-1),shortArrayOf(1,31,2,1,7,26,10,32,11,104,12,18,-1,-1),shortArrayOf(-1,-1),shortArrayOf(0,30,1,31,2,1,5,3,7,26,8,34,9,25,10,32,11,7,12,18,13,45,14,21,15,106,17,22,18,17,19,15,20,20,21,27,-1,-1),shortArrayOf(-1,-1)
        )
    }
}