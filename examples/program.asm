.data
	enterNumberMessage: .asciiz "Please enter a number:\n"
	outputMessage: 			.asciiz "You entered: "

.text
main:
	li $v0, 4
	la $a0, enterNumberMessage
	syscall

	#Read the number and copy to '$s0'
	li $v0, 5
	syscall
	move $s0, $v0

	#Output text
	li $v0, 4
	la $a0, outputMessage
	syscall

	li $t1 4194304
    jr $t1

	#Output number
	li $v0, 1
	move $a0, $s0
	syscall

	#Terminate the program
	li $v0, 10
	syscall