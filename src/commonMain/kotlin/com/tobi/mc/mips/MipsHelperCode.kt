package com.tobi.mc.mips

object MipsHelperCode {

    const val CREATE_CLOSURE = """
#a0 = function code label (remapped to v1)
#a1 = size in bytes of the environment variables array
#a2 = parent size in bytes of the array
#k0 = parent closure pointer
#k1 = parent array pointer
createClosure:
    #Remap a0 to a2 as a0 needs to be used
    move ${'$'}a3, ${'$'}a0
    
    #Allocate 2 words of space for closure data
    # 0 = function label
    # 1 = pointer to memory where the closure array is stored
    li ${'$'}a0, 8
    li ${'$'}v0, 9
    syscall

    #Store the label into the memory location
    sw ${'$'}a3, 0(${'$'}v0)

		checkArraySize:
		beqz ${'$'}a1, noNewVariables

		#Use a3 to store the new size of the array
		add ${'$'}a3, ${'$'}a1, ${'$'}a2

		#Copy closure struct pointer to v1 so v0 can be reused
    move ${'$'}v1, ${'$'}v0

		#Allocate a new array and store location
		move ${'$'}a0, ${'$'}a3
		li ${'$'}v0, 9
		syscall
		sw ${'$'}v0, 4(${'$'}v1)

		#Use t8 as the pointer to the array
		move ${'$'}t8, ${'$'}v0

		#Use a1 as the loop counter
		move ${'$'}a1, ${'$'}zero

		copyClosureLoop:
			beq ${'$'}a1, ${'$'}a3, endCopyClosureLoop

			blt ${'$'}a1, ${'$'}a2, copyParentValue

			li ${'$'}a0, 4
			li ${'$'}v0, 9
			syscall
			sw ${'$'}v0, 0(${'$'}t8)
			b incrementCounter

			copyParentValue:
				add ${'$'}t9, ${'$'}a1, ${'$'}k1
				lw ${'$'}t9, 0(${'$'}t9)
				sw ${'$'}t9, 0(${'$'}t8)

			incrementCounter:
				addi ${'$'}a1, ${'$'}a1, 4
				addi ${'$'}t8, ${'$'}t8, 4
				b copyClosureLoop


		endCopyClosureLoop:
			move ${'$'}v0, ${'$'}v1
			jr ${'$'}ra

		noNewVariables:
			sw ${'$'}k1, 4(${'$'}v0)
			move ${'$'}v0, ${'$'}v0
			jr ${'$'}ra
    """
}