package com.tobi.mc.mips

object MipsHelperCode {

    const val CREATE_CLOSURE = """
#a0 = function code label
#a1 = size in bytes of the environment variables array
#a2 = size in bytes of parents array
#k0 = parent closure pointer
createClosure:
	#Remap a0 to a3 as a0 needs to be used
	move ${'$'}a3, ${'$'}a0

	#Calculate the size of the new closure
	add ${'$'}a0, ${'$'}a1, ${'$'}a2
	addi ${'$'}a0, ${'$'}a0, 8 #4 bytes for label, 4 for new array reference

	#Allocate heap memory for the closure
	li ${'$'}v0, 9
	syscall

	#Store the label at the first mem loc
	sw ${'$'}a3, 0(${'$'}v0)

	#Use ${'$'}a3 as the pointer for current pos in the closure
	addi ${'$'}a3, ${'$'}v0, 4

	#Any new variables?
	blez ${'$'}a1, copyParents

	#Allocate space for new variables
	move ${'$'}a0, ${'$'}a1
	move ${'$'}v1, ${'$'}v0
	li ${'$'}v0, 9
	syscall
	sw ${'$'}v0, 0(${'$'}a3)
	move ${'$'}v0, ${'$'}v1

	copyParents:
		addi ${'$'}a3, ${'$'}a3, 4

		#Use a0 as loop counter
		li ${'$'}a0, 4

		copyParentLoop:
			bgt ${'$'}a0, ${'$'}a2, exitCopyParentLoop
			add ${'$'}v1, ${'$'}k0, ${'$'}a0
			lw ${'$'}v1, 0(${'$'}v1)
			sw ${'$'}v1, 0(${'$'}a3)

			addi ${'$'}a0, ${'$'}a0, 4
			addi ${'$'}a3, ${'$'}a3, 4
			b copyParentLoop

		exitCopyParentLoop:
			jr ${'$'}ra
    """
}