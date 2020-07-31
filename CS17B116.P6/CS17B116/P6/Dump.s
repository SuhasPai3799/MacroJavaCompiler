.text 
 .globl main 
 main:
move $fp, $sp
subu $sp, $sp, 4
sw $ra, -4($fp)
li $v1, 4
move $s0, $v1
move $a0, $s0
jal _halloc
move $s0, $v0
move $s0, $s0
li $v1, 4
move $s1, $v1
move $a0, $s1
jal _halloc
move $s1, $v0
move $s1, $s1
la $s2, Fac_ComputeFac
sw $s2, 0 ($s0)
sw $s0, 0 ($s1)
move $s1, $s1
lw $s0, 0 ($s1)
lw $s0, 0 ($s0)
li $v1, 10
move $s2, $v1
move $a0, $s1
move $a1, $s2
jalr $s0
move $s2, $v0
move $a0 , $s2
jal _print
lw $ra, -4($fp)
addu $sp, $sp, 4
j $ra
.text 
.globl Fac_ComputeFac
Fac_ComputeFac:
sw $fp, -8($sp)
move $fp, $sp
subu $sp, $sp, 80
sw $ra, -4($fp)
sw $s0 , 40($sp)
sw $s1 , 44($sp)
sw $s2 , 48($sp)
sw $s3 , 52($sp)
sw $s4 , 56($sp)
sw $s5 , 60($sp)
sw $s6 , 64($sp)
sw $s7 , 68($sp)
move $s0, $a0
move $s1, $a1
li $v1, 0
move $s2, $v1
move $v0 ,$s2
add $v0, $v0, 1 
slt $v0 ,$s1 ,  $v0
move $s2, $v0
beqz $s2, L2
li $v1, 1
move $s0, $v1
move $s0, $s0
b L3
L2:
move $s2, $s0
lw $s3, 0 ($s2)
lw $s3, 0 ($s3)
li $v1, 1
move $s4, $v1
sub $v0, $s1, $s4
move $s4, $v0
move $a0, $s2
move $a1, $s4
jalr $s3
move $s4, $v0
mul $v0, $s1, $s4
move $s4, $v0
move $s0, $s4
L3:
nop
move $v0, $s0
lw $s0 , 40($sp)
lw $s1 , 44($sp)
lw $s2 , 48($sp)
lw $s3 , 52($sp)
lw $s4 , 56($sp)
lw $s5 , 60($sp)
lw $s6 , 64($sp)
lw $s7 , 68($sp)
lw $ra, -4($fp)
lw $fp, 72 ($sp)
addu $sp, $sp, 80
j $ra
.text 
 .globl _halloc 
_halloc: li $v0, 9 
syscall 
 j $ra 
.text 
.globl _print 
_print: 
li $v0, 1 
syscall 
la $a0, newl 
li $v0, 4 
syscall
 j $ra 
.data 
.align 0 
newl: .asciiz "\n" 
.data 
.align 0 
str_er: 
.asciiz " ERROR: abnormal termination\n"
