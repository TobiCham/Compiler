package com.tobi.mc.mips

object MipsHelperCode {

    const val CALL_CLOSURE = """
# a0 = new closure
callClosure:
    lw ${'$'}a1, 4(${'$'}k0)
    li ${'$'}a2, 4
    mul ${'$'}a1, ${'$'}a1, ${'$'}a2
    sub ${'$'}sp, ${'$'}fp, ${'$'}a1
    
    sw ${'$'}k0, 0(${'$'}sp)
    sw ${'$'}ra, -4(${'$'}sp)
    addi ${'$'}sp, ${'$'}sp, -8

    sub ${'$'}a1, ${'$'}sp, ${'$'}k1
    sw ${'$'}k1, 0(${'$'}a1)
    
    sub ${'$'}fp, ${'$'}sp, ${'$'}k1
    addi ${'$'}fp, ${'$'}fp, -4
    
    lw ${'$'}a1, 8(${'$'}a0)
    mul ${'$'}a1, ${'$'}a1, ${'$'}a2
    sub ${'$'}fp, ${'$'}fp, ${'$'}a1
    
    li ${'$'}k1, 0
    move ${'$'}k0, ${'$'}a0
    
    lw ${'$'}t7, 16(${'$'}k0)
    lw ${'$'}v0, 0(${'$'}k0)
    jr ${'$'}v0
    """

    const val REMOVE_CLOSURE_FRAME = """
removeClosureFrame:
    addi ${'$'}fp, ${'$'}fp, 4
    
    lw ${'$'}a1, 8(${'$'}k0)
    li ${'$'}a2, 4
    mul ${'$'}a1, ${'$'}a1, ${'$'}a2
    add ${'$'}fp, ${'$'}fp, ${'$'}a1
    
    lw ${'$'}k1, 0(${'$'}fp)
    addi ${'$'}fp, ${'$'}fp, 4
    
    add ${'$'}fp, ${'$'}fp, ${'$'}k1
    
    lw ${'$'}ra, (${'$'}fp)
    addi ${'$'}fp, ${'$'}fp, 4
    
    lw ${'$'}k0, (${'$'}fp)
    
    lw ${'$'}a1, 4(${'$'}k0)
    mul ${'$'}a1, ${'$'}a1, ${'$'}a2
    add ${'$'}fp, ${'$'}fp, ${'$'}a1
    
    lw ${'$'}a1, 8(${'$'}k0)
    mul ${'$'}a1, ${'$'}a1, ${'$'}a2
    add ${'$'}sp, ${'$'}fp, ${'$'}a1
    
    lw ${'$'}a1, 4(${'$'}sp)
    add ${'$'}sp, ${'$'}sp, ${'$'}a1
    addi ${'$'}sp, ${'$'}sp, 4
    
    lw ${'$'}t7, 16(${'$'}k0)
    jr ${'$'}ra
    """

    const val CREATE_CLOSURE = """
#a0 = function code label (remapped to v1)
#a1 = stack variables count
#a2 = register variables count
#a3 = new environment variable count
#v0 = output, memory location of closure
createClosure:
    move ${'$'}v1, ${'$'}a0
    
    #Allocate 5 words of space for closure data
    # 0 = function label
    # 1 = stack variable count
    # 2 = register variable count
    # 3 = closure size
    # 4 = pointer to memory where the closure array is stored
    li ${'$'}a0, 20
    li ${'$'}v0, 9
    syscall
    move ${'$'}t8, ${'$'}v0
    
    #Store the data into the memory location
    sw ${'$'}v1, 0(${'$'}v0)
    sw ${'$'}a1, 4(${'$'}v0)
    sw ${'$'}a2, 8(${'$'}v0)
    
    beqz ${'$'}a3, noExtraVariables
    b extraVariables
    
    # If no extra environment variables, can reuse the same array
    noExtraVariables:
        #${'$'}k0 = Global closure pointer
        beqz ${'$'}k0 noClosure
        
        # Existing closure, use parent pointer
        
        #Add parent size and current size
        lw ${'$'}v1, 12(${'$'}k0)
        add ${'$'}v1, ${'$'}v1, ${'$'}a3
        sw ${'$'}v1, 12(${'$'}v0)
        
        lw ${'$'}a3, 16(${'$'}k0)
        sw ${'$'}a3, 16(${'$'}v0)
        b exitClosureFun
        
        #If no closure, use null (0)
        noClosure:
            sw ${'$'}zero, 12(${'$'}v0)
            sw ${'$'}zero, 16(${'$'}v0)
            b exitClosureFun
        
    extraVariables:
        #Calculate the new array size using ${'$'}a2 to store the result
        beqz ${'$'}k0, calcClosureSizeNew
        b calcClosureSizeExisting
        
        calcClosureSizeExisting:
            lw ${'$'}a2, 12(${'$'}k0)
            add ${'$'}v1, ${'$'}a2, ${'$'}a3
            sw ${'$'}v1, 12(${'$'}v0)
            lw ${'$'}t9, 16(${'$'}k0)
            b initCopyClosureLoop
            
        calcClosureSizeNew:
            sw ${'$'}a3, 12(${'$'}v0)
            move ${'$'}v1, ${'$'}a3
            li ${'$'}a2, 0
            li ${'$'}t9, 0
            b initCopyClosureLoop
        
        initCopyClosureLoop:
            #At this point, ${'$'}v1 = size. need to create heap memory of that size of words
            move ${'$'}a1, ${'$'}v0
            li ${'$'}a0, 4
            mul ${'$'}a0, ${'$'}a0, ${'$'}v1
            li ${'$'}v0 9
            syscall
            sw ${'$'}v0, 16(${'$'}a1)
            move ${'$'}a3, ${'$'}v0
            li ${'$'}a1, 0				 # Start loop counter at 0
        
        # ${'$'}a1 = loop counter
        # ${'$'}a2 = parent closure size
        # ${'$'}a3 = pointer to new array (moves across 1 word each loop)
        # ${'$'}v1 = size check condition
        # ${'$'}t9 = pointer to parent array (moves across 1 word each loop)
        copyClosureLoop:
            beq ${'$'}a1, ${'$'}v1, exitClosureFun
            
            #If i < current closure's size, copy its value
            blt ${'$'}a1, ${'$'}a2, copyExistingClosureValue
            
            #Otherwise create new closure value
            li ${'$'}v0, 9
            li ${'$'}a0, 4
            syscall
            sw ${'$'}v0, (${'$'}a3)
            b endCopyClosureLoop
            
            copyExistingClosureValue:
                lw ${'$'}a0, (${'$'}t9)
                sw ${'$'}a0, (${'$'}a3)
            
            endCopyClosureLoop:
                addi ${'$'}a1, ${'$'}a1, 1
                addi ${'$'}a3, ${'$'}a3, 4
                addi ${'$'}t9, ${'$'}t9, 4
                b copyClosureLoop
            
        exitClosureFun:
            move ${'$'}v0, ${'$'}t8
            jr ${'$'}ra
    """

    const val PUSH_ARG = """
# a0 = argument to push
pushArg:
    li ${'$'}a2, 4
    lw ${'$'}a1, 4(${'$'}k0)
    add ${'$'}a1, ${'$'}a1, ${'$'}k1
    mul ${'$'}a1, ${'$'}a1, ${'$'}a2
    addi ${'$'}a1, ${'$'}a1, 8
    
    sub ${'$'}a1, ${'$'}fp, ${'$'}a1
    
    sw ${'$'}a0, 0(${'$'}a1)
    addi ${'$'}k1, ${'$'}k1, 4
    
    jr ${'$'}ra
    """
}